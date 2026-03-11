package com.example.demo.model;

import java.util.HashMap;
import java.util.Map;

public class Mesa {
    private int id;
    private String name;
    private String nickname = "";
    private Map<String, Integer> itensConsumidos = new HashMap<>();

    public Mesa(int id) {
        this.id = id;
        this.name = "Mesa " + id;
    }

    public void adicionarItem(String produtoId, int quantidade) {
        if (quantidade <= 0) return;
        this.itensConsumidos.put(
                produtoId,
                this.itensConsumidos.getOrDefault(produtoId, 0) + quantidade
        );
    }

    public void removerItem(String produtoId) {
        this.itensConsumidos.remove(produtoId);
    }

    public void limparConta() {
        this.itensConsumidos.clear();
        this.nickname = "";
    }

    public Map<String, Integer> getItensConsumidos() {
        return itensConsumidos;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname != null ? nickname : "";
    }

    public boolean isOcupada() {
        return !itensConsumidos.isEmpty();
    }
}