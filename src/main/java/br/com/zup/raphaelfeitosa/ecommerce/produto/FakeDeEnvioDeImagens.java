package br.com.zup.raphaelfeitosa.ecommerce.produto;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Primary
public class FakeDeEnvioDeImagens implements EnvioDeImagens {

    @Value("${link.fake}")
    private String url;

    /**
     * Links fake para imagens que foram feitas upload
     * Uso sem pq são links diferentes
     *
     * @param imagens
     * @return
     */
    public Set<String> enviaFotos(List<MultipartFile> imagens) {

        return imagens.stream()
                .map(imagem -> url
                        + imagem.getOriginalFilename())
                .collect(Collectors.toSet());
    }
}
