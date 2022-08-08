package com.sogeti.carlease.controllers;

import com.sogeti.carlease.exceptions.CarNotFoundException;
import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.services.CarLeaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.xml.bind.ValidationException;
import java.util.List;
import java.util.Optional;

/*
 * Contains all the methods accessible to a EMPLOYEE to maintain Car's data
 * A Broker can do all the CRUD operations for a Car
 */

@RestController
@RequestMapping("/api/car")
public class CarController {
    @Autowired
    private CarLeaseService carLeaseService;


    /*
     * Returns data of all the Cars
     */
    @GetMapping(value="/all")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<List<Car>> readAll() {
        List<Car> cars = carLeaseService.getAllCars();
        if (cars.isEmpty()) {
            throw new CarNotFoundException("No vehicle records were found");
        }
        return new ResponseEntity<List<Car>>(cars, HttpStatus.OK);
    }

    /*
     * Returns data of the Car with given id
     * @PathVariable contains the id of the Car
     * @return the Car Entity with given car id
     */
    @GetMapping(value="/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Car> read(@PathVariable int id) {
        Optional<Car> optCar = carLeaseService.findById(id);
        if (optCar.isPresent()){
            return ResponseEntity.ok(optCar.get());
        } else {
            throw new CarNotFoundException("Car with car Id (" + id + ") not found!");
        }
    }

    /*
     * Creates a new Car
     * @RequestBody contains the all the attributes of the Car
     * @return the newly created Car Entity
     */
    @PostMapping(value="/addNew")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Car> create(@RequestBody final Car car) throws ValidationException {
        Optional<Car> objCar = carLeaseService.findById(car.getCarId());
        if(car.getMake() == null || car.getModel() == null && car.getNettPrice() == 0.0)
            throw new ValidationException("A car cannot be created without Model or Make or NettPrice");
        else if(objCar.isPresent())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Car with ID" + "(" + car.getCarId() + ") already exists");
        else
            return new ResponseEntity<Car>(carLeaseService.createCar(car), HttpStatus.CREATED);
    }

    /*
     * Update the data of an existing Car
     * @PathVariable contains the id of the Car
     * @RequestBody contains the all new the attributes values of the Car
     * @return the updated Car data
     */
    @PutMapping(value="/update/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Car> update(@PathVariable int id, @RequestBody Car car) throws ValidationException {
            return new ResponseEntity<Car>(carLeaseService.updateCar(car, id), HttpStatus.ACCEPTED);
    }

    /*
     * Deletes the data of an existing Car with the given car id
     */
    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    public ResponseEntity<Object> delete(@PathVariable int id) throws ValidationException {
            carLeaseService.deleteCar(id);
            return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }
}
