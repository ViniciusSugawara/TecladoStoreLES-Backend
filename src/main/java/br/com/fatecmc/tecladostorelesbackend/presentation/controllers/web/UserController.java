package br.com.fatecmc.tecladostorelesbackend.presentation.controllers.web;

import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Endereco;
import br.com.fatecmc.tecladostorelesbackend.domain.models.produto.Teclado;
import br.com.fatecmc.tecladostorelesbackend.domain.services.ClienteService;
import br.com.fatecmc.tecladostorelesbackend.domain.services.TecladoMockService;
import br.com.fatecmc.tecladostorelesbackend.domain.services.interfaces.ITecladoService;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.EnderecoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.TecladoRetornoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Controller
@RequestMapping("/user")
public class UserController {
    private ITecladoService tecladoService;
    private ClienteService clienteService;
    public UserController(ITecladoService tecladoService, ClienteService clienteService){
        this.tecladoService = tecladoService;
        this.clienteService = clienteService;
    }
    @GetMapping("/products")
    public ModelAndView getProdutos(){
        ArrayList<TecladoRetornoDTO> teclados = tecladoService.findAll();
        ModelAndView mv = new ModelAndView("user/products/index");

        mv.addObject("teclados", teclados);
        return mv;
    }

    @GetMapping("/products/{id}")
    public ModelAndView getProduct(@PathVariable("id")Long id){
        TecladoRetornoDTO teclado = new TecladoRetornoDTO("Keychron", "C1", 500d);
        teclado.setId(id);

        ModelAndView mv = new ModelAndView("user/products/productdetails");

        mv.addObject("produto", teclado);
        return mv;
    }

    public ModelAndView getPedidos(){
        return new ModelAndView();
    }
    @GetMapping("/profile/{id}")
    public ModelAndView getPerfil(@PathVariable("id")Long id){
        ModelAndView mv = new ModelAndView("user/profile/index");

        ClienteRetornoDTO clienteTeste = clienteService.findById(id);
        Set<EnderecoDTO> enderecosTeste = clienteService.findEnderecosByClienteId(id);
        Set<CartaoDTO> cartoesTeste = clienteService.findCartoesByClienteId(id);

        mv.addObject("cliente", clienteTeste);
        mv.addObject("enderecos", enderecosTeste);
        mv.addObject("cartoes", cartoesTeste);
        return mv;
    }
    @GetMapping("/profile/{id}/updatepassword")
    public ModelAndView getUpdateSenha(){
        return new ModelAndView("user/profile/updatepassword");
    }
    @GetMapping("/profile/{id}/address")
    public ModelAndView getAddEndereco(@PathVariable("id")Long idCliente){
        ModelAndView mv = new ModelAndView("user/profile/addaddress");
        ClienteRetornoDTO cliente = clienteService.findById(idCliente);

        mv.addObject("cliente", cliente);
        mv.addObject("endereco", new Endereco());

        return mv;
    }
    @GetMapping("profile/{id}/payment")
    public ModelAndView getAddCartao(@PathVariable("id")Long idCliente){
        ModelAndView mv = new ModelAndView("user/profile/addpayment");

        mv.addObject("idCliente", idCliente);
        mv.addObject("cartao", new CartaoDTO());

        return mv;
    }
    @GetMapping("/profile/{id}/address/{idEndereco}")
    public ModelAndView getUpdateEndereco(@PathVariable("id")Long idCliente, @PathVariable("idEndereco")Long idEndereco){

        ModelAndView mv = new ModelAndView("user/profile/updateaddress");
        Endereco enderecoParaEditar = clienteService.findEnderecoOnCliente(idCliente, idEndereco);

        mv.addObject("idCliente", idCliente);
        mv.addObject("endereco", enderecoParaEditar);
        return mv;
    }
    @GetMapping("/profile/{id}/payments")
    public ModelAndView getUpdateCartao(){
        return new ModelAndView("user/profile/updatepayment");
    }

    @PostMapping("/profile/{id}/address")
    public ModelAndView addEndereco(@PathVariable("id")Long id, EnderecoDTO endereco){
        endereco.setId(this.clienteService.verifyMaxAddressId());
        ClienteRetornoDTO cliente = clienteService.addEndereco(id, endereco);
        return this.getPerfil(cliente.getId());
    }
    @PostMapping("/profile/{id}/payment")
    public ModelAndView addCartao(@PathVariable("id")Long id, CartaoDTO cartao){
        cartao.setId(this.clienteService.verifyMaxPaymentId());
        ClienteRetornoDTO cliente = clienteService.addCartao(id, cartao);
        return this.getPerfil(cliente.getId());
    }
    @PatchMapping("/profile/{id}/address/{idEndereco}")
    public ModelAndView patchEndereco(@PathVariable("id")Long idCliente, @PathVariable("idEndereco")Long idEndereco, EnderecoDTO endereco){
        endereco.setId(idEndereco);
        this.clienteService.patchEndereco(idCliente, idEndereco, endereco);
        return this.getPerfil(idCliente);
    }
}
