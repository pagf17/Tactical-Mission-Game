package Jogo;

/**
 * Classe que representa um colete
 */
public class Colete extends Item {
    private int pontosExtra;

    /**
     * Construtor de Colete
     * 
     * @param localizacao Divisão onde está o colete.
     * @param pontosExtra Pontos extras que o colete dá.
     */
    public Colete(Divisao localizacao, int pontosExtra) {
        super(localizacao);
        this.pontosExtra = pontosExtra;
    }

    /*
     * Método que permite o jogador usar o colete.
     * Caso o jogador já tenha 100 de vida ao utilizar o colete, ele ganha 25 de vida
     * ficando assim com 125 de vida.
     */
	public void usar(ToCruz toCruz) {
		toCruz.setHp(toCruz.getHp() + pontosExtra > 100 ? 125 : toCruz.getHp() + pontosExtra);
	}

    /**
     * Método que retorna os pontos extras do colete.
     * 
     * @return Pontos extras do colete.
     */
    public int getPontosExtra() {
        return pontosExtra;
    }

    /**
     * Método que retorna a representação em String do colete.
     * 
     * @return Representação em String do colete.
     */
    @Override
    public String toString() {
        return super.toString() + "[Pontos extras: " + pontosExtra + "]";
    }

}
