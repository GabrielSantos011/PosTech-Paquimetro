package com.fiap.posTech.parquimetro.controller;

import com.fiap.posTech.parquimetro.model.Park;
import com.fiap.posTech.parquimetro.service.ParkService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(value = "/park", produces = {"application/json"})
@Tag(name = "Cadastro de Parking", description = "CRUD de Parking")
public class ParkController {
    @Autowired
    private ParkService service;


    @PostMapping(value = "/parking")
    public ResponseEntity<?> parking(@Valid @RequestBody Park parking) {
        return this.service.checkin(parking);
    }

    @PutMapping(value = "/unparking/{id}")
    public ResponseEntity<?> unparking(@PathVariable String id) {
        var unparking = service.checkout(id);
        return ResponseEntity.ok(unparking);
    }

    @GetMapping("/{id}")
    public Optional<Park> findById(@PathVariable String id) {
        return this.service.findById(id);
    }

    @GetMapping
    public ResponseEntity<Page<Park>> findall (
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "quantidade", defaultValue = "20") Integer quantidade,
            @RequestParam(value = "direcao", defaultValue = "ASC") String direcao,
            @RequestParam(value = "ordenacao", defaultValue = "pessoa.nome") String ordenacao) {
        PageRequest pageRequest = PageRequest.of(pagina, quantidade, Sort.Direction.valueOf(direcao), ordenacao);
        var list = service.listaTodos(pageRequest);
        return ResponseEntity.ok().body(list);
    }

    @GetMapping("/ativos")
    public ResponseEntity<Page<Park>> getParksAtivos(
            @RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
            @RequestParam(value = "quantidade", defaultValue = "20") Integer quantidade,
            @RequestParam(value = "direcao", defaultValue = "ASC") String direcao,
            @RequestParam(value = "ordenacao", defaultValue = "pessoa.nome") String ordenacao) {
        PageRequest pageRequest = PageRequest.of(pagina, quantidade, Sort.Direction.valueOf(direcao), ordenacao);
        var list = service.getParksAtivos(pageRequest);
        return ResponseEntity.ok().body(list);
    }

}
