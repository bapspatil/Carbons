package com.bapspatil.carbons.ui;

import android.app.Activity;
import android.content.Intent;
import android.speech.RecognizerIntent;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.MenuItem;
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
    private int currentPageNumber = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        CookieBar.build(MainActivity.this)
                .setLayoutGravity(Gravity.BOTTOM)
                .setBackgroundColor(R.color.colorAccent)
                .setTitle("App developed by Bapusaheb Patil")
                .show();

        // Setting the grid layout for the RecyclerView
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        photosRecyclerView.setLayoutManager(gridLayoutManager);

        // Creating an instance of the PhotosRecyclerViewAdapter and setting it to the RecyclerView
        mAdapter = new PhotosRecyclerViewAdapter(getApplicationContext(), photoItemArrayList, (position, photoImageView) -> {
            // TODO: Implement photo item click
        });
        photosRecyclerView.setAdapter(mAdapter);

        // Setting SearchView click listeners
        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) {
                // Implementation not required
            }

            @Override
            public void onSearchAction(String currentQuery) {
                placeholderImageView.setVisibility(View.GONE);
                photosRecyclerView.smoothScrollToPosition(0);
                searchImages(currentQuery, 1);
            }
        });

        // Setting click listeners for options
        searchView.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                // Voice search
                case R.id.action_voice:
                    startVoiceRecognition();
                    break;

                // AboutMeActivity, Activity with information regarding the developer
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
                    break;
                case R.id.action_grid_3:
                    item.setChecked(true);
                    GridLayoutManager threeGridLayoutManager = new GridLayoutManager(getApplicationContext(), 3);
                    photosRecyclerView.setLayoutManager(threeGridLayoutManager);
                    break;
                case R.id.action_grid_4:
                    item.setChecked(true);
                    GridLayoutManager fourGridLayoutManager = new GridLayoutManager(getApplicationContext(), 4);
                    photosRecyclerView.setLayoutManager(fourGridLayoutManager);
                    break;
            }
        });
    }

    // Search for images with the search query and page number of the results
    public void searchImages(String queryText, int page) {
        photosRecyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        FlickrAPI flickrAPI = NetworkUtils.getCacheEnabledRetrofit(getApplicationContext()).create(FlickrAPI.class);
        Call<FlickrResponse> flickrResponseCall = flickrAPI.searchForImages(Constants.PARAM_SEARCH_METHOD, BuildConfig.FLICKR_API_KEY, Constants.PARAM_FORMAT, Constants.PARAM_NOJSONCALLBACK, Constants.PARAM_EXTRAS, queryText, page);
        flickrResponseCall.enqueue(new Callback<FlickrResponse>() {
            @Override
            public void onResponse(Call<FlickrResponse> call, Response<FlickrResponse> response) {
                photoItemArrayList.clear(); // TODO: Add logic for pagination here by modifying this line
                if(response.body() != null) {
                    photoItemArrayList.addAll(response.body().getPhotos().getPhoto());
                    if(photoItemArrayList.isEmpty()) {
                        placeholderImageView.setImageResource(R.drawable.no_images);
                        placeholderImageView.setVisibility(View.VISIBLE);
                        photosRecyclerView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.GONE);
                    } else {
                        mAdapter.notifyDataSetChanged();
                        placeholderImageView.setImageResource(R.drawable.placeholder_search);
                        placeholderImageView.setVisibility(View.GONE);
                        photosRecyclerView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<FlickrResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Error searching for images!", Toast.LENGTH_LONG).show();
                photosRecyclerView.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    // Start the voice search recognition by Google Voice Search
    private void startVoiceRecognition() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Voice searching...");
        startActivityForResult(intent, VOICE_RECOGNITION_REQUEST_CODE);
    }

    // Handling the results of the voice recognition
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null) {
                if(!matches.isEmpty()) {
                    String query = matches.get(0);
                    placeholderImageView.setVisibility(View.GONE);
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
        }
        else
            super.onBackPressed();
    }
}
