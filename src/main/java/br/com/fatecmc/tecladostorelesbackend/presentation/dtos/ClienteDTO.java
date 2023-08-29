package br.com.fatecmc.tecladostorelesbackend.presentation.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteDTO {
    private String nome;
    private Date dataNascimento;
    private String genero;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private Set<EnderecoDTO> enderecos;
    private Set<Long> cartoesCreditoId;
}
