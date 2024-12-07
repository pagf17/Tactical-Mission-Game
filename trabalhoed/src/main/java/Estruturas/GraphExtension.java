package Estruturas;
public class GraphExtension<T> extends Graph<T> {

    public boolean verificarLigacao(T vertex1, T vertex2) {
        int index1 = getIndex(vertex1); 
        int index2 = getIndex(vertex2); 

        if (indexIsValid(index1) && indexIsValid(index2) && adjMatrix[index1][index2]) {
            return true;
        } else {
            return false;
        }
    }
}
