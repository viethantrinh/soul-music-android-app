package net.branium.view.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import net.branium.R;
import net.branium.databinding.ActivityAlbumBinding;
import net.branium.model.Album;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.adapters.AlbumMusicAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Class này để là activity khi nhấn vào album sẽ hiển ra
 */
public class AlbumActivity extends AppCompatActivity {
    private AlbumMusicAdapter albumMusicAdapter;
    private ActivityAlbumBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_album);
        setUpAlbumData();
        handleEventListener();
    }

    private void handleEventListener() {
        binding.ivAlbumBack.setOnClickListener(v -> finish());
        binding.fltBtnAlbumPlayMusic.setOnClickListener(v -> {
            if (!Constants.ALBUM_SONG_LIST.isEmpty()) {
                Random random = new Random();
                int musicRanPos = random.nextInt(Constants.ALBUM_SONG_LIST.size());
                Intent intent = new Intent(this, MusicActivity.class);
                intent.putExtra("position", musicRanPos);
                startActivity(intent);
            }
        });
    }

    private void setUpAlbumData() {
        int albumPosition = getIntent().getIntExtra("album_position", -1);
        Album album = Constants.ALBUM_LIST.get(albumPosition);
        Glide.with(getApplicationContext()).load(album.getImage()).into(binding.ivAlbumArt);
        binding.tvAlbumTitle.setText(album.getTitle());
        Constants.ALBUM_SONG_LIST.clear();
        Constants.ALBUM_SONG_LIST.addAll(album.getSongs());
        albumMusicAdapter = new AlbumMusicAdapter(Constants.ALBUM_SONG_LIST, this);
        binding.recycleViewAlbumMusicList.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.recycleViewAlbumMusicList.setAdapter(albumMusicAdapter);
    }
}