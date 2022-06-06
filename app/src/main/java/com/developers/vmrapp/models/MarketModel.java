package com.developers.vmrapp.models;

public class MarketModel {

    private String imageUrl;

    private String partName;

    private String partPrice;

    public MarketModel() {

    }

    public MarketModel(String imageUrl, String partName, String partPrice) {

        this.imageUrl = imageUrl;

        this.partName = partName;

        this.partPrice = partPrice;

    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPartName() {
        return partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    public String getPartPrice() {
        return partPrice;
    }

    public void setPartPrice(String partPrice) {
        this.partPrice = partPrice;
    }
}
