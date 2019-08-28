package com.assignment.doordash.doordash;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Log;

import com.assignment.doordash.doordash.model.Restaurant;
import com.assignment.doordash.doordash.network.RestaurantApi;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestaurantListViewModel extends ViewModel {

    private static final String TAG = RestaurantListViewModel.class.getName();

    private MutableLiveData<Restaurant[]> restaurants = new MutableLiveData<>();

    public void fetchRestaurants(int offset) {
        if (offset == 0 && restaurants.getValue() != null) {
            return;
        }
        HashMap<String, String> queryMap = new HashMap<>();
        queryMap.put("lat", "37.422740");
        queryMap.put("lng", "-122.139956");
        queryMap.put("offset", "0");
        queryMap.put("limit", "50");
        RestaurantApi api = RestaurantApi.retrofit.create(RestaurantApi.class);
        Call<Restaurant[]> call = api.getRestaurants(queryMap);
        call.enqueue(new Callback<Restaurant[]>() {
            @Override
            public void onResponse(Call<Restaurant[]> call, Response<Restaurant[]> response) {
                if (response.isSuccessful()) {
                    restaurants.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<Restaurant[]> call, Throwable t) {
                Log.e(TAG, "Failure: " + t.toString());
            }
        });
    }

    public LiveData<Restaurant[]> getRestaurants() {
        return restaurants;
    }
}
