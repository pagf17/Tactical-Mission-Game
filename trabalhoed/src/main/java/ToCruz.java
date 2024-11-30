import Estruturas.LinkedStack;

public class ToCruz {
    private int hp;
    private int damage;
    private LinkedStack<String> Backpack;
    private String currentDivision;

    public ToCruz(int initialDamage, String currentDivision) {
        this.hp = 100;
        this.damage = initialDamage;
        this.currentDivision = currentDivision;
        this.Backpack = new LinkedStack<>();
    }

    public int getHp() {
        return this.hp;
    }

    public int getDamage() {
        return this.damage;
    }

    public String getCurrentDivision() {
        return this.currentDivision;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setCurrentDivision(String currentDivision) {
        this.currentDivision = currentDivision;
    }

    // Metodos ToCruz

    public void takeDamage(int damage) {
        this.hp -= damage;
    }

    public void attack(Inimigo enemy) {
        enemy.takeDamage(this.damage);
    }

    public void addKitToBackpack(String item) {
        this.Backpack.push(item);
    }

    public String useKit() {
        if (this.hp == 100) {
            return "Tó Cruz já está com 100 de vida e não pode usar o kit.";
        }

        if (!this.Backpack.isEmpty()) {
            this.Backpack.pop();
            int hpAntes = this.hp;
            this.hp = Math.min(this.hp + 100, 100);
            return "Tó Cruz usou um kit e recuperou " + (this.hp - hpAntes) + " pontos de vida.";
        } else {
            return "Não há kits na mochila!";
        }
    }

    public boolean isDead() {
        return this.hp <= 0;
    }

    public String useShield() {
        if (this.hp >= 200) {
            return "Tó Cruz já atingiu o limite máximo de vida (200) e não pode usar o colete balístico.";
        } else {
            this.hp = Math.min(this.hp + 100, 200); 
            return "Tó Cruz utilizou um colete balístico e recebeu 100 de vida extra.";
        }
    }
    
    @Override
    public String toString() {
        return "ToCruz{" +
                "HP :" + hp +
                ", Damage :" + damage +
                ", Backpack :" + Backpack +
                ", Current Division :'" + currentDivision + '\'' +
                '}';
    }

}
