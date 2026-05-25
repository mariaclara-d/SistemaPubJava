package com.example.demo.controller;

import com.example.demo.dto.AdicionarItensRequest;
import com.example.demo.dto.AtualizarNicknameRequest;
import com.example.demo.dto.MesaResponse;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Mesa;
import com.example.demo.service.PubService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/mesas")
public class MesaController {

    private static final Logger logger = LoggerFactory.getLogger(MesaController.class);
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
            @Valid @RequestBody AtualizarNicknameRequest request
    ) {
        logger.info("Atualizando nickname da mesa {}", idMesa);
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            logger.warn("Mesa {} não encontrada", idMesa);
            throw new ResourceNotFoundException("Mesa com ID " + idMesa + " não encontrada");
        }

        pubService.atualizarNickname(idMesa, request.getNickname());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idMesa}/itens")
    public ResponseEntity<?> adicionarItens(
            @PathVariable int idMesa,
            @Valid @RequestBody AdicionarItensRequest request
    ) {
        logger.info("Adicionando {} itens à mesa {}", request.getItems().size(), idMesa);
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            logger.warn("Mesa {} não encontrada", idMesa);
            throw new ResourceNotFoundException("Mesa com ID " + idMesa + " não encontrada");
        }

        pubService.adicionarItensMesa(idMesa, request.getItems());
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{idMesa}/itens/{produtoId}")
    public ResponseEntity<?> removerItem(
            @PathVariable int idMesa,
            @PathVariable String produtoId
    ) {
        logger.info("Removendo produto {} da mesa {}", produtoId, idMesa);
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            logger.warn("Mesa {} não encontrada", idMesa);
            throw new ResourceNotFoundException("Mesa com ID " + idMesa + " não encontrada");
        }

        pubService.removerItemMesa(idMesa, produtoId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{idMesa}/fechar")
    public ResponseEntity<?> fecharConta(@PathVariable int idMesa) {
        logger.info("Fechando conta da mesa {}", idMesa);
        Mesa mesa = pubService.getMesa(idMesa);
        if (mesa == null) {
            logger.warn("Mesa {} não encontrada", idMesa);
            throw new ResourceNotFoundException("Mesa com ID " + idMesa + " não encontrada");
        }

        pubService.fecharConta(idMesa);
        return ResponseEntity.ok().build();
    }
}