package com.desafio.banco.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ContaRequestDTO(
    @NotBlank(message = "O nome do titular é obrigatório")
    String titular,

    @NotNull(message = "O saldo inicial é obrigatório")
    @PositiveOrZero(message = "O saldo inicial não pode ser negativo")
    BigDecimal saldoInicial
) {}
