package Jogo;

import Estruturas.LinkedStack;
import Interfaces.Personagens;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

/**
 * Classe que representa o personagem principal do jogo.
 */
public class ToCruz implements Personagens {
    private int hp = 100;
    private int dano;
    private LinkedStack<Kit> Mochila;
    private Divisao divisaoAtual;

    /**
     * Construtor da classe ToCruz.
     * 
     * @param dano Dano que o jogador causa.
     * @param divisaoAtual Divisão onde o jogador se encontra.
     */
    public ToCruz(int dano, Divisao divisaoAtual) {
        this.hp = hp;
        this.dano = dano;
        this.divisaoAtual = divisaoAtual;
        this.Mochila = new LinkedStack<>(2);
    }

    /**
     * Método para retornar a vida do jogador.
     * 
     * @return Vida do jogador.
     */
    public int getHp() {
        return this.hp;
    }

    /**
     * Método para retornar o dano que o jogador causa.
     * 
     * @return Dano que o jogador causa.
     */
    public int getDano() {
        return this.dano;
    }

    /**
     * Método para retornar a divisão onde o jogador se encontra.
     * 
     * @return Divisão onde o jogador se encontra.
     */
    public Divisao getDivisaoAtual() {
        return this.divisaoAtual;
    }

    /**
     * Método para definir a vida do jogador.
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Método para definir o dano que o jogador causa.
     */
    public void setDano(int dano) {
        this.dano = dano;
    }

    /**
     * Método para definir a divisão onde o jogador se encontra.
     * 
     * @param currentDivision Divisão onde o jogador se encontra.
     */
    public void setDivisaoAtual(Divisao currentDivision) {
        this.divisaoAtual = currentDivision;
    }

    /**
     * Método para o jogador perder vida com os ataques dos inimigos
     *
     * @param dano quantidade de dano que o jogador sofre.
     */
    public void levarDano(int dano) {
        this.hp -= dano;
    }

    /**
     * Método para o jogador atacar um inimigo.
     * 
     * @param inimigo Inimigo que o jogador ataca.
     */
    public void atacar(Inimigo inimigo) {
        inimigo.levarDano(this.dano);
    }

    /**
     * Método para adicionar um kit médico na mochila do jogador
     * caso exista espaço na mochila.
     * 
     * @param kit Kit médico que será adicionado na mochila.
     */
    public void addKitMochila(Kit kit) {
        if (this.Mochila.isFull()) {
            PrinterGeral.print(StatusGeral.MOCHILA_CHEIA, null);
        } else {
            this.Mochila.push(kit);
            PrinterGeral.print(StatusGeral.ITEM_ADICIONADO_MOCHILA, null);
        }
    }

    /**
     * Método para verificar se a mochila do jogador está cheia.
     * 
     * @return Valor boolean que define se a mochila está cheia ou não.
     */
    public boolean mochilaCheia() {
        return this.Mochila.isFull();
    }

    /**
     * Método para obter a mochila do jogador.
     * 
     * @return Mochila do jogador.
     */
    public LinkedStack<Kit> getMochila() {
        return this.Mochila;
    }

    /**
     * Método para o jogador usar um kit médico, que faz algumas
     * verificações como se o jogador tem a vida completa e se a mochila 
     * possui algum kit, posteiromente usa o kit e é imprimida uma mensagem 
     * com a quantidade de cura que o jogador recebeu.
     */
    public void usarKit() {
        if (this.hp == 100) {
            PrinterGeral.print(StatusGeral.JOGADOR_VIDA_CHEIA, null);
        }

        if (this.Mochila.isEmpty()) {
            PrinterGeral.print(StatusGeral.MOCHILA_VAZIA, null);
        }

        Kit kit = this.Mochila.pop();
        kit.usar(this);
        PrinterGeral.print(StatusGeral.JOGADOR_CURA_SUCESSO, String.valueOf(kit.getPontosRecuperados()));
    }

    /**
     * Método para o jogador usar um colete, que verifica se o jogador
     * já tem o limite de via que o colete pode fornecer, caso não tenha 
     * o colete é usado e é imprimida uma mensagem com a quantidade de vida
     * que o jogador recebeu.
     * 
     * @param colete Colete que o jogador irá usar.
     */
    public void usarColete(Colete colete) {
        if (this.hp >= 125) {
            PrinterGeral.print(StatusGeral.JOGADOR_COLETE_MAXIMO, null);
        } else {
            colete.usar(this);
            PrinterGeral.print(StatusGeral.JOGADOR_COLETE_SUCESSO, String.valueOf(colete.getPontosExtra()));
        }
    }

    /**
     * Método para verificar se o jogador está morto.
     * 
     * @return Valor boolean que define se o jogador está morto ou não.
     */
    public boolean morto() {
        if (this.hp <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Método para retornar a representação em String do jogador.
     * 
     * @return Representação em String do jogador.
     */
    @Override
    public String toString() {
        return "ToCruz -> [" +
                " HP:" + hp +
                " | Dano:" + dano +
                " | Mochila:" + Mochila +
                " | Divisão atual:'" + divisaoAtual +
                ']';
    }

}
