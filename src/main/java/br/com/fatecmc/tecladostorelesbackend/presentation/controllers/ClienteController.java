package br.com.fatecmc.tecladostorelesbackend.presentation.controllers;

import br.com.fatecmc.tecladostorelesbackend.domain.services.ClienteService;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteEditadoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.CartaoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.EnderecoDTO;
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

    @PostMapping("/{id}/cartoes")
    public ClienteRetornoDTO addCartao(@PathVariable("id")Long id, @RequestBody CartaoDTO cartao){
        return this.clienteService.addCartao(id, cartao);
    }

    @PutMapping("/{id}")
    public ClienteRetornoDTO update(@PathVariable("id") Long id, @RequestBody ClienteEditadoDTO cliente){
        return this.clienteService.update(id, cliente);
    }

    @PatchMapping("/{id}/cartao")
    public ClienteDTO patchWithCartao(@PathVariable("id") Long id, @RequestBody CartaoDTO cartao){
        return this.clienteService.patchWithCartao(id, cartao);
    }

    @PatchMapping("/{id}/enderecos/{idEndereco}")
    public ClienteRetornoDTO patchEndereco(@PathVariable("id") Long id, @PathVariable("idEndereco") Long idEndereco, @RequestBody EnderecoDTO endereco){
        return this.clienteService.patchEndereco(id, idEndereco, endereco);
    }

    @PatchMapping("/{id}/desativar")
    public void deactivate(@PathVariable("id")Long id){
        this.clienteService.deactivateById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteById(Long id){
        this.clienteService.deleteById(id);
    }

}
