package net.branium.view.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
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
import net.branium.model.MusicFiles;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class SplashActivity extends AppCompatActivity {
    public static final int REQUEST_CODE = 1;
    public static ArrayList<MusicFiles> musicFiles = new ArrayList<>();
    private Handler handler;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // User permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permission();
        }
    }

    public static ArrayList<MusicFiles> getAllAudio(Context context) {
        ArrayList<MusicFiles> tempAudioList = new ArrayList<>();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projections = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA, // for path
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = context.getContentResolver().query(uri, projections, null, null, null);

        if (cursor != null) {
//            while (cursor.moveToNext()) {
//                String album = cursor.getString(0);
//                String title = cursor.getString(1);
//                String duration = cursor.getString(2);
//                String path = cursor.getString(3);
//                String artist = cursor.getString(4);
//                MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
//                Log.e("HAHA: ", "Title" + title);
//                tempAudioList.add(musicFiles);
//            }
            if (cursor.moveToFirst()) {
                do {
                    //do whatever you want
                    String album = cursor.getString(0);
                    String title = cursor.getString(1);
                    String duration = cursor.getString(2);
                    String path = cursor.getString(3);
                    String artist = cursor.getString(4);
                    MusicFiles musicFiles = new MusicFiles(path, title, artist, album, duration);
                    Log.e("HAHA: ", "Title" + title);
                    tempAudioList.add(musicFiles);
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
        return tempAudioList;
    }


    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    private void permission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_VIDEO}, REQUEST_CODE);
        } else {
            Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show();
            musicFiles = getAllAudio(this);
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

    @RequiresApi(api = Build.VERSION_CODES.TIRAMISU)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED
                    && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
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
            } else {
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.READ_MEDIA_AUDIO}, REQUEST_CODE);
            }
        }
    }
}