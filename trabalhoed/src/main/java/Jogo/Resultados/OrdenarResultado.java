package Jogo.Resultados;

import java.math.BigDecimal;

import com.github.cliftonlabs.json_simple.JsonObject;

public class OrdenarResultado implements Comparable<OrdenarResultado> {
    JsonObject missao;

    public OrdenarResultado(JsonObject missao) {
        this.missao = missao;
    }

    @Override
    public int compareTo(OrdenarResultado other) {
        Object hp1Obj = this.missao.get("hpFinal");
        Object hp2Obj = other.missao.get("hpFinal");

        int hp1 = convertToInt(hp1Obj);
        int hp2 = convertToInt(hp2Obj);

        return Integer.compare(hp1, hp2);
    }

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
