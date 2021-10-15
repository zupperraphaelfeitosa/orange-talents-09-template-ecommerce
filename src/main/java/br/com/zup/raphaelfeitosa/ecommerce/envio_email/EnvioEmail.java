package br.com.zup.raphaelfeitosa.ecommerce.envio_email;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import br.com.zup.raphaelfeitosa.ecommerce.pergunta_produto.PerguntaProduto;

public interface EnvioEmail {

    void enviaEmailPergunta(PerguntaProduto perguntaProduto);

    void enviaEmailCompraIniciada(CompraProduto compraProduto);
}
