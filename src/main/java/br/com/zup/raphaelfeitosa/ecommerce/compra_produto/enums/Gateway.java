package br.com.zup.raphaelfeitosa.ecommerce.compra_produto.enums;

import br.com.zup.raphaelfeitosa.ecommerce.compra_produto.CompraProduto;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public enum Gateway {
    pagseguro {
        @Override
        public URI urlResponse(CompraProduto compraProduto, UriComponentsBuilder uriBuilder) {
            UriComponents pagseguroResponse = uriBuilder.path("/pagseguro/transacao/{codigo}")
                    .buildAndExpand(compraProduto.getCodigoTransacao());
            return URI.create("pagseguro.com?transacao=" + compraProduto.getCodigoTransacao()+ "?redirectUrl=" + pagseguroResponse);
        }
    },

    paypal {
        @Override
        public URI urlResponse(CompraProduto compraProduto, UriComponentsBuilder uriBuilder) {
            UriComponents paypalResponse = uriBuilder.path("/paypal/transacao/{codigo}")
                    .buildAndExpand(compraProduto.getCodigoTransacao());
            return URI.create("paypal.com?transacao=" + compraProduto.getCodigoTransacao() + "?redirectUrl=" + paypalResponse);
        }
    };

    public abstract URI urlResponse(CompraProduto compraProduto, UriComponentsBuilder uriBuilder);
}
