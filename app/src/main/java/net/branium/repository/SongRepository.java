package net.branium.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import net.branium.model.Song;
import net.branium.serviceapi.RetrofitInstance;
import net.branium.serviceapi.SongAPIService;
import net.branium.utils.Constants;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SongRepository {
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
                    Constants.SONG_LIST.clear();
                    Constants.SONG_LIST.addAll(songListResponse);
                    Collections.shuffle(Constants.SONG_LIST);
                    mutableLiveDataSongList.setValue(Constants.SONG_LIST);
                }
            }

            @Override
            public void onFailure(Call<List<Song>> call, Throwable t) {

            }
        });
        return mutableLiveDataSongList;
    }
}
