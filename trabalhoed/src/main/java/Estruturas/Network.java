package Estruturas;

import Estruturas.Interfaces.NetworkADT;

import java.util.Comparator;
import java.util.Iterator;

public class Network<T> implements NetworkADT<T> {
	protected final int DEFAULT_CAPACITY = 10;
	protected T[] vertices;
	protected double[][] adjMatrix;
	protected int numVertices;

	public Network() {
		adjMatrix = new double[DEFAULT_CAPACITY][DEFAULT_CAPACITY];
		numVertices = 0;
		this.vertices = (T[]) (new Object[DEFAULT_CAPACITY]);
	}

	@Override
	public void addEdge(Object vertex1, Object vertex2, double weight) {
		int index1 = getIndex((T) vertex1);
		int index2 = getIndex((T) vertex2);
		if (indexIsValid(index1) && indexIsValid(index2)) {
			adjMatrix[index1][index2] = weight;
			adjMatrix[index2][index1] = weight;
		}
	}

	@Override
	public void addEdge(Object vertex1, Object vertex2) {
		addEdge(vertex1, vertex2, 0);
	}

	@Override
	public void addVertex(Object vertex) {
		if (numVertices == vertices.length) {
			expandCapacity();
		}
		vertices[numVertices] = (T) vertex;
		for (int i = 0; i <= numVertices; i++) {
			adjMatrix[numVertices][i] = 0;
			adjMatrix[i][numVertices] = 0;
		}
		numVertices++;
	}

	@Override
	public boolean isConnected() {
		if (numVertices == 0) {
			return false;
		} else {
			Iterator<T> it = iteratorBFS(0);
			int count = 0;
			while (it.hasNext()) {
				it.next();
				count++;
			}
			return (count == numVertices);
		}
	}

	@Override
	public boolean isEmpty() {
		if (numVertices == 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public Iterator<T> iteratorBFS(Object startVertex) {
		return iteratorBFS(getIndex((T) startVertex));
	}

	/**
	 * Returns an iterator that performs a breadth first search
	 * traversal starting at the given index.
	 *
	 * @param startIndex the index to begin the search from
	 * @return an iterator that performs a breadth first traversal
	 */
	public Iterator<T> iteratorBFS(int startIndex) {
		Integer x;
		LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();
		ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
		if (!indexIsValid(startIndex))
			return resultList.iterator();
		boolean[] visited = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++)
			visited[i] = false;

		traversalQueue.enqueue(new Integer(startIndex));
		visited[startIndex] = true;
		while (!traversalQueue.isEmpty()) {
			x = traversalQueue.dequeue();
			resultList.addToRear(vertices[x.intValue()]);
			/**
			 * Find all vertices adjacent to x that have
			 * not been visited and queue them up
			 */
			for (int i = 0; i < numVertices; i++) {
				if (adjMatrix[x.intValue()][i] > 0 && !visited[i]) {
					traversalQueue.enqueue(new Integer(i));
					visited[i] = true;
				}
			}
		}
		return resultList.iterator();
	}

	@Override
	public Iterator<T> iteratorDFS(Object startVertex) {
		return iteratorDFS(getIndex((T) startVertex));
	}

	/**
	 * Returns an iterator that performs a depth first search
	 * traversal starting at the given index.
	 *
	 * @param startIndex the index to begin the search traversal from
	 * @return an iterator that performs a depth first traversal
	 */
	public Iterator<T> iteratorDFS(int startIndex) {
		Integer x;
		boolean found;
		LinkedStack<Integer> traversalStack = new LinkedStack<Integer>();
		ArrayUnorderedList<T> resultList = new ArrayUnorderedList<T>();
		boolean[] visited = new boolean[numVertices];
		if (!indexIsValid(startIndex))
			return resultList.iterator();
		for (int i = 0; i < numVertices; i++)
			visited[i] = false;

		traversalStack.push(new Integer(startIndex));
		resultList.addToRear(vertices[startIndex]);
		visited[startIndex] = true;
		while (!traversalStack.isEmpty()) {
			x = traversalStack.peek();
			found = false;
			/**
			 * Find a vertex adjacent to x that has not been visited
			 * and push it on the stack
			 */
			for (int i = 0; (i < numVertices) && !found; i++) {
				if (adjMatrix[x.intValue()][i] > 0 && !visited[i]) {
					traversalStack.push(new Integer(i));
					resultList.addToRear(vertices[i]);
					visited[i] = true;
					found = true;
				}
			}
			if (!found && !traversalStack.isEmpty())
				traversalStack.pop();
		}
		return resultList.iterator();
	}

	public Iterator<T> iteratorShortestPath(Object startVertex, Object targetVertex) {
		return iteratorShortestPath(getIndex((T) startVertex), getIndex((T) targetVertex));
	}

	public Iterator<T> iteratorShortestPath(int startIndex, int targetIndex) {
		ArrayUnorderedList<T> templist = new ArrayUnorderedList<>();
		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
			return templist.iterator();
		}

		Iterator<Integer> it = iteratorShortestPathIndices(startIndex, targetIndex);
		while (it.hasNext()) {
			templist.addToRear(vertices[it.next()]);
		}
		return templist.iterator();
	}

