package com.app.jobfizzerxp.model;

public class MiscellaneousModel {

    private String name;

    public MiscellaneousModel() {
    }

    private String price;

    public MiscellaneousModel(String name, String price) {
        this.name = name;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }


}
