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
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PessoaService {

    private final PessoaRepository pessoaRepository;
    private final EnderecoRepository enderecoRepository;
    private final VeiculoRepository veiculoRepository;
    private final ModelMapper modelMapper;
    private final MongoTemplate mongoTemplate;

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<String> handleOptimistcLockingFaulureException(
            OptimisticLockingFailureException ex) {
        return  ResponseEntity.status(HttpStatus.CONFLICT).
                body("Erro de concorrência: A Pessoa foi atualizado por outro usuario");
    }

    public Page<PessoaDTO> findAll(Pageable pageable) {
        return pessoaRepository.findAll(pageable).map(this::toPessoaDTO);
    }
    @Transactional
    public PessoaDTO findById(String codigo) {
        Pessoa pessoa = pessoaRepository.findById(codigo).orElseThrow(() -> new ControllerNotFoundException("Usuario não encontrado"));
        return toPessoaDTO(pessoa);
    }
    @Transactional
    public PessoaDTO save(PessoaDTO pessoaDTO) {
        Pessoa pessoa = toPessoa(pessoaDTO);
        if (pessoaDTO.getEnderecoDTO() != null) {
            Endereco endereco = toEndereco(pessoaDTO.getEnderecoDTO());
            enderecoRepository.save(endereco);
            pessoa.setEndereco(endereco);
        }

        List<VeiculoDTO> veiculosDTO = pessoaDTO.getVeiculosDTO();
        List<Veiculo> veiculos = new ArrayList<>();

        if (veiculosDTO != null && !veiculosDTO.isEmpty()) {
            for (VeiculoDTO veiculoDTO : veiculosDTO) {
                Veiculo veiculo = toVeiculo(veiculoDTO);
                veiculoRepository.save(veiculo);
                veiculos.add(veiculo);
            }
            pessoa.setVeiculos(veiculos);
        }
        pessoa = pessoaRepository.save(pessoa);
        return toPessoaDTO(pessoa);
    }
    @Transactional
    public PessoaDTO update(String codigo, PessoaDTO pessoaDTO) {
        try {
            Pessoa pessoa = pessoaRepository.findById(codigo)
                    .orElseThrow(() -> new ControllerNotFoundException("Usuario não encontrado"));
            pessoa.setNome(pessoaDTO.getNome());
            pessoa.setEmail(pessoaDTO.getEmail());
            pessoa.setCpf(pessoaDTO.getCpf());
            pessoa.setRg(pessoaDTO.getRg());
            pessoa.setCelular(pessoaDTO.getCelular());
            pessoa.setDataNascimento(pessoaDTO.getDataNascimento());

            updateEndereco(pessoaDTO.getEnderecoDTO(), pessoa);

            List<VeiculoDTO> veiculosDTO = pessoaDTO.getVeiculosDTO();
            List<Veiculo> veiculos = new ArrayList<>();

            if (veiculosDTO != null && !veiculosDTO.isEmpty()) {
                for (VeiculoDTO veiculoDTO : veiculosDTO) {
                    Veiculo veiculo = pessoa.getVeiculos()
                            .stream()
                            .filter(v -> v.getId().equals(veiculoDTO.getId()))
                            .findFirst()
                            .orElse(null);

                    if (veiculo != null) {
                        veiculo.setModelo(veiculoDTO.getModelo());
                        veiculo.setCor(veiculoDTO.getCor());
                        veiculo.setAnoFabrica(veiculoDTO.getAnoFabrica());
                        veiculo.setAnoModelo(veiculoDTO.getAnoModelo());
                        veiculo.setPlaca(veiculoDTO.getPlaca());
                        veiculoRepository.save(veiculo);
                    } else {
                        Veiculo novoVeiculo = toVeiculo(veiculoDTO);
                        veiculoRepository.save(novoVeiculo);
                        veiculos.add(novoVeiculo);
                    }
                }
                pessoa.setVeiculos(veiculos);
            }

            pessoa = pessoaRepository.save(pessoa);
            return toPessoaDTO(pessoa);
        } catch (EntityNotFoundException e) {
            throw new ControllerNotFoundException("Usuario com id:" + codigo + " não encontrado");
        }

    }
    @Transactional
    public void delete(String codigo) {
        pessoaRepository.deleteById(codigo);
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

    private void updateEndereco(EnderecoDTO enderecoDTO, Pessoa pessoa) {
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

            enderecoRepository.save(endereco);
        }
    }
}
