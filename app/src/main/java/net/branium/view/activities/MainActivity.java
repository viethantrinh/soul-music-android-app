package net.branium.view.activities;

import static net.branium.view.activities.MusicActivity.position;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.navigation.NavigationBarView;

import net.branium.R;
import net.branium.databinding.ActivityMainBinding;
import net.branium.utils.Constants;
import net.branium.view.adapters.ViewPagerAdapter;
import net.branium.view.fragments.main.HomeFragment;
import net.branium.view.fragments.main.LoveFragment;
import net.branium.view.fragments.main.MiniPlayerFragment;
import net.branium.view.fragments.main.PlaylistFragment;
import net.branium.view.fragments.main.RankFragment;
import net.branium.view.fragments.main.UserFragment;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ViewPagerAdapter viewPagerAdapter;
    private ActivityMainBinding binding;
    private static ViewPager2 viewPagerMain;
    public static Fragment miniPlayerFragment = new MiniPlayerFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        
        configuration();
        binding.fragBottomPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MusicActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    private void configuration() {
        viewPagerMain = binding.viewPagerMain;
        binding.viewPagerMain.setOffscreenPageLimit(5);
        binding.bottomNavigation.setSelectedItemId(R.id.nav_home);

        viewPagerAdapter = new ViewPagerAdapter(
                getSupportFragmentManager(),
                getLifecycle(),
                List.of(new HomeFragment(), new PlaylistFragment(), new RankFragment(), new LoveFragment(), new UserFragment())
        );

        binding.viewPagerMain.setAdapter(viewPagerAdapter);

        binding.viewPagerMain.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0 -> binding.bottomNavigation.setSelectedItemId(R.id.nav_home);
                    case 1 -> binding.bottomNavigation.setSelectedItemId(R.id.nav_playlist);
                    case 2 -> binding.bottomNavigation.setSelectedItemId(R.id.nav_rank);
                    case 3 -> binding.bottomNavigation.setSelectedItemId(R.id.nav_love);
                    case 4 -> binding.bottomNavigation.setSelectedItemId(R.id.nav_user);
                }
                super.onPageSelected(position);
            }
        });

        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    binding.viewPagerMain.setCurrentItem(0, true);
                }

                if (itemId == R.id.nav_playlist) {
                    binding.viewPagerMain.setCurrentItem(1, true);
                }

                if (itemId == R.id.nav_rank) {
                    binding.viewPagerMain.setCurrentItem(2, true);
                }

                if (itemId == R.id.nav_love) {
                    binding.viewPagerMain.setCurrentItem(3, true);
                }

                if (itemId == R.id.nav_user) {
                    binding.viewPagerMain.setCurrentItem(4, true);
                }
                return true;
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Constants.MINI_PLAYER_ACTIVE) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(binding.fragBottomPlayer.getId(), miniPlayerFragment);
            fragmentTransaction.commit();
        }
    }

    public static ViewPager2 getViewPagerMain() {
        return viewPagerMain;
    }
}