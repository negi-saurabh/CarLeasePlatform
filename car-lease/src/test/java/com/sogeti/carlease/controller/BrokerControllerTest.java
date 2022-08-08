package com.sogeti.carlease.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sogeti.carlease.models.Customer;
import com.sogeti.carlease.services.BrokerService;
import com.sogeti.carlease.services.CarLeaseService;
import com.sogeti.carlease.services.LoginService;
import com.sogeti.carlease.utils.JWTUtility;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
import org.springframework.test.web.servlet.MvcResult;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class BrokerControllerTest {


  @Autowired
  ObjectMapper mapper;
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private BrokerService brokerService;
  @MockBean
  private CarLeaseService carLeaseService;
  @MockBean
  private LoginService loginService;
  @Autowired
  private JWTUtility jwtUtility;
  private String token;

  private Customer mockCustomer;

  private static String asJson(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @BeforeEach
  public void setup() {
    List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("BROKER");
    Mockito.when(loginService.loadUserByUsername(Mockito.anyString()))
        .thenReturn(new User("admin", "password", authorities));
    token = jwtUtility.generateToken("admin", authorities);
    mockCustomer = getCustomer();
  }

  @Test
  @DisplayName("GET /api/customer/all - Found All")
  public void testGetAllCustomers() throws Exception {
    List<Customer> customers = new ArrayList<>();
    customers.add(mockCustomer);

    when(brokerService.getAllCustomers()).thenReturn(customers);
    mockMvc.perform(get("/api/customer/all")
            .header("AUTHORIZATION", "Bearer " + token)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].customerId", is(mockCustomer.getCustomerId())))
        .andExpect(jsonPath("$[0].name", is(mockCustomer.getName())))
        .andExpect(jsonPath("$[0].street", is(mockCustomer.getStreet())))
        .andExpect(jsonPath("$[0].houseNumber", is(mockCustomer.getHouseNumber())))
        .andExpect(jsonPath("$[0].zipCode", is(mockCustomer.getZipCode())))
        .andExpect(jsonPath("$[0].place", is(mockCustomer.getPlace())))
        .andExpect(jsonPath("$[0].emailAddress", is(mockCustomer.getEmailAddress())))
        .andExpect(jsonPath("$[0].phoneNumber", is(mockCustomer.getPhoneNumber())))
        .andExpect(content().contentType(APPLICATION_JSON));

  }

  @Test
  @DisplayName("GET /api/customer/4 - Found")
  public void testGetCustomerById() throws Exception {
    when(brokerService.findById(Mockito.anyInt())).thenReturn(Optional.of(mockCustomer));
    mockMvc.perform(get("/api/customer/" + mockCustomer.getCustomerId())
            .header("AUTHORIZATION", "Bearer " + token)
            .contentType(APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  @DisplayName("GET /api/customer/{id} - Not Found")
  public void testGetCustomerByIdNotFound() throws Exception {
    when(brokerService.findById(20)).thenReturn(Optional.empty());

    mockMvc.perform(get("/api/customer/" + 20)
            .header("AUTHORIZATION", "Bearer " + token)
            .contentType(APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  @DisplayName("POST /api/customer/addNew - Success")
  public void testAddCustomer() throws Exception {
    when(brokerService.createCustomer(Mockito.any(Customer.class))).thenReturn(mockCustomer);

    mockMvc.perform(post("/api/customer/addNew").content(asJson(mockCustomer))
            .header("AUTHORIZATION", "Bearer " + token)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().contentType(APPLICATION_JSON))
        .andReturn();
  }

  @Test
  @DisplayName("PUT /api/customer/update - Success")
  public void testUpdateCustomer() throws Exception {
    when(brokerService.updateCustomer(mockCustomer, 3)).thenReturn(mockCustomer);

    mockMvc.perform(put("/api/customer/update/" + mockCustomer.getCustomerId())
            .content(this.mapper.writeValueAsBytes(mockCustomer))
            .header("AUTHORIZATION", "Bearer " + token)
            .contentType(APPLICATION_JSON))
        .andExpect(status().isAccepted())
        .andExpect(content().contentType(APPLICATION_JSON));
  }

  @Test
  @DisplayName("DELETE /api/customer/delete - Success")
  public void testDeleteCustomer() throws Exception {
    BrokerService serviceSpy = Mockito.spy(brokerService);
    doNothing().when(serviceSpy).deleteCustomer(mockCustomer.getCustomerId());
    mockMvc.perform(
            delete("/api/customer/delete/" + mockCustomer.getCustomerId()).content(asJson(mockCustomer))
                .header("AUTHORIZATION", "Bearer " + token)
                .contentType(APPLICATION_JSON))
        .andExpect(status().isNoContent());
  }

  @Test
  @DisplayName("GET /api/customer/calculateLease - Success")
  public void testCarLeaseValue() throws Exception {
    BrokerService serviceSpy = Mockito.spy(brokerService);
    when(serviceSpy.calculateCarLease(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyDouble(),
        Mockito.anyString(), Mockito.anyString())).thenReturn(Mockito.anyDouble());
    MvcResult result = mockMvc.perform(get("/api/customer/calculateLease")
            .header("AUTHORIZATION", "Bearer " + token)
            .param("Mileage", "45000")
            .param("Duration", "60")
            .param("InterestRate", "4.5")
            .param("Make", "honda")
            .param("Model", "city")
            .contentType(APPLICATION_JSON))
        .andReturn();

    assertEquals(239.76, result.getResponse().getContentAsString());
  }

  private Customer getCustomer() {
    Customer customer = new Customer();
    customer.setCustomerId(3);
    customer.setEmailAddress("xyz@gmail.com");
    customer.setHouseNumber("A-50");
    customer.setName("Sam");
    customer.setPlace("xyz");
    customer.setPhoneNumber("+9112312312");
    customer.setStreet("hgh");
    customer.setZipCode("asd");
    return customer;
  }

}
