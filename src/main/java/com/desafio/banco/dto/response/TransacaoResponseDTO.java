package com.desafio.banco.dto.response;

import com.desafio.banco.model.enums.TipoTransacao;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransacaoResponseDTO(
    Long id,
    TipoTransacao tipo,
    BigDecimal valor,
    LocalDateTime dataHora,
    String numeroContaOrigem,
    String numeroContaDestino
) {}
