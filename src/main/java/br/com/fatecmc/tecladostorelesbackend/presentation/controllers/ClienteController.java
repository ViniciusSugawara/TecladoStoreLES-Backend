package br.com.fatecmc.tecladostorelesbackend.presentation.controllers;

import br.com.fatecmc.tecladostorelesbackend.domain.models.Cliente;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.*;
import br.com.fatecmc.tecladostorelesbackend.domain.services.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")
public class ClienteController {
    private ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @GetMapping
    public List<ClienteRetornoDTO> findAll() {
        return this.clienteService.findAll();
    }

    @GetMapping("/{id}")
    public ClienteRetornoDTO findById(@PathVariable("id")Long id){
        return this.clienteService.findById(id);
    }

    @PostMapping
    public ClienteRetornoDTO save(@RequestBody ClienteCadastroDTO cliente){
        return this.clienteService.save(cliente);
    }

    @PostMapping("/{id}/enderecos")
    public ClienteRetornoDTO addEndereco(@PathVariable("id")Long id, @RequestBody EnderecoDTO endereco){
        return this.clienteService.addEndereco(id, endereco);
    }

    @PutMapping("/{id}")
    public Cliente update(Long id, @RequestBody Cliente cliente){
        return this.clienteService.update(id, cliente);
    }

    @PatchMapping("/{id}/cartao")
    public ClienteDTO patchWithCartao(@PathVariable("id") Long id, @RequestBody CartaoDTO cartao){
        return this.clienteService.patchWithCartao(id, cartao);
    }

    @PatchMapping("/{id}/endereco")
    public ClienteDTO patchWithEndereco(@PathVariable("id") Long id, @RequestBody EnderecoDTO endereco){
        return this.clienteService.patchWithEndereco(id, endereco);
    }
    @DeleteMapping("/{id}")
    public void deleteById(Long id){
        this.clienteService.deleteById(id);
    }

}
