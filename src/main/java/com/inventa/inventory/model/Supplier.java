package com.inventa.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name="suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String contact;
    private String address;
    private Long admin_id;

    public Supplier() {
    }

    public Supplier(String name, String contact, String address, Long admin_id) {
        setName(name);
        setContact(contact);
        setAddress(address);
        setAdminId(admin_id);
    }

    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getName() { 
        return name; 
    }

    public void setName(String name) { 
        this.name = name; 
    }

    public String getContact() { 
        return contact; 
    }

    public void setContact(String contact) { 
        this.contact = contact; 
    }

    public String getAddress() { 
        return address; 
    }

    public void setAddress(String address) { 
        this.address = address; 
    }

    public Long getAdminId() { 
        return admin_id; 
    }

    public void setAdminId(Long admin_id) { 
        this.admin_id = admin_id; 
    }
}
