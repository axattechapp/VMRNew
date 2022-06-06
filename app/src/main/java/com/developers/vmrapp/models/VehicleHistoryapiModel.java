package com.developers.vmrapp.models;

import java.io.Serializable;
import java.util.List;

public class VehicleHistoryapiModel {

    public List<Datum> data;
    public class Datum implements Serializable {
        public int id;
        public String maintenanceimage;
        public String photoofreceipt;
        public String maintenancetitle;
        public String quantity;
        public String supplier;
        public String condition;
        public String description;
        public String updated;
        public String  created;
        public int user;
        public int vehicle;
        public String flagg;
        public String current_km;
        public String next_service;
        public String notes;
        public String serviceimage;
        public String servicetitle;
        public String date;
        public String servicecompany;
        public String minorormajorservice;
        public String completed;
        public String replace;
    }
}
