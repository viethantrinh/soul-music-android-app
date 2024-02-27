package net.branium.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import net.branium.R;
import net.branium.adapters.ViewPagerAdapter;
import net.branium.fragments.main.HomeFragment;
import net.branium.fragments.main.LoveFragment;
import net.branium.fragments.main.PlaylistFragment;
import net.branium.fragments.main.RankFragment;
import net.branium.fragments.main.UserFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    ViewPager2 viewPagerMain;
    ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        viewPagerMain = findViewById(R.id.view_pager_main);

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
}