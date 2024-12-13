package Jogo.Resultados;

import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonObject;

/**
 * Classe responsável por ordenar os resultados das missões.
 */
public class OrdenarResultado implements Comparable<OrdenarResultado> {
    JsonObject missao;

    /**
     * Construtor da classe OrdenarResultado.
     * 
     * @param missao o resultado da missão.
     */
    public OrdenarResultado(JsonObject missao) {
        this.missao = missao;
    }

    /**
     * Método que compara dois resultados de missões, neste caso mais especificamente
     * o HP restante do jogador.
     * 
     * @param other o outro resultado de missão.
     * @return um inteiro que indica a comparação entre os resultados.
     */
    @Override
    public int compareTo(OrdenarResultado other) {
        Object hp1Obj = this.missao.get("hpFinal");
        Object hp2Obj = other.missao.get("hpFinal");

        int hp1 = convertToInt(hp1Obj);
        int hp2 = convertToInt(hp2Obj);

        return Integer.compare(hp2, hp1);
    }

    /**
     * Método que converte um valor para um inteiro.
     * 
     * @param value o valor a converter.
     * @return o valor convertido para inteiro.
     */
    private int convertToInt(Object value) {
        if (value instanceof Integer) {
            return (int) value;
        } else if (value instanceof BigDecimal) {
            return ((BigDecimal) value).intValue();
        } else {
            throw new IllegalArgumentException("Valor inesperado: " + value);
        }
    }
}
