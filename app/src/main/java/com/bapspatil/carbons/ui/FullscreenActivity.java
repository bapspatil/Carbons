package com.bapspatil.carbons.ui;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bapspatil.carbons.R;
import com.bapspatil.carbons.model.PhotoItem;
import com.bapspatil.carbons.util.Constants;
import com.bapspatil.carbons.util.GlideApp;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.aviran.cookiebar2.CookieBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class FullscreenActivity extends AppCompatActivity {

    @BindView(R.id.fullscreenPhotoImageView)
    ImageView fullscreenPhotoImageView;
    @BindView(R.id.backButton)
    CircleImageView backButton;

    private PhotoItem mPhotoItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        ButterKnife.bind(this);

        // Show hint to tell user to click on the image to go full-screen
        CookieBar.build(FullscreenActivity.this)
                .setLayoutGravity(Gravity.TOP)
                .setBackgroundColor(R.color.colorAccent)
                .setTitle("Click on the image to go full-screen")
                .show();

        // Get the intent from the calling Activity
        if (getIntent().hasExtra(Constants.EXTRA_PHOTOITEM))
            mPhotoItem = getIntent().getParcelableExtra(Constants.EXTRA_PHOTOITEM);
        else
            showError();

        // If mPhotoItem is not null...
        if (mPhotoItem != null) {
            // ...load the image into the fullscreenPhotoImageView and...
            GlideApp.with(getApplicationContext())
                    .load(mPhotoItem.getUrlM())
                    .error(R.drawable.placeholder_error)
                    .fallback(R.drawable.placeholder_error)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.placeholder_loading)
                    .centerCrop()
                    .into(fullscreenPhotoImageView);

            // ...set the click listener on the fullscreenPhotoImageView to toggle fullscreen and...
            fullscreenPhotoImageView.setOnClickListener(new View.OnClickListener() {
                boolean isFullscreen = false;

                @Override
                public void onClick(View v) {
                    if (isFullscreen) {
                        showSystemUI();
                        isFullscreen = false;
                    } else {
                        hideSystemUI();
                        isFullscreen = true;
                    }
                }
            });

            // ...set the click listener on the backButton to go back to the MainActivity.
            backButton.setOnClickListener(v -> onBackPressed());
        }
        // Else...
        else {
            // ...show an error.
            showError();
        }
    }

    // Show an error to inform the user to go back to the MainActivity
    private void showError() {
        CookieBar.build(FullscreenActivity.this)
                .setLayoutGravity(Gravity.TOP)
                .setBackgroundColor(R.color.colorPrimaryDark)
                .setTitle("Error occurred! Please hit the back button and try again.")
                .setDuration(4000)
                .show();
    }

    // Hide the system UI and backButton
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
        backButton.setVisibility(View.GONE);
    }

    // Show the system UI and backButton
    private void showSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        backButton.setVisibility(View.VISIBLE);
    }
}
