package Estruturas;

public class NetworkExtension<T> extends Network<T> {

    /**
     * Construtor
     *
     * @param vertex1
     * @param vertex2
     * @return
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
     * @param index
     * @return
     */
    public T getVertex(int index) {
        if (indexIsValid(index)) {
            return vertices[index];
        } else {
            throw new IndexOutOfBoundsException("Index is not valid");
        }
    }

    /**
     * Atualiza o valor da aresta entre dois vértices
     * 
     * @param vertex1 
     * @param vertex2
     * @param value
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

    public double calcularDistanciaDoCaminho(int startIndex, int endIndex, int[] predecessores) {
        double distancia = 0;
        int atual = endIndex;

        // Reconstruir o caminho para calcular a distância total
        while (atual != startIndex) {
            int predecessor = predecessores[atual];
            distancia += adjMatrix[atual][predecessor]; // Soma a distância entre o atual e o predecessor
            atual = predecessor;
        }

        return distancia;
    }
}
