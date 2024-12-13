package Jogo;

import Estruturas.ArrayUnorderedList;
import Interfaces.StatusGeral;
import Jogo.Printer.PrinterGeral;

/**
 * Classe que representa uma divisão
 */
public class Divisao {
    private String nome;
    protected boolean entradaSaida = false;
    private ArrayUnorderedList<Inimigo> inimigos;
    private ArrayUnorderedList<Item> itens;

    /**
     * Construtor de Divisao
     * 
     * @param nome Nome da divisão.
     */
    public Divisao(String nome) {
        this.nome = nome;
        this.inimigos = new ArrayUnorderedList<>();
        this.itens = new ArrayUnorderedList<>();
        this.entradaSaida = entradaSaida;
    }

    /**
     * Método que adiciona um inimigo à divisão.
     * 
     * @param inimigo Inimigo a ser adicionado.
     */
    public void addInimigo(Inimigo inimigo) {
        inimigos.addToRear(inimigo);
    }

    /**
     * Método que adiciona um item à divisão.
     * 
     * @param item Item a ser adicionado.
     */
    public void addItem(Item item) {
        itens.addToRear(item);
    }

    /**
     * Método que devolve a lista de inimigos da divisão.
     * 
     * @return Lista de inimigos da divisão.
     */
    public ArrayUnorderedList<Inimigo> getInimigos() {
        return inimigos;
    }

    /**
     * Método que devolve a lista de itens da divisão.
     * 
     * @return Lista de itens da divisão.
     */
    public ArrayUnorderedList<Item> getItens() {
        return itens;
    }

    /**
     * Método que devolve se a divisão é, ou não, uma entrada/saída.
     * 
     * @return se a divisão é, ou não, uma entrada/saída.
     */
    public boolean isEntradaSaida() {
        return entradaSaida;
    }

    /**
     * Método para definir se certa divisão é, ou não, uma entrada/saída.
     * 
     * @param entradaSaida Booleano que define se a divisão é, ou não, uma entrada/saída.
     */
    public void setEntradaSaida(boolean entradaSaida) {
        this.entradaSaida = entradaSaida;
    }

    /**
     * Método para devolver o nome da divisão.
     * 
     * @return Nome da divisão.
     */
    public String getNome() {
        return nome;
    }

    /**
     * Método para definir o nome da divisão.
     * 
     * @param nome Nome da divisão.
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * Método para definir a lista de inimigos na divisão.
     * 
     * @param inimigos Lista de inimigos na divisão.    
     */
    public void setInimigos(ArrayUnorderedList<Inimigo> inimigos) {
        this.inimigos = inimigos;
    }

    /**
     * Método para listar todos os inimigos presentes na divisão.
     * Itera pela lista de inimigos e imprime cada um caso não seja nulo.
     */
    public void listarInimigos() {
        for (Inimigo inimigo : inimigos) {
            if (inimigo != null) {
                PrinterGeral.print(StatusGeral.LISTAR_INIMIGO, inimigo.toString());
            }
        }
    }

    /**
     * Método para listar todos os itens presentes na divisão.
     * Itera pela lista de itens e imprime cada um caso não seja nulo.
     */
    public void listarItens() {
        for (Item item : itens) {
            if (item != null) {
                PrinterGeral.print(StatusGeral.LISTAR_ITEM, item.toString());
            }
        }
    }

    /**
     * Método para devolver a representação em String da divisão.
     * 
     * @return Representação em String da divisão.
     */
    @Override
    public String toString() {
        return nome + (entradaSaida ? " (Entrada/Saída)" : "");
    }
    
}
