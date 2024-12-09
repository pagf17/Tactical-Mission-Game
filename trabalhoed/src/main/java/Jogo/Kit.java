package Jogo;

public class Kit extends Item {
    private int pontosRecuperados;

    public Kit(Divisao localizacao, int pontosRecuperados) {
        super(localizacao);
        this.pontosRecuperados = pontosRecuperados;
    }

    public int getPontosRecuperados() {
        return pontosRecuperados;
    }

    public void usar(ToCruz toCruz) {
        toCruz.setHp(toCruz.getHp() + pontosRecuperados > 100 ? 100 : toCruz.getHp() + pontosRecuperados);
    }

    @Override
    public String toString() {
        return super.toString() + " [ Kit Médico ] [Pontos recuperados: " + pontosRecuperados + "]";
    }
}
