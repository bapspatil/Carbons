package com.bapspatil.carbons.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bapspatil.carbons.BuildConfig;
import com.bapspatil.carbons.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AboutMeActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.google_play_iv)
    ImageView playButton;
    @BindView(R.id.github_iv)
    ImageView githubButton;
    @BindView(R.id.twitter_iv)
    ImageView twitterButton;
    @BindView(R.id.chrome_iv)
    ImageView chromeButton;
    @BindView(R.id.version_tv)
    TextView versionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_me);
        ButterKnife.bind(this);

        // Setting up toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setting the version name and version code for the app
        versionTextView.setText(BuildConfig.VERSION_NAME + "\n(version code: " + BuildConfig.VERSION_CODE + ")");

        // Setting click listeners to all the links related to the developer
        playButton.setOnClickListener(view -> {
            String url = "https://play.google.com/store/apps/dev?id=7368032842071222295";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        chromeButton.setOnClickListener(view -> {
            String url = "https://bapspatil.com";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        githubButton.setOnClickListener(view -> {
            String url = "https://github.com/bapspatil";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
        twitterButton.setOnClickListener(view -> {
            String url = "https://twitter.com/baps_patil";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Fade in when you go back to the MainActivity
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Go back to the parent activity, i.e. the MainActivity
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
