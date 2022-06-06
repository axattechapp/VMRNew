package com.developers.vmrapp.models;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LogbookServiceViewdapiModel2 {

    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public LogbookServiceViewdapiModel2 withData(Data data) {
        this.data = data;
        return this;
    }
    @Generated("jsonschema2pojo")
    public class Data {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("user")
        @Expose
        public Integer user;
        @SerializedName("vehicle")
        @Expose
        public Integer vehicle;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("notes")
        @Expose
        public String notes;
        @SerializedName("photoofreceipt")
        @Expose
        public String photoofreceipt;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Data withId(Integer id) {
            this.id = id;
            return this;
        }

        public Integer getUser() {
            return user;
        }

        public void setUser(Integer user) {
            this.user = user;
        }

        public Data withUser(Integer user) {
            this.user = user;
            return this;
        }

        public Integer getVehicle() {
            return vehicle;
        }

        public void setVehicle(Integer vehicle) {
            this.vehicle = vehicle;
        }

        public Data withVehicle(Integer vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Data withDate(String date) {
            this.date = date;
            return this;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Data withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public String getPhotoofreceipt() {
            return photoofreceipt;
        }

        public void setPhotoofreceipt(String photoofreceipt) {
            this.photoofreceipt = photoofreceipt;
        }

        public Data withPhotoofreceipt(String photoofreceipt) {
            this.photoofreceipt = photoofreceipt;
            return this;
        }

    }
}