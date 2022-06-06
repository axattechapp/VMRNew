package com.developers.vmrapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetVehicleDetailModel {


    public Data data;

    public int unpaid_count;

    public class Data {
        public int id;
        public String msg;
        public String vehicleimage;
        public String vehicle_vinnumber;
        public String vehicle_regnumber;
        public String current_km;
        public String vehicle_make;
        public String vehicle_model;
        public String vehicle_year;
        public String reg_due_date;
        public String prefferedsupp;
        public String contact_number;
        public String next_service;
        public boolean payment_complete;
        public String updated;
        public String created;
        public int user;
    }

}
