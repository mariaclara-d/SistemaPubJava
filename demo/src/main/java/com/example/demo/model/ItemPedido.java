package com.example.demo.model;

public class ItemPedido {
    private String id;
    private String name;
    private double price;
    private String category;
    private int quantity;

    public ItemPedido() {
    }

    public ItemPedido(Produto p, int quantity) {
        this.id = p.getId();
        this.name = p.getName();
        this.price = p.getPrice();
        this.category = p.getCategory();
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }
}