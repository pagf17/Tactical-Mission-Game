import Estruturas.ArrayUnorderedList;

public class Divisao {
    private String nome;
    private ArrayUnorderedList<Inimigo> inimigos; // Lista de inimigos na divisão
    private ArrayUnorderedList<Item> itens; // Lista de itens na divisão

    // Construtor
    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new ArrayUnorderedList<>(); // Inicializa a lista de inimigos
        this.itens = new ArrayUnorderedList<>(); // Inicializa a lista de itens
    }

    // Métodos para adicionar inimigos e itens
    public void adicionarInimigo(Inimigo inimigo) {
        inimigos.addToRear(inimigo);
    }

    public void adicionarItem(Item item) {
        itens.addToRear(item);
    }

    // Métodos para obter inimigos e itens
    public ArrayUnorderedList<Inimigo> getInimigos() {
        return inimigos;
    }

    public ArrayUnorderedList<Item> getItens() {
        return itens;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "Divisão: " + nome;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Divisao divisao = (Divisao) obj;
        return nome.equalsIgnoreCase(divisao.nome);
    }

    @Override
    public int hashCode() {
        return nome.toLowerCase().hashCode();
    }
}
