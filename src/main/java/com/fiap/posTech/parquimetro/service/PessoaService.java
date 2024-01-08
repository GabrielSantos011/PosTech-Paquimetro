package com.fiap.posTech.parquimetro.service;

import com.fiap.posTech.parquimetro.controller.exception.ControllerNotFoundException;
import com.fiap.posTech.parquimetro.dto.EnderecoDTO;
import com.fiap.posTech.parquimetro.dto.PessoaDTO;
import com.fiap.posTech.parquimetro.dto.VeiculoDTO;
import com.fiap.posTech.parquimetro.model.Endereco;
import com.fiap.posTech.parquimetro.model.Pessoa;
import com.fiap.posTech.parquimetro.model.Veiculo;
import com.fiap.posTech.parquimetro.repository.EnderecoRepository;
import com.fiap.posTech.parquimetro.repository.PessoaRepository;
import com.fiap.posTech.parquimetro.repository.VeiculoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class PessoaService {

    private final PessoaRepository repository;
    private final EnderecoRepository enderecoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @Autowired
    public PessoaService(PessoaRepository repository, ModelMapper modelMapper, MongoTemplate mongoTemplate,
                         EnderecoRepository enderecoRepository, VeiculoRepository veiculoRepository) {
        this.repository = repository;
        this.modelMapper = modelMapper;
        this.mongoTemplate = mongoTemplate;
        this.enderecoRepository = enderecoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    public Page<PessoaDTO> findAll(Pageable pageable) {
        Page<Pessoa> pessoas = repository.findAll(pageable);
        return pessoas.map(this::toPessoaDTO);
    }

    public PessoaDTO findById(String codigo) {
        Pessoa pessoa = repository.findById(codigo).orElseThrow(() -> new ControllerNotFoundException("Usuario não encontrado"));
        return toPessoaDTO(pessoa);
    }

    public PessoaDTO save(PessoaDTO pessoaDTO) {
        Pessoa pessoa = toPessoa(pessoaDTO);
        if (pessoaDTO.getEnderecoDTO() != null) {
            Endereco endereco = toEndereco(pessoaDTO.getEnderecoDTO());
            enderecoRepository.save(endereco);
            pessoa.setEndereco(endereco);
        }

        if (pessoaDTO.getVeiculoDTO() != null) {
            Veiculo veiculo = toVeiculo(pessoaDTO.getVeiculoDTO());
            veiculoRepository.save(veiculo);
            pessoa.setVeiculo(veiculo);
        }
        pessoa = repository.save(pessoa);
        return toPessoaDTO(pessoa);
    }

    public PessoaDTO update(String codigo, PessoaDTO pessoaDTO) {
        try {
            Pessoa pessoa = repository.findById(codigo)
                    .orElseThrow(() -> new ControllerNotFoundException("Usuario não encontrado"));
            pessoa.setNome(pessoaDTO.getNome());
            pessoa.setEmail(pessoaDTO.getEmail());
            pessoa.setCpf(pessoaDTO.getCpf());
            pessoa.setRg(pessoaDTO.getRg());
            pessoa.setCelular(pessoaDTO.getCelular());
            pessoa.setDataNascimento(pessoaDTO.getDataNascimento());

            EnderecoDTO enderecoDTO = pessoaDTO.getEnderecoDTO();
            if (enderecoDTO != null) {
                Endereco endereco = pessoa.getEndereco();
                endereco.setTipoLogradouro(enderecoDTO.getTipoLogradouro());
                endereco.setLogradouro(enderecoDTO.getLogradouro());
                endereco.setNumero(enderecoDTO.getNumero());
                endereco.setComplemento(enderecoDTO.getComplemento());
                endereco.setBairro(enderecoDTO.getBairro());
                endereco.setCidade(enderecoDTO.getCidade());
                endereco.setUf(enderecoDTO.getUf());
                endereco.setCep(enderecoDTO.getCep());

                pessoa.setEndereco(endereco);
            }

            VeiculoDTO veiculoDTO = pessoaDTO.getVeiculoDTO();
            if(veiculoDTO != null) {
                Veiculo veiculo = pessoa.getVeiculo();
                veiculo.setModelo(veiculoDTO.getModelo());
                veiculo.setCor(veiculoDTO.getCor());
                veiculo.setAnoFabrica(veiculoDTO.getAnoFabrica());
                veiculo.setAnoModelo(veiculoDTO.getAnoModelo());
                veiculo.setPlaca(veiculoDTO.getPlaca());

                pessoa.setVeiculo(veiculo);
            }

            pessoa = repository.save(pessoa);
            return toPessoaDTO(pessoa);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Usuario com id:" + codigo + " não encontrado");
        }

    }

    public void delete(String codigo) {
        repository.deleteById(codigo);
    }

    private PessoaDTO toPessoaDTO(Pessoa pessoa) {
        return modelMapper.map(pessoa, PessoaDTO.class);
    }

    private Pessoa toPessoa(PessoaDTO pessoaDTO) {
        return modelMapper.map(pessoaDTO, Pessoa.class);
    }
    private Endereco toEndereco(EnderecoDTO enderecoDTO) {
        return modelMapper.map(enderecoDTO, Endereco.class);
    }

    private Veiculo toVeiculo(VeiculoDTO veiculoDTO) {
        return modelMapper.map(veiculoDTO, Veiculo.class);
    }

}
