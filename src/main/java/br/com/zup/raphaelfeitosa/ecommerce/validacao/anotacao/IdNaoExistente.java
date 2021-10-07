package br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {IdNaoExistenteValidador.class})
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface IdNaoExistente {

    Class<?> classeDominio();
    String nomeCampo();
    String message() default "Cadastro não existente no banco de dados!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
