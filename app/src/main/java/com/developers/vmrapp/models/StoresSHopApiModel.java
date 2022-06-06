package com.developers.vmrapp.models;

import java.io.Serializable;
import java.util.List;

public class StoresSHopApiModel {

    public List<Datum> data;

    public class Datum implements Serializable {
        public int id;
        public String logo;
        public String name;
        public String description;
        public String email;
        public String Location;
        public String contact_number;
        public String openhours;
        public String Services;
        public String updated;
        public String created;
        public int addedby;
        public int updatedby;
    }

}
