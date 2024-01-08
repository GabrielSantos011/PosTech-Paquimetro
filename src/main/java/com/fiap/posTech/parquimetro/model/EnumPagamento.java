package com.fiap.posTech.parquimetro.model;

public enum EnumPagamento {
    DINHEIRO("Dinherio"),
    DEBITO("Débito"),
    CREDITO("Crédito"),
    PIX("Pix");

    private String descricao;

    EnumPagamento(String descricao) {
        this.descricao = descricao;
    }
}

