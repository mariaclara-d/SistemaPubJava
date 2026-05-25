package com.example.demo.controller;
import com.example.demo.dto.ProdutoRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Produto;
import com.example.demo.service.PubService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
public class ProdutoController {

    private static final Logger logger = LoggerFactory.getLogger(ProdutoController.class);
    private final PubService pubService;

    public ProdutoController(PubService pubService) {
        this.pubService = pubService;
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return pubService.getCardapio();
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@Valid @RequestBody ProdutoRequest request) {
        logger.info("Criando novo produto: {}", request.getName());
        Produto produto = pubService.cadastrarProduto(
                request.getName(),
                request.getPrice(),
                request.getCategory()
        );
        return ResponseEntity.ok(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> atualizarProduto(
            @PathVariable String id,
            @Valid @RequestBody ProdutoRequest request
    ) {
        logger.info("Atualizando produto {}", id);
        Produto produto = pubService.atualizarProduto(
                id,
                request.getName(),
                request.getPrice(),
                request.getCategory()
        );

        if (produto == null) {
            logger.warn("Produto {} não encontrado", id);
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado");
        }

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerProduto(@PathVariable String id) {
        logger.info("Removendo produto {}", id);
        boolean removido = pubService.removerProduto(id);

        if (!removido) {
            logger.warn("Produto {} não encontrado", id);
            throw new ResourceNotFoundException("Produto com ID " + id + " não encontrado");
        }

        return ResponseEntity.ok().build();
    }
}
