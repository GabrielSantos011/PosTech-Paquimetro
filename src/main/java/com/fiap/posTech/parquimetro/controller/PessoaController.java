package com.fiap.posTech.parquimetro.controller;

import com.fiap.posTech.parquimetro.dto.PessoaDTO;
import com.fiap.posTech.parquimetro.service.PessoaService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoas")
public class PessoaController {

    private final PessoaService pessoaService;

    @Autowired
    public PessoaController(PessoaService pessoaService) {
        this.pessoaService = pessoaService;
    }

    @GetMapping
    public ResponseEntity<Page<PessoaDTO>> findAll(@PageableDefault(size = 10, page = 0, sort= "codigo")
                                                   Pageable pageable) {
        Page<PessoaDTO> pessoas = pessoaService.findAll(pageable);
        return ResponseEntity.ok(pessoas);
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<PessoaDTO> findById(@PathVariable String codigo) {
        var pessoa = pessoaService.findById(codigo);
        return ResponseEntity.ok(pessoa);
    }

    @PostMapping
    public ResponseEntity<PessoaDTO> save(@Valid @RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO savedPessoa = pessoaService.save(pessoaDTO);
        return new ResponseEntity<>(savedPessoa, HttpStatus.CREATED);
    }

    @PutMapping("/{codigo}")
    public ResponseEntity<PessoaDTO> update(@PathVariable String codigo,
                                            @Valid @RequestBody PessoaDTO pessoaDTO) {
        PessoaDTO userUpdate = pessoaService.update(codigo, pessoaDTO);
        return ResponseEntity.ok().body(userUpdate);
    }

    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> delete(@PathVariable String codigo) {
        pessoaService.delete(codigo);
        return ResponseEntity.noContent().build();
    }
}
