package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProdutoRequest {
    @NotBlank(message = "Nome do produto não pode estar em branco")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String name;

    @Positive(message = "Preço deve ser maior que zero")
    private double price;

    @NotBlank(message = "Categoria não pode estar em branco")
    @Size(min = 3, max = 50, message = "Categoria deve ter entre 3 e 50 caracteres")
    private String category;

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