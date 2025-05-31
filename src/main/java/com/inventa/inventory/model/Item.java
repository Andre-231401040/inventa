package com.inventa.inventory.model;

public class Item {
    private String image;
    private String name;
    private String category;
    private Long stock;
    private String status;

    public Item(String image, String name, String category, Long stock, String status) {
        setImage(image);
        setName(name);
        setCategory(category);
        setStock(stock);
        setStatus(status);
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

    public Long getStock() { 
        return stock; 
    }

    public void setStock(Long stock) { 
        this.stock = stock;
    }

    public String getStatus() { 
        return status; 
    }

    public void setStatus(String status) { 
        this.status = status;
    }
}
