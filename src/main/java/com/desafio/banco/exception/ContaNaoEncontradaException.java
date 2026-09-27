package com.desafio.banco.exception;

public class ContaNaoEncontradaException extends NegocioException {
    public ContaNaoEncontradaException(Long id) {
        super("Conta com ID " + id + " não encontrada.");
    }
}
