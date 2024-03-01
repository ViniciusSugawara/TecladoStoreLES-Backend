package br.com.fatecmc.tecladostorelesbackend.presentation.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Response<T>{

    private HttpStatus status;
    private String message;
    private T data;
}
