package net.branium.view.love;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.databinding.ActivityLoveAddBinding;
import net.branium.model.Playlist;
import net.branium.model.Song;
import net.branium.utils.Constants;
import net.branium.view.home.HomeFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

public class LoveAddActivity extends AppCompatActivity {
    private ActivityLoveAddBinding binding;
    private LoveAddPlaylistAdapter adapter;
    private LoveAddActivityViewModel viewModel;
    private Song songToAdd = new Song();
    private int albumSongPosition = -1;
    private int albumPosition = -1;
    public static List<Integer> lovePlaylistIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_add);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_love_add);
        setUpLovePlaylistData();
        handleEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpLovePlaylistData();
    }

    private void handleEventListener() {
        binding.ivLoveAddBack.setOnClickListener(v -> finish());
        binding.mtBtnLoveAddNew.setOnClickListener(v -> {
            Intent intent = new Intent(this, LoveCreateActivity.class);
            intent.putExtra("album_song_position", albumSongPosition);
            intent.putExtra("album_position", albumPosition);
            startActivity(intent);
        });
        binding.mtBtnLoveAddConfirm.setOnClickListener(v -> {
            if (!isDuplicatedSong()) {
                // gửi api xuống server để update các bài hát vào playlist tương ứng
                for (Integer id : lovePlaylistIds) {
                    Playlist playlistToUpdate = findUserPlaylistsById(id);
                    if (playlistToUpdate != null) {
                        playlistToUpdate.addSong(songToAdd);
                        playlistToUpdate.setSongNumber(playlistToUpdate.getSongs().size());
                    }
                    // update ...
                    viewModel.updatePlaylist(playlistToUpdate);
                }
                Toast.makeText(this, "Thêm bài hát thành công!", Toast.LENGTH_SHORT).show();
                finish();
            }
            lovePlaylistIds.clear(); // xóa các id đi vì đã xong nhiệm vụ
        });
    }

    private boolean isDuplicatedSong() {
        for (int i = 0; i < lovePlaylistIds.size(); i++) {
            final int id = lovePlaylistIds.get(i);
            Playlist playlist = findUserPlaylistsById(id);
            if (playlist != null) {
                List<Song> songsInPlaylist = playlist.getSongs();
                if (songsInPlaylist.contains(songToAdd)) {
                    Toast.makeText(this, "Bài hát đã tồn tại trong playlist " + playlist.getTitle(), Toast.LENGTH_SHORT).show();
                    return true;
                }
            }
        }
        return false;
    }

    private Playlist findUserPlaylistsById(int id) {
        for (Playlist playlist : Constants.USER_PLAYLIST_LIST) {
            if (playlist.getId() == id) {
                return playlist;
            }
        }
        return null;
    }

    private void setUpLovePlaylistData() {
        albumPosition = getIntent().getIntExtra("album_position", -1);
        albumSongPosition = getIntent().getIntExtra("album_song_position", -1);
        songToAdd = Constants.ALBUM_SONG_LIST.get(albumSongPosition);
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserId = firebaseUser.getUid();
        viewModel = new ViewModelProvider(this).get(LoveAddActivityViewModel.class);
        viewModel.getAllUserPlaylist(currentUserId).observe(this, new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                adapter = new LoveAddPlaylistAdapter(Constants.USER_PLAYLIST_LIST, getApplicationContext());
                binding.recycleViewLoveAdd.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
                binding.recycleViewLoveAdd.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        });
    }
}