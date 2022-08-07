package com.sogeti.carlease.services;

import com.sogeti.carlease.exceptions.CarNotFoundException;
import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.repositories.CarRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@Service
public class CarLeaseService {

    @Autowired
    public CarRepository carRepository;

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public Optional<Car> findById(int id) {
        return carRepository.findById(id);
    }

    public Car createCar(Car car) {
        return carRepository.saveAndFlush(car);
    }

    public Car updateCar(Car car, int id) {
        if (id != (car.getCarId())) {
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Id in URI does not match car id to update");
        }
        Optional<Car> op = carRepository.findById(id);
        if (!op.isPresent()) {
            throw new CarNotFoundException("Car with id (" + id + ") not found!");
        }
        Car originalCar = op.get();
        BeanUtils.copyProperties(car, originalCar);
        return carRepository.save(originalCar);
    }

    public void deleteCar(int id) {
        carRepository.deleteById(id);
    }

    public double calculateCarLease(double mileage, double duration, double interestRate, String make, String model) {
        Car result = carRepository.findByModelAndMake(model, make);
        Optional<Car> carForLease = Optional.ofNullable(result);
        double carNettPrice = carForLease.isPresent() ? carForLease.get().getNettPrice() : 0;
        if (carNettPrice == 0) {
            return 1.1f;
            //Throw new Exception("The combination of model and male is not present");
        } else {
            return (((mileage / 12) * duration) / carNettPrice) + (((interestRate / 100) * carNettPrice) / 12);
        }
    }
}