package br.com.zup.raphaelfeitosa.ecommerce.validacao.anotacao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdNaoExistenteValidador implements ConstraintValidator<IdNaoExistente, Long> {

    @PersistenceContext
    EntityManager entityManager;

    private String classe;
    private String campo;

    @Override
    public void initialize(IdNaoExistente constraintAnnotation) {
        classe = constraintAnnotation.classeDominio().getSimpleName();
        campo = constraintAnnotation.nomeCampo();
    }

    @Override
    public boolean isValid(Long id, ConstraintValidatorContext constraintValidatorContext) {

        if (id == null) return true;
        Query query = entityManager.createQuery("SELECT c FROM " + classe + " c WHERE c." + campo + "= :ID");
        query.setParameter("ID", id);
        return !query.getResultList().isEmpty();
    }
}
