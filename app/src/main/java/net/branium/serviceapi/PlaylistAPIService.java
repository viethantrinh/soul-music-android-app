package net.branium.serviceapi;

import net.branium.model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface PlaylistAPIService {

    @GET("users/{id}/playlists")
    Call<List<Playlist>> getUserPlaylists(@Path(value = "id") String id);


}
