package br.com.zup.raphaelfeitosa.ecommerce.pagamento.enums;

public enum StatusPagamento {
    SUCESSO,
    ERRO;

    public static StatusPagamento statusPayPal(Integer status) {
        return status == 1 ? ERRO : SUCESSO;
    }

    public static StatusPagamento statusPagSeguro(String status) {
        return status.equals("ERRO") ? ERRO : SUCESSO;
    }
}
