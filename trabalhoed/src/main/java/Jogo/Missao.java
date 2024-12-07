package Jogo;
import com.github.cliftonlabs.json_simple.JsonObject;

public class Missao {
    private String codMissao;
    private int versao;
    private Objetivo objetivo;
    private boolean objetivoConcluido;
    private boolean extraido; 

    public Missao(String codMissao, int versao, Objetivo objetivo) {
        this.codMissao = codMissao;
        this.versao = versao;
        this.objetivo = objetivo;
        this.objetivoConcluido = false;
    }

    public static Missao carregarMissao(JsonObject jsonObject) {
        try {
            String codMissao = (String) jsonObject.get("codMissao");
            int versao = ((Number) jsonObject.get("versao")).intValue();

            JsonObject alvoJson = (JsonObject) jsonObject.get("alvo");
            Objetivo objetivo = Objetivo.carregarObjetivo(alvoJson);

            return new Missao(codMissao, versao, objetivo);
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
