package com.sogeti.carlease.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/*
 * Using Lombok for auto-generation of boiler plate getters, setters, all-arg and no-arg constructors
 */

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int carId;
    private String make;
    private String model;
    private String version;
    private int numberOfDoors;
    private String co2Emissions;
    private double grossPrice;
    private double nettPrice;

}
