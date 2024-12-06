import Estruturas.ArrayUnorderedList;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Missao {
    private String codMissao;
    private int versao;
    private Objetivo objetivo;
    private boolean objetivoConcluido;
    private boolean extraido; 
    private ArrayUnorderedList<String> entradasSaidas;

    public Missao(String codMissao, int versao, Objetivo objetivo, ArrayUnorderedList<String> entradasSaidas) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.objetivo = objetivo;
        this.entradasSaidas = entradasSaidas;
        this.objetivoConcluido = false;
    }

    public static Missao carregarMissao(JsonObject jsonObject) {
        try {
            String codMissao = (String) jsonObject.get("codMissao");
            int versao = ((Number) jsonObject.get("versao")).intValue();

            JsonObject alvoJson = (JsonObject) jsonObject.get("alvo");
            Objetivo objetivo = Objetivo.carregarObjetivo(alvoJson);

            JsonArray entradasSaidasJson = (JsonArray) jsonObject.get("entradasSaidas");
            ArrayUnorderedList<String> entradasSaidas = new ArrayUnorderedList<>();
            for (Object obj : entradasSaidasJson) {
                entradasSaidas.addToRear((String) obj);
            }

            return new Missao(codMissao, versao, objetivo, entradasSaidas);
        } catch (Exception e) {
            System.out.println("Erro ao carregar a missão: " + e.getMessage());
            return null;
        }
    }

    public void setObjetivoConcluido(boolean objetivoConcluido) {
        this.objetivoConcluido = objetivoConcluido;
    }

    public void iniciar() {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") iniciada!");
        System.out.println("Objetivo: " + objetivo);
        System.out.println("Depois, extraia-o e saia por uma das divisões de entrada/saída.");
    }

    public void completar() {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") completada com sucesso!");
        extraido = true;
        System.out.println("O jogo terminou. Parabéns!");
    }

    public boolean isCompleta() {
        return extraido;
    }

    public boolean isSaidaValida(String divisaoAtual) {
        for (String divisao : entradasSaidas) {
            if (divisao.equalsIgnoreCase(divisaoAtual)) {
                return true; 
            }
        }
        return false; 
    }

    // Getters
    public String getCodMissao() {
        return codMissao;
    }

    public int getVersao() {
        return versao;
    }

    public Objetivo getObjetivo() {
        return objetivo;
    }

    public ArrayUnorderedList<String> getEntradasSaidas() {
        return entradasSaidas;
    }

    @Override
    public String toString() {
        return "Missao -> [" +
            "Código:'" + codMissao + '\'' +
            " | Versão: " + versao +
            " | Objetivo: '" + objetivo + '\'' +
            " | Extraído: " + extraido +
            " | Entradas/Saídas: " + String.join(", ", entradasSaidas) +
            ']';
    }
}
