package com.example.tutocrmapp.services;

import com.example.tutocrmapp.exceptions.ResourceAlreadyExistException;
import com.example.tutocrmapp.exceptions.ResourceNotFoundException;
import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.repositories.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

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

    @BeforeEach
    void setUp() {
        this.customerService = new CustomerService(customerRepository);
    }

    @Test
    void itShouldListCustomers() {
        // Given
        // When
        this.customerService.getCustomers();

        // Then
        verify(this.customerRepository).findAll();
    }

    @Test
    void itShouldSaveNewCustomer() {
        // Given
        // When
        this.customerService.create(moud);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(this.customerRepository).save(argumentCaptor.capture());

        Customer excepted = argumentCaptor.getValue();

        // Then
        assertThat(excepted.getLastName()).isEqualTo(moud.getLastName());
        assertThat(excepted.getFirstName()).isEqualTo(moud.getFirstName());
        assertThat(excepted.getPhoneNumber()).isEqualTo(moud.getPhoneNumber());
    }

    @Test
    void itShouldNotSaveCustomerWhenPhoneNumberExist() {
        // Given
        when(this.customerRepository.findByPhoneNumber(dalanda.getPhoneNumber())).thenReturn(Optional.ofNullable(dalanda));

        // When
        // Then
        assertThatThrownBy(() -> this.customerService.create(dalanda))
                .isInstanceOf(ResourceAlreadyExistException.class)
                .hasMessage("Le numéro de téléphone " +dalanda.getPhoneNumber()+ " existe déjà");

        verify(this.customerRepository, never()).save(dalanda);
    }

    @Test
    void itShouldShowCustomerById() {
        // Given
        when(this.customerRepository.findById(moud.getId())).thenReturn(Optional.ofNullable(moud));
        // When
        this.customerService.show(moud.getId());

        ArgumentCaptor<Long> argumentCaptor = ArgumentCaptor.forClass(Long.class);

        verify(this.customerRepository).findById(argumentCaptor.capture());

        Long customerId = argumentCaptor.getValue();

        // Then
        assertThat(customerId).isEqualTo(customerId);

    }

    @Test
    void itShouldNotShowWhenCustomerIdDoesnotExist() {
        // Given
        long customerId = 23;

        // When
        // Then
        assertThatThrownBy(() -> this.customerService.show(customerId))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Le client avec pour ID " +customerId+ " n'existe pas");
    }

    @Test
    void itShouldUpdateCustomer() {
        // Given
        long customerId = 1L;
        Customer moudUpdated = Customer.builder()
                .firstName("Mamoudou")
                .lastName("Diallo")
                .phoneNumber("620_02_94_89")
                .build();

        // When
        when(this.customerRepository.findById(customerId)).thenReturn(Optional.ofNullable(moud));

        this.customerService.update(customerId, moudUpdated);

        ArgumentCaptor<Customer> argumentCaptor = ArgumentCaptor.forClass(Customer.class);

        verify(this.customerRepository).save(argumentCaptor.capture());

        // Then

        Customer excepted = argumentCaptor.getValue();

        assertThat(excepted.getFirstName()).isEqualTo(moudUpdated.getFirstName());
        assertThat(excepted.getLastName()).isEqualTo(moud.getLastName());
    }

    @Test
    void itShouldDeleteCustomerById() {
        // Given
        when(this.customerRepository.findById(dalanda.getId())).thenReturn(Optional.ofNullable(dalanda));
        // When
        this.customerService.delete(dalanda.getId());

        // Then
        verify(this.customerRepository).delete(dalanda);
    }
}
