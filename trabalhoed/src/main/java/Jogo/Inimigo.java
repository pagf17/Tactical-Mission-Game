package Jogo;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayUnorderedList;
import Interfaces.Personagens;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

public class Inimigo implements Personagens {
    private String nome;
    private Divisao divisao;
    private int dano;
    private int hp;

    public Inimigo(String nome, Divisao divisao, int dano, int hp) {
        this.nome = nome;
        this.divisao = divisao;
        this.dano = dano;
        this.hp = hp;
    }

    public String getNome() {
        return this.nome;
    }

    public Divisao getDivisao() {
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

    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void atacar(ToCruz toCruz) {
        toCruz.levarDano(this.dano);
    }

    public void levarDano(int dano) {
        this.hp -= dano;
    }

    public boolean morto() {
        return this.hp <= 0;
    }

    public void mover(Divisao novaDivisao) {
        this.divisao = novaDivisao;
        PrinterGeral.moverInimigo(this.nome, novaDivisao.getNome());
    }

    public static ArrayUnorderedList<Inimigo> carregarInimigos(JsonObject jsonObject, Mapa mapa) {
        ArrayUnorderedList<Inimigo> inimigos = new ArrayUnorderedList<>();

        JsonArray inimigosJson = (JsonArray) jsonObject.get("inimigos");
        if (inimigosJson != null) {
            for (Object obj : inimigosJson) {
                JsonObject inimigoJson = (JsonObject) obj;
                String nome = (String) inimigoJson.get("nome");
                int poder = ((Number) inimigoJson.get("poder")).intValue();
                String divisaoNome = (String) inimigoJson.get("divisao");

                Divisao divisao = mapa.obterDivisao(divisaoNome);

                if (divisao != null) {
                    Inimigo inimigo = new Inimigo(nome, divisao, poder, 100);
                    inimigos.addToRear(inimigo);
                } else {
                    PrinterGeral.print(StatusGeral.DIVISAO_NAO_ENCONTRADA_INIMIGO, nome);
                }
            }
        } else {
            PrinterGeral.print(StatusGeral.CHAVE_INIMIGOS_NAO_ENCONTRADA, null);
        }
        return inimigos;
    }

    @Override
    public String toString() {
        return "Inimigo [" +
                " Nome: '" + nome + '\'' +
                "| Dano: " + dano +
                "| HP: " + hp +
                "| Divisao: '" + divisao + '\'' +
                ']';
    }
}