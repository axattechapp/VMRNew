package com.developers.vmrapp.models;

import java.util.List;

public class UserProfileModel {

    public Data data;

    public class Data {
        public int id;
        public String last_login;
        public String email;
        public String name;
        public String mobile;
        public boolean is_admin;
        public boolean is_active;
        public boolean is_staff;
        public boolean is_superuser;
        public String role;
        public String login_type;
        public Object profileimage;
        public String password;
        public String age;
        public String location;
        public String updated;
        public String created;
        public List<Object> groups;
        public List<Object> user_permissions;
    }

}
