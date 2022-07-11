package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.services.BrokerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

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
}
