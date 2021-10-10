package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenResponse;
import br.com.zup.raphaelfeitosa.ecommerce.config.security.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/autenticacao")
public class UsuarioAutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public TokenResponse autenticar(@RequestBody @Valid UsuarioAutenticacaoRequest usuarioAutenticacaoRequest) {
        UsernamePasswordAuthenticationToken dadosLogin = usuarioAutenticacaoRequest.converter();

        try {
            Authentication authentication = authenticationManager.authenticate(dadosLogin);
            String token = tokenService.gerarToken(authentication);
            return new TokenResponse(token, "Bearer");

        } catch (AuthenticationException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }
}