package com.neshan.task1.domain.model.animate;

import org.neshan.common.model.LatLng;

public class CurrentJourneyEvent {

    private String event = "BEGIN_JOURNEY";
    private LatLng currentLatLng;

    public LatLng getCurrentLatLng() {
        return currentLatLng;
    }

    public void setCurrentLatLng(LatLng currentLatLng) {
        this.currentLatLng = currentLatLng;
    }
}