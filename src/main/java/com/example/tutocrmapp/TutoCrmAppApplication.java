package com.example.tutocrmapp;

import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.repositories.CustomerRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

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
    public CommandLineRunner loadData(final CustomerRepository customerRepository)
    {
        return args -> {
            Customer customer = Customer.builder()
                    .firstName("Moud")
                    .lastName("Diallo")
                    .phoneNumber("620029489")
                    .build();

            //customerRepository.save(customer);
        };
    }
}
