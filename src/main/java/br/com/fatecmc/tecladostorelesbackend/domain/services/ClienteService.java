package br.com.fatecmc.tecladostorelesbackend.domain.services;

import br.com.fatecmc.tecladostorelesbackend.domain.models.Cartao;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.Endereco;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.ClienteRepository;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteEditadoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.EnderecoDTO;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;
    private ModelMapper mapper;
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
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
        if(senhaInvalida(cliente.getSenha())){
            throw new RuntimeException("Senha invalida");
        }
        Cliente clienteMapeado = mapper.map(cliente, Cliente.class);
        clienteMapeado.getEnderecos().add(mapper.map(cliente.getEnderecoResidencial(), Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(this.clienteRepository.save(clienteMapeado), ClienteRetornoDTO.class);
        clienteRetorno.setEnderecosId(convertEnderecosToId(clienteMapeado));
        return clienteRetorno;
    }

    private boolean senhaInvalida(String senha){
        String regex = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);
        return matcher.matches();
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

    // Edição de um endereço já existente no cliente
    public ClienteRetornoDTO patchEndereco(Long idCliente, Long idEndereco, EnderecoDTO endereco){
        verifyById(idCliente);

        mapper.getConfiguration().setSkipNullEnabled(true);

        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).get();
        Endereco enderecoEncontrado = clienteRetornado.getEnderecos()
                .stream()
                .filter(endereco1 -> endereco1.getId() == idEndereco)
                .findFirst()
                .get();
        mapper.map(endereco, enderecoEncontrado);
        clienteRetornado.getEnderecos().add(enderecoEncontrado);

        mapper.getConfiguration().setSkipNullEnabled(false);

        Cliente clienteSalvo = this.clienteRepository.save(clienteRetornado);
        return mapper.map(clienteSalvo, ClienteRetornoDTO.class);
    }

    public void deactivateById(Long idCliente){
        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).orElseThrow();
        clienteRetornado.setAtivo(false);
        this.clienteRepository.save(clienteRetornado);
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
