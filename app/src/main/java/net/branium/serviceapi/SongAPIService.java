package net.branium.serviceapi;

import net.branium.model.Song;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SongAPIService {

    @GET("songs")
    Call<List<Song>> getSongs();
}
