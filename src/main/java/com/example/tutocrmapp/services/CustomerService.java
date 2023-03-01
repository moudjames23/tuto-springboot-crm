package com.example.tutocrmapp.services;

import com.example.tutocrmapp.exceptions.ResourceAlreadyExistException;
import com.example.tutocrmapp.exceptions.ResourceNotFoundException;
import com.example.tutocrmapp.models.Customer;
import com.example.tutocrmapp.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;


    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public List<Customer> getCustomers()
    {
        return this.customerRepository.findAll();
    }

    public Customer create(Customer customer)
    {
        Optional<Customer> optionalCustomer = this.customerRepository.findByPhoneNumber(customer.getPhoneNumber());

        if (optionalCustomer.isPresent())
            throw new ResourceAlreadyExistException("Le numéro de téléphone " +customer.getPhoneNumber()+ " existe déjà");

        return this.customerRepository.save(customer);
    }

    public Customer show(long customerId)
    {
        return getCustomerById(customerId);
    }

    public Customer getCustomerById(long customerId)
    {
        return  this.customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Le client avec pour ID " +customerId+ " n'existe pas"));
    }

    public Customer update(Long customerId, Customer customer)
    {
        this.getCustomerById(customerId);

        customer.setId(customerId);

        return this.customerRepository.save(customer);

    }

    public void delete(long customerId)
    {
        this.customerRepository.delete(getCustomerById(customerId));
    }
}
