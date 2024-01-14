package com.fiap.posTech.parquimetro.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fiap.posTech.parquimetro.model.EnumPagamento;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.br.CPF;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PessoaDTO {
        String id;
        @NotBlank(message = "O nome não pode estar em branco.")
        String nome;
        @Email(message = "E-mail inválido.")
        String email;
        @CPF(message = "CPF inválido.")
        String cpf;
        String rg;
        String celular;
        LocalDate dataNascimento;
        @JsonProperty("endereco")
        EnderecoDTO enderecoDTO;
        @JsonProperty("veiculos")
        List<VeiculoDTO> veiculosDTO;

        private EnumPagamento formaPagamento;
}