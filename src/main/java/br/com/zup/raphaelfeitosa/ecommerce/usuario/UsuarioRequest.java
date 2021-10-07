package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao.CampoUnicoGenerico;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UsuarioRequest {

    @NotBlank
    @Email
    @CampoUnicoGenerico(
            classeDominio = Usuario.class,
            nomeCampo = "email",
            message = "Email existente no banco de dados")
    private String email;

    @NotBlank(message = "A senha não pode ser em branco ou nula")
    @Size(min = 6, message = "A senha deve ter no minimo 6 caracteres")
    private String senha;

    public UsuarioRequest(String email, String senha) {
        this.email = email;
        this.senha = senha;
    }

    public Usuario toUsuario() {
        return new Usuario(email, senha);
    }
}
