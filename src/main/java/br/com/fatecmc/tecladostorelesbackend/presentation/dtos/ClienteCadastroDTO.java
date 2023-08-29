package br.com.fatecmc.tecladostorelesbackend.presentation.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ClienteCadastroDTO {
    private String nome;
    private Date dataNascimento;
    private String genero;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private EnderecoDTO enderecoResidencial;
    private EnderecoDTO enderecoCobranca;
    private EnderecoDTO enderecoEntrega;
}
