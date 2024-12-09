package Jogo;

public class Colete extends Item {
    private int pontosExtra;

    public Colete(Divisao localizacao, int pontosExtra) {
        super(localizacao);
        this.pontosExtra = pontosExtra;
    }

	public void usar(ToCruz toCruz) {
		toCruz.setHp(toCruz.getHp() + pontosExtra > 100 ? 125 : toCruz.getHp() + pontosExtra);
	}

    @Override
    public String toString() {
        return super.toString() + "[Pontos extras: " + pontosExtra + "]";
    }

}
