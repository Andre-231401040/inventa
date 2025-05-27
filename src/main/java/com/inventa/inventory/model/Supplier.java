package com.inventa.inventory.model;

import jakarta.persistence.*;

@Entity
@Table(name="suppliers")
public class Supplier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String contact;
    private String address;
    private Integer admin_id;

    public Supplier() {
    }

    public Supplier(String name, String contact, String address, Integer admin_id) {
        setName(name);
        setContact(contact);
        setAddress(address);
        setAdminId(admin_id);
    }

    public Integer getId() { 
        return id; 
    }

    public void setId(Integer id) { 
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

    public Integer getAdminId() { 
        return admin_id; 
    }

    public void setAdminId(Integer admin_id) { 
        this.admin_id = admin_id; 
    }
}
