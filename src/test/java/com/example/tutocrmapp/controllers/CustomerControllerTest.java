package com.example.tutocrmapp.controllers;

import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.repositories.CustomerRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @BeforeAll
    @AfterAll
    public void clearDatabase()
    {
        this.customerRepository.deleteAll();
    }

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    Customer moud = Customer.builder()
            .firstName("Moud")
            .lastName("Diallo")
            .phoneNumber("620_02_94_89")
            .build();

    Customer dalanda = Customer.builder()
            .firstName("Dalanda")
            .lastName("Kassé")
            .phoneNumber("626_65_68_20")
            .build();

    Customer yans = Customer.builder()
            .firstName("Lamine")
            .lastName("Yansaneé")
            .phoneNumber("622_09_51_43")
            .build();

    @Test
    @Order(1)
    void itShouldListEmptyCustomers() throws Exception{
        // Given
        // When
        // Then
        this.mockMvc.perform(
                MockMvcRequestBuilders.get("/api/v1/customers")
                        .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data", hasSize(0)));
    }

    @Test
    @Order(2)
    void itShouldListAllCustomers() throws Exception{
        // Given
        List<Customer> customerList = Arrays.asList(moud, dalanda);
        this.customerRepository.saveAll(customerList);

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data", hasSize(2)));
    }

    @Test
    @Order(2)
    void itShouldDisplayCustomerById() throws Exception {
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.firstName",  is(moud.getFirstName())))
                .andExpect(jsonPath("$.data.lastName",  is(moud.getLastName())))
                .andExpect(jsonPath("$.data.phoneNumber",  is(moud.getPhoneNumber())));
    }

    @Test
    @Order(3)
    void itShouldNotDisplayCustomerById() throws Exception {
        // Given
        long customerId = 23;

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/v1/customers/" +customerId)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status", is(404)))
                .andExpect(jsonPath("$.message", is("Le client avec pour ID " +customerId+" n'existe pas")));

    }

    @Test
    @Order(4)
    void itShouldCreateNewCustomer()  throws Exception{
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(yans))
                )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.data.firstName",  is(yans.getFirstName())))
                .andExpect(jsonPath("$.data.lastName",  is(yans.getLastName())))
                .andExpect(jsonPath("$.data.phoneNumber",  is(yans.getPhoneNumber())));
    }

    @Test
    @Order(5)
    void itShouldNotCreateNewCustomerWhenPhonNumberExists() throws Exception{
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(dalanda))
                )
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status", is(409)))
                .andExpect(jsonPath("$.message", is("Le numéro de téléphone "+dalanda.getPhoneNumber()+" existe déjà")));
    }

    @Test
    @Order(6)
    void itShouldNotCreateNewCustomerWhenFirstNameIsMissing() throws Exception{
        // Given
        Customer leon = Customer.builder()
                //.firstName("Léon")
                .lastName("Diallo")
                .phoneNumber("622_00_00_01")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(leon))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.firstName", is("Le prénom est obligatoire")));

    }

    @Test
    @Order(7)
    void itShouldNotCreateNewCustomerWhenLastNameIsMissing() throws Exception{
        // Given
        Customer leon = Customer.builder()
                .firstName("Léon")
                //.lastName("Diallo")
                .phoneNumber("622_00_00_01")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(leon))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.lastName", is("Le nom est obligatoire")));

    }

    @Test
    @Order(8)
    void itShouldNotCreateNewCustomerWhenPhoneNumberIsMissing() throws Exception{
        // Given
        Customer leon = Customer.builder()
                .firstName("Léon")
                .lastName("Diallo")
                //.phoneNumber("622_00_00_01")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(leon))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.phoneNumber", is("Le numéro de téléphone est obligatoire")));

    }

    @Test
    @Order(8)
    void itShouldNotCreateNewCustomerWhenAllFieldsAreMissing() throws Exception{
        // Given
        Customer leon = Customer.builder()
                //.firstName("Léon")
                //.lastName("Diallo")
                //.phoneNumber("622_00_00_01")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/v1/customers" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(leon))
                )
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("NOK")))
                .andExpect(jsonPath("$.errors.lastName", is("Le nom est obligatoire")))
                .andExpect(jsonPath("$.errors.firstName", is("Le prénom est obligatoire")))
                .andExpect(jsonPath("$.errors.phoneNumber", is("Le numéro de téléphone est obligatoire")));

    }

    @Test
    @Order(9)
    void itShouldUpdateCustomer() throws Exception{
        // Given
        Customer moudUpdated = Customer.builder()
                .firstName("Mamoudou")
                .lastName("Diallo")
                .phoneNumber("620_02_94_89")
                .build();

        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.put("/api/v1/customers/1" )
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(moudUpdated))
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("OK")))
                .andExpect(jsonPath("$.status", is(200)))
                .andExpect(jsonPath("$.data.firstName",  is(moudUpdated.getFirstName())))
                .andExpect(jsonPath("$.data.lastName",  is(moudUpdated.getLastName())))
                .andExpect(jsonPath("$.data.phoneNumber",  is(moudUpdated.getPhoneNumber())));
    }

    @Test
    @Order(10)
    void itShouldDeleteCustomerById() throws Exception{
        // Given
        // When
        // Then
        this.mockMvc.perform(
                        MockMvcRequestBuilders.delete("/api/v1/customers/3")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("OK")));

    }
}
