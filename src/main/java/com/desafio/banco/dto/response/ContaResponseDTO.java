package com.desafio.banco.dto.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ContaResponseDTO(
    Long id,
    String titular,
    String numeroConta,
    BigDecimal saldo,
    LocalDateTime dataCriacao
) {}
