package com.example.demo.service;

import com.example.demo.dto.ItemQuantidadeRequest;
import com.example.demo.model.Mesa;
import com.example.demo.model.Produto;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PubServiceTest {

    @Test
    void naoDeveAdicionarItemComQuantidadeInvalida() {
        PubService service = new PubService();
        Mesa mesa = service.getMesa(1);
        assertNotNull(mesa);

        ItemQuantidadeRequest itemInvalido = new ItemQuantidadeRequest();
        itemInvalido.setIdProduto("1");
        itemInvalido.setQuantity(0);

        service.adicionarItensMesa(1, List.of(itemInvalido));

        assertTrue(mesa.getItensConsumidos().isEmpty());
    }

    @Test
    void atualizarProdutoInexistenteDeveRetornarNull() {
        PubService service = new PubService();

        Produto atualizado = service.atualizarProduto("id-inexistente", "Novo", 15.0, "Bebidas");

        assertNull(atualizado);
    }

    @Test
    void fecharContaDeveLimparItensENickname() {
        PubService service = new PubService();
        Mesa mesa = service.getMesa(2);
        assertNotNull(mesa);

        service.atualizarNickname(2, "Cliente X");

        ItemQuantidadeRequest itemValido = new ItemQuantidadeRequest();
        itemValido.setIdProduto("1");
        itemValido.setQuantity(2);
        service.adicionarItensMesa(2, List.of(itemValido));

        assertFalse(mesa.getItensConsumidos().isEmpty());
        assertEquals("Cliente X", mesa.getNickname());

        service.fecharConta(2);

        assertTrue(mesa.getItensConsumidos().isEmpty());
        assertEquals("", mesa.getNickname());
    }
}
