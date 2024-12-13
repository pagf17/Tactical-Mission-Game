package Interfaces;

/**
 * Interface que representa os personagens do jogo.
 */
public interface Personagens {
    /**
     * Método que retorna a vida do personagem.
     * 
     * @return Vida do personagem.
     */
    public int getHp();

    /**
     * Método que retorna o dano do personagem.
     * 
     * @return Dano do personagem.
     */
    public int getDano();

    /**
     * Método que define a vida do personagem.
     * 
     * @param hp Vida do personagem.
     */
    public void setHp(int hp);

    /**
     * Método que define o dano do personagem.
     * 
     * @param dano Dano do personagem.
     */
    public void setDano(int dano);

    /**
     * Método que faz o personagem levar dano.
     * 
     * @param dano Dano que o personagem levará.
     */
    public void levarDano(int dano);

    /**
     * Método que verifica se o personagem está morto.
     * 
     * @return True se o personagem estiver morto, false caso contrário.
     */
    public boolean morto();
}