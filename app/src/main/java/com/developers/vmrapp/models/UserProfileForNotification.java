package com.developers.vmrapp.models;

import java.util.ArrayList;
import java.util.Date;

public class UserProfileForNotification {
    public Data data;



    public class Data{
        public int id;
        public Object last_login;
        public String email;
        public String name;
        public String mobile;
        public boolean is_admin;
        public boolean is_active;
        public boolean is_superuser;
        public String role;
        public String login_type;
        public Object profileimage;
        public String password;
        public String age;
        public Object location;
        public boolean push_notification;
        public boolean reminder;
        public boolean is_staff;
        public Date updated;
        public Date created;
        public Object device_type;
        public Object device_id;
        public ArrayList<Object> groups;
        public ArrayList<Object> user_permissions;
    }






}