	private Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) {
		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
			throw new IllegalArgumentException("Índices inválidos ou fora do limite.");
		}

		double[] weights = new double[numVertices];
		int[] predecessors = new int[numVertices];
		boolean[] visited = new boolean[numVertices];
		PriorityQueue<int[]> priorityQueue = new PriorityQueue<>();

		for (int i = 0; i < numVertices; i++) {
			weights[i] = Double.POSITIVE_INFINITY;
			predecessors[i] = -1;
			visited[i] = false;
		}
		weights[startIndex] = 0;
		priorityQueue.addElement(new int[] { startIndex, 0 }, 0);

		while (!priorityQueue.isEmpty()) {
			int[] current = priorityQueue.removeNext();
			int u = current[0];

			if (visited[u])
				continue;
			visited[u] = true;

			if (u == targetIndex)
				break;

			for (int i = 0; i < numVertices; i++) {
				if (adjMatrix[u][i] > 0 && !visited[i]) {
					double newWeight = weights[u] + adjMatrix[u][i]; // Verificação do peso da aresta
					if (newWeight < weights[i]) {
						weights[i] = newWeight;
						predecessors[i] = u;
						priorityQueue.addElement(new int[] { i, (int) newWeight }, (int) newWeight);
					}
				}
			}
		}

		ArrayUnorderedList<Integer> path = new ArrayUnorderedList<>();
		if (weights[targetIndex] == Double.POSITIVE_INFINITY) {
			return path.iterator();
		}

		int current = targetIndex;
		while (current != -1) {
			path.addToFront(current);
			current = predecessors[current];
		}

		return path.iterator();
	}

	protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited,
			double[] pathWeight, double weight) {
		for (int i = 0; i < numVertices; i++)
			if ((pathWeight[i] == weight) && !visited[i])
				for (int j = 0; j < numVertices; j++)
					if ((adjMatrix[i][j] < Double.POSITIVE_INFINITY) &&
							visited[j])
						return i;

		return -1;
	}

	@Override
	public int size() {
		return numVertices;
	}

	@Override
	public double shortestPathWeight(Object startVertex, Object targetVertex) {
		return shortestPathWeight(getIndex((T) startVertex), getIndex((T) targetVertex));
	}

	private double shortestPathWeight(int startIndex, int targetIndex) {
		double result = 0;
		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex)) {
			return 0;
		}

		int index = startIndex;
		int[] pathLength = new int[numVertices];
		int[] predecessor = new int[numVertices];
		LinkedQueue<Integer> traversalQueue = new LinkedQueue<Integer>();

		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) || (startIndex == targetIndex)) {
			return 0;
		}

		boolean[] visited = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++) {
			visited[i] = false;
		}

		traversalQueue.enqueue(Integer.valueOf(startIndex));
		visited[startIndex] = true;
		pathLength[startIndex] = 0;
		predecessor[startIndex] = -1;

		while (!traversalQueue.isEmpty() && (index != targetIndex)) {
			index = (traversalQueue.dequeue()).intValue();

			/**
			 * Update the pathLength for each unvisited vertex adjacent
			 * to the vertex at the current index.
			 */
			for (int i = 0; i < numVertices; i++) {
				if (adjMatrix[index][i] > 0 && !visited[i]) {
					pathLength[i] = pathLength[index] + 1;
					predecessor[i] = index;
					traversalQueue.enqueue(Integer.valueOf(i));
					visited[i] = true;
				}
			}
		}

		if (index != targetIndex) {// no path must have been found
			return 0;
		}

		index = targetIndex;
		while (predecessor[index] != -1) {
			result += adjMatrix[index][predecessor[index]];
			index = predecessor[index];
		}
		return result;
	}

	/**
	 * Returns a string representation of the adjacency matrix.
	 * 
	 * @param index
	 * @return
	 */
	protected boolean indexIsValid(int index) {
		return (index < numVertices && index >= 0);
	}

	/**
	 * Expands the capacity of the network by expanding the size of the
	 * 
	 * @return the new capacity
	 */
	protected void expandCapacity() {
		T[] larger = (T[]) (new Object[vertices.length * 2]);
		for (int i = 0; i < vertices.length; i++) {
			larger[i] = vertices[i];
		}
		vertices = larger;

		double[][] largerMatrix = new double[vertices.length * 2][vertices.length * 2];
		for (int i = 0; i < adjMatrix.length; i++) {
			for (int j = 0; j < adjMatrix[i].length; j++) {
				largerMatrix[i][j] = adjMatrix[i][j];
			}
		}
		adjMatrix = largerMatrix;
	}

	/**
	 * Returns the index value of the first occurrence of the vertex.
	 * 
	 * @param vertex
	 * @return the index value of the given vertex
	 */
	public int getIndex(T vertex) {
		int found = -1;
		for (int i = 0; i < numVertices; i++) {
			if (vertices[i].equals(vertex)) {
				found = i;
				break;
			}
		}
		return found;
	}

	protected int[] getEdgeWithWeightOf(double weight, boolean[] visited) {
		int[] edge = new int[2];
		edge[0] = -1;
		edge[1] = -1;
		for (int i = 0; i < numVertices; i++) {
			if (visited[i]) {
				for (int j = 0; j < numVertices; j++) {
					if (!visited[j] && (adjMatrix[i][j] == weight)) {
						edge[0] = i;
						edge[1] = j;
						break;
					}
				}
			}
		}
		return edge;
	}

	@Override
	public void removeEdge(Object vertex1, Object vertex2) {
		int index1 = getIndex((T) vertex1);
		int index2 = getIndex((T) vertex2);
		if (indexIsValid(index1) && indexIsValid(index2)) {
			adjMatrix[index1][index2] = 0;
			adjMatrix[index2][index1] = 0;
		}
	}

	@Override
	public void removeVertex(Object vertex) {
		int index = getIndex((T) vertex);
		if (indexIsValid(index)) {
			for (int i = index; i < numVertices - 1; i++) {
				vertices[i] = vertices[i + 1];
				for (int j = 0; j < numVertices; j++) {
					adjMatrix[i][j] = adjMatrix[i + 1][j];
				}
			}
			numVertices--;
		}
	}

	@Override
	public String toString() {
		String result = "";
		for (int i = 0; i < numVertices; i++) {
			for (int j = 0; j < numVertices; j++) {
				result += adjMatrix[i][j] + " ";
			}
			result += "\n";
		}
		return result;
	}
}
