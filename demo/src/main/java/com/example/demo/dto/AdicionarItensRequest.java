package com.example.demo.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class AdicionarItensRequest {
    @NotEmpty(message = "Lista de itens não pode estar vazia")
    @Valid
    private List<ItemQuantidadeRequest> items;

    public List<ItemQuantidadeRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemQuantidadeRequest> items) {
        this.items = items;
    }
}