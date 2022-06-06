package com.developers.vmrapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RegistrationModel {

    public User user;
    public String token;
    public int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public class User{
        public int id;
        public Object last_login;
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
        public String updated;
        public String created;
        public List<Object> groups;
        public List<Object> user_permissions;
    }
}
