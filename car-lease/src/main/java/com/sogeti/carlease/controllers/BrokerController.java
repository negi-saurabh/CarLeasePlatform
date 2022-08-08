package com.sogeti.carlease.controllers;

import com.sogeti.carlease.exceptions.CarNotFoundException;
import com.sogeti.carlease.exceptions.CustomerNotFoundException;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.services.BrokerService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;

/*
 * Contains all the methods accessible to a BROKER to maintain Customer's data
 * A Broker can do all the CRUD operations for a Customer
 */

@RestController
@RequestMapping("/api/customer")
public class BrokerController {

  @Autowired
  private BrokerService brokerService;


  /*
   * Returns data of all the Customers
   */
  @GetMapping(value = "/all")
  @PreAuthorize("hasAuthority('BROKER')")
  public ResponseEntity<List<Customer>> readAll() {
    List<Customer> customers = brokerService.getAllCustomers();
    if (customers.isEmpty()) {
      throw new CarNotFoundException("No Customer records were found");
    }
    return new ResponseEntity<List<Customer>>(customers, HttpStatus.OK);
  }

  /*
   * Returns data of the Customer with given id
   * @PathVariable contains the id of the Customer
   * @return the Customer Entity with given customer id
   */
  @GetMapping(value = "/{id}")
  @PreAuthorize("hasAuthority('BROKER')")
  public ResponseEntity<Customer> read(@PathVariable int id) {
    Optional<Customer> optCustomer = brokerService.findById(id);
    if (optCustomer.isPresent()) {
      return ResponseEntity.ok(optCustomer.get());
    } else {
      throw new CustomerNotFoundException("Customer with customer Id (" + id + ") not found!");
    }
  }


  /*
   * Creates a new Customer
   * @RequestBody contains the all the attributes of the Customer
   * @return the newly created Customer Entity
   */
  @PostMapping(value = "/addNew")
  @PreAuthorize("hasAuthority('BROKER')")
  public ResponseEntity<Customer> create(@RequestBody final Customer customer) {
    Optional<Customer> objCustomer = brokerService.findById(customer.getCustomerId());
      if (objCustomer.isPresent()) {
          throw new HttpClientErrorException(HttpStatus.CONFLICT,
              "Customer with ID" + "(" + customer.getCustomerId() + ") already exists");
      } else {
          return new ResponseEntity<Customer>(brokerService.createCustomer(customer),
              HttpStatus.CREATED);
      }
  }

  /*
   * Update the data of an existing Customer
   * @PathVariable contains the id of the Customer
   * @RequestBody contains the all new the attributes values of the Customer
   * @return the updated Customer data
   */
  @PutMapping(value = "/update/{id}")
  @PreAuthorize("hasAuthority('BROKER')")
  public ResponseEntity<Customer> update(@PathVariable int id, @RequestBody Customer customer) {
    return new ResponseEntity<Customer>(brokerService.updateCustomer(customer, id),
        HttpStatus.ACCEPTED);
  }

  /*
   * Deletes the data of an existing Customer with the given customer id
   */
  @DeleteMapping(value = "/delete/{id}")
  @PreAuthorize("hasAuthority('BROKER')")
  public ResponseEntity<Object> delete(@PathVariable int id) {
    brokerService.deleteCustomer(id);
    return new ResponseEntity<Object>(HttpStatus.NO_CONTENT);
  }

  /*
   * Calculates the value of car lease for a Broker
   * Communicates with CarLeaseService in order to calculate the lease value
   * @RequestParam contains Mileage, Duration , InterestRate , Make and Model which are necessary
   * for lease value to be calculated.
   */
  @RequestMapping(value = "/calculateLease", method = RequestMethod.GET)
  @PreAuthorize("hasAuthority('BROKER')")
  public Double calculateLease(@RequestParam(value = "Mileage", required = true) double mileage,
      @RequestParam(value = "Duration", required = true) double duration,
      @RequestParam(value = "InterestRate", required = true) double interestRate,
      @RequestParam(value = "Make", required = true) String make,
      @RequestParam(value = "Model", required = true) String model) {
    return brokerService.calculateCarLease(mileage, duration, interestRate, make, model);
  }
}
