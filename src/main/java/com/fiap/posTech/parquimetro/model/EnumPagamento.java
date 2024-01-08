package com.fiap.posTech.parquimetro.model;

public enum EnumPagamento {
    DINHEIRO("1", "DINHEIRO"),
    DEBITO("2", "DEBITO"),
    CREDITO("3", "CREDITO"),
    PIX("4", "PIX");

    private String description;
    private Long value;

    EnumPagamento(String description, Long value) {
        this.description = description;
        this.value = value;
    }

    public String getDescription() {
        return this.description;
    }

    public Long getValue() {
        return this.value;
    }
}

