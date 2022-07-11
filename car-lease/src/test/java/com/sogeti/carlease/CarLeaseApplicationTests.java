package com.sogeti.carlease;

import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.repositories.CarRepository;
import com.sogeti.carlease.repositories.CustomerRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class CarLeaseApplicationTests {

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void contextLoads() {
		Assertions.assertNotNull(carRepository);
		Assertions.assertNotNull(customerRepository);
	}

	@Test
	void doesCarExitsById() {
		Car car = new Car(4, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
		carRepository.save(car);
		Boolean actualResult = carRepository.doesCarExitsById(4);
		assertThat(actualResult).isTrue();
	}

	@Test
	void doesCustomerExitsById() {
		Customer customer = new Customer(5, "y", "z",
			"12FV", "A", "B" ,"C", "D" );
		customerRepository.saveAndFlush(customer);
		Boolean actualResult = customerRepository.doesCustomerExitsById(5);
		assertThat(actualResult).isTrue();
	}
}
