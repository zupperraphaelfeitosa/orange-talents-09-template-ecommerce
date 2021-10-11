package br.com.zup.raphaelfeitosa.ecommerce.envio_email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class FakeDeEnvioDeEmail implements EnvioEmail {

    @Value("${email.fake.remetente}")
    private String fakeRemetente;

    @Override
    public void envia(String destinatario, String assunto, String mensagem) {
        System.out.println("Novo email recebido!!!");
        System.out.println("Assunto: Nova pergunta para o produto: "+ assunto);
        System.out.println("Mensagem: " + mensagem);
        System.out.println("Remetente: " + fakeRemetente);
        System.out.println("Destinatario: " + destinatario);
    }
}
