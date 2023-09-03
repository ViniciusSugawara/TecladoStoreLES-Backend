package br.com.fatecmc.tecladostorelesbackend.presentation.dtos;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BaseDTO {
    @EqualsAndHashCode.Include
    private Long id;
}
