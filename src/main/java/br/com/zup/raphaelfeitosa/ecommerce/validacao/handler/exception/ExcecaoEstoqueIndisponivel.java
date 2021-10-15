package br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception;

public class ExcecaoEstoqueIndisponivel  extends RuntimeException{

    public ExcecaoEstoqueIndisponivel(Integer quantidadeEstoqueDisponivel) {
        super("Estoque indisponivel para a quantidade solicitada no momento! Quantidade Atual: " + quantidadeEstoqueDisponivel);
    }
}
