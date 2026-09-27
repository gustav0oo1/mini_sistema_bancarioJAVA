package com.desafio.banco.repository;

import com.desafio.banco.model.Conta;
import com.desafio.banco.model.Transacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransacaoRepository extends JpaRepository<Transacao, Long> {
    List<Transacao> findByContaOrigemOrContaDestinoOrderByDataHoraDesc(Conta contaOrigem, Conta contaDestino);
}
