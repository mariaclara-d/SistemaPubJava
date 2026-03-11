package com.example.demo.controller;

import com.example.demo.dto.AdicionarItensRequest;
import com.example.demo.dto.AtualizarNicknameRequest;
import com.example.demo.dto.MesaResponse;
import com.example.demo.model.Mesa;
import com.example.demo.service.PubService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
@CrossOrigin(origins = "*")
public class MesaController {

    private final PubService pubService;

    public MesaController(PubService pubService) {
        this.pubService = pubService;
    }

    @GetMapping
    public List<MesaResponse> listarMesas() {
        return pubService.getTodasMesas().stream()
                .map(mesa -> new MesaResponse(
                        mesa.getId(),
                        mesa.getName(),
                        mesa.getNickname(),
                        pubService.montarPedidosDaMesa(mesa)
                ))
                .toList();
    }

    @PutMapping("/{idMesa}/nickname")
    public ResponseEntity<?> atualizarNickname(
            @PathVariable int idMesa,
            @RequestBody AtualizarNicknameRequest request
    ) {
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            return ResponseEntity.notFound().build();
        }

        pubService.atualizarNickname(idMesa, request.getNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idMesa}/itens")
    public ResponseEntity<?> adicionarItens(
            @PathVariable int idMesa,
            @RequestBody AdicionarItensRequest request
    ) {
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            return ResponseEntity.notFound().build();
        }

        pubService.adicionarItensMesa(idMesa, request.getItems());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idMesa}/itens/{produtoId}")
    public ResponseEntity<?> removerItem(
            @PathVariable int idMesa,
            @PathVariable String produtoId
    ) {
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            return ResponseEntity.notFound().build();
        }

        pubService.removerItemMesa(idMesa, produtoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idMesa}/fechar")
    public ResponseEntity<?> fecharConta(@PathVariable int idMesa) {
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            return ResponseEntity.notFound().build();
        }

        pubService.fecharConta(idMesa);
        return ResponseEntity.ok().build();
    }
}