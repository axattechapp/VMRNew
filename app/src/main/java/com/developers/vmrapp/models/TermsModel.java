package com.developers.vmrapp.models;

import java.util.List;

public class TermsModel {

    public List<Datum> data;

    public class Datum{
        public int id;
        public String terms;
        public String updated;
        public String created;
    }
}
