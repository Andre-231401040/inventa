package com.inventa.inventory.model;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String image;
    private String name;
    private String category;
    private String pic;
    private String location;
    private Integer qty;
    private Float fee;
    @Column(name = "`condition`")
    private String condition;
    private String description;
    @Column(name = "`status`")
    private String status;
    private LocalDate date;
    private Integer supplier_id;
    private Integer admin_id;

    public Transaction() {

    }

    public Transaction(String image, String name, String category, String pic, String location, Integer qty, Float fee, String condition, String description, String status, LocalDate date, Integer supplier_id, Integer admin_id) {
        setImage(image);
        setName(name);
        setCategory(category);
        setPIC(pic);
        setLocation(location);
        setQty(qty);
        setFee(fee);
        setCondition(condition);
        setDescription(description);
        setStatus(status);
        setDate(date);
        setSupplierId(supplier_id);
        setAdminId(admin_id);
    }

    public Integer getId() { 
        return id; 
    }

    public void setId(Integer id) { 
        this.id = id; 
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPIC() {
        return pic;
    }

    public void setPIC(String pic) {
        this.pic = pic;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getQty() { 
        return qty; 
    }

    public void setQty(Integer qty) { 
        this.qty = qty;
    }

    public Float getFee() { 
        return fee; 
    }

    public void setFee(Float fee) { 
        this.fee = fee;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getSupplierId() { 
        return supplier_id; 
    }

    public void setSupplierId(Integer supplier_id) { 
        this.supplier_id = supplier_id;
    }

    public Integer getAdminId() { 
        return admin_id; 
    }

    public void setAdminId(Integer admin_id) { 
        this.admin_id = admin_id;
    }
}
