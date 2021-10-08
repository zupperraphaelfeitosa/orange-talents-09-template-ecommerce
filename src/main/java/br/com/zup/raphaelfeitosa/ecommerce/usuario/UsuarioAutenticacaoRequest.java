package br.com.zup.raphaelfeitosa.ecommerce.usuario;


import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class UsuarioAutenticacaoRequest {

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String senha;

    public UsuarioAutenticacaoRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public UsernamePasswordAuthenticationToken converter() {
        return new UsernamePasswordAuthenticationToken(email, senha);
    }
}