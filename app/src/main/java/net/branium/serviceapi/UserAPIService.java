package net.branium.serviceapi;

import net.branium.model.User;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserAPIService {

    @POST("users")
    Call<User> createUser(@Body User user);

    @DELETE("users/{id}")
    Call<Void> deleteUser(@Path("id") String id);

    @PUT("users")
    Call<Void> updateUser(@Body User user);

}
