package com.desafio.banco.controller;

import com.desafio.banco.dto.request.ContaRequestDTO;
import com.desafio.banco.dto.request.TransacaoRequestDTO;
import com.desafio.banco.dto.response.ContaResponseDTO;
import com.desafio.banco.dto.response.TransacaoResponseDTO;
import com.desafio.banco.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contas")
@Tag(name = "Contas", description = "Endpoints para gerenciamento de contas e operações bancárias individuais")
public class ContaController {

    private final ContaService contaService;

    public ContaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @Operation(summary = "Criar uma nova conta")
    public ResponseEntity<ContaResponseDTO> criar(@Valid @RequestBody ContaRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(contaService.criarConta(request));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar dados e saldo da conta")
    public ResponseEntity<ContaResponseDTO> buscar(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscarPorId(id));
    }

    @PostMapping("/{id}/depositar")
    @Operation(summary = "Realizar um depósito na conta")
    public ResponseEntity<Void> depositar(@PathVariable Long id, @Valid @RequestBody TransacaoRequestDTO request) {
        contaService.depositar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/sacar")
    @Operation(summary = "Realizar um saque da conta")
    public ResponseEntity<Void> sacar(@PathVariable Long id, @Valid @RequestBody TransacaoRequestDTO request) {
        contaService.sacar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/extrato")
    @Operation(summary = "Consultar o extrato da conta")
    public ResponseEntity<List<TransacaoResponseDTO>> extrato(@PathVariable Long id) {
        return ResponseEntity.ok(contaService.buscarExtrato(id));
    }
}
