package net.branium.serviceapi;

import net.branium.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserAPIService {

    @POST("users")
    Call<User> createUser(@Body User user);
}
