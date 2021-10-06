package br.com.zup.raphaelfeitosa.ecommerce.usuario;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.security.Signature;
import java.time.LocalDate;

@Entity
@Table(name = "tb_users")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private LocalDate dataCadastro = LocalDate.now();

    @Deprecated
    public Usuario(){}

    public Usuario(String email, String senha) {
        this.email = email;
        this.senha = encriptarSenha(senha);
    }

    private static String encriptarSenha(String senha){
        return new BCryptPasswordEncoder().encode(senha);
    }
}
