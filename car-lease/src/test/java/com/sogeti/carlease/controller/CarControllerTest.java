package com.sogeti.carlease.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.carlease.controllers.CarController;
import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.services.LoginService;
import com.sogeti.carlease.utils.JWTUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarController carController;

    @MockBean
    private LoginService loginService;

    @Autowired
    private JWTUtility jwtUtility;

    private String token;

    @BeforeEach
    public void setup() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("EMPLOYEE");
        Mockito.when(loginService.loadUserByUsername(Mockito.anyString())).thenReturn(new User("admin", "password", authorities));
        token = jwtUtility.generateToken("admin", authorities);
    }

    @Test
    @DisplayName("GET /api/car/all - Found All")
    public void testGetAllCars() throws Exception{
        Car mockCar =  getCar();
        List<Car> cars = new ArrayList<>();
        cars.add(mockCar);

        mockMvc.perform(get("/api/car/all")
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/car/4 - Found")
    public void testGetCarById() throws Exception {
        // mock set up
        Car mockCar = getCar();

        mockMvc.perform(get("/api/car/" + mockCar.getCarId())
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/car/1 - Not Found")
    public void testGetCarByIdNotFound() throws Exception {
        Car mockCar = getCar();

        mockMvc.perform(get("/api/car/" + 44)
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/car/addNew - Success")
    public void testAddCar() throws Exception {
        Car mockCar = getCar();

        mockMvc.perform(post("/api/car/" +"addNew").content(asJson(mockCar))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType(APPLICATION_JSON))
                        .andReturn();
    }

    @Test
    @DisplayName("PUT /api/car/update - Success")
    public void testUpdateCar() throws Exception {
        Car mockCar = getCar();

        mockMvc.perform(put("/api/car/" +"update"+mockCar.getCarId()).content(asJson(mockCar))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType(APPLICATION_JSON))
                        .andReturn();
    }

    @Test
    @DisplayName("PUT /api/car/update - Not Found")
    public void testUpdateCarNotFound() throws Exception {
        Car mockCar = getCar();

        mockMvc.perform(put("/api/car/" +"update"+mockCar.getCarId()).content(asJson(mockCar))
                .header("AUTHORIZATION","Bearer "+token)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/car/delete - Success")
    public void testDeleteCar() throws Exception {
        Car mockCar = getCar();

        mockMvc.perform(delete("/api/car/delete/" +  mockCar.getCarId())
                .header("AUTHORIZATION","Bearer "+token)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /api/car/delete - Not Found")
    public void testDeleteNotFoundCar() throws Exception {
        Car mockCar = getCar();
        mockMvc.perform(delete("/api/car/delete/" + 1)
                .header("AUTHORIZATION","Bearer "+token)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

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
