package Interfaces;

public interface Personagens {

    public int getHp();

    public int getDano();

    public void setHp(int hp);

    public void setDano(int dano);

    public void levarDano(int dano);

    public boolean morto();
}