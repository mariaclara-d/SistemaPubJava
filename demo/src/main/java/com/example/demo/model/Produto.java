package com.example.demo.model;

import java.util.UUID;

public class Produto {
    private String id;
    private String name;
    private double price;
    private String category;

    public Produto() {
    }

    public Produto(String name, double price, String category) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public Produto(String id, String name, double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
