package com.fiap.posTech.parquimetro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("endereco")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Endereco {
    @Id
    private String id;
    private String cep;
    private String tipoLogradouro; //Trabalho, Home, etc
    private String logradouro;
    private String numero;
    private String complemento;
    private String bairro;
    private String cidade;
    private String uf;

    @DBRef
    private Pessoa pessoa;

    @Version
    private Long version;
}
