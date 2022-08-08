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
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int customerId;
    private String name;
    private String street;
    private String houseNumber;
    private String zipCode;
    private String place;
    private String emailAddress;
    private String phoneNumber;

}
