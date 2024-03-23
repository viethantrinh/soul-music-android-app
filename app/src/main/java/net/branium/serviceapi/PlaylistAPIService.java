package net.branium.serviceapi;

import net.branium.model.Playlist;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PlaylistAPIService {

    @GET("playlists/users/{id}")
    Call<List<Playlist>> getPlaylistsByUserId(@Path(value = "id") String id);

    @PUT("playlists")
    Call<Playlist> updatePlaylist(@Body Playlist playlist);

    @POST("playlists/users/{id}")
    Call<Void> createPlaylistToUser(@Body Playlist playlist, @Path(value = "id") String id);


}
