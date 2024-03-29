package com.sogeti.carlease.services;

import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.repositories.CustomerRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
 * Service class for Broker
 */

@Service
public class BrokerService {

  @Autowired
  public CustomerRepository customerRepository;

  @Autowired
  public CarLeaseService carLeaseService;

  @Autowired
  public void setCarLeaseService(CarLeaseService carLeaseService) {
    this.carLeaseService = carLeaseService;
  }

  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  public Optional<Customer> findById(int id) {
    return customerRepository.findById(id);
  }

  public Customer createCustomer(Customer customer) {
    return customerRepository.saveAndFlush(customer);
  }

  public Customer updateCustomer(Customer customer, int id) {
    Customer existingCustomer = customerRepository.getReferenceById(id);
    BeanUtils.copyProperties(customer, existingCustomer, "customerId");
    return customerRepository.saveAndFlush(existingCustomer);
  }

  public void deleteCustomer(int id) {
    customerRepository.deleteById(id);
  }

  public double calculateCarLease(double mileage, double duration, double interestRate, String make,
      String model) {
    return carLeaseService.calculateCarLease(mileage, duration, interestRate, make, model);
  }
}
