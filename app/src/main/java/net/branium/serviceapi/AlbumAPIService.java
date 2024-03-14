package net.branium.serviceapi;

import net.branium.model.Album;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AlbumAPIService {

    @GET("albums")
    Call<List<Album>> getAlbums();
}
