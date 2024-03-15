package net.branium.serviceapi;

import net.branium.utils.Constants;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static Retrofit retrofit = null;

    public static SongAPIService getSongAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.EMULATOR_FULL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(SongAPIService.class);
    }

    public static AlbumAPIService getAlbumAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.EMULATOR_FULL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(AlbumAPIService.class);
    }

    public static UserAPIService getUserAPIService() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Constants.EMULATOR_FULL_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(UserAPIService.class);
    }


}
