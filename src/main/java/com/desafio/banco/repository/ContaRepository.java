package com.desafio.banco.repository;

import com.desafio.banco.model.Conta;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, Long> {
    Optional<Conta> findByNumeroConta(String numeroConta);
}
