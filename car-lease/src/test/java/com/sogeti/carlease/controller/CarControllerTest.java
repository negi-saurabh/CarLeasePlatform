package com.sogeti.carlease.controller;

import com.sogeti.carlease.controllers.CarController;
import com.sogeti.carlease.controllers.LoginController;
import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.models.JWTRequest;
import com.sogeti.carlease.services.LoginService;
import com.sogeti.carlease.utils.JWTUtility;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.asMediaType;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CarController carController;

    @MockBean
    private LoginController loginController;



   /* @Test
    @DisplayName("GET /api/car/all - Found All")
    public void testGetAllCars() throws Exception{
        Car mockCar =  getCar();
        List<Car> cars = new ArrayList<>();
        cars.add(mockCar);

        given(carController.readAll()).willReturn(cars);
        mockMvc.perform(get("/api/car/all"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("$[0].make", is(mockCar.getMake())));
    }*/

    @Test
    @DisplayName("GET /api/car/4 - Found")
    public void testGetCarById() throws Exception {
        // mock set up
        Car mockCar = getCar();
        given(carController.read(4)).willReturn(mockCar);

        JWTRequest jwtRequest = new JWTRequest();
        jwtRequest.setUserName("admin");
        jwtRequest.setPassword("password");
        jwtRequest.setAuthorities(new ArrayList<>());

        final String token = loginController.authenticate(jwtRequest).getJwtToken();
        mockMvc.perform(get("/api/car/" + mockCar.getCarId()).header("Authorization", token).contentType(APPLICATION_JSON).with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andExpect(jsonPath("make", is(mockCar.getMake())));
    }

   /*  @Test
    @DisplayName("GET /api/car/1 - Not Found")
    public void testGetCarByIdNotFound() throws Exception {
        Car mockCar = getCar();

        given(carController.read(1)).willReturn(mockCar);
        mockMvc.perform(get("/api/car/" + mockCar.getCarId()))
                .andExpect(status().isNotFound());
    }*/

   /* @Test
    @DisplayName("POST /api/car/addNew - Success")
    public void testAddCar() throws Exception {
        Car mockCar = getCar();

        doNothing().when(carController).create(mockCar);
        mockMvc.perform(post("/api/car" +"/addNew").content(asJson(mockCar)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();
    }
*/
   /* @Test
    @DisplayName("PUT /api/car/update - Success")
    public void testUpdateCar() throws Exception {
        Car mockCar = getCar();

        doNothing().when(carController).update(5, mockCar);
        mockMvc.perform(put("/api/car" +"/update"+mockCar.getCarId()).content(asJson(mockCar)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();
    }*/

   /* @Test
    @DisplayName("PUT /api/car/update - Not Found")
    public void testUpdateCarNotFound() throws Exception {
        Car mockCar = getCar();

        doNothing().when(carController).update(1, mockCar);
        mockMvc.perform(put("/api/car" +"/update"+mockCar.getCarId()).content(asJson(mockCar)))
                .andExpect(status().isNotFound());
    }*/

   /* @Test
    @DisplayName("DELETE /api/car/delete - Success")
    public void testDeleteCar() throws Exception {
        Car mockCar = getCar();

        doNothing().when(carController).delete(4);
        mockMvc.perform(delete("/api/car/delete/" +  mockCar.getCarId()))
                .andExpect(status().isOk());

    }*/

   /* @Test
    @DisplayName("DELETE /api/car/delete - Not Found")
    public void testDeleteNotFoundCar() throws Exception {
        Car mockCar = getCar();

        doNothing().when(carController).delete(1);
        mockMvc.perform(delete("/api/car/delete/" +  mockCar.getCarId()))
                .andExpect(status().isNotFound());
    }*/

    private Car getCar() {
        Car car = new Car();
        car.setCarId(4);
        car.setGrossPrice(33000);
        car.setNettPrice(40000);
        car.setCo2Emissions("23.6");
        car.setMake("Volvo");
        car.setModel("X40");
        car.setVersion("1.0");
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
