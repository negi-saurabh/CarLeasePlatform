package com.sogeti.carlease.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


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

   /* @JsonManagedReference
    @OneToMany(cascade = CascadeType.ALL)
    private List<Car> cars;

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }*/
}
