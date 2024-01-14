package com.fiap.posTech.parquimetro.model;

import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Document("pessoa")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {

    @Id
    private String id;
    private String cpf;
    private String rg;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String celular;

    @DBRef
    private Endereco endereco;

    @DBRef
    private List<Veiculo> veiculos;

    @DBRef
    private List<Pagamento> pagamentos;

    @Version
    private Long version;

    @Column(name = "forma_pagamento")
    @Enumerated(EnumType.STRING)
    private EnumPagamento formaPagamento;

    public void definirFormaPagamento(EnumPagamento formaPagamento) {
        this.formaPagamento = formaPagamento;
    }

    public void adicionarPagamento(Pagamento pagamento) {
        if (pagamentos == null) {
            pagamentos = new ArrayList<>();
        }
        pagamentos.add(pagamento);
    }


}
