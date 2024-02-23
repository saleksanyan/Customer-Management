package org.example;
import jakarta.persistence.*;

/**
 * customer entity
 */
@Entity
@Table(name = "Customers")
@NamedQueries({
        //get customers
        @NamedQuery(
                name = "get_customers",
                query = "SELECT c FROM Customer c "
        )
})
public class Customer {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String email;
    private String address;

    public Customer(String name, String email, String address) {
        this.name = name;
        this.email = email;
        this.address = address;
    }

    public Customer() {

    }
//getters and setters
    public Long getId() {
        return id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
