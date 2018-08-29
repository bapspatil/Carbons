package com.bapspatil.carbons.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.bapspatil.carbons.BuildConfig;
import com.bapspatil.carbons.R;
import com.bapspatil.carbons.adapter.PhotosRecyclerViewAdapter;
import com.bapspatil.carbons.model.FlickrResponse;
import com.bapspatil.carbons.model.PhotoItem;
import com.bapspatil.carbons.network.FlickrAPI;
import com.bapspatil.carbons.util.Constants;
import com.bapspatil.carbons.util.NetworkUtils;
import com.wang.avi.AVLoadingIndicatorView;

import org.aviran.cookiebar2.CookieBar;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.placeholderImageView)
    ImageView placeholderImageView;
    @BindView(R.id.photosRecyclerView)
    RecyclerView photosRecyclerView;
    @BindView(R.id.progressBar)
    AVLoadingIndicatorView progressBar;
    @BindView(R.id.search_view)
    FloatingSearchView searchView;

    private final int VOICE_RECOGNITION_REQUEST_CODE = 13;
    private PhotosRecyclerViewAdapter mAdapter;
    private ArrayList<PhotoItem> photoItemArrayList = new ArrayList<>();

    // Pagination
    private static final int FIRST_PAGE = 1;
    private int currentPage = FIRST_PAGE;
    private String mQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        // Show greeting
        CookieBar.build(MainActivity.this)
                .setLayoutGravity(Gravity.BOTTOM)
                .setBackgroundColor(R.color.colorAccent)
                .setTitle("App developed by Bapusaheb Patil")
                .show();

        // Setting the grid layout for the RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        photosRecyclerView.setLayoutManager(gridLayoutManager);

        // Setting ItemAnimator to the RecyclerView
        photosRecyclerView.setItemAnimator(new LandingAnimator());

        // Creating an instance of the PhotosRecyclerViewAdapter and setting it to the RecyclerView
        mAdapter = new PhotosRecyclerViewAdapter(getApplicationContext(), photoItemArrayList, (position, photoImageView) -> {
            PhotoItem photoItem = photoItemArrayList.get(position);
            Intent fullscreenIntent = new Intent(getApplicationContext(), FullscreenActivity.class);
            fullscreenIntent.putExtra(Constants.EXTRA_PHOTOITEM, photoItem);
            Bundle options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, photoImageView, "PhotoTransition").toBundle();
            startActivity(fullscreenIntent, options);
        });
        photosRecyclerView.setAdapter(mAdapter);

        // Pagination for RecyclerView
        paginateRecyclerView(gridLayoutManager);

        // Setting SearchView click listeners
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // Implementation not required
            }

            // Performing the search functionality
            @Override
            public void onSearchAction(String currentQuery) {
                if(!currentQuery.equals("") && !currentQuery.isEmpty()) {
                    mQuery = currentQuery;
                    placeholderImageView.setVisibility(View.GONE);
                    photosRecyclerView.smoothScrollToPosition(0);
                    searchImages(currentQuery, 1);
                }
            }
        });

        // Setting click listeners for options
        searchView.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                // Voice search
                case R.id.action_voice:
                    startVoiceRecognition();
                    break;

                // AboutMeActivity, an Activity with information regarding the developer
                case R.id.action_about_me:
                    Intent intentToAboutMe = new Intent(this, AboutMeActivity.class);
                    ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this, android.R.anim.fade_in, android.R.anim.fade_out);
                    startActivity(intentToAboutMe, options.toBundle());
                    break;

                // Number of grid columns handling
                case R.id.action_grid_2:
                    item.setChecked(true);
                    GridLayoutManager twoGridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
                    photosRecyclerView.setLayoutManager(twoGridLayoutManager);
                    paginateRecyclerView(twoGridLayoutManager);
                    break;
                case R.id.action_grid_3:
                    item.setChecked(true);
                    GridLayoutManager threeGridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                    photosRecyclerView.setLayoutManager(threeGridLayoutManager);
                    paginateRecyclerView(threeGridLayoutManager);
                    break;
                case R.id.action_grid_4:
                    item.setChecked(true);
                    GridLayoutManager fourGridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
                    photosRecyclerView.setLayoutManager(fourGridLayoutManager);
                    paginateRecyclerView(fourGridLayoutManager);
                    break;
            }
        });
    }

    // Pagination for RecyclerView
    private void paginateRecyclerView(GridLayoutManager gridLayoutManager) {
        final int[] pastVisibleItems = new int[1];
        final int[] visibleItemCount = new int[1];
        final int[] totalItemCount = new int[1];
        photosRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                                   @Override
                                                   public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                                       super.onScrolled(recyclerView, dx, dy);
                                                       visibleItemCount[0] = gridLayoutManager.getChildCount();
                                                       totalItemCount[0] = gridLayoutManager.getItemCount();
                                                       pastVisibleItems[0] = gridLayoutManager.findFirstVisibleItemPosition();

                                                       if ((visibleItemCount[0] + pastVisibleItems[0]) >= totalItemCount[0]) {
                                                           // Reached bottom of RecyclerView; load more data
                                                           currentPage += 1;
                                                           searchImages(mQuery, currentPage);
                                                       }
                                                   }
                                               }
        );
    }

    // Search for images with the search query and page number of the results
    public void searchImages(String queryText, int page) {
        // Get the Retrofit service
        FlickrAPI flickrAPI = NetworkUtils.getCacheEnabledRetrofit(getApplicationContext()).create(FlickrAPI.class);

        // Create a Retrofit Call and pass in the parameters for the GET request
        Call<FlickrResponse> flickrResponseCall = flickrAPI.searchForImages(Constants.PARAM_SEARCH_METHOD, BuildConfig.FLICKR_API_KEY, Constants.PARAM_FORMAT, Constants.PARAM_NOJSONCALLBACK, Constants.PARAM_EXTRAS, queryText, page);

        // Load the first page results
        if (page == 1) {
            // Show loading
            photosRecyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            // Enqueue the call and send the request
            flickrResponseCall.enqueue(new Callback<FlickrResponse>() {
                // If the response is successful
                @Override
                public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                    photoItemArrayList.clear();
                    if (response.body() != null) {
                        // Add all the photos to the photoItemArrayList
                        photoItemArrayList.addAll(response.body().getPhotos().getPhoto());
                        if (photoItemArrayList.isEmpty()) {
                            // If the list of photos is empty, show the empty state
                            placeholderImageView.setImageResource(R.drawable.no_images);
                            placeholderImageView.setVisibility(View.VISIBLE);
                            photosRecyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            // If the list of photos is not empty, notify the adapter and show them in the RecyclerView
                            mAdapter.notifyDataSetChanged();

                            // Hide loading
                            placeholderImageView.setImageResource(R.drawable.placeholder_search);
                            placeholderImageView.setVisibility(View.GONE);
                            photosRecyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                }

                // If the response has failed
                @Override
                public void onFailure(Call<FlickrResponse> call, Throwable t) {
                    // Show a error toast
                    Toast.makeText(getApplicationContext(), "Error searching for images!", Toast.LENGTH_LONG).show();
                    placeholderImageView.setImageResource(R.drawable.placeholder_search);
                    placeholderImageView.setVisibility(View.VISIBLE);
                    photosRecyclerView.setVisibility(View.INVISIBLE);
                    progressBar.setVisibility(View.GONE);
                }
            });
        }
        // Load the next page results
        else {
            // Enqueue the call and send the request
            flickrResponseCall.enqueue(new Callback<FlickrResponse>() {
                // If the response is successful
                @Override
                public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                    if (response.body() != null) {
                        // Add all the photos to the photoItemArrayList
                        photoItemArrayList.addAll(response.body().getPhotos().getPhoto());
                        if (!photoItemArrayList.isEmpty()) {
                            mAdapter.addAll(photoItemArrayList);
                        }
                    }
                }

                // If the response has failed
                @Override
                public void onFailure(Call<FlickrResponse> call, Throwable t) {
                    // Show a error toast
                    Toast.makeText(getApplicationContext(), "Error searching for images!", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    // Start the voice search recognition by Google Voice Search
    private void startVoiceRecognition() {
        // Intent to start Google Voice Recognition
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    // Handling the results of the voice recognition
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null) {
                if (!matches.isEmpty()) {
                    String query = matches.get(0);
                    placeholderImageView.setVisibility(View.GONE);
                    mQuery = query;
                    photosRecyclerView.smoothScrollToPosition(0);
                    searchImages(query, 1);
                    searchView.setSearchText(query);
                    Toast.makeText(this, "Searching for " + query + "...", Toast.LENGTH_LONG).show();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        // Clear the search query when the user hits the back button
        if (!searchView.getQuery().isEmpty()) {
            searchView.clearQuery();
            placeholderImageView.setImageResource(R.drawable.placeholder_search);
            placeholderImageView.setVisibility(View.VISIBLE);
            photosRecyclerView.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            CookieBar.build(MainActivity.this)
                    .setLayoutGravity(Gravity.BOTTOM)
                    .setBackgroundColor(R.color.colorPrimaryDark)
                    .setTitle("Press back again to exit.")
                    .show();
        } else
            super.onBackPressed();
    }
}
