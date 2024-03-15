package net.branium.repository;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import net.branium.model.Album;
import net.branium.serviceapi.RetrofitInstance;
import net.branium.utils.Constants;

import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlbumRepository {
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
                    Constants.ALBUM_LIST.clear();
                    Constants.ALBUM_LIST.addAll(albumListResponse);
                    Collections.shuffle(Constants.ALBUM_LIST);
                    mutableLiveDataAlbumList.setValue(Constants.ALBUM_LIST);
                }
            }

            @Override
            public void onFailure(Call<List<Album>> call, Throwable t) {

            }
        });

        return mutableLiveDataAlbumList;
    }
}
