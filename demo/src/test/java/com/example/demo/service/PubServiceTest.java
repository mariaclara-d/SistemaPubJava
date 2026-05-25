package com.example.demo.service;

import com.example.demo.dto.ItemQuantidadeRequest;
import com.example.demo.model.ContaFechada;
import com.example.demo.model.ItemPedido;
import com.example.demo.model.Mesa;
import com.example.demo.model.Produto;
import com.example.demo.repository.ContaFechadaRepository;
import com.example.demo.repository.MesaRepository;
import com.example.demo.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DisplayName("PubService - Testes de Negócio")
class PubServiceTest {

    @Autowired
    private PubService pubService;

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private ContaFechadaRepository contaFechadaRepository;

    @BeforeEach
    void setUp() {
        mesaRepository.deleteAll();
        produtoRepository.deleteAll();
        contaFechadaRepository.deleteAll();
        
        // Inicializa dados de teste
        for (int i = 1; i <= 3; i++) {
            mesaRepository.save(new Mesa(i));
        }
        
        produtoRepository.save(new Produto("1", "Cerveja", 12.0, "Bebidas"));
        produtoRepository.save(new Produto("2", "Refrigerante", 6.0, "Bebidas"));
    }

    @Test
    @DisplayName("Deve retornar todas as mesas")
    void testGetTodasMesas() {
        List<Mesa> mesas = pubService.getTodasMesas();
        assertEquals(3, mesas.size());
    }

    @Test
    @DisplayName("Deve retornar mesa por ID")
    void testGetMesaPorId() {
        Mesa mesa = pubService.getMesa(1);
        assertNotNull(mesa);
        assertEquals(1, mesa.getId());
    }

    @Test
    @DisplayName("Deve retornar null para mesa inexistente")
    void testGetMesaInexistente() {
        Mesa mesa = pubService.getMesa(999);
        assertNull(mesa);
    }

    @Test
    @DisplayName("Deve retornar cardápio com produtos")
    void testGetCardapio() {
        List<Produto> cardapio = pubService.getCardapio();
        assertEquals(2, cardapio.size());
    }

    @Test
    @DisplayName("Deve atualizar nickname da mesa")
    void testAtualizarNickname() {
        String novoNickname = "Garotos";
        pubService.atualizarNickname(1, novoNickname);
        
        Mesa mesa = pubService.getMesa(1);
        assertEquals(novoNickname, mesa.getNickname());
    }

    @Test
    @DisplayName("Não deve atualizar nickname de mesa inexistente")
    void testAtualizarNicknameInexistente() {
        assertDoesNotThrow(() -> pubService.atualizarNickname(999, "Novo"));
    }

    @Test
    @DisplayName("Deve adicionar itens válidos à mesa")
    @Transactional
    void testAdicionarItensValidos() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(2);

        pubService.adicionarItensMesa(1, List.of(item));

