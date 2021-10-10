package br.com.zup.raphaelfeitosa.ecommerce.produto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

public interface EnvioDeImagens {

    Set<String> enviaFotos(List<MultipartFile> imagens);
}
