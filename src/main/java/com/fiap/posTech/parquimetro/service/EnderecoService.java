package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.dto.EnderecoDTO;
import com.fiap.posTech.parquimetro.model.Endereco;
import com.fiap.posTech.parquimetro.repository.EnderecoRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class EnderecoService {
    private final EnderecoRepository enderecoRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public EnderecoService(EnderecoRepository enderecoRepository, ModelMapper modelMapper, MongoTemplate mongoTemplate) {
        this.enderecoRepository = enderecoRepository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
    }


    public void save(EnderecoDTO enderecoDTO) {
        Endereco endereco = toEndereco(enderecoDTO);
        enderecoRepository.save(endereco);
    }


    private Endereco toEndereco(EnderecoDTO enderecoDTO) {
        return modelMapper.map(enderecoDTO, Endereco.class);
    }

}
