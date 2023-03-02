package com.example.tutocrmapp;

import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class TutoCrmAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(TutoCrmAppApplication.class, args);
    }

    @Bean
    ModelMapper modelMapper() {
        return  new ModelMapper();
    }

    @Bean
    CommandLineRunner dataLoader(final CustomerRepository customerRepository) {
        return args -> {
            Customer customer1 = Customer.builder()
                    .id(1L)
                    .firstName("M'Mah")
                    .lastName("Touré")
                    .phoneNumber("623232323")
                    .build();

            Customer customer2 = Customer.builder()
                    .id(2L)
                    .firstName("Grand")
                    .lastName("P")
                    .phoneNumber("624242424")
                    .build();

            Customer customer3 = Customer.builder()
                    .id(3L)
                    .firstName("Koto")
                    .lastName("Woulin")
                    .phoneNumber("625252525")
                    .build();

            List<Customer> customers = Arrays.asList(customer1, customer2, customer3);

            customerRepository.saveAll(customers);

        };
    }
}
