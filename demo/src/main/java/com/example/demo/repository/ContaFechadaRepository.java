package com.example.demo.repository;

import com.example.demo.model.ContaFechada;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ContaFechadaRepository extends JpaRepository<ContaFechada, String> {
    List<ContaFechada> findByStatus(String status);
}
