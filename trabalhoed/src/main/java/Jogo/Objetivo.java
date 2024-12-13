package Jogo;
import com.github.cliftonlabs.json_simple.JsonObject;

import Jogo.Printer.PrinterGeral;

public class Objetivo {
    private String tipo;        
    private String divisaoAlvo;  
    private boolean concluido;  

    public Objetivo(String tipo, String divisaoAlvo) {
        this.tipo = tipo;
        this.divisaoAlvo = divisaoAlvo;
        this.concluido = false;
    }

    public void concluir() {
        this.concluido = true;
        PrinterGeral.concluirObjetivo(tipo, divisaoAlvo);
    }

    public boolean isConcluido() {
        return concluido;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDivisaoAlvo() {
        return divisaoAlvo;
    }

    public static Objetivo carregarObjetivo(JsonObject alvoJson) {
        try {
            String divisaoAlvo = (String) alvoJson.get("divisao");
            String tipo = (String) alvoJson.get("tipo");

            return new Objetivo(tipo, divisaoAlvo);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public String toString() {
        return "[ Tipo='" + tipo + '\'' +
                "| Divisão-Alvo='" + divisaoAlvo + '\'' +
                "| Concluído=" + concluido +
                ']';
    }
}
