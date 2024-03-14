package net.branium.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import net.branium.model.Album;
import net.branium.serviceapi.RetrofitInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
    private List<Album> albumList = new ArrayList<>();
    private MutableLiveData<List<Album>> mutableLiveDataAlbumList = new MutableLiveData<>();
    private Application application;


    public AlbumRepository(Application application) {
        this.application = application;
    }

    public MutableLiveData<List<Album>> getMutableLiveDataAlbumList() {
        var albumApiService = RetrofitInstance.getAlbumAPIService();
        albumApiService.getAlbums().enqueue(new Callback<List<Album>>() {
            @Override
            public void onResponse(Call<List<Album>> call, Response<List<Album>> response) {
                List<Album> albumListResponse = response.body();
                if (albumListResponse != null && !albumListResponse.isEmpty()) {
                    albumList = albumListResponse;
                    Collections.shuffle(albumList);
                    mutableLiveDataAlbumList.setValue(albumList);
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

        return mutableLiveDataAlbumList;
    }
}
