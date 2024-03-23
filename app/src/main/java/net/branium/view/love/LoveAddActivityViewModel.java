package net.branium.view.love;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.branium.model.Playlist;
import net.branium.repository.PlaylistRepository;

import java.util.List;

public class LoveAddActivityViewModel extends AndroidViewModel {
    private PlaylistRepository playlistRepo;
    public LoveAddActivityViewModel(@NonNull Application application) {
        super(application);
        this.playlistRepo = new PlaylistRepository(application);
    }

    public LiveData<List<Playlist>> getAllUserPlaylist(String uid) {
        return playlistRepo.getMutableLiveDataPlaylistList(uid);
    }

    public void updatePlaylist(Playlist playlist) {
        playlistRepo.updatePlaylist(playlist);
    }
}
