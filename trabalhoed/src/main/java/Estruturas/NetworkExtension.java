package Estruturas;

import java.util.Iterator;

import Jogo.Divisao;

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

    public int[] dijkstra(Divisao startDivisao, Divisao targetDivisao) {
        int startIndex = getIndex((T) startDivisao);
        int targetIndex = getIndex((T) targetDivisao);

        // Verifica se os índices são válidos
        if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
            throw new IllegalArgumentException("Divisões inválidas");
        }

        int[] distancias = new int[numVertices]; // Distâncias mínimas
        int[] predecessores = new int[numVertices]; // Predecessores para reconstrução do caminho
        boolean[] visitados = new boolean[numVertices]; // Marca os vértices visitados

        // Inicializa os arrays
        for (int i = 0; i < numVertices; i++) {
            distancias[i] = Integer.MAX_VALUE; // Inicializa com valor infinito
            visitados[i] = false;
            predecessores[i] = -1; // Não há predecessores inicialmente
        }
        distancias[startIndex] = 0; // A distância do ponto de origem para si mesmo é 0

        // Usando uma fila de prioridade (simulada com um array simples)
        PriorityQueue<Integer> queue = new PriorityQueue<>();
        queue.addElement(startIndex, 0);

        while (!queue.isEmpty()) {
            int u = queue.removeNext(); // Pega o vértice com a menor distância

            // Se o vértice já foi visitado, ignoramos
            if (visitados[u])
                continue;
            visitados[u] = true;

            // Se encontramos o destino, podemos parar
            if (u == targetIndex) {
                break;
            }

            // Explorar todos os vizinhos de u
            for (int v = 0; v < numVertices; v++) {
                if (adjMatrix[u][v] > 0 && !visitados[v]) { // Se há conexão e v não foi visitado
                    int novaDistancia = distancias[u] + (int) adjMatrix[u][v];
                    if (novaDistancia < distancias[v]) { // Se encontrar um caminho mais curto
                        distancias[v] = novaDistancia;
                        predecessores[v] = u; // Atualiza o predecessor
                        queue.addElement(v, novaDistancia); // Adiciona v à fila de prioridade
                    }
                }
            }
        }

        return predecessores;
    }

}
