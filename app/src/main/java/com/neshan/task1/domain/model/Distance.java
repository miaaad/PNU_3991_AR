
package com.neshan.task1.domain.model;

import com.google.gson.annotations.SerializedName;

public class Distance {

    @SerializedName("text")
    private String mText;
    @SerializedName("value")
    private Double mValue;

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Double getValue() {
        return mValue;
    }

    public void setValue(Double value) {
        mValue = value;
    }

}
