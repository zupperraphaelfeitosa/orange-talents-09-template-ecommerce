package br.com.zup.raphaelfeitosa.ecommerce.validacao.handler.exception;

public class ExcecaoPagamentoInvalido extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ExcecaoPagamentoInvalido(String messagem) {
        super(messagem);
    }
}
