package net.branium.repository;

import net.branium.model.User;
import net.branium.serviceapi.RetrofitInstance;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private User createdUser = new User();

    public UserRepository() {
    }

    public User createUser(User user) {

        var userApiService = RetrofitInstance.getUserAPIService();
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
}
