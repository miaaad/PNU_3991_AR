
package com.neshan.task1.domain.model;

import com.google.gson.annotations.SerializedName;


public class OverviewPolyline {

    @SerializedName("points")
    private String mPoints;

    public String getPoints() {
        return mPoints;
    }

    public void setPoints(String points) {
        mPoints = points;
    }

}
