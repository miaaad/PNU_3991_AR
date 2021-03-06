package com.neshan.task1.domain.usecase.base;


import com.neshan.task1.domain.model.searchModel.SearchResponse;
import com.neshan.task1.domain.repository.MapsRepository;

import javax.inject.Inject;

import io.reactivex.Single;


public class SearchUseCase extends UseCase<SearchResponse, Void> {

    private final MapsRepository mapsRepository;

    public @Inject
    SearchUseCase(MapsRepository mapsRepository) {
        this.mapsRepository = mapsRepository;
    }

    String term;
    double lat;
    double lng;

    public void setData(String term, double lat, double lng) {
        this.term = term;
        this.lat = lat;
        this.lng = lng;
    }

    @Override
    Single<SearchResponse> buildUseCase(Void aVoid) {
        return mapsRepository.searchLocation(term, lat, lng);
    }

}