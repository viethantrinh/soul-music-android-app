package net.branium.view.love;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import net.branium.R;
import net.branium.databinding.ActivityLoveCreateBinding;
import net.branium.model.Playlist;
import net.branium.model.Song;
import net.branium.repository.PlaylistRepository;
import net.branium.utils.Constants;
import net.branium.view.album.AlbumActivity;

public class LoveCreateActivity extends AppCompatActivity {
    private ActivityLoveCreateBinding binding;
    private Song songToAdd = new Song();
    private int albumSongPosition = -1;
    private PlaylistRepository playlistRepo;
    private boolean checkAlbum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_love_create);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_love_create);
        checkAlbum = getIntent().getBooleanExtra("flag_check_album", false);
        if(checkAlbum) {
            albumSongPosition = getIntent().getIntExtra("album_song_position", -1);
            songToAdd = Constants.ALBUM_SONG_LIST.get(albumSongPosition);
        }
        playlistRepo = new PlaylistRepository(getApplication());
        handleEventListener();
    }

    private void handleEventListener() {
        binding.mtBtnLoveCreateCancel.setOnClickListener(v -> finish());
        binding.mtBtnLoveCreateAdd.setOnClickListener(v -> {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String lovePlaylistName = binding.etLoveCreateLovePlaylistName.getText().toString();
                if (!isDuplicated(lovePlaylistName)) {
                    Playlist playlist = new Playlist();
                    if(checkAlbum) {
                        playlist.addSong(songToAdd);
                    }
                    playlist.setSongNumber(playlist.getSongs().size());
                    playlist.setTitle(binding.etLoveCreateLovePlaylistName.getText().toString());
                    playlistRepo.createPlaylistToUser(playlist, currentUser.getUid());
                    Toast.makeText(this, "Tạo danh sách yêu thích thành công!", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        });
    }

    private boolean isDuplicated(String lovePlaylistName) {
        for (Playlist playlist : Constants.USER_PLAYLIST_LIST) {
            if (playlist.getTitle().equalsIgnoreCase(lovePlaylistName)) {
                Toast.makeText(this, "Tên danh sách yêu thích đã tồn tại", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }
}