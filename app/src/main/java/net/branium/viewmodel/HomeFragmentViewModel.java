package net.branium.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.branium.model.Album;
import net.branium.model.Song;
import net.branium.repository.AlbumRepository;
import net.branium.repository.SongRepository;

import java.util.List;

public class HomeFragmentViewModel extends AndroidViewModel {

    private SongRepository songRepo;
    private AlbumRepository albumRepo;

    public HomeFragmentViewModel(@NonNull Application application) {
        super(application);
        this.songRepo = new SongRepository(application);
        this.albumRepo = new AlbumRepository(application);
    }

    public LiveData<List<Song>> getAllSongs() {
        return songRepo.getMutableLiveDataSongList();
    }

    public LiveData<List<Album>> getAllAlbums() {
        return albumRepo.getMutableLiveDataAlbumList();
    }
}
