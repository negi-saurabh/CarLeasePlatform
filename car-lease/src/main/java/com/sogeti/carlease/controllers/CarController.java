package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.repositories.CarRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarController {
    @Autowired
    private CarRepository carRepository;

    @GetMapping
    public List<Car> carList(){
        return carRepository.findAll();
    }

    @GetMapping("/getCar/{id}")
    public Car get(@PathVariable Long id){
        return carRepository.getReferenceById(id);
    }

    @PostMapping("/createCar")
    public Car create(@RequestBody final Car car){
        return carRepository.saveAndFlush(car);
    }

    @GetMapping("/getAllCars")
    public List<Car> getAllCars(){
        return carRepository.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteCar(@PathVariable Long id){
        //need to check children records before deleting
        carRepository.deleteById(id);
    }

    @RequestMapping(value= "{id}", method = RequestMethod.PUT)
    public Car update(@PathVariable Long id, @RequestBody Car car){
        // we need to add the validation that all attributes are passed in, else return a 400 bad payload
        Car existingCar = carRepository.getReferenceById(id);
        BeanUtils.copyProperties(car, existingCar, "carId");
        return carRepository.saveAndFlush(existingCar);
    }

    @GetMapping
    @RequestMapping("/getLeasePrice/{id}")
    public Float getPrice(@PathVariable Long id){
        return 1111.8f;
    }

}
