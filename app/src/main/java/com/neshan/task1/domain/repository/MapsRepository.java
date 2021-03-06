package com.neshan.task1.domain.repository;

import com.neshan.task1.domain.model.RoutingModel;
import com.neshan.task1.domain.model.searchModel.SearchResponse;
import io.reactivex.Single;

public interface MapsRepository {

    Single<RoutingModel> getDirection(String origin,
                                      String type,
                                      String destination);

    Single<SearchResponse> searchLocation(String term,
                                          double lat,
                                          double lng);

}