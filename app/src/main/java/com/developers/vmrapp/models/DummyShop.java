package com.developers.vmrapp.models;

public class DummyShop {

    private Integer imageId;

    private String shopName;

    private String address;

    private String distance;

    public DummyShop() {

    }

    public DummyShop(Integer imageId, String shopName, String address, String distance) {

        this.imageId = imageId;

        this.shopName = shopName;

        this.address = address;

        this.distance = distance;
    }

    public Integer getImageId() {
        return imageId;
    }

    public void setImageId(Integer imageId) {
        this.imageId = imageId;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

}
