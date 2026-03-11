package com.example.demo.service;

import com.example.demo.dto.ItemQuantidadeRequest;
import com.example.demo.model.ItemPedido;
import com.example.demo.model.Mesa;
import com.example.demo.model.Produto;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PubService {

    private final List<Mesa> mesas = new ArrayList<>();
    private final List<Produto> cardapio = new ArrayList<>();

    public PubService() {
        for (int i = 1; i <= 12; i++) {
            mesas.add(new Mesa(i));
        }

        cardapio.add(new Produto("1", "Cerveja Brahma 600ml", 12.0, "Bebidas"));
        cardapio.add(new Produto("2", "Coca-Cola Lata", 6.0, "Bebidas"));
        cardapio.add(new Produto("3", "Heineken 330ml", 10.0, "Bebidas"));
        cardapio.add(new Produto("4", "Água Mineral 500ml", 4.0, "Bebidas"));
        cardapio.add(new Produto("6", "Caipirinha", 15.0, "Bebidas"));
        cardapio.add(new Produto("9", "Winston", 28.0, "Cigarros"));
        cardapio.add(new Produto("10", "Lucky Strike Double", 10.0, "Cigarros"));
        cardapio.add(new Produto("11", "Marlboro", 12.0, "Cigarros"));
        cardapio.add(new Produto("12", "Vinho Tinto Seco", 60.0, "Vinhos"));
    }

    public List<Mesa> getTodasMesas() {
        return mesas;
    }

    public Mesa getMesa(int id) {
        return mesas.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public List<Produto> getCardapio() {
        return cardapio;
    }

    public Produto buscarProdutoPorId(String id) {
        return cardapio.stream()
                .filter(p -> p.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public List<ItemPedido> montarPedidosDaMesa(Mesa mesa) {
        List<ItemPedido> orders = new ArrayList<>();

        mesa.getItensConsumidos().forEach((produtoId, quantidade) -> {
            Produto produto = buscarProdutoPorId(produtoId);
            if (produto != null) {
                orders.add(new ItemPedido(produto, quantidade));
            }
        });

        return orders;
    }

    public void atualizarNickname(int idMesa, String nickname) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) {
            mesa.setNickname(nickname);
        }
    }

    public void adicionarItensMesa(int idMesa, List<ItemQuantidadeRequest> items) {
        Mesa mesa = getMesa(idMesa);
        if (mesa == null || items == null) return;

        for (ItemQuantidadeRequest item : items) {
            Produto produto = buscarProdutoPorId(item.getIdProduto());
            if (produto != null && item.getQuantity() > 0) {
                mesa.adicionarItem(item.getIdProduto(), item.getQuantity());
            }
        }
    }

    public void removerItemMesa(int idMesa, String produtoId) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) {
            mesa.removerItem(produtoId);
        }
    }

    public void fecharConta(int idMesa) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) {
            mesa.limparConta();
        }
    }

    public Produto cadastrarProduto(String name, double price, String category) {
        Produto produto = new Produto(name, price, category);
        cardapio.add(produto);
        return produto;
    }

    public Produto atualizarProduto(String id, String name, double price, String category) {
        Produto produto = buscarProdutoPorId(id);
        if (produto == null) return null;

        produto.setName(name);
        produto.setPrice(price);
        produto.setCategory(category);
        return produto;
    }

    public boolean removerProduto(String id) {
        return cardapio.removeIf(p -> p.getId().equals(id));
    }
}