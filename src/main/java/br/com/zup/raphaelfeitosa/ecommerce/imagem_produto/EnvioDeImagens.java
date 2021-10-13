package br.com.zup.raphaelfeitosa.ecommerce.imagem_produto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface EnvioDeImagens {

    Set<String> enviaFotos(List<MultipartFile> imagens);
}
