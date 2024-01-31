package com.example.snacz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.VideoView;

public class activity_splash extends AppCompatActivity {

    private VideoView videoViewSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoViewSplash = findViewById(R.id.splash_cnt);
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        videoViewSplash.setVideoPath(videoPath);

        // Start playing the video
        videoViewSplash.start();

        // Set a listener for video completion
        videoViewSplash.setOnCompletionListener(mp -> {
            // Delay the activity transition after the video finishes playing
            new Handler().postDelayed(() -> {
                String authToken = SharedPreferencesManager.getInstance(activity_splash.this).getAuthToken();
                Intent intent = (authToken != null) ?
                        new Intent(activity_splash.this, MainActivity.class) :
                        new Intent(activity_splash.this, PhoneNumberEntryActivity.class);

                startActivity(intent);
                finish(); // Close the splash activity
            }, 1200); // Adjust the delay time (in milliseconds) as needed
        });
    }

    // Override onPause to stop video playback when the activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        if (videoViewSplash.isPlaying()) {
            videoViewSplash.stopPlayback();
        }
    }
}
