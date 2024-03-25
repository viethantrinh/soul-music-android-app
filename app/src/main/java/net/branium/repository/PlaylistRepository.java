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
        playlistApiService.getPlaylistsByUserId(uid).enqueue(new Callback<List<Playlist>>() {
            @Override
            public void onResponse(Call<List<Playlist>> call, Response<List<Playlist>> response) {
                List<Playlist> playlistListResponse = response.body();
                if (playlistListResponse != null && !playlistListResponse.isEmpty()) {
                    Constants.USER_PLAYLIST_LIST.clear();
                    Constants.USER_PLAYLIST_LIST.addAll(playlistListResponse);
                    Constants.USER_PLAYLIST_LIST.forEach(playlist -> System.out.println(playlist));
                    mutableLiveDataPlaylistList.setValue(Constants.USER_PLAYLIST_LIST);
                }
            }

            @Override
            public void onFailure(Call<List<Playlist>> call, Throwable t) {

            }
        });

        return mutableLiveDataPlaylistList;
    }

    public void updatePlaylist(Playlist playlist) {
        var playlistApiService = RetrofitInstance.getPlaylistAPIService();
        playlistApiService.updatePlaylist(playlist).enqueue(new Callback<Playlist>() {
            @Override
            public void onResponse(Call<Playlist> call, Response<Playlist> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<Playlist> call, Throwable t) {

            }
        });
    }
    public void createPlaylistToUser(Playlist playlist, String id) {
        var playlistApiService = RetrofitInstance.getPlaylistAPIService();
        playlistApiService.createPlaylistToUser(playlist, id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                System.out.println("Lỗi rồi!");
            }
        });
    }
}
