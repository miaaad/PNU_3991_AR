
package com.neshan.task1.domain.model;

import java.util.List;
import com.google.gson.annotations.SerializedName;


public class RoutingModel {

    @SerializedName("routes")
    private List<Route> mRoutes;

    public List<Route> getRoutes() {
        return mRoutes;
    }

    public void setRoutes(List<Route> routes) {
        mRoutes = routes;
    }

}
