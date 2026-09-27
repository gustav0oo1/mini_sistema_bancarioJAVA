package com.desafio.banco.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record TransferenciaRequestDTO(
    @NotNull(message = "O ID da conta de origem é obrigatório")
    Long contaOrigemId,

    @NotNull(message = "O ID da conta de destino é obrigatório")
    Long contaDestinoId,

    @NotNull(message = "O valor é obrigatório")
    @Positive(message = "O valor deve ser maior que zero")
    BigDecimal valor
) {}
