package net.branium.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import net.branium.model.Playlist;
import net.branium.serviceapi.RetrofitInstance;
import net.branium.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistRepository {
    private MutableLiveData<List<Playlist>> mutableLiveDataPlaylistList = new MutableLiveData<>();
    private Application application;


    public PlaylistRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Playlist>> getMutableLiveDataPlaylistList(String uid) {
        var playlistApiService = RetrofitInstance.getPlaylistAPIService();
        playlistApiService.getUserPlaylists(uid).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                List<Playlist> playlistListResponse = response.body();
                if (playlistListResponse != null && !playlistListResponse.isEmpty()) {
                    Constants.USER_PLAYLIST_LIST.clear();
                    Constants.USER_PLAYLIST_LIST.addAll(playlistListResponse);
                    mutableLiveDataPlaylistList.setValue(Constants.USER_PLAYLIST_LIST);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });

        return mutableLiveDataPlaylistList;
    }
}
