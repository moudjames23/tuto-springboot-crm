package com.example.tutocrmapp.repositories;

import com.example.tutocrmapp.models.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest( properties = {
        "spring.jpa.properties.javax.persistence.validation.mode=none"
})
class CustomerRepositoryTest {

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    void itShouldFindCustomerByPhoneNumber() {
        // Given
        Customer customer = Customer.builder()
                .firstName("Moud")
                .lastName("Diallo")
                .phoneNumber("620_00_00_00")
                .build();
        this.customerRepository.save(customer);

        // When
        Optional<Customer> excepted = this.customerRepository.findByPhoneNumber(customer.getPhoneNumber());

        // Then
        assertThat(excepted).isPresent();
        assertThat(excepted.get().getFirstName()).isEqualTo(customer.getFirstName());
        assertThat(excepted.get().getLastName()).isEqualTo(customer.getLastName());
        assertThat(excepted.get().getPhoneNumber()).isEqualTo(customer.getPhoneNumber());
    }

    @Test
    void itShouldNotFindCustomerWhenPhoneNumberDoesExist() {
        // Given
        String phone = "626656820";

        // When
        Optional<Customer> customer = this.customerRepository.findByPhoneNumber(phone);

        // Then
        assertThat(customer).isEmpty();
    }


    @Test
    void itShouldSaveCustomerWhenFirstNameIsRequire() {
        // Given
        Customer customer = Customer.builder()
                .lastName("Diallo")
                .phoneNumber("620_00_00_01")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldSaveCustomerWhenLastNameIsRequire() {
        // Given
        Customer customer = Customer.builder()
                .firstName("Moud")
                .phoneNumber("620_00_00_01")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void itShouldSaveCustomerWhenPhoneNumberIsRequire() {
        // Given
        Customer customer = Customer.builder()
                .lastName("Moud")
                .firstName("Diallo")
                .build();

        // When
        // Then
        assertThatThrownBy(() -> customerRepository.save(customer))
                .isInstanceOf(DataIntegrityViolationException.class);
    }
}
