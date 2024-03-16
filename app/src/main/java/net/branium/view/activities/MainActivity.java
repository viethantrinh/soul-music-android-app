package net.branium.view.activities;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import net.branium.R;
import net.branium.service.MusicService;
import net.branium.view.adapters.ViewPagerAdapter;
import net.branium.view.fragments.main.HomeFragment;
import net.branium.view.fragments.main.LoveFragment;
import net.branium.view.fragments.main.PlaylistFragment;
import net.branium.view.fragments.main.RankFragment;
import net.branium.view.fragments.main.UserFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    public static ViewPager2 viewPagerMain;
    ViewPagerAdapter viewPagerAdapter;
    MusicService musicService;
    public static final String MUSIC_LAST_PLAYED = "LAST_PLAYED";
    public static final String MUSIC_FILE = "STORED_MUSIC";
    public static final String ARTIST_NAME = "ARTIST NAME";
    public static final String SONG_NAME = "SONG NAME";
    public static boolean SHOW_MINI_PLAYER = false;
    public static String PATH_TO_FRAG = null;
    public static String ARTIST_TO_FRAG = null;
    public static String SONG_NAME_TO_FRAG = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bottom navigation
        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPagerMain = findViewById(R.id.view_pager_main);
        viewPagerMain.setOffscreenPageLimit(5);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                List.of(new HomeFragment(), new PlaylistFragment(), new RankFragment(), new LoveFragment(), new UserFragment())
        );
        viewPagerMain.setAdapter(viewPagerAdapter);
        viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 -> bottomNavigation.setSelectedItemId(R.id.nav_home);
                    case 1 -> bottomNavigation.setSelectedItemId(R.id.nav_playlist);
                    case 2 -> bottomNavigation.setSelectedItemId(R.id.nav_rank);
                    case 3 -> bottomNavigation.setSelectedItemId(R.id.nav_love);
                    case 4 -> bottomNavigation.setSelectedItemId(R.id.nav_user);
                }
                super.onPageSelected(position);
            }
        });
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    viewPagerMain.setCurrentItem(0, true);
                }

                if (itemId == R.id.nav_playlist) {
                    viewPagerMain.setCurrentItem(1, true);
                }

                if (itemId == R.id.nav_rank) {
                    viewPagerMain.setCurrentItem(2, true);
                }

                if (itemId == R.id.nav_love) {
                    viewPagerMain.setCurrentItem(3, true);
                }

                if (itemId == R.id.nav_user) {
                    viewPagerMain.setCurrentItem(4, true);
                }

                return true;
            }
        });
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        SharedPreferences preferences = getSharedPreferences(MUSIC_LAST_PLAYED, MODE_PRIVATE);
//        String path = preferences.getString(MUSIC_FILE, null);
//        String artist = preferences.getString(ARTIST_NAME, null);
//        String songName = preferences.getString(SONG_NAME, null);
//        if(path != null) {
//            SHOW_MINI_PLAYER = true;
//            PATH_TO_FRAG = path;
//            ARTIST_TO_FRAG = artist;
//            SONG_NAME_TO_FRAG = songName;
//        }else {
//            SHOW_MINI_PLAYER = false;
//            PATH_TO_FRAG = null;
//            ARTIST_TO_FRAG = null;
//            SONG_NAME_TO_FRAG = null;
//        }
//    }
//
//    @Override
//    public void onServiceConnected(ComponentName name, IBinder service) {
//        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
//        musicService = myBinder.getService();
//    }
//
//    @Override
//    public void onServiceDisconnected(ComponentName name) {
//        musicService = null;
//    }
}