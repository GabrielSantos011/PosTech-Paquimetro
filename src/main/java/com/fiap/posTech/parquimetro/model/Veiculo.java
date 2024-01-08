package com.fiap.posTech.parquimetro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Year;

@Document("veiculo")
@Data
@AllArgsConstructor
public class Veiculo {

    @Id
    private String id;

    private String modelo;
    private String cor;
    private String anoFabrica;
    private String anoModelo;
    private String placa;

    @Version
    private Long version;

    public Veiculo() {}


}
