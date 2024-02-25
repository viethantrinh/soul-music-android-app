package net.branium.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import net.branium.R;
import net.branium.fragments.HomeFragment;
import net.branium.fragments.PlaylistFragment;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigation;
    FrameLayout mainFrameLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        bottomNavigation = findViewById(R.id.bottom_navigation);
        mainFrameLayout = findViewById(R.id.main_frame_layout);
        bottomNavigation.setSelectedItemId(R.id.nav_home);
        setFragment(new HomeFragment());

        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.nav_home) {
                    setFragment(new HomeFragment());
                }

                if (item.getItemId() == R.id.nav_playlist) {
                    setFragment(new PlaylistFragment());
                }
                return true;
            }
        });
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(mainFrameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}