package com.example.demo.service;

import com.example.demo.dto.ItemQuantidadeRequest;
import com.example.demo.model.ContaFechada;
import com.example.demo.model.ItemPedido;
import com.example.demo.model.Mesa;
import com.example.demo.model.Produto;
import com.example.demo.repository.ContaFechadaRepository;
import com.example.demo.repository.MesaRepository;
import com.example.demo.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PubService {

    private final MesaRepository mesaRepository;
    private final ProdutoRepository produtoRepository;
    private final ContaFechadaRepository contaFechadaRepository;

    public PubService(MesaRepository mesaRepository, ProdutoRepository produtoRepository, ContaFechadaRepository contaFechadaRepository) {
        this.mesaRepository = mesaRepository;
        this.produtoRepository = produtoRepository;
        this.contaFechadaRepository = contaFechadaRepository;
        inicializar();
    }

    private void inicializar() {
        if (mesaRepository.count() == 0) {
            for (int i = 1; i <= 12; i++) mesaRepository.save(new Mesa(i));
        }
        if (produtoRepository.count() == 0) {
            produtoRepository.save(new Produto("1", "Cerveja Brahma 600ml", 12.0, "Bebidas"));
            produtoRepository.save(new Produto("2", "Coca-Cola Lata", 6.0, "Bebidas"));
            produtoRepository.save(new Produto("3", "Heineken 330ml", 10.0, "Bebidas"));
            produtoRepository.save(new Produto("4", "Água Mineral 500ml", 4.0, "Bebidas"));
            produtoRepository.save(new Produto("6", "Caipirinha", 15.0, "Bebidas"));
            produtoRepository.save(new Produto("9", "Winston", 28.0, "Cigarros"));
            produtoRepository.save(new Produto("10", "Lucky Strike Double", 10.0, "Cigarros"));
            produtoRepository.save(new Produto("11", "Marlboro", 12.0, "Cigarros"));
            produtoRepository.save(new Produto("12", "Vinho Tinto Seco", 60.0, "Vinhos"));
        }
    }

    public List<Mesa> getTodasMesas() { return mesaRepository.findAll(); }

    public Mesa getMesa(int id) { return mesaRepository.findById(id).orElse(null); }

    public List<Produto> getCardapio() { return produtoRepository.findAll(); }

    public Produto buscarProdutoPorId(String id) { return produtoRepository.findById(id).orElse(null); }

    public List<ItemPedido> montarPedidosDaMesa(Mesa mesa) {
        return mesa.getItensConsumidos().entrySet().stream()
                .map(e -> {
                    Produto p = buscarProdutoPorId(e.getKey());
                    return p != null ? new ItemPedido(p, e.getValue()) : null;
                })
                .filter(i -> i != null)
                .toList();
    }

    @Transactional
    public void atualizarNickname(int idMesa, String nickname) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) { mesa.setNickname(nickname); mesaRepository.save(mesa); }
    }

    @Transactional
    public void adicionarItensMesa(int idMesa, List<ItemQuantidadeRequest> items) {
        Mesa mesa = getMesa(idMesa);
        if (mesa == null || items == null) return;
        for (ItemQuantidadeRequest item : items) {
            Produto produto = buscarProdutoPorId(item.getIdProduto());
            if (produto != null && item.getQuantity() > 0)
                mesa.adicionarItem(item.getIdProduto(), item.getQuantity());
        }
        mesaRepository.save(mesa);
    }

    @Transactional
    public void removerItemMesa(int idMesa, String produtoId) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) { mesa.removerItem(produtoId); mesaRepository.save(mesa); }
    }

    @Transactional
    public void fecharConta(int idMesa) {
        Mesa mesa = getMesa(idMesa);
        if (mesa != null) { mesa.limparConta(); mesaRepository.save(mesa); }
    }

    @Transactional
    public ContaFechada fecharContaComStatus(int idMesa, String status, String clienteNome) {
        Mesa mesa = getMesa(idMesa);
        if (mesa == null) return null;
        List<ItemPedido> itens = montarPedidosDaMesa(mesa);
        double total = itens.stream().mapToDouble(i -> i.getPrice() * i.getQuantity()).sum();
        String nome = (clienteNome != null && !clienteNome.isBlank()) ? clienteNome : mesa.getNickname();
        ContaFechada conta = new ContaFechada(idMesa, mesa.getName(), nome, itens, total, status);
        contaFechadaRepository.save(conta);
        mesa.limparConta();
        mesaRepository.save(mesa);
        return conta;
    }

    public List<ContaFechada> getHistorico() { return contaFechadaRepository.findAll(); }

    public List<ContaFechada> getFiado() { return contaFechadaRepository.findByStatus("fiado"); }

    @Transactional
    public boolean quitarFiado(String contaId) {
        return contaFechadaRepository.findById(contaId).map(c -> {
            c.setStatus("pago");
            contaFechadaRepository.save(c);
            return true;
        }).orElse(false);
    }

    public Produto cadastrarProduto(String name, double price, String category) {
        return produtoRepository.save(new Produto(name, price, category));
    }

    @Transactional
    public Produto atualizarProduto(String id, String name, double price, String category) {
        Produto produto = buscarProdutoPorId(id);
        if (produto == null) return null;
        produto.setName(name); produto.setPrice(price); produto.setCategory(category);
        return produtoRepository.save(produto);
    }

    public boolean removerProduto(String id) {
        if (!produtoRepository.existsById(id)) return false;
        produtoRepository.deleteById(id);
        return true;
    }
}
