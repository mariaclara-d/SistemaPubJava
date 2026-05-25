package com.example.demo.model;

import jakarta.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "mesas")
public class Mesa {

    @Id
    private int id;
    private String name;
    private String nickname = "";

    @ElementCollection
    @CollectionTable(name = "mesa_itens", joinColumns = @JoinColumn(name = "mesa_id"))
    @MapKeyColumn(name = "produto_id")
    @Column(name = "quantidade")
    private Map<String, Integer> itensConsumidos = new HashMap<>();

    public Mesa() {}

    public Mesa(int id) {
        this.id = id;
        this.name = "Mesa " + id;
    }

    public void adicionarItem(String produtoId, int quantidade) {
        if (quantidade <= 0) return;
        this.itensConsumidos.put(produtoId, this.itensConsumidos.getOrDefault(produtoId, 0) + quantidade);
    }

    public void removerItem(String produtoId) {
        this.itensConsumidos.remove(produtoId);
    }

    public void limparConta() {
        this.itensConsumidos.clear();
        this.nickname = "";
    }

    public Map<String, Integer> getItensConsumidos() { return itensConsumidos; }
    public int getId() { return id; }
    public String getName() { return name; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname != null ? nickname : ""; }
    public boolean isOcupada() { return !itensConsumidos.isEmpty(); }
}
