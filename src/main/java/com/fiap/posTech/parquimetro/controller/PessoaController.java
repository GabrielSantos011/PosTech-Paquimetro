package com.fiap.posTech.parquimetro.controller;

import com.fiap.posTech.parquimetro.controller.exception.ErrorResponse;
import com.fiap.posTech.parquimetro.controller.exception.FormaPagamentoInvalidaException;
import com.fiap.posTech.parquimetro.dto.PessoaDTO;
import com.fiap.posTech.parquimetro.model.EnumPagamento;
import com.fiap.posTech.parquimetro.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/pessoas", produces = {"application/json"})
@Tag(name = "Cadastro de Pessoas", description = "CRUD de Pessoas com o respectivo endereço e veículos")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;


    @GetMapping
    @Operation(summary = "Obtem todas as pessoas cadastradas", method = "GET")
    public ResponseEntity<Page<PessoaDTO>> findAll(@PageableDefault(size = 10, page = 0, sort= "nome")
                                                   Pageable pageable) {
        Page<PessoaDTO> pessoas = pessoaService.findAll(pageable);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Procurar pessoa por Id", method = "GET")
    public ResponseEntity<PessoaDTO> findById(@PathVariable String id) {
        var pessoa = pessoaService.findById(id);
        return ResponseEntity.ok(pessoa);
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova pessoa", method = "POST")
    public ResponseEntity<?> save(@Valid @RequestBody PessoaDTO pessoaDTO,
                                          @RequestParam String formaPagamento) {
        try {
            if (!EnumPagamento.contains(formaPagamento)) {
                throw new FormaPagamentoInvalidaException("Método de Pagamento Inválido");
            }
            EnumPagamento metodoPagamento = EnumPagamento.valueOf(formaPagamento.toUpperCase());
            pessoaDTO.setFormaPagamento(metodoPagamento);
            PessoaDTO savedPessoa = pessoaService.save(pessoaDTO);
            return new ResponseEntity<>(savedPessoa, HttpStatus.CREATED);
        }
        catch (FormaPagamentoInvalidaException e) {
            ErrorResponse errorResponse = new ErrorResponse(e.getTimestamp(), e.getStatus(), e.getError(), e.getMessage());
            return ResponseEntity.status(e.getStatus()).body(errorResponse);
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Alterar uma pessoa já cadastrada por Id", method = "PUT")
    public ResponseEntity<PessoaDTO> update(@PathVariable String id,
                                            @Valid @RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO userUpdate = pessoaService.update(id, pessoaDTO);
        return ResponseEntity.ok().body(userUpdate);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir uma pessoa já cadastrada por Id", method = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        pessoaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/forma-pagamento")
    @Operation(summary = "Cadastrar metodo de pagamento por Id", method = "POST")
    public ResponseEntity<Object> definirFormaPagamento(@PathVariable String id,
                                                        @RequestParam @Valid String formaPagamento) {
        try {
            if (!EnumPagamento.contains(formaPagamento)) {
                throw new FormaPagamentoInvalidaException("Método de Pagamento Inválido");
            }
            EnumPagamento metodoPagamento = EnumPagamento.valueOf(formaPagamento.toUpperCase());
            PessoaDTO pessoaDTO = pessoaService.definirFormaPagamento(id, metodoPagamento);
            return ResponseEntity.ok(pessoaDTO);
        } catch (FormaPagamentoInvalidaException e) {
            return ResponseEntity.status(e.getStatus()).body(new ErrorResponse(e.getTimestamp(), e.getStatus(), e.getError(), e.getMessage()));
        }

    }

}
