package Jogo;
import com.github.cliftonlabs.json_simple.JsonObject;

import Jogo.Printer.PrinterGeral;

/**
 * Classe que representa uma missão.
 * 
 */
public class Missao {
    private String codMissao;
    private int versao;
    private Objetivo objetivo;
    private boolean objetivoConcluido;
    private boolean extraido; 

    /**
     * Construtor da classe Missao.
     * 
     * @param codMissao Código da missão.
     * @param versao Versão da missão.
     * @param objetivo Objetivo da missão.
     */
    public Missao(String codMissao, int versao, Objetivo objetivo) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.objetivo = objetivo;
        this.objetivoConcluido = false;
    }

    /**
     * Método que carrega a missão a partir de um arquivo JSON.
     * Porcura no arquivo pelos atributos da missão, 'codMissao', 'versao' e 'alvo'.
     * depois chama o método 'carregarObjetivo' para carregar o objetivo da missão.
     * e por fim retorna a missão carregada.
     * 
     * @param jsonObject arquivo JSON que contêm a missão.
     * @return Missão carregada.
     */
    public static Missao carregarMissao(JsonObject jsonObject) {
        try {
            String codMissao = (String) jsonObject.get("codMissao");
            int versao = ((Number) jsonObject.get("versao")).intValue();

            JsonObject alvoJson = (JsonObject) jsonObject.get("alvo");
            Objetivo objetivo = Objetivo.carregarObjetivo(alvoJson);

            return new Missao(codMissao, versao, objetivo);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Método para definir se o objetivo da missão foi concluído ou não
     * 
     * @param objetivoConcluido Valor boolean que define se o objetivo foi resgatado ou não.
     */
    public void setObjetivoConcluido(boolean objetivoConcluido) {
        this.objetivoConcluido = objetivoConcluido;
    }

    /**
     * Método para iniciar a missão, que basicamente imprime as informações
     * da missão iniciada.
     */
    public void iniciar() {
        PrinterGeral.infoMissao(codMissao, versao, objetivo.toString());
    }

    /**
     * Método que define o objetivo como extraido, ou seja, concluído.
     * e posteriormente imprime as informações da missão concluída.
     */
    public void completar() {
        extraido = true;
        PrinterGeral.terminarMissao(codMissao, versao);
    }

    /**
     * Método que verifica se a missão foi concluída ou não.
     * 
     * @return Valor boolean que define se a missão foi concluída ou não.
     */
    public boolean isCompleta() {
        return extraido;
    }

    /**
     * Método que retorna o código da missão.
     * 
     * @return Código da missão.
     */
    public String getCodMissao() {
        return codMissao;
    }

    /**
     * Método que retorna a versão da missão.
     * 
     * @return Versão da missão.
     */
    public int getVersao() {
        return versao;
    }

    /**
     * Método que retorna o objetivo da missão.
     * 
     * @return Objetivo da missão.
     */
    public Objetivo getObjetivo() {
        return objetivo;
    }

    /**
     * Método que retorna uma representação em String da missão.
     * 
     * @return Representação em String da missão.
     */
    @Override
    public String toString() {
        return "Missao -> [" +
            "Código:'" + codMissao + '\'' +
            " | Versão: " + versao +
            " | Objetivo: '" + objetivo + '\'' +
            " | Extraído: " + extraido +
            ']';
    }
}
