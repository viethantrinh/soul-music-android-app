package net.branium.repository;

import net.branium.model.User;
import net.branium.serviceapi.RetrofitInstance;
import net.branium.serviceapi.UserAPIService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private User createdUser = new User();
    private UserAPIService userApiService;

    public UserRepository() {
        userApiService = RetrofitInstance.getUserAPIService();
    }

    public User createUser(User user) {

        userApiService.createUser(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                createdUser = response.body();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });
        return createdUser;
    }

    public void deleteUser(String id) {
        userApiService.deleteUser(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
            }
        });
    }

    public void updateUser(User user) {
        userApiService.updateUser(user).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {

            }
        });
    }
}
