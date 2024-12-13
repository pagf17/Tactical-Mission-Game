package Jogo;

import com.github.cliftonlabs.json_simple.JsonArray;
import com.github.cliftonlabs.json_simple.JsonObject;

import Estruturas.ArrayUnorderedList;
import Interfaces.Personagens;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

/**
 * Classe que representa um inimigo
 */
public class Inimigo implements Personagens {
    private String nome;
    private Divisao divisao;
    private int dano;
    private int hp;

    /**
     * Construtor do Inimigo
     * 
     * @param nome Nome do inimigo.
     * @param divisao Divisão onde está o inimigo.
     * @param dano Dano que o inimigo dá.
     * @param hp Vida do inimigo.
     */
    public Inimigo(String nome, Divisao divisao, int dano, int hp) {
        this.nome = nome;
        this.divisao = divisao;
        this.dano = dano;
        this.hp = hp;
    }

    /**
     * Método que devolve o nome do inimigo.
     * 
     * @return Nome do inimigo.
     */
    public String getNome() {
        return this.nome;
    }

    /**
     * Método que devolve a divisão onde está o inimigo.
     * 
     * @return Divisão onde está o inimigo.
     */
    public Divisao getDivisao() {
        return this.divisao;
    }

    /**
     * Método que devolve o dano do inimigo.
     * 
     * @return Dano do inimigo.
     */
    public int getDano() {
        return this.dano;
    }

    /**
     * Método que devolve a vida do inimigo.
     * 
     * @return Vida do inimigo.
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * Método que define o nome do inimigo.
     * 
     * @param nome Nome do inimigo.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Método que define a divisão onde está o inimigo.
     * 
     * @param divisao Divisão onde está o inimigo.
     */
    public void setDivisao(Divisao divisao) {
        this.divisao = divisao;
    }

    /**
     * Método que define o dano do inimigo.
     * 
     * @param dano Dano do inimigo.
     */
    public void setDano(int dano) {
        this.dano = dano;
    }

    /**
     * Método que define a vida do inimigo.
     * 
     * @param hp Vida do inimigo.
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Método que reduz a vida do inimigo.
     * 
     * @param dano Dano do jogador que o inimigo leva.
     */
    public void levarDano(int dano) {
        this.hp -= dano;
    }

    /**
     * Método que verifica se o inimigo está morto.
     * 
     * @return Verdadeiro se o inimigo estiver morto, falso caso contrário.
     */
    public boolean morto() {
        return this.hp <= 0;
    }

    /**
     * Método que define a nova divisão do inimigo.
     * 
     * @param novaDivisao Divisão para onde o inimigo se vai mover.
     */
    public void mover(Divisao novaDivisao) {
        this.divisao = novaDivisao;
        PrinterGeral.moverInimigo(this.nome, novaDivisao.getNome());
    }

    /**
     * Método para carregar inimigos a partir de um ficheiro JSON
     * Este método lê os 'inimigos' do ficheiro JSON e cria um Inimigo
     * para cada um deles, adicionando-os posteriormente à lista de inimigos
     * da divisão correspondente.
     * 
     * @param jsonObject o ficheiro JSON.
     * @param mapa o mapa que contêm as divisões onde os inimigos vão ser colocados
     * @return a lista de inimigos carregados.
     */
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

    /**
     * Método que retorna a representação em String do inimigo.
     * 
     * @return Representação em String do inimigo.
     */
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