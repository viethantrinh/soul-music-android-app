package net.branium.view.activities;


import static net.branium.view.fragments.main.PlaylistFragment.MY_SORT_PREF;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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