package com.developers.vmrapp.models;

        import java.util.List;
        import javax.annotation.Generated;
        import com.google.gson.annotations.Expose;
        import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class HistroyMdel {

    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public HistroyMdel withData(List<Datum> data) {
        this.data = data;
        return this;
    }
    @Generated("jsonschema2pojo")
    public class Datum {

        @SerializedName("id")
        @Expose
        private Integer id;
        @SerializedName("user")
        @Expose
        private Integer user;
        @SerializedName("vehicle")
        @Expose
        private Integer vehicle;
        @SerializedName("notes")
        @Expose
        private String notes;
        @SerializedName("photoofreceipt")
        @Expose
        private Object photoofreceipt;
        @SerializedName("photoofreceipt2")
        @Expose
        private Object photoofreceipt2;
        @SerializedName("photoofreceipt3")
        @Expose
        private Object photoofreceipt3;
        @SerializedName("photoofreceipt4")
        @Expose
        private Object photoofreceipt4;
        @SerializedName("photoofreceipt5")
        @Expose
        private Object photoofreceipt5;
        @SerializedName("flagg")
        @Expose
        private String flagg;
        @SerializedName("maintenanceimage")
        @Expose
        private String maintenanceimage;
        @SerializedName("maintenancetitle")
        @Expose
        private String maintenancetitle;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("updated")
        @Expose
        private String updated;
        @SerializedName("created")
        @Expose
        private String created;

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

    }
}