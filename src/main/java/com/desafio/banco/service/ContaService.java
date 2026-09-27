package com.desafio.banco.service;

import com.desafio.banco.dto.request.ContaRequestDTO;
import com.desafio.banco.dto.request.TransferenciaRequestDTO;
import com.desafio.banco.dto.response.ContaResponseDTO;
import com.desafio.banco.dto.response.TransacaoResponseDTO;
import com.desafio.banco.exception.ContaNaoEncontradaException;
import com.desafio.banco.exception.SaldoInsuficienteException;
import com.desafio.banco.model.Conta;
import com.desafio.banco.model.Transacao;
import com.desafio.banco.model.enums.TipoTransacao;
import com.desafio.banco.repository.ContaRepository;
import com.desafio.banco.repository.TransacaoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContaService {

    private final ContaRepository contaRepository;
    private final TransacaoRepository transacaoRepository;

    public ContaService(ContaRepository contaRepository, TransacaoRepository transacaoRepository) {
        this.contaRepository = contaRepository;
        this.transacaoRepository = transacaoRepository;
    }

    @Transactional
    public ContaResponseDTO criarConta(ContaRequestDTO request) {
        Conta conta = new Conta(request.titular(), request.saldoInicial());
        Conta contaSalva = contaRepository.save(conta);
        return toContaResponseDTO(contaSalva);
    }

    @Transactional(readOnly = true)
    public ContaResponseDTO buscarPorId(Long id) {
        return toContaResponseDTO(buscarEntidadeConta(id));
    }

    @Transactional
    public void depositar(Long id, BigDecimal valor) {
        Conta conta = buscarEntidadeConta(id);
        conta.setSaldo(conta.getSaldo().add(valor));
        
        Transacao transacao = new Transacao(TipoTransacao.DEPOSITO, valor, conta, null);
        transacaoRepository.save(transacao);
        contaRepository.save(conta);
    }

    @Transactional
    public void sacar(Long id, BigDecimal valor) {
        Conta conta = buscarEntidadeConta(id);
        validarSaldo(conta, valor);
        
        conta.setSaldo(conta.getSaldo().subtract(valor));
        
        Transacao transacao = new Transacao(TipoTransacao.SAQUE, valor, conta, null);
        transacaoRepository.save(transacao);
        contaRepository.save(conta);
    }

    @Transactional
    public void transferir(TransferenciaRequestDTO request) {
        Conta origem = buscarEntidadeConta(request.contaOrigemId());
        Conta destino = buscarEntidadeConta(request.contaDestinoId());
        
        validarSaldo(origem, request.valor());
        
        origem.setSaldo(origem.getSaldo().subtract(request.valor()));
        destino.setSaldo(destino.getSaldo().add(request.valor()));
        
        Transacao transacao = new Transacao(TipoTransacao.TRANSFERENCIA, request.valor(), origem, destino);
        transacaoRepository.save(transacao);
        
        contaRepository.save(origem);
        contaRepository.save(destino);
    }

    @Transactional(readOnly = true)
    public List<TransacaoResponseDTO> buscarExtrato(Long id) {
        Conta conta = buscarEntidadeConta(id);
        return transacaoRepository.findByContaOrigemOrContaDestinoOrderByDataHoraDesc(conta, conta)
                .stream()
                .map(this::toTransacaoResponseDTO)
                .collect(Collectors.toList());
    }

    private Conta buscarEntidadeConta(Long id) {
        return contaRepository.findById(id)
                .orElseThrow(() -> new ContaNaoEncontradaException(id));
    }

    private void validarSaldo(Conta conta, BigDecimal valor) {
        if (conta.getSaldo().compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }
    }

    private ContaResponseDTO toContaResponseDTO(Conta conta) {
        return new ContaResponseDTO(
                conta.getId(),
                conta.getTitular(),
                conta.getNumeroConta(),
                conta.getSaldo(),
                conta.getDataCriacao()
        );
    }

    private TransacaoResponseDTO toTransacaoResponseDTO(Transacao transacao) {
        return new TransacaoResponseDTO(
                transacao.getId(),
                transacao.getTipo(),
                transacao.getValor(),
                transacao.getDataHora(),
                transacao.getContaOrigem().getNumeroConta(),
                transacao.getContaDestino() != null ? transacao.getContaDestino().getNumeroConta() : null
        );
    }
}
