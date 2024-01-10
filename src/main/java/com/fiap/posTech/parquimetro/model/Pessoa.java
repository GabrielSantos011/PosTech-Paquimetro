package com.fiap.posTech.parquimetro.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Singular;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Version;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("pessoa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pessoa {
    @Id
    private String codigo;
    private String cpf;
    private String rg;
    private String nome;
    private LocalDate dataNascimento;
    private String email;
    private String celular;

    @DBRef
    private Endereco endereco;

    @Singular
    @DBRef
    private List<Veiculo> veiculos;

    @Version
    private Long version;

}
