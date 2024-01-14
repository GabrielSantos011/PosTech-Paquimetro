package com.fiap.posTech.parquimetro.config;

import com.fiap.posTech.parquimetro.dto.EnderecoDTO;
import com.fiap.posTech.parquimetro.dto.PessoaDTO;
import com.fiap.posTech.parquimetro.dto.VeiculoDTO;
import com.fiap.posTech.parquimetro.model.Endereco;
import com.fiap.posTech.parquimetro.model.Pessoa;
import com.fiap.posTech.parquimetro.model.Veiculo;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class ModelMapperConfig {

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.createTypeMap(Endereco.class, EnderecoDTO.class)
                .addMapping(Endereco::getTipoLogradouro, EnderecoDTO::setTipoLogradouro)
                .addMapping(Endereco::getLogradouro, EnderecoDTO::setLogradouro)
                .addMapping(Endereco::getNumero, EnderecoDTO::setNumero)
                .addMapping(Endereco::getComplemento, EnderecoDTO::setComplemento)
                .addMapping(Endereco::getBairro, EnderecoDTO::setBairro)
                .addMapping(Endereco::getCidade, EnderecoDTO::setCidade)
                .addMapping(Endereco::getUf, EnderecoDTO::setUf)
                .addMapping(Endereco::getCep, EnderecoDTO::setCep);

        modelMapper.createTypeMap(Veiculo.class, VeiculoDTO.class)
                .addMapping(Veiculo::getModelo, VeiculoDTO::setModelo)
                .addMapping(Veiculo::getCor, VeiculoDTO::setCor)
                .addMapping(Veiculo::getAnoFabrica, VeiculoDTO::setAnoFabrica)
                .addMapping(Veiculo::getAnoModelo, VeiculoDTO::setAnoModelo)
                .addMapping(Veiculo::getPlaca, VeiculoDTO::setPlaca);

        TypeMap<Pessoa, PessoaDTO> typeMap = modelMapper.createTypeMap(Pessoa.class, PessoaDTO.class);
        typeMap.addMapping(src -> src.getEndereco(), PessoaDTO::setEnderecoDTO);
        typeMap.addMappings(mapper -> mapper.map(
                src -> mapVeiculosToVeiculoDTOList(src.getVeiculos()), PessoaDTO::setVeiculosDTO
        ));

        return modelMapper;
    }

    private List<VeiculoDTO> mapVeiculosToVeiculoDTOList(List<Veiculo> veiculos) {
        return veiculos != null ?
                veiculos.stream().map(this::toVeiculoDTO).collect(Collectors.toList()) : null;
    }

    private VeiculoDTO toVeiculoDTO(Veiculo veiculo) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(veiculo, VeiculoDTO.class);
    }
}