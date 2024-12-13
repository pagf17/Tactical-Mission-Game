package Jogo;

/**
 * Classe que representa um kit médico.
 */
public class Kit extends Item {
    private int pontosRecuperados;

    /**
     * Construtor da classe Kit.
     * 
     * @param localizacao Divisao onde o kit se encontra.
     * @param pontosRecuperados Pontos de vida que o kit recupera.
     */
    public Kit(Divisao localizacao, int pontosRecuperados) {
        super(localizacao);
        this.pontosRecuperados = pontosRecuperados;
    }

    /**
     * Método para obter os pontos de vida que o kit recupera.
     * 
     * @return Pontos de vida que o kit recupera.
     */
    public int getPontosRecuperados() {
        return pontosRecuperados;
    }

    /*
     * Método que permite o jogador usar o kit.
     * Caso o jogador já tenha 100 pontos de vida, o kit não terá efeito.
     */
    public void usar(ToCruz toCruz) {
        toCruz.setHp(toCruz.getHp() + pontosRecuperados > 100 ? 100 : toCruz.getHp() + pontosRecuperados);
    }

    /**
     * Método que retorna a representação em String do kit.
     * 
     * @return Representação em String do kit.
     */
    @Override
    public String toString() {
        return super.toString() + " [ Kit Médico ] [Pontos recuperados: " + pontosRecuperados + "]";
    }
}
