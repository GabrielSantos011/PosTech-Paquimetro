package com.fiap.posTech.parquimetro.controller;

import com.fiap.posTech.parquimetro.dto.PessoaDTO;
import com.fiap.posTech.parquimetro.service.PessoaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
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
@Tag(name = "pessoas", description = "Crud de Pessoas com o respectivo endereço e veículos")
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

    @GetMapping("/{codigo}")
    @Operation(summary = "Procurar pessoa por Id", method = "GET")
    public ResponseEntity<PessoaDTO> findById(@PathVariable String codigo) {
        var pessoa = pessoaService.findById(codigo);
        return ResponseEntity.ok(pessoa);
    }

    @PostMapping
    @Operation(summary = "Cadastrar uma nova pessoa", method = "POST")
    public ResponseEntity<PessoaDTO> save(@Valid @RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO savedPessoa = pessoaService.save(pessoaDTO);
        return new ResponseEntity<>(savedPessoa, HttpStatus.CREATED);
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Alterar uma pessoa já cadastrada por Id", method = "PUT")
    public ResponseEntity<PessoaDTO> update(@PathVariable String codigo,
                                            @Valid @RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO userUpdate = pessoaService.update(codigo, pessoaDTO);
        return ResponseEntity.ok().body(userUpdate);
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Excluir uma pessoa já cadastrada por Id", method = "DELETE")
    public ResponseEntity<Void> delete(@PathVariable String codigo) {
        pessoaService.delete(codigo);
        return ResponseEntity.noContent().build();
    }
}
