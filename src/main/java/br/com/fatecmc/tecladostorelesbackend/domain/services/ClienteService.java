package br.com.fatecmc.tecladostorelesbackend.domain.services;

import br.com.fatecmc.tecladostorelesbackend.data.repositories.CartaoRepository;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.EnderecoRepository;
import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Cartao;
import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Endereco;
import br.com.fatecmc.tecladostorelesbackend.data.repositories.ClienteRepository;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteEditadoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.EnderecoDTO;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ClienteService {
    private ClienteRepository clienteRepository;
    private EnderecoRepository enderecoRepository;
    private CartaoRepository cartaoRepository;
    private ModelMapper mapper;
    public ClienteService(ClienteRepository clienteRepository, EnderecoRepository enderecoRepository, CartaoRepository cartaoRepository) {
        this.clienteRepository = clienteRepository;
        this.enderecoRepository = enderecoRepository;
        this.cartaoRepository = cartaoRepository;
        this.mapper = new ModelMapper();
    }

    public List<ClienteRetornoDTO> findAll () {

        List<ClienteRetornoDTO> listaMapeada = new ArrayList<>();

        this.clienteRepository.findAll(Sort.by(Sort.Direction.ASC, "id"))
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

    public Endereco findEnderecoOnCliente(Long idCliente, Long idEndereco){
        return clienteRepository.findById(idCliente)
                .get()
                .getEnderecos()
                .stream()
                .filter(endereco1 -> endereco1.getId() == idEndereco)
                .findFirst()
                .get();
    }

    public Set<EnderecoDTO> findEnderecosByClienteId(Long idCliente){
        Cliente cliente = this.clienteRepository.findById(idCliente).get();

        Set<EnderecoDTO> enderecosMapeados = new HashSet<>();
        cliente.getEnderecos()
                .stream()
                .forEach(endereco -> {
            EnderecoDTO enderecomap = mapper.map(endereco, EnderecoDTO.class);
            enderecosMapeados.add(enderecomap);
        });

        return enderecosMapeados;
    }


    public Set<CartaoDTO> findCartoesByClienteId(Long idCliente) {
        Cliente cliente = this.clienteRepository.findById(idCliente).get();

        Set<CartaoDTO> cartoesMapeados = new HashSet<>();
        cliente.getCartoesCredito()
                .stream()
                .forEach(cartao -> {
                    CartaoDTO cartaomap = mapper.map(cartao, CartaoDTO.class);
                    cartoesMapeados.add(cartaomap);
                });

        return cartoesMapeados;
    }

    public ClienteRetornoDTO save(ClienteCadastroDTO cliente){
        if(senhaInvalida(cliente.getSenha())){
            throw new RuntimeException("Senha invalida");
        }
        mapper.getConfiguration().setSkipNullEnabled(true);
        Cliente clienteMapeado = mapper.map(cliente, Cliente.class);
        clienteMapeado.getEnderecos().add(mapper.map(cliente.getEnderecoResidencial(), Endereco.class));

        ClienteRetornoDTO clienteRetorno = mapper.map(this.clienteRepository.save(clienteMapeado), ClienteRetornoDTO.class);
        clienteRetorno.setEnderecosId(convertEnderecosToId(clienteMapeado));

        mapper.getConfiguration().setSkipNullEnabled(false);
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

    // Edição de um endereço já existente no cliente
    public ClienteRetornoDTO patchEndereco(Long idCliente, Long idEndereco, EnderecoDTO endereco){
        verifyById(idCliente);

        mapper.getConfiguration().setSkipNullEnabled(true);

        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).get();
        Endereco enderecoEncontrado = findEnderecoOnCliente(idCliente, idEndereco);
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

    public void reactivateById(Long idCliente){
        Cliente clienteRetornado = this.clienteRepository.findById(idCliente).orElseThrow();
        clienteRetornado.setAtivo(true);
        this.clienteRepository.save(clienteRetornado);
    }

    public void deleteById(Long idCliente){
        this.clienteRepository.deleteById(idCliente);
    }

    public Long verifyMaxAddressId(){
        return this.enderecoRepository.findAll()
                .stream()
                .mapToLong(Endereco::getId)
                .max().orElse(0l) + 1;
    }
    public Long verifyMaxPaymentId(){
        return this.cartaoRepository.findAll()
                .stream()
                .mapToLong(Cartao::getId)
                .max().orElse(0l) + 1;
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

    private boolean senhaInvalida(String senha){
        String regex = "^(.{0,7}|[^0-9]*|[^A-Z]*|[^a-z]*|[a-zA-Z0-9]*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(senha);
        return matcher.matches();
    }

}
