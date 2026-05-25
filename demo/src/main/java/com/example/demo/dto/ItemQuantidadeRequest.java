package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public class ItemQuantidadeRequest {
    @NotBlank(message = "ID do produto não pode estar em branco")
    private String idProduto;

    @Positive(message = "Quantidade deve ser maior que zero")
    private int quantity;

    public String getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(String idProduto) {
        this.idProduto = idProduto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}