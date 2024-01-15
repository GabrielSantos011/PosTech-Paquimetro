package com.fiap.posTech.parquimetro.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("veiculo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    private String id;

    private String modelo;
    private String cor;
    private String anoFabrica;
    private String anoModelo;
    private String placa;

    @JsonIgnore
    @DBRef
    private Pessoa pessoa;

    @JsonIgnore
    @Version
    private Long version;

}
