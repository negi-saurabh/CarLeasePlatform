package com.sogeti.carlease.controller;

import com.sogeti.carlease.controllers.CarController;
import com.sogeti.carlease.models.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CarController carController;

    @Test
    public void testGetAllCars() throws Exception{
        Car car = new Car(4, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
        List<Car> cars = new ArrayList<>();
        cars.add(car);

        given(carController.readAll()).willReturn(cars);
        mvc.perform(get("/api/car/all").contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$[0].make", is(car.getMake())));
    }


    @Test
    public void testGetCar() throws Exception {
        Car car = new Car(1, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
        given(carController.read(1)).willReturn(car);
        mvc.perform(get("/api/car/" + car.getCarId()).contentType(APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("make", is(car.getMake())));
    }
    @Test
    public void testDeleteCar() throws Exception {
        Car car = new Car(4, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
        doNothing().when(carController).delete(4);
        mvc.perform(delete("/api/car/delete/" +  car.getCarId()).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void testAddCar() throws Exception {
        Car car = new Car(4, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
        doNothing().when(carController).create(car);
        mvc.perform(post("/api/car" +"/addNew").content(asJson(car)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
    @Test
    public void testUpdateCar() throws Exception {
        Car car = new Car(4, "Honda", "civis", "1.0", 4, "234" , 234.0, 234.8 );
        doNothing().when(carController).update(4, car);
        mvc.perform(put("/api/car" +"/update"+car.getCarId()).content(asJson(car)).contentType(APPLICATION_JSON))
                .andExpect(status().isOk()).andReturn();
    }
    private Car getCar() {
        Car car = new Car();
        car.setGrossPrice(34534);
        car.setNettPrice(34535);
        car.setCo2Emissions("232");
        car.setMake("Toyota");
        car.setModel("xyz");
        car.setVersion("3.0");
        return car;
    }

    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
