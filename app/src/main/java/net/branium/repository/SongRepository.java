package net.branium.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import net.branium.model.Song;
import net.branium.serviceapi.RetrofitInstance;
import net.branium.serviceapi.SongAPIService;
import net.branium.utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
    private List<Song> songList = new ArrayList<>();
    private MutableLiveData<List<Song>> mutableLiveDataSongList = new MutableLiveData<>();
    private Application application;

    public SongRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Song>> getMutableLiveDataSongList() {
        SongAPIService songAPIService = RetrofitInstance.getSongAPIService();
        songAPIService.getSongs().enqueue(new Callback<List<Song>>() {
            @Override
            public void onResponse(Call<List<Song>> call, Response<List<Song>> response) {
                List<Song> songListResponse = response.body();
                if (songListResponse != null && !songListResponse.isEmpty()) {
                    songList = songListResponse;
                    Collections.shuffle(songList);
                    Constants.HOME_SONG_LIST.clear();
                    Constants.HOME_SONG_LIST.addAll(songList);
                    Constants.PLAYLIST_SONG_LIST.clear();
                    Constants.PLAYLIST_SONG_LIST.addAll(songList);
                    mutableLiveDataSongList.setValue(songList);
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
        return mutableLiveDataSongList;
    }
}
