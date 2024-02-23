package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.hibernate.*;
import org.hibernate.query.Query;

public class CustomerDao{

    private List<Customer> customers = new ArrayList<>();

    public CustomerDao(Session session) {

        Query<Customer> getCustomers = session.createNamedQuery("get_customers", Customer.class);
            customers = getCustomers.getResultList();
    }

    //create a customer
    public Customer createCustomer(Customer c, Session session){
        Customer customer = new Customer(c.getName(),c.getEmail(),c.getAddress());
        session.persist(customers);
        return customer;
    }

    //get a customer
    public Optional<Customer> getCustomer(long id) {
        return Optional.ofNullable(customers.get((int) id));
    }

    //get all customers
    public List<Customer> getAll() {
        return customers;
    }

    public void addCustomer(Customer customer, Session s) {
        customers.add(customer);
        s.persist(customer);
    }

    //update a customer
    public void update(Customer customer, String[] params, Session s) {
        customer.setName(Objects.requireNonNull(
                params[0], "Name cannot be null"));
        customer.setEmail(Objects.requireNonNull(
                params[1], "Email cannot be null"));

        customers.add(customer);
        s.persist(customer);
    }

    //delete a customer
    public boolean deleteCustomer(long customerId, Session session){
        Customer customerToDelete = session.get(Customer.class, customerId);
        if (customerToDelete != null) {
            session.delete(customerToDelete);
            session.getTransaction().commit();
            return true;
        }
        return false;
    }

}