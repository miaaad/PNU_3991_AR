package com.neshan.task1.domain.usecase.base;

import com.neshan.task1.domain.model.RoutingModel;
import com.neshan.task1.domain.repository.MapsRepository;
import javax.inject.Inject;
import io.reactivex.Single;


public class RouteUseCase extends UseCase<RoutingModel, Void> {

    private final MapsRepository mapsRepository;

    public @Inject
    RouteUseCase(MapsRepository mapsRepository) {
        this.mapsRepository = mapsRepository;
    }

    String origin;
    String type;
    String destination;

    public void setData(String origin, String type, String destination) {
        this.destination = destination;
        this.origin = origin;
        this.type = type;
    }

    @Override
    Single<RoutingModel> buildUseCase(Void aVoid) {
        return mapsRepository.getDirection(origin, type, destination);
    }

}