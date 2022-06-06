package com.developers.vmrapp.models;

public class VehicleModel {

    private String name;

    private String model;

    private String imageUrl;

    private Integer kilometers;

    private String identification;

    private Double serviceProgress;

    public VehicleModel() {

    }

    public VehicleModel(String name, String model, String imageUrl, Integer kilometers, String identification, Double serviceProgress) {

        this.name = name;

        this.model = model;

        this.imageUrl = imageUrl;

        this.kilometers = kilometers;

        this.identification = identification;

        this.serviceProgress = serviceProgress;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Integer getKilometers() {
        return kilometers;
    }

    public void setKilometers(Integer kilometers) {
        this.kilometers = kilometers;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public Double getServiceProgress() {
        return serviceProgress;
    }

    public void setServiceProgress(Double serviceProgress) {
        this.serviceProgress = serviceProgress;
    }
}
