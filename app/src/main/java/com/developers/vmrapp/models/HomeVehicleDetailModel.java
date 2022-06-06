package com.developers.vmrapp.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class HomeVehicleDetailModel {

    public int unpaid_count;
    public boolean plan_redirection;

    @SerializedName("data")
    @Expose
    public List<Datum> data = null;

    public class Datum implements Serializable {

        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("vehicleimage")
        @Expose
        public String vehicleimage;
        @SerializedName("vehicle_vinnumber")
        @Expose
        public String vehicleVinnumber;
        @SerializedName("vehicle_regnumber")
        @Expose
        public String vehicleRegnumber;
        @SerializedName("current_km")
        @Expose
        public String currentKm;
        @SerializedName("vehicle_make")
        @Expose
        public String vehicleMake;
        @SerializedName("vehicle_model")
        @Expose
        public String vehicleModel;
        @SerializedName("vehicle_year")
        @Expose
        public String vehicleYear;
        @SerializedName("reg_due_date")
        @Expose
        public String regDueDate;
        @SerializedName("insurance_date")
        @Expose
        public String Insurance_date;
        @SerializedName("prefferedsupp")
        @Expose
        public String prefferedsupp;
        @SerializedName("contact_number")
        @Expose
        public String contactNumber;
        @SerializedName("next_service")
        @Expose
        public String nextService;
        @SerializedName("updated")
        @Expose
        public String updated;
        @SerializedName("created")
        @Expose
        public String created;
        @SerializedName("user")
        @Expose
        public Integer user;


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getVehicleimage() {
            return vehicleimage;
        }

        public void setVehicleimage(String vehicleimage) {
            this.vehicleimage = vehicleimage;
        }

        public String getVehicleVinnumber() {
            return vehicleVinnumber;
        }

        public void setVehicleVinnumber(String vehicleVinnumber) {
            this.vehicleVinnumber = vehicleVinnumber;
        }

        public String getVehicleRegnumber() {
            return vehicleRegnumber;
        }

        public void setVehicleRegnumber(String vehicleRegnumber) {
            this.vehicleRegnumber = vehicleRegnumber;
        }

        public String getCurrentKm() {
            return currentKm;
        }

        public void setCurrentKm(String currentKm) {
            this.currentKm = currentKm;
        }

        public String getVehicleMake() {
            return vehicleMake;
        }

        public void setVehicleMake(String vehicleMake) {
            this.vehicleMake = vehicleMake;
        }

        public String getVehicleModel() {
            return vehicleModel;
        }

        public void setVehicleModel(String vehicleModel) {
            this.vehicleModel = vehicleModel;
        }

        public String getVehicleYear() {
            return vehicleYear;
        }

        public void setVehicleYear(String vehicleYear) {
            this.vehicleYear = vehicleYear;
        }

        public String getRegDueDate() {
            return regDueDate;
        }

        public void setRegDueDate(String regDueDate) {
            this.regDueDate = regDueDate;
        }

        public String getInsurance_date() {
            return Insurance_date;
        }

        public void setInsurance_date(String insurance_date) {
            Insurance_date = insurance_date;
        }

        public String getPrefferedsupp() {
            return prefferedsupp;
        }

        public void setPrefferedsupp(String prefferedsupp) {
            this.prefferedsupp = prefferedsupp;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getNextService() {
            return nextService;
        }

        public void setNextService(String nextService) {
            this.nextService = nextService;
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

        public Integer getUser() {
            return user;
        }

        public void setUser(Integer user) {
            this.user = user;
        }
    }
}
