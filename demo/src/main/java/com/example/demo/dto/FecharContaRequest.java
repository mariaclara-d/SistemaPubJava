package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class FecharContaRequest {
    @NotBlank(message = "Status não pode estar em branco")
    @Pattern(regexp = "pago|fiado", message = "Status deve ser 'pago' ou 'fiado'")
    private String status;

    @NotBlank(message = "Nome do cliente não pode estar em branco")
    private String clienteNome;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getClienteNome() { return clienteNome; }
    public void setClienteNome(String clienteNome) { this.clienteNome = clienteNome; }
}
