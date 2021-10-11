package br.com.zup.raphaelfeitosa.ecommerce.envio_email;

public interface EnvioEmail {

    void envia(String destinatario, String assunto, String mensagem);
}
