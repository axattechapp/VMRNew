package com.developers.vmrapp.models;

public class ShopModel {

    private String imageUrl;

    private String shopName;

    private String address;

    private String distance;

    public ShopModel() {

    }

    public ShopModel(String imageUrl, String shopName, String address, String distance) {

        this.imageUrl = imageUrl;

        this.shopName = shopName;

        this.address = address;

        this.distance = distance;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
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
