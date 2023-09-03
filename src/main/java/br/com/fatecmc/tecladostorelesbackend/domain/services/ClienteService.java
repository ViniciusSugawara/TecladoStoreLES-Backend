package br.com.fatecmc.tecladostorelesbackend.domain.services;

import br.com.fatecmc.tecladostorelesbackend.data.repositories.EnderecoRepository;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cartao;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Endereco;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.ClienteRepository;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteEditadoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.EnderecoDTO;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
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
                        clienteMapeado.setEnderecosId(convertEnderecosToId(cliente));
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
        clienteRetorno.setEnderecosId(convertEnderecosToId(cliente));
        return clienteRetorno;
    }

    public ClienteRetornoDTO save(ClienteCadastroDTO cliente){
        Cliente clienteMapeado = mapper.map(cliente, Cliente.class);
        clienteMapeado.getEnderecos().add(mapper.map(cliente.getEnderecoResidencial(), Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(this.clienteRepository.save(clienteMapeado), ClienteRetornoDTO.class);
        clienteRetorno.setEnderecosId(convertEnderecosToId(clienteMapeado));
        return clienteRetorno;
    }

    public ClienteRetornoDTO addEndereco(Long idCliente, EnderecoDTO endereco){
        verifyById(idCliente);
        Cliente cliente = this.clienteRepository.findById(idCliente).get();
        cliente.getEnderecos().add(mapper.map(endereco, Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(
                this.clienteRepository.save(cliente), ClienteRetornoDTO.class
        );

        clienteRetorno.setEnderecosId(convertEnderecosToId(cliente));

        return clienteRetorno;
    }

    public ClienteRetornoDTO addCartao(Long idCliente, CartaoDTO cartao){
        verifyById(idCliente);
        Cliente cliente = this.clienteRepository.findById(idCliente).get();
        cliente.getCartoesCredito().add(mapper.map(cartao, Cartao.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(
                this.clienteRepository.save(cliente), ClienteRetornoDTO.class
        );

        clienteRetorno.setCartoesCreditoId(convertCartoesToId(cliente));

        return clienteRetorno;
    }

    public ClienteRetornoDTO update(Long idCliente, ClienteEditadoDTO cliente){
        verifyById(idCliente);

        cliente.setId(idCliente);

        this.mapper.getConfiguration().setSkipNullEnabled(true);

        Cliente clienteSalvo = this.clienteRepository.findById(idCliente).get();

        mapper.map(cliente, clienteSalvo);

        ClienteRetornoDTO clienteRetornado =  mapper.map(
                this.clienteRepository.save(clienteSalvo), ClienteRetornoDTO.class
        );
        clienteRetornado.setEnderecosId(convertEnderecosToId(clienteSalvo));

        this.mapper.getConfiguration().setSkipNullEnabled(false);
        return clienteRetornado;
    }

    public ClienteDTO patchWithCartao(Long idCliente, CartaoDTO cartao){
        verifyById(idCliente);

        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).get();
        clienteRetornado.getCartoesCredito().add(mapper.map(cartao, Cartao.class));
        Cliente clienteSalvo = this.clienteRepository.save(clienteRetornado);
        return mapper.map(clienteSalvo, ClienteDTO.class);
    }

    // Edição de um endereço já existente no cliente

    public ClienteDTO patchEndereco(Long idCliente, EnderecoDTO endereco){
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
    private Set<Long> convertEnderecosToId(Cliente cliente){
        return cliente.getEnderecos().stream().map(endereco -> endereco.getId()).collect(Collectors.toSet());
    }
    private Set<Long> convertCartoesToId(Cliente cliente){
        return cliente.getCartoesCredito().stream().map(cartao -> cartao.getId()).collect(Collectors.toSet());
    }

}
