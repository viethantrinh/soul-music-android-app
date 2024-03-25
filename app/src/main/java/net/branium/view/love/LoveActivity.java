package net.branium.view.love;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.branium.R;
import net.branium.databinding.ActivityLoveBinding;
import net.branium.model.Playlist;
import net.branium.utils.Constants;
import net.branium.view.musicplayer.MusicActivity;

import java.util.Random;

/**
 * Class này để là activity khi nhấn vào một love playlist sẽ hiển ra
 */
public class LoveActivity extends AppCompatActivity {

    private ActivityLoveBinding binding;
    public static LoveMusicAdapter loveMusicAdapter;
    private int lovePosition;
    public static LoveViewModel viewModel;
    public static Playlist playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love);
        viewModel = new ViewModelProvider(this).get(LoveViewModel.class);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_love);
        setUpLoveData();
        handleEventListener();
    }

    @Override
    protected void onResume() {
        setUpLoveData();
        super.onResume();
    }
    private void handleEventListener() {
        binding.ivLoveBack.setOnClickListener(v -> finish());
        binding.fltBtnLovePlayMusic.setOnClickListener(v -> {
            if (!Constants.PLAYLIST_SONG_LIST.isEmpty()) {
                Random random = new Random();
                int musicRanPos = random.nextInt(Constants.PLAYLIST_SONG_LIST.size());
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("position", musicRanPos);
                intent.putExtra("type", 1);
                startActivity(intent);
            }
        });
    }
    private void setUpLoveData() {
        lovePosition = getIntent().getIntExtra("love_playlist_position", -1);
        playlist = Constants.USER_PLAYLIST_LIST.get(lovePosition);
        Glide.with(getApplicationContext()).load(playlist.getImage()).into(binding.ivLoveArt);
        binding.tvLoveTitle.setText(playlist.getTitle());
        Constants.PLAYLIST_SONG_LIST.clear();
        Constants.PLAYLIST_SONG_LIST.addAll(playlist.getSongs());
        loveMusicAdapter = new LoveMusicAdapter(this, Constants.PLAYLIST_SONG_LIST);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL, false);
        binding.recycleViewLoveMusicList.setLayoutManager(layoutManager);
        binding.recycleViewLoveMusicList.setAdapter(loveMusicAdapter);
        registerForContextMenu(binding.recycleViewLoveMusicList);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        return super.onContextItemSelected(item);
    }
}