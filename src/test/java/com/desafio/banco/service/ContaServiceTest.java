package com.desafio.banco.service;

import com.desafio.banco.dto.request.ContaRequestDTO;
import com.desafio.banco.dto.request.TransferenciaRequestDTO;
import com.desafio.banco.dto.response.ContaResponseDTO;
import com.desafio.banco.exception.SaldoInsuficienteException;
import com.desafio.banco.model.Conta;
import com.desafio.banco.model.Transacao;
import com.desafio.banco.repository.ContaRepository;
import com.desafio.banco.repository.TransacaoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContaServiceTest {

    @Mock
    private ContaRepository contaRepository;

    @Mock
    private TransacaoRepository transacaoRepository;

    @InjectMocks
    private ContaService contaService;

    private Conta contaOrigem;
    private Conta contaDestino;

    @BeforeEach
    void setUp() {
        contaOrigem = new Conta("João", new BigDecimal("100.00"));
        contaOrigem.setId(1L);
        
        contaDestino = new Conta("Maria", new BigDecimal("50.00"));
        contaDestino.setId(2L);
    }

    @Test
    @DisplayName("Deve depositar com sucesso")
    void deveDepositarComSucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));

        contaService.depositar(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("150.00"), contaOrigem.getSaldo());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
        verify(contaRepository, times(1)).save(contaOrigem);
    }

    @Test
    @DisplayName("Deve sacar com sucesso")
    void deveSacarComSucesso() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));

        contaService.sacar(1L, new BigDecimal("50.00"));

        assertEquals(new BigDecimal("50.00"), contaOrigem.getSaldo());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
        verify(contaRepository, times(1)).save(contaOrigem);
    }

    @Test
    @DisplayName("Deve falhar saque por saldo insuficiente")
    void deveFalharSaqueSaldoInsuficiente() {
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));

        assertThrows(SaldoInsuficienteException.class, () -> 
            contaService.sacar(1L, new BigDecimal("150.00")));
        
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }

    @Test
    @DisplayName("Deve transferir com sucesso")
    void deveTransferirComSucesso() {
        TransferenciaRequestDTO request = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("50.00"));
        
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        contaService.transferir(request);

        assertEquals(new BigDecimal("50.00"), contaOrigem.getSaldo());
        assertEquals(new BigDecimal("100.00"), contaDestino.getSaldo());
        verify(transacaoRepository, times(1)).save(any(Transacao.class));
        verify(contaRepository, times(1)).save(contaOrigem);
        verify(contaRepository, times(1)).save(contaDestino);
    }

    @Test
    @DisplayName("Deve falhar transferência por saldo insuficiente")
    void deveFalharTransferenciaSaldoInsuficiente() {
        TransferenciaRequestDTO request = new TransferenciaRequestDTO(1L, 2L, new BigDecimal("150.00"));
        
        when(contaRepository.findById(1L)).thenReturn(Optional.of(contaOrigem));
        when(contaRepository.findById(2L)).thenReturn(Optional.of(contaDestino));

        assertThrows(SaldoInsuficienteException.class, () -> 
            contaService.transferir(request));
        
        verify(transacaoRepository, never()).save(any(Transacao.class));
    }
}
