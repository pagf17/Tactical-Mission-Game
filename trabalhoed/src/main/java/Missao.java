import Estruturas.ArrayUnorderedList;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Missao {
    private String codMissao; // Código único da missão
    private int versao; // Versão da missão
    private Objetivo objetivo; // Descrição do objetivo
    private boolean objetivoConcluido; // Objetivo alcançado ou não
    private boolean extraido; // Objetivo extraído ou não
    private ArrayUnorderedList<String> entradasSaidas; // Lista de divisões de entrada e saída

    // Construtor
    public Missao(String codMissao, int versao, Objetivo objetivo, ArrayUnorderedList<String> entradasSaidas) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.objetivo = objetivo;
        this.entradasSaidas = entradasSaidas;
        this.objetivoConcluido = false;
        this.extraido = false;
    }

    // Método para carregar missão a partir do JSON
    public static Missao carregarMissao(JsonObject jsonObject) {
        try {
            String codMissao = (String) jsonObject.get("codMissao");
            int versao = ((Number) jsonObject.get("versao")).intValue();

            // Carregar objetivo
            JsonObject alvoJson = (JsonObject) jsonObject.get("alvo");
            Objetivo objetivo = Objetivo.carregarObjetivo(alvoJson);

            // Carregar divisões de entrada/saída
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

    // Método para iniciar a missão
    public void iniciar() {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") iniciada!");
        System.out.println("Objetivo: " + objetivo);
        System.out.println("Depois, extraia-o e saia por uma das divisões de entrada/saída.");
    }

    // Verificar progresso da missão
    public void verificarProgresso(String divisaoAtual) {
        // Verifica se o jogador alcançou a divisão-alvo
        if (!objetivoConcluido && divisaoAtual.equalsIgnoreCase(objetivo.getDivisaoAlvo())) {
            System.out.println("Você alcançou a divisão-alvo: " + objetivo.getDivisaoAlvo());
            objetivo.concluir();
            objetivoConcluido = true;
        }
        // Verifica se o jogador saiu por uma divisão válida
        else if (objetivoConcluido && isSaidaValida(divisaoAtual)) {
            System.out.println("Você extraiu o objetivo e saiu pela divisão: " + divisaoAtual);
            completar();
        } else {
            System.out.println("Continue explorando.");
        }
    }

    // Completa a missão
    private void completar() {
        System.out.println("Missão " + codMissao + " (Versão " + versao + ") completada com sucesso!");
        extraido = true;
    }

    // Verifica se a missão foi completada
    public boolean isCompleta() {
        return extraido;
    }

    // Verifica se a divisão de saída é válida
    private boolean isSaidaValida(String divisaoAtual) {
        // Itera pela lista de entradas/saídas e verifica se a divisão está na lista
        for (String divisao : entradasSaidas) {
            if (divisao.equalsIgnoreCase(divisaoAtual)) {
                return true; // A divisão de saída é válida
            }
        }
        return false; // Se não for uma divisão de saída válida
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
        return "Missao{" +
                "Código='" + codMissao + '\'' +
                ", Versão=" + versao +
                ", Objetivo='" + objetivo + '\'' +
                ", Objetivo Concluído=" + objetivoConcluido +
                ", Extraído=" + extraido +
                ", Entradas/Saídas=" + entradasSaidas +
                '}';
    }
}