        Mesa mesa = pubService.getMesa(1);
        assertTrue(mesa.getItensConsumidos().containsKey("1"));
        assertEquals(2, mesa.getItensConsumidos().get("1"));
    }

    @Test
    @DisplayName("Não deve adicionar itens com quantidade inválida")
    @Transactional
    void testAdicionarItensQuantidadeInvalida() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(0);

        pubService.adicionarItensMesa(1, List.of(item));

        Mesa mesa = pubService.getMesa(1);
        assertFalse(mesa.getItensConsumidos().containsKey("1"));
    }

    @Test
    @DisplayName("Não deve adicionar itens com quantidade negativa")
    @Transactional
    void testAdicionarItensQuantidadeNegativa() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(-5);

        pubService.adicionarItensMesa(1, List.of(item));

        Mesa mesa = pubService.getMesa(1);
        assertFalse(mesa.getItensConsumidos().containsKey("1"));
    }

    @Test
    @DisplayName("Não deve adicionar itens de produto inexistente")
    @Transactional
    void testAdicionarItensProdutoInexistente() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("999");
        item.setQuantity(2);

        pubService.adicionarItensMesa(1, List.of(item));

        Mesa mesa = pubService.getMesa(1);
        assertFalse(mesa.getItensConsumidos().containsKey("999"));
    }

    @Test
    @DisplayName("Deve remover item da mesa")
    @Transactional
    void testRemoverItem() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(2);

        pubService.adicionarItensMesa(1, List.of(item));
        pubService.removerItemMesa(1, "1");

        Mesa mesa = pubService.getMesa(1);
        assertFalse(mesa.getItensConsumidos().containsKey("1"));
    }

    @Test
    @DisplayName("Deve retornar pedidos montados da mesa")
    @Transactional
    void testMontarPedidosDaMesa() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(2);

        pubService.adicionarItensMesa(1, List.of(item));

        Mesa mesa = pubService.getMesa(1);
        List<ItemPedido> pedidos = pubService.montarPedidosDaMesa(mesa);

        assertEquals(1, pedidos.size());
        assertEquals("Cerveja", pedidos.get(0).getName());
        assertEquals(2, pedidos.get(0).getQuantity());
        assertEquals(12.0, pedidos.get(0).getPrice());
    }

    @Test
    @DisplayName("Deve fechar conta da mesa")
    @Transactional
    void testFecharConta() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(1);

        pubService.atualizarNickname(1, "Mesa Teste");
        pubService.adicionarItensMesa(1, List.of(item));
        assertDoesNotThrow(() -> pubService.fecharConta(1));
    }

    @Test
    @DisplayName("Deve fechar conta com status e salvar no histórico")
    void testFecharContaComStatus() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(1);

        pubService.adicionarItensMesa(1, List.of(item));
        pubService.atualizarNickname(1, "João");

        ContaFechada conta = pubService.fecharContaComStatus(1, "pago", "João Silva");

        assertNotNull(conta);
        assertEquals("pago", conta.getStatus());
        assertEquals("João Silva", conta.getClienteNome());
        assertEquals(12.0, conta.getTotal());
        assertEquals(1, conta.getItens().size());
    }

    @Test
    @DisplayName("Deve retornar null ao fechar conta de mesa inexistente")
    void testFecharContaMesaInexistente() {
        ContaFechada conta = pubService.fecharContaComStatus(999, "pago", "Cliente");
        assertNull(conta);
    }

    @Test
    @DisplayName("Deve buscar produto por ID")
    void testBuscarProdutoPorId() {
        Produto produto = pubService.buscarProdutoPorId("1");
        assertNotNull(produto);
        assertEquals("Cerveja", produto.getName());
    }

    @Test
    @DisplayName("Deve retornar null para produto inexistente")
    void testBuscarProdutoInexistente() {
        Produto produto = pubService.buscarProdutoPorId("999");
        assertNull(produto);
    }

    @Test
    @DisplayName("Deve retornar histórico de contas")
    void testGetHistorico() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(1);

        pubService.adicionarItensMesa(1, List.of(item));
        pubService.fecharContaComStatus(1, "pago", "Cliente 1");

        pubService.adicionarItensMesa(2, List.of(item));
        pubService.fecharContaComStatus(2, "pago", "Cliente 2");

        List<ContaFechada> historico = pubService.getHistorico();
        assertEquals(2, historico.size());
    }

    @Test
    @DisplayName("Deve retornar apenas contas em aberto (fiado)")
    void testGetFiado() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(1);

        pubService.adicionarItensMesa(1, List.of(item));
        pubService.fecharContaComStatus(1, "fiado", "Cliente Fiado");

        pubService.adicionarItensMesa(2, List.of(item));
        pubService.fecharContaComStatus(2, "pago", "Cliente Pago");

        List<ContaFechada> fiado = pubService.getFiado();
        assertEquals(1, fiado.size());
        assertEquals("fiado", fiado.get(0).getStatus());
    }

    @Test
    @DisplayName("Deve quitar conta em aberto")
    void testQuitarFiado() {
        ItemQuantidadeRequest item = new ItemQuantidadeRequest();
        item.setIdProduto("1");
        item.setQuantity(1);

        pubService.adicionarItensMesa(1, List.of(item));
        ContaFechada contaFiado = pubService.fecharContaComStatus(1, "fiado", "Cliente");

        boolean resultado = pubService.quitarFiado(contaFiado.getId());
        
        assertTrue(resultado);
        assertEquals("pago", contaFechadaRepository.findById(contaFiado.getId()).get().getStatus());
    }

    @Test
    @DisplayName("Deve retornar false ao quitar conta inexistente")
    void testQuitarFiadoInexistente() {
        boolean resultado = pubService.quitarFiado("999");
        assertFalse(resultado);
    }

    @Test
    @DisplayName("Deve atualizar produto")
    void testAtualizarProduto() {
        Produto produtoAtualizado = pubService.atualizarProduto("1", "Cerveja Premium", 15.0, "Bebidas");

        assertNotNull(produtoAtualizado);
        assertEquals("Cerveja Premium", produtoAtualizado.getName());
        assertEquals(15.0, produtoAtualizado.getPrice());
    }

    @Test
    @DisplayName("Deve remover produto")
    void testRemoverProduto() {
        boolean removido = pubService.removerProduto("2");

        assertTrue(removido);
        assertNull(pubService.buscarProdutoPorId("2"));
    }

    @Test
    @DisplayName("Deve retornar false ao remover produto inexistente")
    void testRemoverProdutoInexistente() {
        boolean removido = pubService.removerProduto("999");
        assertFalse(removido);
    }
}
