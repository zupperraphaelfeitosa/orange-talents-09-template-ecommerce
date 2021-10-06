package br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Constraint(validatedBy = {CampoUnicoValidador.class})
@Retention(RetentionPolicy.RUNTIME)
public @interface CampoUnicoGenerico {
    Class<?> classeDominio();
    String nomeCampo();
    String message() default "Email existente no banco de dados!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
