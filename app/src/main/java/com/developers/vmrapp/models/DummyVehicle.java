package com.developers.vmrapp.models;

public class DummyVehicle {

    private String name;

    private String model;

    private Integer imageId;

    private Integer kilometers;

    private String identification;

    private Double serviceProgress;

    public DummyVehicle() {

    }

    public DummyVehicle(String name, String model, Integer imageId, Integer kilometers, String identification, Double serviceProgress) {

        this.name = name;

        this.model = model;

        this.imageId = imageId;

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

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
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
