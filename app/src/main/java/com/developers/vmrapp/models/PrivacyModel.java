package com.developers.vmrapp.models;

import java.util.List;

public class PrivacyModel {

    public List<Datum> data;

    public class Datum{
        public int id;
        public String privacy;
        public String updated;
        public String created;
    }
}
