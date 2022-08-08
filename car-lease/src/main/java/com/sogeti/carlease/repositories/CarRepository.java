package com.sogeti.carlease.repositories;

import com.sogeti.carlease.models.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/*
 * Just by extending the JpaRepository we can use implementation of relevant query Methods
 * like 'findById' or 'findAll'
 */

public interface CarRepository extends JpaRepository<Car, Integer> {

  Car findByModelAndMake(String model, String make);

  @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Car c WHERE c.carId = ?1")
  Boolean
  doesCarExitsById(Integer id);
}

