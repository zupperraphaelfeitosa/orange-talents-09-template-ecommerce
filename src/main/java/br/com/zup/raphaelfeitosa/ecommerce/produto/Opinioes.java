package br.com.zup.raphaelfeitosa.ecommerce.produto;

import br.com.zup.raphaelfeitosa.ecommerce.opiniao_produto.OpiniaoProduto;

import java.util.OptionalDouble;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Opinioes {

    private Set<OpiniaoProduto> opinioes;

    public Opinioes(Set<OpiniaoProduto> opinioes) {
        this.opinioes = opinioes;
    }

    public <T> Set<T> mapeiaOpinioes(Function<OpiniaoProduto, T> funcaoMapeadora) {
        return this.opinioes.stream().map(funcaoMapeadora)
                .collect(Collectors.toSet());
    }

    public double mediaDeNotas() {
        Set<Integer> notas = mapeiaOpinioes(OpiniaoProduto::getNota);
        OptionalDouble media = notas.stream().mapToInt(nota -> nota).average();
        return media.orElse(0.0);
    }

    public Integer totalDeMedias() {
        return opinioes.size();
    }

}
