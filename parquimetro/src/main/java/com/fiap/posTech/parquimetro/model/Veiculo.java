package com.fiap.posTech.parquimetro.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.Year;

@Document("refinamento")
@Data
@NoArgsConstructor
public class Veiculo {

    @Id
    private String id;

    private String modelo;
    private String cor;
    private Year anoFabrica;
    private Year anoModelo;

}
