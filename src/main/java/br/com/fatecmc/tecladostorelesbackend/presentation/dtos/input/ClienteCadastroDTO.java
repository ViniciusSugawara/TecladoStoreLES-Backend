package br.com.fatecmc.tecladostorelesbackend.presentation.dtos.input;

import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.BaseDTO;
import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output.EnderecoDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteCadastroDTO extends BaseDTO {
    private String nome;
    private Date dataNascimento;
    private String genero;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private EnderecoDTO enderecoResidencial;
}