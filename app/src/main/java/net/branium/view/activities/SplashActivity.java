package net.branium.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import net.branium.R;
import net.branium.model.Song;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    public static List<Song> musicFiles = new ArrayList<>();
    public static boolean shuffleBoolean = false;
    public static boolean repeatBoolean = false;
    private Handler handler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // Splash screen and auth
        handler = new Handler();
        mAuth = FirebaseAuth.getInstance();
        handler.postDelayed(
                () -> {
                    Intent intent = null;
                    if (mAuth.getCurrentUser() != null) {
                        intent = new Intent(SplashActivity.this, MainActivity.class);
                    } else {
                        intent = new Intent(SplashActivity.this, AuthActivity.class);
                    }
                    startActivity(intent);
                    finish();
                }, 2000
        );
    }
}