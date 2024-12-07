package Jogo;
import Estruturas.LinkedStack;
import Interfaces.Personagens;

public class ToCruz implements Personagens {
    private int hp = 100;
    private int dano;
    private LinkedStack<Kit> Mochila;
    private Divisao divisaoAtual;

    public ToCruz(int dano, Divisao divisaoAtual) {
        this.hp = hp;
        this.dano = dano;
        this.divisaoAtual = divisaoAtual;
        this.Mochila = new LinkedStack<>();
    }

    public int getHp() {
        return this.hp;
    }

    public int getDano() {
        return this.dano;
    }

    public Divisao getDivisaoAtual() {
        return this.divisaoAtual;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setDivisaoAtual(Divisao currentDivision) {
        this.divisaoAtual = currentDivision;
    }

    public void levarDano(int dano) {
        this.hp -= dano;
    }

    public void atacar(Inimigo inimigo) {
        inimigo.levarDano(this.dano);
    }

    public void addKitMochila(Kit kit) {
        this.Mochila.push(kit);
    }

    public LinkedStack<Kit> getMochila() {
        return this.Mochila;
    }

    public String usarKit() {
        if (this.hp == 100) {
            return "Tó Cruz já está com 100 de vida e não pode usar o kit.";
        } 
        
        if (this.Mochila.isEmpty()) {
            return "To Cruz não possui kits.";
        }
    
        Kit kit = this.Mochila.pop();
        if (kit == null) {
            return "Não há kits na mochila!";  
        }

        kit.usar(this);
        return "To Cruz usou um kit e recuperou " + kit.getPontosRecuperados() + " pontos de vida.";
    }

    public String usarColete(Colete colete) {
        if (this.hp >= 125) {
            return "Tó Cruz já atingiu o limite máximo de vida (125) e não pode usar o colete balístico.";
        } else {
            colete.usar(this);
            return "Tó Cruz utilizou um colete balístico e recebeu 25 de vida extra.";
        }
    }
    
    public boolean morto() {
        return this.hp <= 0;
    }
    
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
