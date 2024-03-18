package br.com.fatecmc.tecladostorelesbackend.presentation.dtos.output;

import br.com.fatecmc.tecladostorelesbackend.presentation.dtos.BaseDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClienteRetornoDTO extends BaseDTO {
    private String nome;
    private LocalDate dataNascimento;
    private String genero;
    private String cpf;
    private String telefone;
    private String email;
    private String senha;
    private Set<Long> enderecosId;
    private Set<Long> cartoesCreditoId;
}
