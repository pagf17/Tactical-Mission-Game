import Estruturas.ArrayUnorderedList;
import Estruturas.LinkedList;

public class Divisao {
    private String nome;
    private ArrayUnorderedList<Inimigo> inimigos; 
    private ArrayUnorderedList<Item> itens; 

    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new ArrayUnorderedList<>(); 
        this.itens = new ArrayUnorderedList<>(); 
    }

    public void addInimigo(Inimigo inimigo) {
        inimigos.addToRear(inimigo);
    }

    public void addItem(Item item) {
        itens.addToRear(item);
    }

    public ArrayUnorderedList<Inimigo> getInimigos() {
        return inimigos;
    }

    public ArrayUnorderedList<Item> getItens() {
        return itens;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setInimigos(ArrayUnorderedList<Inimigo> inimigos) {
        this.inimigos = inimigos;
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
}
