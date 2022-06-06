package com.developers.vmrapp.models;

public class DummyHistory {

    private Integer imageId;

    private String serviceName;

    private String serviceDate;

    private String serviceShopName;

    public DummyHistory() {

    }


    public DummyHistory(Integer imageId, String serviceName, String serviceDate, String serviceShopName) {

        this.imageId = imageId;

        this.serviceName = serviceName;

        this.serviceDate = serviceDate;

        this.serviceShopName = serviceShopName;

    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServiceDate() {
        return serviceDate;
    }

    public void setServiceDate(String serviceDate) {
        this.serviceDate = serviceDate;
    }

    public String getServiceShopName() {
        return serviceShopName;
    }

    public void setServiceShopName(String serviceShopName) {
        this.serviceShopName = serviceShopName;
    }
}
