package net.branium.serviceapi;

import net.branium.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String PATH = Constants.FULL_PATH;
    private static Retrofit retrofit = null;

    // songs
    public static SongAPIService getSongAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SongAPIService.class);
    }

    // albums
    public static AlbumAPIService getAlbumAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(AlbumAPIService.class);
    }

    // users
    public static UserAPIService getUserAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserAPIService.class);
    }

    // playlists
    public static PlaylistAPIService getPlaylistAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(PlaylistAPIService.class);
    }


}
