package com.sogeti.carlease.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long carId;
    private String make;
    private String model;
    private String version;
    private String numberOfDoors;
    private String co2Emissions;
    private float grossPrice;
    private float nettPrice;

    public Car(){
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNumberOfDoors() {
        return numberOfDoors;
    }

    public void setNumberOfDoors(String numberOfDoors) {
        this.numberOfDoors = numberOfDoors;
    }

    public String getCo2Emissions() {
        return co2Emissions;
    }

    public void setCo2Emissions(String co2Emissions) {
        this.co2Emissions = co2Emissions;
    }

    public float getGrossPrice() {
        return grossPrice;
    }

    public void setGrossPrice(float grossPrice) {
        this.grossPrice = grossPrice;
    }

    public float getNettPrice() {
        return nettPrice;
    }

    public void setNettPrice(float nettPrice) {
        this.nettPrice = nettPrice;
    }
}
