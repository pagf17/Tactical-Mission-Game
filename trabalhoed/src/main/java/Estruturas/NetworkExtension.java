package Estruturas;

public class NetworkExtension<T> extends Network<T> {

    /**
     * Devolve se os dois vértices estão conectados
     * 
     * @param vertex1 primeiro vértice
     * @param vertex2 segundo vértice
     * @return true se estão conectados, false caso contrário
     */
    public boolean verificarLigacao(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2) && adjMatrix[index1][index2] != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Retorna o vértice do índice especificado
     * 
     * @param index índice do vértice
     * @return vértice do índice especificado
     */
    public T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        } else {
            throw new IndexOutOfBoundsException("Index is not valid");
        }
    }

    /**
     * Atualiza o valor do peso da aresta entre dois vértices
     * 
     * @param vertex1  primeiro vértice
     * @param vertex2 segundo vértice
     * @param value novo valor do peso
     */
    public void atualizarPeso(T vertex1, T vertex2, double value) {
        int index1 = getIndex(vertex1);
        int index2 = getIndex(vertex2);

        if (indexIsValid(index1) && indexIsValid(index2)) {
            adjMatrix[index1][index2] = value;
            adjMatrix[index2][index1] = value;
        } else {
            throw new IllegalArgumentException("Vértices inválidos");
        }
    }
}
