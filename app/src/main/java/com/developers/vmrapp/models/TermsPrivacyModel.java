package com.developers.vmrapp.models;

import java.util.ArrayList;
import java.util.Date;

public class TermsPrivacyModel {
    public ArrayList<Datum> data;

    public class Datum{
        public int id;
        public String title;
        public String description;
        public Date updated;
        public Date created;
    }

}
