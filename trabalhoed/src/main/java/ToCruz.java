import Estruturas.LinkedStack;

public class ToCruz {
    private int hp;
    private int dano;
    private LinkedStack<Item> Mochila;
    private String divisaoAtual;

    public ToCruz(int dano, String divisaoAtual) {
        this.hp = 100;
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

    public String getDivisaoAtual() {
        return this.divisaoAtual;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDano(int dano) {
        this.dano = dano;
    }

    public void setDivisaoAtual(String currentDivision) {
        this.divisaoAtual = currentDivision;
    }

    // Metodos ToCruz

    public void levarDano(int dano) {
        this.hp -= dano;
    }

    public void atacar(Inimigo enemy) {
        enemy.levarDano(this.dano);
    }

    public void addKitMochila(Item item) {
        this.Mochila.push(item);
    }

    public LinkedStack<Item> getMochila() {
        return this.Mochila;
    }

    public String usarKit() {
        if (this.hp == 100) {
            return "Tó Cruz já está com 100 de vida e não pode usar o kit.";
        }
    
        if (!this.Mochila.isEmpty()) {
            Item item = Mochila.pop();  
            int hpAntes = this.hp;
    
            this.hp = Math.min(this.hp + 100, 100);
    
            return "Tó Cruz usou um kit e recuperou " + (this.hp - hpAntes) + " pontos de vida.";
        } else {
            return "Não há kits na mochila!"; 
        }
    }
    

    public boolean estaMorto() {
        return this.hp <= 0;
    }

    public String usarColete() {
        if (this.hp >= 200) {
            return "Tó Cruz já atingiu o limite máximo de vida (200) e não pode usar o colete balístico.";
        } else {
            this.hp = Math.min(this.hp + 100, 200); 
            return "Tó Cruz utilizou um colete balístico e recebeu 100 de vida extra.";
        }
    }
    
    @Override
    public String toString() {
        return "ToCruz -> [" +
                " HP:" + hp +
                " | Dano:" + dano +
                " | Mochila:" + Mochila +
                " | Divisão atual:'" + divisaoAtual + '\'' +
                ']';
    }

}
