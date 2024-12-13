package Jogo;
import Estruturas.LinkedStack;
import Interfaces.Personagens;
import Jogo.Printer.PrinterGeral;
import Interfaces.StatusGeral;

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

    public void usarKit() {
        if (this.hp == 100) {
            PrinterGeral.print(StatusGeral.JOGADOR_VIDA_CHEIA, null);
        } 
        
        if (this.Mochila.isEmpty()) {
            PrinterGeral.print(StatusGeral.MOCHILA_VAZIA, null);
        }
    
        Kit kit = this.Mochila.pop();
        if (kit == null) {
            PrinterGeral.print(StatusGeral.MOCHILA_VAZIA, null); 
        }

        kit.usar(this);
        PrinterGeral.print(StatusGeral.JOGADOR_CURA_SUCESSO, String.valueOf(kit.getPontosRecuperados()));
    }

    public void usarColete(Colete colete) {
        if (this.hp >= 125) {
            PrinterGeral.print(StatusGeral.JOGADOR_COLETE_MAXIMO, null);
        } else {
            colete.usar(this);
            PrinterGeral.print(StatusGeral.JOGADOR_COLETE_SUCESSO, String.valueOf(colete.getPontosExtra()));
        }
    }
    
    public boolean morto() {
        if (this.hp <= 0){
            return true;
        } else {
            return false;
        }
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
