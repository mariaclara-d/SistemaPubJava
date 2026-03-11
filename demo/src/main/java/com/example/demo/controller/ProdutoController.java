package com.example.demo.controller;
import com.example.demo.dto.ProdutoRequest;
import com.example.demo.model.Produto;
import com.example.demo.service.PubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produtos")
@CrossOrigin(origins = "*")
public class ProdutoController {

    private final PubService pubService;

    public ProdutoController(PubService pubService) {
        this.pubService = pubService;
    }

    @GetMapping
    public List<Produto> listarProdutos() {
        return pubService.getCardapio();
    }

    @PostMapping
    public ResponseEntity<Produto> criarProduto(@RequestBody ProdutoRequest request) {
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
            @RequestBody ProdutoRequest request
    ) {
        Produto produto = pubService.atualizarProduto(
                id,
                request.getName(),
                request.getPrice(),
                request.getCategory()
        );

        if (produto == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(produto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removerProduto(@PathVariable String id) {
        boolean removido = pubService.removerProduto(id);

        if (!removido) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().build();
    }
}
