package com.neshan.task1.domain.model.animate;

import org.neshan.common.model.LatLng;

public class BeginJourneyEvent {

    private String event = "BEGIN_JOURNEY";
    private LatLng beginLatLng;

    public LatLng getBeginLatLng() {
        return beginLatLng;
    }

    public void setBeginLatLng(LatLng beginLatLng) {
        this.beginLatLng = beginLatLng;
    }

}