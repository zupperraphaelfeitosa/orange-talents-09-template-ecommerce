package br.com.zup.raphaelfeitosa.ecommerce.validacoes.handler;

public class ErrorDeValidacaoDto {

    private final String campo;
    private final String error;

    public ErrorDeValidacaoDto(String campo, String error) {
        this.campo = campo;
        this.error = error;
    }

    public String getCampo() {
        return campo;
    }

    public String getError() {
        return error;
    }
}
