package com.example.snacz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class activity_splash extends AppCompatActivity {

    private VideoView videoViewSplash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        videoViewSplash = findViewById(R.id.splash_cnt);
        // Set video file path or URI to the videoView
        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.splash;
        videoViewSplash.setVideoPath(videoPath);

        // Start playing the video
        videoViewSplash.start();

        // Set a listener for video completion
        videoViewSplash.setOnCompletionListener(mp -> {

            String authToken = SharedPreferencesManager.getInstance(this).getAuthToken();
            // Jump to the authentication activities after the video finishes playing
            Intent intent;
            if (authToken != null) {
                intent = new Intent(activity_splash.this, MainActivity.class);
            } else {
                intent = new Intent(activity_splash.this, PhoneNumberEntryActivity.class);
            }
            startActivity(intent);
            finish(); // Close the splash activity
        });

        // Stop video playback after 2 seconds (if video is shorter than 2 seconds)
        videoViewSplash.postDelayed(() -> {
            if (videoViewSplash.isPlaying()) {
                videoViewSplash.stopPlayback();
            }
        }, 2400);
    }
}