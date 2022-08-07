package com.sogeti.carlease.controllers;

import com.sogeti.carlease.exceptions.CarNotFoundException;
import com.sogeti.carlease.exceptions.CustomerNotFoundException;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.services.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class BrokerController {

    @Autowired
    private BrokerService brokerService;

    @GetMapping(value="/all")
    @PreAuthorize("hasAuthority('BROKER')")
    public ResponseEntity<List<Customer>> readAll() {
        List<Customer> customers = brokerService.getAllCustomers();
        if (customers.isEmpty()) {
            throw new CarNotFoundException("No Customer records were found");
        }
        return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
    }

    @GetMapping(value="/{id}")
    @PreAuthorize("hasAuthority('BROKER')")
    public ResponseEntity<Customer> read(@PathVariable int id) {
        Optional<Customer> optCustomer = brokerService.findById(id);
        if (optCustomer.isPresent()){
            return ResponseEntity.ok(optCustomer.get());
        } else {
            throw new CustomerNotFoundException("Customer with customer Id (" + id + ") not found!");
        }
    }

    @PostMapping(value="/addNew")
    @PreAuthorize("hasAuthority('BROKER')")
    public ResponseEntity<Customer> create(@RequestBody final Customer customer) {
        Optional<Customer> objCustomer = brokerService.findById(customer.getCustomerId());
       if(objCustomer.isPresent())
            throw new HttpClientErrorException(HttpStatus.CONFLICT, "Customer with ID" + "(" + customer.getCustomerId() + ") already exists");
        else
            return new ResponseEntity<Customer>(brokerService.createCustomer(customer), HttpStatus.CREATED);
    }

    @PutMapping(value="/update/{id}")
    @PreAuthorize("hasAuthority('BROKER')")
    public ResponseEntity<Customer> update(@PathVariable int id, @RequestBody Customer customer){
        return new ResponseEntity<Customer>(brokerService.updateCustomer(customer, id), HttpStatus.ACCEPTED);
    }

    @DeleteMapping(value = "/delete/{id}")
    @PreAuthorize("hasAuthority('BROKER')")
    public ResponseEntity<Object> delete(@PathVariable int id){
        brokerService.deleteCustomer(id);
        return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value="/calculateLease", method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('BROKER')")
    public double calculateLease(@RequestParam(value = "Mileage" , required = true) double mileage,
                                 @RequestParam(value = "Duration", required = true) double duration,
                                 @RequestParam(value = "InterestRate", required = true) double interestRate,
                                 @RequestParam(value = "Make", required = true) String make,
                                 @RequestParam(value = "Model", required = true) String model){
        return brokerService.calculateCarLease(mileage,duration,interestRate,make,model);
    }
}
