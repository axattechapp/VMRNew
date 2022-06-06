package com.developers.vmrapp.models;

import java.io.Serializable;
import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class VehicleHistoryModel2  {

    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public VehicleHistoryModel2 withData(List<Datum> data) {
        this.data = data;
        return this;
    }
    @Generated("jsonschema2pojo")
    public class Datum implements Serializable{


        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("current_km")
        @Expose
        public String currentKm;
        @SerializedName("next_service")
        @Expose
        public String nextService;
        @SerializedName("notes")
        @Expose
        public String notes;
        @SerializedName("serviceimage")
        @Expose
        public String serviceimage;
        @SerializedName("photoofreceipt")
        @Expose
        public Object photoofreceipt;
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
        @SerializedName("servicetitle")
        @Expose
        public String servicetitle;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("servicecompany")
        @Expose
        public String servicecompany;
        @SerializedName("minorormajorservice")
        @Expose
        public String minorormajorservice;
        @SerializedName("completed")
        @Expose
        public String completed;
        @SerializedName("replace")
        @Expose
        public String replace;
        @SerializedName("updated")
        @Expose
        public String updated;
        @SerializedName("created")
        @Expose
        public String created;
        @SerializedName("user")
        @Expose
        public Integer user;
        @SerializedName("vehicle")
        @Expose
        public Integer vehicle;
        @SerializedName("flagg")
        @Expose
        public String flagg;
        @SerializedName("maintenanceimage")
        @Expose
        public String maintenanceimage;
        @SerializedName("maintenancetitle")
        @Expose
        public String maintenancetitle;
        @SerializedName("description")
        @Expose
        public String description;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Datum withId(Integer id) {
            this.id = id;
            return this;
        }

        public String getCurrentKm() {
            return currentKm;
        }

        public void setCurrentKm(String currentKm) {
            this.currentKm = currentKm;
        }

        public Datum withCurrentKm(String currentKm) {
            this.currentKm = currentKm;
            return this;
        }

        public String getNextService() {
            return nextService;
        }

        public void setNextService(String nextService) {
            this.nextService = nextService;
        }

        public Datum withNextService(String nextService) {
            this.nextService = nextService;
            return this;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public Datum withNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public String getServiceimage() {
            return serviceimage;
        }

        public void setServiceimage(String serviceimage) {
            this.serviceimage = serviceimage;
        }

        public Datum withServiceimage(String serviceimage) {
            this.serviceimage = serviceimage;
            return this;
        }

        public Object getPhotoofreceipt() {
            return photoofreceipt;
        }

        public void setPhotoofreceipt(Object photoofreceipt) {
            this.photoofreceipt = photoofreceipt;
        }

        public Datum withPhotoofreceipt(Object photoofreceipt) {
            this.photoofreceipt = photoofreceipt;
            return this;
        }

        public Object getPhotoofreceipt2() {
            return photoofreceipt2;
        }

        public void setPhotoofreceipt2(Object photoofreceipt2) {
            this.photoofreceipt2 = photoofreceipt2;
        }

        public Datum withPhotoofreceipt2(Object photoofreceipt2) {
            this.photoofreceipt2 = photoofreceipt2;
            return this;
        }

        public Object getPhotoofreceipt3() {
            return photoofreceipt3;
        }

        public void setPhotoofreceipt3(Object photoofreceipt3) {
            this.photoofreceipt3 = photoofreceipt3;
        }

        public Datum withPhotoofreceipt3(Object photoofreceipt3) {
            this.photoofreceipt3 = photoofreceipt3;
            return this;
        }

        public Object getPhotoofreceipt4() {
            return photoofreceipt4;
        }

        public void setPhotoofreceipt4(Object photoofreceipt4) {
            this.photoofreceipt4 = photoofreceipt4;
        }

        public Datum withPhotoofreceipt4(Object photoofreceipt4) {
            this.photoofreceipt4 = photoofreceipt4;
            return this;
        }

        public Object getPhotoofreceipt5() {
            return photoofreceipt5;
        }

        public void setPhotoofreceipt5(Object photoofreceipt5) {
            this.photoofreceipt5 = photoofreceipt5;
        }

        public Datum withPhotoofreceipt5(Object photoofreceipt5) {
            this.photoofreceipt5 = photoofreceipt5;
            return this;
        }

        public String getServicetitle() {
            return servicetitle;
        }

        public void setServicetitle(String servicetitle) {
            this.servicetitle = servicetitle;
        }

        public Datum withServicetitle(String servicetitle) {
            this.servicetitle = servicetitle;
            return this;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Datum withDate(String date) {
            this.date = date;
            return this;
        }

        public String getServicecompany() {
            return servicecompany;
        }

        public void setServicecompany(String servicecompany) {
            this.servicecompany = servicecompany;
        }

        public Datum withServicecompany(String servicecompany) {
            this.servicecompany = servicecompany;
            return this;
        }

        public String getMinorormajorservice() {
            return minorormajorservice;
        }

        public void setMinorormajorservice(String minorormajorservice) {
            this.minorormajorservice = minorormajorservice;
        }

        public Datum withMinorormajorservice(String minorormajorservice) {
            this.minorormajorservice = minorormajorservice;
            return this;
        }

        public String getCompleted() {
            return completed;
        }

        public void setCompleted(String completed) {
            this.completed = completed;
        }

        public Datum withCompleted(String completed) {
            this.completed = completed;
            return this;
        }

        public String getReplace() {
            return replace;
        }

        public void setReplace(String replace) {
            this.replace = replace;
        }

        public Datum withReplace(String replace) {
            this.replace = replace;
            return this;
        }

        public String getUpdated() {
            return updated;
        }

        public void setUpdated(String updated) {
            this.updated = updated;
        }

        public Datum withUpdated(String updated) {
            this.updated = updated;
            return this;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public Datum withCreated(String created) {
            this.created = created;
            return this;
        }

        public Integer getUser() {
            return user;
        }

        public void setUser(Integer user) {
            this.user = user;
        }

        public Datum withUser(Integer user) {
            this.user = user;
            return this;
        }

        public Integer getVehicle() {
            return vehicle;
        }

        public void setVehicle(Integer vehicle) {
            this.vehicle = vehicle;
        }

        public Datum withVehicle(Integer vehicle) {
            this.vehicle = vehicle;
            return this;
        }

        public String getFlagg() {
            return flagg;
        }

        public void setFlagg(String flagg) {
            this.flagg = flagg;
        }

        public Datum withFlagg(String flagg) {
            this.flagg = flagg;
            return this;
        }

        public String getMaintenanceimage() {
            return maintenanceimage;
        }

        public void setMaintenanceimage(String maintenanceimage) {
            this.maintenanceimage = maintenanceimage;
        }

        public Datum withMaintenanceimage(String maintenanceimage) {
            this.maintenanceimage = maintenanceimage;
            return this;
        }

        public String getMaintenancetitle() {
            return maintenancetitle;
        }

        public void setMaintenancetitle(String maintenancetitle) {
            this.maintenancetitle = maintenancetitle;
        }

        public Datum withMaintenancetitle(String maintenancetitle) {
            this.maintenancetitle = maintenancetitle;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Datum withDescription(String description) {
            this.description = description;
            return this;
        }

    }
}