package com.developers.vmrapp.models;

import java.util.List;

public class PlanModel {

    public List<Datum> data;

    public class Datum{
        public int id;
        public String plan_name;
        public int cars;
        public String plan_duration;
        public int days;
        public double price;
        public boolean is_active;
        public String updated;
        public String created;
    }

}
