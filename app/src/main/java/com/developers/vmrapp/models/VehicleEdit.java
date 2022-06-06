package com.developers.vmrapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class VehicleEdit {

    @SerializedName("data")
    @Expose
    private String data;

    public VehicleEdit(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }
}
