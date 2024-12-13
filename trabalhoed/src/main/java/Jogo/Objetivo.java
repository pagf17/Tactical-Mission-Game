package Jogo;
import com.github.cliftonlabs.json_simple.JsonObject;

import Jogo.Printer.PrinterGeral;

/**
 * Classe que representa um objetivo.
 */
public class Objetivo {
    private String tipo;        
    private String divisaoAlvo;  
    private boolean concluido;  

    /**
     * Construtor da classe Objetivo.
     * 
     * @param tipo tipo do objetivo (quimico, bomba, refem, etc).
     * @param divisaoAlvo divisão onde o objetivo se encontra.
     */
    public Objetivo(String tipo, String divisaoAlvo) {
        this.tipo = tipo;
        this.divisaoAlvo = divisaoAlvo;
        this.concluido = false;
    }

    /**
     * Método para concluir o objetivo alterando o atributo 'concluido' para true.
     */
    public void concluir() {
        this.concluido = true;
        PrinterGeral.concluirObjetivo(tipo, divisaoAlvo);
    }

    /**
     * Método para saber se o objetivo foi concluído ou não.
     * 
     * @return Valor boolean que define se o objetivo foi concluído ou não.
     */
    public boolean isConcluido() {
        return concluido;
    }

    /**
     * Método para obter o tipo do objetivo.
     * 
     * @return Tipo do objetivo (quimico, bomba, refem, etc).
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Método para obter a divisão onde o objetivo se encontra.
     * 
     * @return Divisão onde o objetivo se encontra.
     */
    public String getDivisaoAlvo() {
        return divisaoAlvo;
    }

    /**
     * Método que carrega o objetivo a partir de um arquivo JSON.
     * Porcura no arquivo pelos atributos do objetivo, 'divisao' e 'tipo'.
     * Por fim retorna o objetivo carregado.
     * 
     * @param alvoJson arquivo JSON que contêm o objetivo.
     * @return Objetivo carregado.
     */
    public static Objetivo carregarObjetivo(JsonObject alvoJson) {
        try {
            String divisaoAlvo = (String) alvoJson.get("divisao");
            String tipo = (String) alvoJson.get("tipo");

            return new Objetivo(tipo, divisaoAlvo);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método que retorna a representação em String do objetivo.
     * 
     * @return Representação em String do objetivo.
     */
    @Override
    public String toString() {
        return "[ Tipo='" + tipo + '\'' +
                "| Divisão-Alvo='" + divisaoAlvo + '\'' +
                "| Concluído=" + concluido +
                ']';
    }
}
