package br.com.fatecmc.tecladostorelesbackend.domain.services;

import br.com.fatecmc.tecladostorelesbackend.data.repositories.EnderecoRepository;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cartao;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Endereco;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.*;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.ClienteRepository;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
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

    public List<ClienteRetornoDTO> findAll () {

        List<ClienteRetornoDTO> listaMapeada = new ArrayList<>();

        this.clienteRepository.findAll()
                .stream()
                .forEach(cliente -> {
                    ClienteRetornoDTO clienteMapeado = mapper.map(cliente, ClienteRetornoDTO.class);

                    if(!cliente.getEnderecos().isEmpty()){
                        clienteMapeado.setEnderecosId(
                                cliente.getEnderecos()
                                        .stream()
                                        .map(endereco -> endereco.getId())
                                        .collect(Collectors.toSet())
                        );
                    }

                    if(!cliente.getCartoesCredito().isEmpty()){
                        clienteMapeado.setCartoesCreditoId(
                                cliente.getCartoesCredito()
                                        .stream()
                                        .map(cartao -> cartao.getId())
                                        .collect(Collectors.toSet())
                        );
                    }
                    listaMapeada.add(clienteMapeado);
                });

        return listaMapeada;
    }

    public ClienteRetornoDTO findById(Long idCliente){
        verifyById(idCliente);

        Cliente cliente = this.clienteRepository.findById(idCliente).get();
        ClienteRetornoDTO clienteRetorno = mapper.map(cliente, ClienteRetornoDTO.class);
        clienteRetorno.setEnderecosId(cliente.getEnderecos().stream().map(endereco -> endereco.getId()).collect(Collectors.toSet()));
        return clienteRetorno;
    }

    public ClienteRetornoDTO save(ClienteCadastroDTO cliente){
        Cliente clienteMapeado = mapper.map(cliente, Cliente.class);
        clienteMapeado.getEnderecos().add(mapper.map(cliente.getEnderecoResidencial(), Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(this.clienteRepository.save(clienteMapeado), ClienteRetornoDTO.class);
        clienteRetorno.setEnderecosId(clienteMapeado.getEnderecos().stream().map(endereco -> endereco.getId()).collect(Collectors.toSet()));
        return clienteRetorno;
    }

    public ClienteRetornoDTO addEndereco(Long idCliente, EnderecoDTO endereco){
        verifyById(idCliente);
        Cliente cliente = this.clienteRepository.findById(idCliente).get();
        cliente.getEnderecos().add(mapper.map(endereco, Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(
                this.clienteRepository.save(cliente), ClienteRetornoDTO.class
        );

        clienteRetorno.setEnderecosId(
                cliente.getEnderecos()
                        .stream()
                        .map(enderecos -> enderecos.getId())
                        .collect(Collectors.toSet())
        );

        return clienteRetorno;
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
