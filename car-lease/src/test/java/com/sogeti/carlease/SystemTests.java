package com.sogeti.carlease;

import com.sogeti.carlease.models.Car;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class SystemTests {

    @Test
    public void testCreateReadDelete(){
        RestTemplate restTemplate = new RestTemplate();

        String url = "http://localhost:8080/api/car";
        Car car = new Car(4, "Honda", "civic", "1.0", 4, "234" , 234.0, 234.8 );
        ResponseEntity<Car> entity = restTemplate.postForEntity(url+"/addNew", car, Car.class);

        Car[] cars = restTemplate.getForObject(url+"/all", Car[].class);
        assertThat(cars).extracting(Car::getModel).containsOnly("civic");

        restTemplate.delete(url + "/delete/" +entity.getBody().getCarId());
        assertThat(restTemplate.getForObject(url, Car[].class)).isEmpty();


    }
}
