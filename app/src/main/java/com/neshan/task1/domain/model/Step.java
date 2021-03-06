
package com.neshan.task1.domain.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class Step {

    @SerializedName("distance")
    private Distance mDistance;
    @SerializedName("duration")
    private Duration mDuration;
    @SerializedName("instruction")
    private String mInstruction;
    @SerializedName("maneuver")
    private String mManeuver;
    @SerializedName("name")
    private String mName;
    @SerializedName("polyline")
    private String mPolyline;
    @SerializedName("start_location")
    private List<Double> mStartLocation;

    public Distance getDistance() {
        return mDistance;
    }

    public void setDistance(Distance distance) {
        mDistance = distance;
    }

    public Duration getDuration() {
        return mDuration;
    }

    public void setDuration(Duration duration) {
        mDuration = duration;
    }

    public String getInstruction() {
        return mInstruction;
    }

    public void setInstruction(String instruction) {
        mInstruction = instruction;
    }

    public String getManeuver() {
        return mManeuver;
    }

    public void setManeuver(String maneuver) {
        mManeuver = maneuver;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getPolyline() {
        return mPolyline;
    }

    public void setPolyline(String polyline) {
        mPolyline = polyline;
    }

    public List<Double> getStartLocation() {
        return mStartLocation;
    }

    public void setStartLocation(List<Double> startLocation) {
        mStartLocation = startLocation;
    }

}
