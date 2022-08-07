package com.sogeti.carlease.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.carlease.controllers.CarController;
import com.sogeti.carlease.models.Car;
import com.sogeti.carlease.services.CarLeaseService;
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
import java.util.Optional;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerTest {

    /*
     * We can @Autowire MockMvc because the WebApplicationContext provides an
     * instance/bean for us
     */
    @Autowired
    private MockMvc mockMvc;

    /*
     * We use @MockBean because the WebApplicationContext does not provide
     * any @Component, @Service or @Repository beans instance/bean of this service
     * in its context. It only loads the beans solely required for testing the
     * controller.
     */
    @MockBean
    private CarLeaseService carLeaseService;

    @MockBean
    private LoginService loginService;

    @Autowired
    private JWTUtility jwtUtility;

    /*
     * Jackson mapper for Object -> JSON conversion
     */
    @Autowired
    ObjectMapper mapper;

    private String token;

    private Car mockCar;

    @BeforeEach
    public void setup() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("EMPLOYEE");
        when(loginService.loadUserByUsername(Mockito.anyString())).thenReturn(new User("admin", "password", authorities));
        token = jwtUtility.generateToken("admin", authorities);
        mockCar =  getCar();
    }

    @Test
    @DisplayName("GET /api/car/all - Found All")
    public void testGetAllCars() throws Exception{
        List<Car> cars = new ArrayList<>();
        cars.add(mockCar);

        when(carLeaseService.getAllCars()).thenReturn(cars);
        mockMvc.perform(get("/api/car/all")
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$[0].carId", is(mockCar.getCarId())))
                        .andExpect(jsonPath("$[0].make", is(mockCar.getMake())))
                        .andExpect(jsonPath("$[0].model", is(mockCar.getModel())))
                        .andExpect(jsonPath("$[0].version", is(mockCar.getVersion())))
                        .andExpect(jsonPath("$[0].numberOfDoors", is(mockCar.getNumberOfDoors())))
                        .andExpect(jsonPath("$[0].co2Emissions", is(mockCar.getCo2Emissions())))
                        .andExpect(jsonPath("$[0].grossPrice", is(mockCar.getGrossPrice())))
                        .andExpect(jsonPath("$[0].nettPrice", is(mockCar.getNettPrice())))
                        .andExpect(content().contentType(APPLICATION_JSON));

    }

    @Test
    @DisplayName("GET /api/car/{id} - Found with ID")
    public void testGetCarById() throws Exception {
        when(carLeaseService.findById(Mockito.anyInt())).thenReturn(Optional.of(mockCar));
        mockMvc.perform(get("/api/car/" + mockCar.getCarId())
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/car/{id} - Not Found")
    public void testGetCarByIdNotFound() throws Exception {
        when(carLeaseService.findById(20)).thenReturn(Optional.empty());
        mockMvc.perform(get("/api/car/" + 20)
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                        .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/car/addNew - Success")
    public void testAddCar() throws Exception {
        when(carLeaseService.createCar(Mockito.any(Car.class))).thenReturn(mockCar);

        mockMvc.perform(post("/api/car/addNew").content(asJson(mockCar))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isCreated())
                        .andExpect(content().contentType(APPLICATION_JSON))
                        .andReturn();
    }

    @Test
    @DisplayName("PUT /api/car/update - Success")
    public void testUpdateCar() throws Exception {
        when(carLeaseService.updateCar( mockCar,1)).thenReturn(mockCar);
        mockMvc.perform(put("/api/car/update/" + mockCar.getCarId())
                        .content(this.mapper.writeValueAsBytes(mockCar))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                        .andExpect(status().isAccepted())
                        .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("DELETE /api/car/delete - Success")
    public void testDeleteCar() throws Exception {
        CarLeaseService serviceSpy = Mockito.spy(carLeaseService);
        doNothing().when(serviceSpy).deleteCar(mockCar.getCarId());
        mockMvc.perform(delete("/api/car/delete/" +  mockCar.getCarId())
                .header("AUTHORIZATION","Bearer "+token)
                .contentType(APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }


    private Car getCar() {
        Car car = new Car();
        car.setCarId(1);
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
