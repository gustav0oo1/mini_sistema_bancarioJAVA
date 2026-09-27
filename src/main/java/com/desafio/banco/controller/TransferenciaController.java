package com.desafio.banco.controller;

import com.desafio.banco.dto.request.TransferenciaRequestDTO;
import com.desafio.banco.service.ContaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transferencias")
@Tag(name = "Transferências", description = "Endpoints para operações entre contas")
public class TransferenciaController {

    private final ContaService contaService;

    public TransferenciaController(ContaService contaService) {
        this.contaService = contaService;
    }

    @PostMapping
    @Operation(summary = "Realizar uma transferência entre duas contas")
    public ResponseEntity<Void> transferir(@Valid @RequestBody TransferenciaRequestDTO request) {
        contaService.transferir(request);
        return ResponseEntity.ok().build();
    }
}
