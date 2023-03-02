package com.example.tutocrmapp.controllers;

import com.example.tutocrmapp.dtos.CustomerDto;
import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.services.CustomerService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.tutocrmapp.MyHttResponse.response;

@RestController
@RequestMapping("api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;
    private final ModelMapper modelMapper;

    public CustomerController(CustomerService customerService, ModelMapper modelMapper) {
        this.customerService = customerService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Object> customers()
    {
        List<CustomerDto> customers = this.customerService.getCustomers()
                .stream()
                .map(customer -> modelMapper.map(customer, CustomerDto.class))
                .collect(Collectors.toList());

        return response(HttpStatus.OK, "OK", customers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> show(@PathVariable("id") long customerId)
    {
        Customer customer = this.customerService.show(customerId);

        return response(HttpStatus.OK, "OK", customer);
    }

    @PostMapping
    public ResponseEntity<Object> add(@RequestBody @Valid CustomerDto customerDto)
    {
        Customer customer = this.customerService.create(modelMapper.map(customerDto, Customer.class));

        return response(HttpStatus.CREATED, "OK", customer);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Object> update(@PathVariable("id") long customerId, @Valid @RequestBody CustomerDto customerDto)
    {
        Customer customer = this.customerService.update(
                customerId, modelMapper.map(customerDto, Customer.class)
                );

        return  response(HttpStatus.OK, "OK", customer);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> delete(@PathVariable("id") long customerId)
    {
        this.customerService.delete(customerId);

        return response(HttpStatus.OK, "OK", null);
    }
}
