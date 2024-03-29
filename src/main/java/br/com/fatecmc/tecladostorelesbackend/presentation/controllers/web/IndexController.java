package br.com.fatecmc.tecladostorelesbackend.presentation.controllers.web;

import br.com.fatecmc.tecladostorelesbackend.domain.models.cliente.Cliente;
import br.com.fatecmc.tecladostorelesbackend.domain.services.ClienteService;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteCadastroDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input.ClienteEditadoDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.ClienteRetornoDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;

@Controller
public class IndexController {
    private ClienteService clienteService;

    public IndexController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }
    @RequestMapping({"", "/", "index", "index.html"})
    public String getIndex(){
        return "index";
    }

    @GetMapping({"/admin/clients"})
    public ModelAndView getClientes(){
        ModelAndView mv = new ModelAndView("admin/clients/index");
        mv.addObject("clients", clienteService.findAll());
        return mv;
    }

    @GetMapping("/admin/clients/store")
    public ModelAndView cadastrar(ClienteCadastroDTO cliente) {
        ModelAndView mv = new ModelAndView("admin/clients/store");
        mv.addObject("cliente", cliente);
        return mv;
    }

    @GetMapping("/admin/clients/update/{idCliente}")
    public ModelAndView editar(@PathVariable("idCliente") Long idCliente){
        ModelAndView mv = new ModelAndView("admin/clients/update");
        ClienteRetornoDTO cliente = clienteService.findById(idCliente);
        mv.addObject("cliente", cliente);
        return mv;
    }

    @PostMapping("/admin/clients")
    public ModelAndView salvar(ClienteCadastroDTO cliente, BindingResult result){
        if(result.hasErrors()){
            return cadastrar(cliente);
        }
        clienteService.save(cliente);
        return cadastrar(new ClienteCadastroDTO());
    }
    @PutMapping("/admin/clients/{id}")
    public ModelAndView update(@PathVariable("id") Long idCliente, ClienteEditadoDTO cliente, BindingResult result){
        if(result.hasErrors()){
            return editar(idCliente);
        }
        clienteService.update(idCliente,cliente);
        return getClientes();
    }

    @DeleteMapping("/admin/clients/{id}")
    public ModelAndView deactivateById(@PathVariable("id") Long idCliente){
        clienteService.deactivateById(idCliente);
        return getClientes();
    }
    @PatchMapping("/admin/clients/reactivate/{id}")
    public ModelAndView reactivate(@PathVariable("id") Long idCliente){
        clienteService.reactivateById(idCliente);
        return getClientes();
    }
}
