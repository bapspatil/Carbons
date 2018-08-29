package com.bapspatil.carbons.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bapspatil.carbons.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);
        setContentView(R.layout.activity_main);
    }
}
