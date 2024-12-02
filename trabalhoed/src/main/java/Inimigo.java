import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayUnorderedList;

public class Inimigo {
    private String nome;
    private String divisao;
    private int dano;
    private int hp;

    public Inimigo(String nome, String divisao, int dano, int hp) {
        this.nome = nome;
        this.divisao = divisao;
        this.dano = dano;
        this.hp = hp;
    }

    public String getNome() {
        return this.nome;
    }

    public String getDivisao() {
        return this.divisao;
    }

    public int getDano() {
        return this.dano;
    }

    public int getHp() {
        return this.hp;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDivisao(String divisao) {
        this.divisao = divisao;
    }

    public void setdano(int dano) {
        this.dano = dano;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    // Metodos Inimigo

    public void atacar(ToCruz toCruz) {
        toCruz.levarDano(this.dano);
    }

    public void levarDano(int dano) {
        this.hp -= dano;
    }

    public boolean estaMorto() {
        return this.hp <= 0;
    }

    public static ArrayUnorderedList<Inimigo> carregarInimigos(JsonObject jsonObject) {
        ArrayUnorderedList<Inimigo> inimigos = new ArrayUnorderedList<>();
        
        JsonArray inimigosJson = (JsonArray) jsonObject.get("inimigos");
        if (inimigosJson != null) {
            for (Object obj : inimigosJson) {
                JsonObject inimigoJson = (JsonObject) obj;
    
                String nome = (String) inimigoJson.get("nome");
                int poder = ((Number) inimigoJson.get("poder")).intValue();
                String divisao = (String) inimigoJson.get("divisao");
    
                Inimigo inimigo = new Inimigo(nome, divisao, poder, 100); 
                inimigos.addToRear(inimigo);
            }
        } else {
            System.out.println("Chave 'inimigos' não encontrada no JSON.");
        }
        
        return inimigos;
    }
    
    @Override
    public String toString() {
        return "Enemy {" +
                "nome='" + nome + '\'' +
                ", dano=" + dano +
                ", HP=" + hp +
                ", divisao='" + divisao + '\'' +
                '}';
    }
}