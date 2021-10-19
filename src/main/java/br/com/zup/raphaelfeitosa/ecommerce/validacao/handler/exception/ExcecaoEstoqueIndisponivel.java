package br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception;

public class ExcecaoEstoqueIndisponivel  extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public ExcecaoEstoqueIndisponivel(String mensagem, Integer quantidadeEstoqueDisponivel) {
        super(mensagem + quantidadeEstoqueDisponivel);
    }
}
