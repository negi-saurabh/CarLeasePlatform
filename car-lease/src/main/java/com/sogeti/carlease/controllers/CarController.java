package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.services.CarLeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/car")
public class CarController {
    @Autowired
    private CarLeaseService carLeaseService;

    @GetMapping(value="/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public List<Car> readAll() {
        return carLeaseService.getAllCars();
    }

    @GetMapping(value="/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Car read(@PathVariable int id) {
        Optional<Car> car = carLeaseService.findById(id);
        return car.stream().findFirst().orElse(null);
    }

    @PostMapping(value="/addNew")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Car create(@RequestBody final Car car) throws ValidationException {
        if(car.getMake() != null && car.getModel() != null && car.getNettPrice() != 0.0)
            return carLeaseService.createCar(car);
        else
            throw new ValidationException("A car cannot be created without Model or Make or NettPrice");
    }

    @RequestMapping(value="/update/{id}", method = RequestMethod.PUT)
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public Car update(@PathVariable int id, @RequestBody Car car) throws ValidationException {
        if(carLeaseService.findById(id).isPresent()) {
            return carLeaseService.updateCar(car, id);
        }else{
            throw new ValidationException("A car with this id does not exits");
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public void delete(@PathVariable int id){
        //need to check children records before deleting
        carLeaseService.deleteCar(id);
    }
}
