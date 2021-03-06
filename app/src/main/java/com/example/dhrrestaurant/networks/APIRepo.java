package com.example.dhrrestaurant.networks;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;


import com.example.dhrrestaurant.comp.CApp;
import com.example.dhrrestaurant.entity.Categories;
import com.example.dhrrestaurant.entity.Restaurants;
import com.example.dhrrestaurant.entity.Location;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIRepo {

    private APIInterface apiInterface;
    private long totalPages;

    public long getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(long totalPages) {
        this.totalPages = totalPages;
    }

    public APIRepo()
    {
        apiInterface = CApp.getAPIClient().create(APIInterface.class);
    }

    public LiveData<List<Categories>> getCategoryFromAPI()
    {
        Log.d("fatal", "getCategoryFromAPI");
        final MutableLiveData<List<Categories>> mutableLiveData = new MutableLiveData<>();
        apiInterface.getCategories().enqueue(new Callback<ServerResponses>() {
            @Override
            public void onResponse(Call<ServerResponses> call, Response<ServerResponses> response) {

                if(response.isSuccessful())
                {
                    mutableLiveData.setValue(response.body().getCategories());
                }
            }

            @Override
            public void onFailure(Call<ServerResponses> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }

    public List<Restaurants> getRestaurantsFromAPIAsPaging(long entityId, String entityType, long start, long categoryId)
    {
        List<Restaurants> restaurantsList = new ArrayList<>();
        Call<ServerResponses> callingEntity = apiInterface.searchRestaurantsAsPaging(entityId, entityType, start, categoryId);
        try
        {
            Response<ServerResponses> response = callingEntity.execute();
            restaurantsList.addAll(response.body().getRestaurants());
            totalPages = response.body().getResults_found();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return restaurantsList;
    }

    public LiveData<List<Restaurants>> getRestaurantsFromAPI(long entityId, String entityType, int start, int count, long categoryId)
    {
        Log.d("fatal", "getRestaurantsFromAPI");
        final MutableLiveData<List<Restaurants>> mutableLiveData = new MutableLiveData<>();
        apiInterface.searchRestaurants(entityId, entityType, start, count, categoryId).enqueue(new Callback<ServerResponses>() {
            @Override
            public void onResponse(Call<ServerResponses> call, Response<ServerResponses> response) {

                if(response.isSuccessful())
                {
                    mutableLiveData.setValue(response.body().getRestaurants());

                }
            }

            @Override
            public void onFailure(Call<ServerResponses> call, Throwable t) {

            }
        });
        return mutableLiveData;
    }


    public LiveData<List<Location>> getLocationSuggestions(String query)
    {
        final MutableLiveData<List<Location>> mutableLiveData = new MutableLiveData<>();
        apiInterface.searchLocations(query).enqueue(new Callback<ServerResponses>() {
            @Override
            public void onResponse(Call<ServerResponses> call, Response<ServerResponses> response) {
                if(response.isSuccessful())
                    mutableLiveData.setValue(response.body().getLocation_suggestions());
            }

            @Override
            public void onFailure(Call<ServerResponses> call, Throwable t) {

            }
        });

        return mutableLiveData;
    }
}
