package com.neshan.task1.domain.model.animate;

import org.neshan.common.model.LatLng;

public class EndJourneyEvent {

    public String event = "END_JOURNEY";
    private LatLng endJourneyLatLng;

    public LatLng getEndJourneyLatLng() {
        return endJourneyLatLng;
    }

    public void setEndJourneyLatLng(LatLng endJourneyLatLng) {
        this.endJourneyLatLng = endJourneyLatLng;
    }
}