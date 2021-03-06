package com.neshan.task1.domain.repository;

import com.neshan.task1.domain.model.RoutingModel;
import com.neshan.task1.domain.model.searchModel.SearchResponse;
import com.neshan.task1.domain.remote.ApiService;
import io.reactivex.Single;


public class MapsRepositoryImp implements MapsRepository {

    private final ApiService retrofit;

    public MapsRepositoryImp(ApiService retrofitService) {
        retrofit = retrofitService;
    }

    @Override
    public Single<RoutingModel> getDirection(String origin, String type, String destination) {
        return retrofit.getDirection(origin, type, destination);
    }

    @Override
    public Single<SearchResponse> searchLocation(String term, double lat, double lng) {
        return retrofit.searchLocation(term, lat, lng);
    }
}