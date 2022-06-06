package com.developers.vmrapp.models;

import java.io.Serializable;
import java.util.List;

public class DummyMarket {

    public List<Datum> data;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class User{
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
        public String updated;
        public String created;
        public List<Object> groups;
        public List<Object> user_permissions;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }
    }

    public class Datum implements Serializable {
        public int id;
        public User user;
        public String partimage;
        public String partname;
        public String partdescription;
        public String quantity;
        public String makemodel;
        public String condition;
        public String price;
        public boolean sold;
        public String updated;
        public String created;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        public String getPartimage() {
            return partimage;
        }

        public void setPartimage(String partimage) {
            this.partimage = partimage;
        }

        public String getPartname() {
            return partname;
        }

        public void setPartname(String partname) {
            this.partname = partname;
        }

        public String getPartdescription() {
            return partdescription;
        }

        public void setPartdescription(String partdescription) {
            this.partdescription = partdescription;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getMakemodel() {
            return makemodel;
        }

        public void setMakemodel(String makemodel) {
            this.makemodel = makemodel;
        }

        public String getCondition() {
            return condition;
        }

        public void setCondition(String condition) {
            this.condition = condition;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public boolean isSold() {
            return sold;
        }

        public void setSold(boolean sold) {
            this.sold = sold;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
