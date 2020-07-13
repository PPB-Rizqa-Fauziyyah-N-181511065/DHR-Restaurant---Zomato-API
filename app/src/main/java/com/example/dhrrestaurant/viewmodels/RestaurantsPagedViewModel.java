package com.example.dhrrestaurant.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.example.dhrrestaurant.comp.ProgressBarInterface;
import com.example.dhrrestaurant.entity.Categories;
import com.example.dhrrestaurant.entity.Constant;
import com.example.dhrrestaurant.entity.Restaurants;
import com.example.dhrrestaurant.viewmodelsfactory.RestaurantsDataSourceFactory;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RestaurantsPagedViewModel extends ViewModel {

    private Executor executor;
    private LiveData<PagedList<Restaurants>> pagedListLiveData;
    private Categories.Category category;
    private long cityId;
    private ProgressBarInterface progressBarInterface;

    public RestaurantsPagedViewModel(Categories.Category category, long cityId, ProgressBarInterface progressBarInterface)
    {
        this.category = category;
        this.cityId = cityId;
        this.progressBarInterface = progressBarInterface;
        init();
    }

    private void init()
    {
        executor = Executors.newFixedThreadPool(5);

        RestaurantsDataSourceFactory restaurantsDataSourceFactory = new RestaurantsDataSourceFactory(category, cityId, progressBarInterface);

        PagedList.Config pagedListConfig =
                (new PagedList.Config.Builder())
                        .setEnablePlaceholders(false)
                        .setInitialLoadSizeHint(Constant.LOAD_ITEM_COUNT)
                        .setPageSize(Constant.LOAD_PAGE_SIZE).build();

        pagedListLiveData = (new LivePagedListBuilder(restaurantsDataSourceFactory, pagedListConfig))
                .setFetchExecutor(executor)
                .build();
    }

    public LiveData<PagedList<Restaurants>> getRestaurantLiveData()
    {
        return pagedListLiveData;
    }

}
