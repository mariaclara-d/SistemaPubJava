package com.example.demo.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "contas_fechadas")
public class ContaFechada {

    @Id
    private String id;
    private int mesaId;
    private String mesaNome;
    private String clienteNome;
    private double total;
    private String status;
    private LocalDateTime horario;

    @ElementCollection
    @CollectionTable(name = "conta_itens", joinColumns = @JoinColumn(name = "conta_id"))
    private List<ItemPedido> itens;

    public ContaFechada() {}

    public ContaFechada(int mesaId, String mesaNome, String clienteNome, List<ItemPedido> itens, double total, String status) {
        this.id = UUID.randomUUID().toString();
        this.mesaId = mesaId;
        this.mesaNome = mesaNome;
        this.clienteNome = clienteNome;
        this.itens = itens;
        this.total = total;
        this.status = status;
        this.horario = LocalDateTime.now();
    }

    public String getId() { return id; }
    public int getMesaId() { return mesaId; }
    public String getMesaNome() { return mesaNome; }
    public String getClienteNome() { return clienteNome; }
    public List<ItemPedido> getItens() { return itens; }
    public double getTotal() { return total; }
    public String getStatus() { return status; }
    public LocalDateTime getHorario() { return horario; }
    public void setStatus(String status) { this.status = status; }
}
