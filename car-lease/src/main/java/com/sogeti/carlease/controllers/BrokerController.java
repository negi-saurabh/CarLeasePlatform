package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.services.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @GetMapping(value="/all")
    public List<Customer> readAll() {
        return brokerService.getAllCustomers();
    }

    @GetMapping(value="/{id}")
    public List<Customer> read(@PathVariable int id) {
        Optional<Customer> customer = brokerService.findById(id);
        return customer.map(List::of).orElse(Collections.emptyList());
    }

    @PostMapping(value="/addNew")
    public Customer create(@RequestBody final Customer customer) {
        return brokerService.createCustomer(customer);
    }

    @RequestMapping(value= "/update/{id}", method = RequestMethod.PUT)
    public Customer update(@PathVariable int id, @RequestBody Customer customer){
        return brokerService.updateCustomer(customer, id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable int id){
        //need to check children records before deleting
        brokerService.deleteCustomer(id);
    }

    @RequestMapping(value="/calculateLease", method = RequestMethod.GET)
    public double calculateLease(@RequestParam(value = "Mileage" , required = true) double mileage,
                                 @RequestParam(value = "Duration", required = true) double duration,
                                 @RequestParam(value = "InterestRate", required = true) double interestRate,
                                 @RequestParam(value = "Make", required = true) String make,
                                 @RequestParam(value = "Model", required = true) String model){
        return brokerService.calculateCarLease(mileage,duration,interestRate,make,model);
    }
}
