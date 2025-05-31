package com.inventa.inventory.model;

public class Dashboard {
    private Integer totalItems;
    private Integer itemsIn;
    private Integer itemsOut;
    private Integer itemsLent;
    private Integer totalSuppliers;
    private Integer totalTransactions;

    public Dashboard(Integer totalItems, Integer itemsIn, Integer itemsOut, Integer itemsLent, Integer totalSuppliers, Integer totalTransactions) {
        setTotalItems(totalItems);
        setItemsIn(itemsIn);
        setItemsOut(itemsOut);
        setItemsLent(itemsLent);
        setTotalSuppliers(totalSuppliers);
        setTotalTransactions(totalTransactions);
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }
    public Integer getItemsIn() {
        return itemsIn;
    }

    public void setItemsIn(Integer itemsIn) {
        this.itemsIn = itemsIn;
    }
    public Integer getItemsOut() {
        return itemsOut;
    }

    public void setItemsOut(Integer itemsOut) {
        this.itemsOut = itemsOut;
    }
    public Integer getItemsLent() {
        return itemsLent;
    }

    public void setItemsLent(Integer itemsLent) {
        this.itemsLent = itemsLent;
    }
    public Integer getTotalSuppliers() {
        return totalSuppliers;
    }

    public void setTotalSuppliers(Integer totalSuppliers) {
        this.totalSuppliers = totalSuppliers;
    }
    public Integer getTotalTransactions() {
        return totalTransactions;
    }

    public void setTotalTransactions(Integer totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
}
