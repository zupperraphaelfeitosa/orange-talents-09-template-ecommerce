package br.com.zup.raphaelfeitosa.ecommerce.validacoes.anotacoes;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CampoUnicoValidador implements ConstraintValidator<CampoUnicoGenerico, String> {

    @PersistenceContext
    EntityManager entityManager;

    private String classe;
    private String campo;

    @Override
    public void initialize(CampoUnicoGenerico constraintAnnotation) {
        classe = constraintAnnotation.classeDominio().getSimpleName();
        campo = constraintAnnotation.nomeCampo();
    }

    @Override
    public boolean isValid(String valorDoCampo, ConstraintValidatorContext constraintValidatorContext) {
        Query query = entityManager.createQuery("SELECT c FROM " + classe + " c WHERE " + campo + " = :CAMPO");
        query.setParameter("CAMPO", valorDoCampo);
        return query.getResultList().isEmpty();
    }
}
