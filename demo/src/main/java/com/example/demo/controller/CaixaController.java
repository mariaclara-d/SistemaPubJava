package com.example.demo.controller;

import com.example.demo.dto.FecharContaRequest;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.ContaFechada;
import com.example.demo.service.PubService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/caixa")
public class CaixaController {

    private static final Logger logger = LoggerFactory.getLogger(CaixaController.class);
    private final PubService pubService;

    public CaixaController(PubService pubService) {
        this.pubService = pubService;
    }

    @PostMapping("/fechar/{idMesa}")
    public ResponseEntity<ContaFechada> fecharConta(
            @PathVariable int idMesa,
            @Valid @RequestBody FecharContaRequest request
    ) {
        logger.info("Fechando conta da mesa {} com status {}", idMesa, request.getStatus());
        ContaFechada conta = pubService.fecharContaComStatus(idMesa, request.getStatus(), request.getClienteNome());
        if (conta == null) {
            logger.warn("Falha ao fechar conta da mesa {}", idMesa);
            throw new ResourceNotFoundException("Mesa com ID " + idMesa + " não encontrada");
        }
        return ResponseEntity.ok(conta);
    }

    @GetMapping("/historico")
    public List<ContaFechada> getHistorico() {
        logger.info("Recuperando histórico de contas");
        return pubService.getHistorico();
    }

    @GetMapping("/fiado")
    public List<ContaFechada> getFiado() {
        logger.info("Recuperando contas em aberto (fiado)");
        return pubService.getFiado();
    }

    @PatchMapping("/fiado/{contaId}/quitar")
    public ResponseEntity<?> quitarFiado(@PathVariable String contaId) {
        logger.info("Quitando conta em aberto {}", contaId);
        boolean ok = pubService.quitarFiado(contaId);
        if (!ok) {
            logger.warn("Conta {} não encontrada", contaId);
            throw new ResourceNotFoundException("Conta com ID " + contaId + " não encontrada");
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/kpis")
    public ResponseEntity<Map<String, Object>> getKpis() {
        List<ContaFechada> historico = pubService.getHistorico();

        double totalDia = historico.stream()
                .filter(c -> "pago".equals(c.getStatus()))
                .mapToDouble(ContaFechada::getTotal).sum();

        double totalFiado = historico.stream()
                .filter(c -> "fiado".equals(c.getStatus()))
                .mapToDouble(ContaFechada::getTotal).sum();

        long mesasAtendidas = historico.size();

        double ticketMedio = mesasAtendidas > 0 ? (totalDia + totalFiado) / mesasAtendidas : 0;

        Map<String, Long> produtosMaisVendidos = historico.stream()
                .flatMap(c -> c.getItens().stream())
                .collect(Collectors.groupingBy(
                        i -> i.getName(),
                        Collectors.summingLong(i -> i.getQuantity())
                ));

        Map<String, Double> vendasPorCategoria = historico.stream()
                .flatMap(c -> c.getItens().stream())
                .collect(Collectors.groupingBy(
                        i -> i.getCategory(),
                        Collectors.summingDouble(i -> i.getPrice() * i.getQuantity())
                ));

        return ResponseEntity.ok(Map.of(
                "totalDia", totalDia,
                "totalFiado", totalFiado,
                "mesasAtendidas", mesasAtendidas,
                "ticketMedio", ticketMedio,
                "produtosMaisVendidos", produtosMaisVendidos,
                "vendasPorCategoria", vendasPorCategoria
        ));
    }
}
