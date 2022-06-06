package com.developers.vmrapp.models;

        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class LogbookServiceViewdapiModel3 {

    @SerializedName("data")
    @Expose
    public Data data;

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public LogbookServiceViewdapiModel3 withData(Data data) {
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
        @SerializedName("photoofreceipt2")
        @Expose
        public Object photoofreceipt2;
        @SerializedName("photoofreceipt3")
        @Expose
        public Object photoofreceipt3;
        @SerializedName("photoofreceipt4")
        @Expose
        public Object photoofreceipt4;
        @SerializedName("photoofreceipt5")
        @Expose
        public Object photoofreceipt5;

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

        public Object getPhotoofreceipt2() {
            return photoofreceipt2;
        }

        public void setPhotoofreceipt2(Object photoofreceipt2) {
            this.photoofreceipt2 = photoofreceipt2;
        }

        public Data withPhotoofreceipt2(Object photoofreceipt2) {
            this.photoofreceipt2 = photoofreceipt2;
            return this;
        }

        public Object getPhotoofreceipt3() {
            return photoofreceipt3;
        }

        public void setPhotoofreceipt3(Object photoofreceipt3) {
            this.photoofreceipt3 = photoofreceipt3;
        }

        public Data withPhotoofreceipt3(Object photoofreceipt3) {
            this.photoofreceipt3 = photoofreceipt3;
            return this;
        }

        public Object getPhotoofreceipt4() {
            return photoofreceipt4;
        }

        public void setPhotoofreceipt4(Object photoofreceipt4) {
            this.photoofreceipt4 = photoofreceipt4;
        }

        public Data withPhotoofreceipt4(Object photoofreceipt4) {
            this.photoofreceipt4 = photoofreceipt4;
            return this;
        }

        public Object getPhotoofreceipt5() {
            return photoofreceipt5;
        }

        public void setPhotoofreceipt5(Object photoofreceipt5) {
            this.photoofreceipt5 = photoofreceipt5;
        }

        public Data withPhotoofreceipt5(Object photoofreceipt5) {
            this.photoofreceipt5 = photoofreceipt5;
            return this;
        }

    }
}