package com.sogeti.carlease.controllers;

import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.repositories.CustomerRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping
    public List<Customer> customerList(){
        return customerRepository.findAll();
    }

    @GetMapping
    @RequestMapping("{id}")
    public Customer get(@PathVariable Long id){
        return customerRepository.getReferenceById(id);
    }

    @PostMapping("/createCustomers")
    public Customer create(@RequestBody final Customer customer){
        return customerRepository.saveAndFlush(customer);
    }

    @GetMapping("/getAllCustomers")
    public List<Customer> getAllCustomers(){
        return customerRepository.findAll();
    }

    @RequestMapping(value = "{id}", method = RequestMethod.DELETE)
    public void deleteCar(@PathVariable Long id){
        //need to check children records before deleting
        customerRepository.deleteById(id);
    }

    @RequestMapping(value= "{id}", method = RequestMethod.PUT)
    public Customer update(@PathVariable Long id, @RequestBody Customer customer){
        // we need to add the validation that all attributes are passed in, else return a 400 bad payload
        Customer existingCustomer = customerRepository.getReferenceById(id);
        BeanUtils.copyProperties(customer, existingCustomer, "customerId");
        return customerRepository.saveAndFlush(existingCustomer);
    }
}
