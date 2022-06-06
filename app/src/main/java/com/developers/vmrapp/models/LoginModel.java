package com.developers.vmrapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LoginModel {

    @SerializedName("token")
    @Expose
    public String token;
    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("payment")
    @Expose
    public boolean payment;

    public boolean isPayment() {
        return payment;
    }

    public void setPayment(boolean payment) {
        this.payment = payment;
    }

    public class User {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("last_login")
        @Expose
        public String lastLogin;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("is_admin")
        @Expose
        public Boolean isAdmin;
        @SerializedName("is_active")
        @Expose
        public Boolean isActive;
        @SerializedName("is_staff")
        @Expose
        public Boolean isStaff;
        @SerializedName("is_superuser")
        @Expose
        public Boolean isSuperuser;
        @SerializedName("role")
        @Expose
        public String role;
        @SerializedName("login_type")
        @Expose
        public String loginType;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("updated")
        @Expose
        public String updated;
        @SerializedName("created")
        @Expose
        public String created;
        @SerializedName("groups")
        @Expose
        public List<Object> groups = null;
        @SerializedName("user_permissions")
        @Expose
        public List<Object> userPermissions = null;

    }
}
