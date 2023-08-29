package br.com.fatecmc.tecladostorelesbackend.domain.services;

import br.com.fatecmc.tecladostorelesbackend.data.repositories.EnderecoRepository;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cartao;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Endereco;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.ClienteDTO;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.ClienteRepository;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.EnderecoDTO;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;
    private ModelMapper mapper;
    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.mapper = new ModelMapper();
    }

    public List<ClienteDTO> findAll () {
        return this.clienteRepository.findAll()
                .stream()
                .map(user -> mapper.map(user, ClienteDTO.class))
                .collect(Collectors.toList());
    }

    public ClienteDTO findById(Long idCliente){
        verifyById(idCliente);

        Cliente cliente = this.clienteRepository.findById(idCliente).get();
        return mapper.map(cliente, ClienteDTO.class);
    }

    public Cliente save(ClienteCadastroDTO cliente){
        Cliente clienteMapeado = mapper.map(cliente, Cliente.class);
        return this.clienteRepository.save(clienteMapeado);
    }

    public Cliente update(Long idCliente, Cliente cliente){
        verifyById(idCliente);

        return this.clienteRepository.save(cliente);
    }

    public ClienteDTO patchWithCartao(Long idCliente, CartaoDTO cartao){
        verifyById(idCliente);

        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).get();
        cartao.setClienteId(idCliente);
        clienteRetornado.getCartoesCredito().add(mapper.map(cartao, Cartao.class));
        Cliente clienteSalvo = this.clienteRepository.save(clienteRetornado);
        return mapper.map(clienteSalvo, ClienteDTO.class);
    }

    public ClienteDTO patchWithEndereco(Long idCliente, EnderecoDTO endereco){
        verifyById(idCliente);

        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).get();
        System.out.println("Salvando endereço");

        Cliente clienteSalvo = this.clienteRepository.save(clienteRetornado);
        return mapper.map(clienteSalvo, ClienteDTO.class);
    }

    public void deleteById(Long idCliente){
        this.clienteRepository.deleteById(idCliente);
    }

    private void verifyById(Long id){
        if(!this.clienteRepository.existsById(id)) {
            throw new RuntimeException("Id não existe");
        }
    }
}
