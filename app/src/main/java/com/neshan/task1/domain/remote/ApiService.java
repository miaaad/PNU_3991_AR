package com.neshan.task1.domain.remote;

import com.neshan.task1.domain.model.RoutingModel;
import com.neshan.task1.domain.model.searchModel.SearchResponse;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiService {

    @Headers("Api-Key: service.DXs8lVJQV9bEpYKcddU0FiiYUwOh10ObnVn1T7aK")
    @GET("v2/direction/no-traffic?parameters")
    Single<RoutingModel> getDirection(@Query("origin") String origin,
                                      @Query("type") String type,
                                      @Query("destination") String destination);

    @Headers("Api-Key: service.DXs8lVJQV9bEpYKcddU0FiiYUwOh10ObnVn1T7aK")
    @GET("v1/search")
    Single<SearchResponse> searchLocation(@Query("term") String term,
                                          @Query("lat") double lat,
                                          @Query("lng") double lng);
}