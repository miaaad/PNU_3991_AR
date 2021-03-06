
package com.neshan.task1.domain.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Route {

    @SerializedName("legs")
    private List<Leg> mLegs;
    @SerializedName("overview_polyline")
    private OverviewPolyline mOverviewPolyline;

    public List<Leg> getLegs() {
        return mLegs;
    }

    public void setLegs(List<Leg> legs) {
        mLegs = legs;
    }

    public OverviewPolyline getOverviewPolyline() {
        return mOverviewPolyline;
    }

    public void setOverviewPolyline(OverviewPolyline overviewPolyline) {
        mOverviewPolyline = overviewPolyline;
    }

}
