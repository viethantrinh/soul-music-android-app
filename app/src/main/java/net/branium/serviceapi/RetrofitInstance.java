package net.branium.serviceapi;

import net.branium.view.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;

    public static SongAPIService getSongAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OPPO_FULL_PATH_ON_WINDOW)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SongAPIService.class);
    }

    public static AlbumAPIService getAlbumAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.OPPO_FULL_PATH_ON_WINDOW)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(AlbumAPIService.class);
    }
}
