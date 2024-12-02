import com.github.cliftonlabs.json_simple.JsonObject;

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
        System.out.println("Objetivo concluído: " + tipo + " na divisão " + divisaoAlvo);
    }

    public boolean isConcluido() {
        return concluido;
    }

    // Getters
    public String getTipo() {
        return tipo;
    }

    public String getDivisaoAlvo() {
        return divisaoAlvo;
    }

    @Override
    public String toString() {
        return "Objetivo{" +
                "Tipo='" + tipo + '\'' +
                ", Divisão-Alvo='" + divisaoAlvo + '\'' +
                ", Concluído=" + concluido +
                '}';
    }

    public static Objetivo carregarObjetivo(JsonObject alvoJson) {
        try {
            String divisaoAlvo = (String) alvoJson.get("divisao");
            String tipo = (String) alvoJson.get("tipo");

            return new Objetivo(tipo, divisaoAlvo);
        } catch (Exception e) {
            System.out.println("Erro ao carregar o objetivo: " + e.getMessage());
            return null;
        }
    }
}
