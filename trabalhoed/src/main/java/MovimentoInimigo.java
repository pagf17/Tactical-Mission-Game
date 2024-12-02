public class MovimentoInimigo {
    private Inimigo inimigo;
    private Divisao divisaoOrigem;
    private Divisao divisaoDestino;

    public MovimentoInimigo(Inimigo inimigo, Divisao divisaoOrigem, Divisao divisaoDestino) {
        this.inimigo = inimigo;
        this.divisaoOrigem = divisaoOrigem;
        this.divisaoDestino = divisaoDestino;
    }

    public Inimigo getInimigo() {
        return inimigo;
    }

    public Divisao getDivisaoOrigem() {
        return divisaoOrigem;
    }

    public Divisao getDivisaoDestino() {
        return divisaoDestino;
    }
}

