package com.example.demo.dto;


import java.util.List;

public class AdicionarItensRequest {
    private List<ItemQuantidadeRequest> items;

    public List<ItemQuantidadeRequest> getItems() {
        return items;
    }

    public void setItems(List<ItemQuantidadeRequest> items) {
        this.items = items;
    }
}