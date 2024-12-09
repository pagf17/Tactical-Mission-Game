package Jogo;

import Estruturas.ArrayUnorderedList;

public class Divisao {
    private String nome;
    public boolean entradaSaida = false;
    private ArrayUnorderedList<Inimigo> inimigos;
    private ArrayUnorderedList<Item> itens;

    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new ArrayUnorderedList<>();
        this.itens = new ArrayUnorderedList<>();
        this.entradaSaida = entradaSaida;
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

    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
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
        return nome + (entradaSaida ? " (Entrada/Saída)" : "");
    }

    public void listarInimigos() {
        for (Inimigo inimigo : inimigos) {
            if (inimigo != null) {
                System.out.println(inimigo);
            }
        }
    }

    public void listarItens() {
        for (Item item : itens) {
            if (item != null) {
                System.out.println(item);
            }
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || getClass() != obj.getClass())
            return false;
        Divisao divisao = (Divisao) obj;
        return nome.equalsIgnoreCase(divisao.nome);
    }
}
