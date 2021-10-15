package br.com.zup.raphaelfeitosa.ecommerce.envio_email;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Component
@Primary
public class FakeDeEnvioDeEmail implements EnvioEmail {

    @Value("${email.fake.remetente}")
    private String fakeRemetente;

    @Override
    public void enviaEmailPergunta(PerguntaProduto perguntaProduto) {
        System.out.println("Novo email recebido!!!");
        System.out.println("Assunto: Nova pergunta para o produto: " + perguntaProduto.getProduto().getNome());
        System.out.println("Mensagem: " + perguntaProduto.getTitulo());
        System.out.println("Remetente: " + fakeRemetente);
        System.out.println("Destinatario: " + perguntaProduto.getUsuario().getEmail());
    }

    @Override
    public void enviaEmailCompraIniciada(CompraProduto compraProduto) {
        System.out.println("Novo email recebido!!!");
        System.out.println("Remetente: " + fakeRemetente);
        System.out.println("Destinatario: " + compraProduto.getUsuario().getEmail());
        System.out.println("Assunto: Você comprou o produto " + compraProduto.getProduto().getNome());
        System.out.println("---------------------------------------------");
        System.out.println("Resumo da compra");
        System.out.println("Produto: " + compraProduto.getProduto().getNome());
        System.out.println("quantidade: " + compraProduto.getQuantidade());
        System.out.println("Valor unitário: " + compraProduto.getValorMomentoAtual());
        System.out.println("Valor Total: " + compraProduto.getValorTotalDaCompra());
        System.out.println("Gateway pagamento: " + compraProduto.getGateway());
        System.out.println("Status da transação: " + compraProduto.getStatusCompra());
        System.out.println("Codigo da transação: " + compraProduto.getCodigoTransacao());
        System.out.println("Data da compra: " + compraProduto.getDataCompra());
        System.out.println("Email vendedor: " + compraProduto.getProduto().getUsuario().getEmail());
    }
}
