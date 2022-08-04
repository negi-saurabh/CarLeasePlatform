package com.sogeti.carlease.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.carlease.controllers.BrokerController;
import com.sogeti.carlease.models.Customer;
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
public class BrokerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BrokerController brokerController;

    @MockBean
    private LoginService loginService;

    @Autowired
    private JWTUtility jwtUtility;

    private String token;

    @BeforeEach
    public void setup() {
        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("BROKER");
        Mockito.when(loginService.loadUserByUsername(Mockito.anyString())).thenReturn(new User("admin", "password", authorities));
        token = jwtUtility.generateToken("admin", authorities);
    }

    @Test
    @DisplayName("GET /api/customer/all - Found All")
    public void testGetAllCars() throws Exception{
        Customer mockCustomer =  getCustomer();
        List<Customer> customers = new ArrayList<>();
        customers.add(mockCustomer);

        mockMvc.perform(get("/api/customer/all")
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    @DisplayName("GET /api/customer/4 - Found")
    public void testGetCarById() throws Exception {
        // mock set up
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(get("/api/customer/" + mockCustomer.getCustomerId())
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("GET /api/customer/1 - Not Found")
    public void testGetCarByIdNotFound() throws Exception {
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(get("/api/customer/" + 44)
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("POST /api/customer/addNew - Success")
    public void testAddCar() throws Exception {
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(post("/api/customer/" +"addNew").content(asJson(mockCustomer))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @DisplayName("PUT /api/customer/update - Success")
    public void testUpdateCar() throws Exception {
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(put("/api/customer/" +"update"+mockCustomer.getCustomerId()).content(asJson(mockCustomer))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON))
                .andReturn();
    }

    @Test
    @DisplayName("PUT /api/customer/update - Not Found")
    public void testUpdateCarNotFound() throws Exception {
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(put("/api/customer/" +"update"+mockCustomer.getCustomerId()).content(asJson(mockCustomer))
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("DELETE /api/customer/delete - Success")
    public void testDeleteCar() throws Exception {
        Customer mockCustomer =  getCustomer();

        mockMvc.perform(delete("/api/customer/delete/" +  mockCustomer.getCustomerId())
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("DELETE /api/customer/delete - Not Found")
    public void testDeleteNotFoundCar() throws Exception {
        Customer mockCustomer =  getCustomer();
        mockMvc.perform(delete("/api/customer/delete/" + 1)
                        .header("AUTHORIZATION","Bearer "+token)
                        .contentType(APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    private Customer getCustomer(){
        Customer customer = new Customer();
        customer.setCustomerId(3);
        customer.setEmailAddress("xyz@gmail.com");
        customer.setHouseNumber("A-50");
        customer.setName("Sam");
        customer.setPlace("sdaasasd");
        customer.setPhoneNumber("+9112312312");
        customer.setStreet("hgh");
        customer.setZipCode("asd");
        return customer;
    }

    private static String asJson(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
