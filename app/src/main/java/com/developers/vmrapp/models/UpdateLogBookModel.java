package com.developers.vmrapp.models;

public class UpdateLogBookModel {

    public Data data;

    public class Data{
        public int id;
        public String odometerimage;
        public String current_km;
        public String next_service;
        public String servicecompany;
        public String notes;
        public String updated;
        public String created;
        public int user;
        public int vehicle;
    }

}
