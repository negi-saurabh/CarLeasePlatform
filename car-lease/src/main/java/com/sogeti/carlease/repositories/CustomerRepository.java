package com.sogeti.carlease.repositories;

import com.sogeti.carlease.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CustomerRepository extends JpaRepository<Customer , Integer> {
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN TRUE ELSE FALSE END FROM Customer c WHERE c.customerId = ?1")
    Boolean doesCustomerExitsById(int i);
}
