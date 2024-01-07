package com.fiap.posTech.parquimetro.dto;

public record EnderecoDTO(
        Integer cep,
        String tipoLogradouro,
        String logradouro,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String uf
) {
}
