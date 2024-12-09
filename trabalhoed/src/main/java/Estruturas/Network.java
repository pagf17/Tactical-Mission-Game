package Estruturas;

import Estruturas.Interfaces.NetworkADT;

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
		ArrayUnorderedList templist = new ArrayUnorderedList();
		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex))
			return templist.iterator();

		Iterator<Integer> it = iteratorShortestPathIndices(startIndex,
				targetIndex);
		while (it.hasNext())
			templist.addToRear(vertices[(it.next()).intValue()]);
		return templist.iterator();
	}

	protected Iterator<Integer> iteratorShortestPathIndices(int startIndex, int targetIndex) {
		int index;
		double weight;
		int[] predecessor = new int[numVertices];
		LinkedHeap<Double> traversalMinHeap = new LinkedHeap<Double>();
		ArrayUnorderedList<Integer> resultList = new ArrayUnorderedList<Integer>();
		LinkedStack<Integer> stack = new LinkedStack<Integer>();

		int[] pathIndex = new int[numVertices];
		double[] pathWeight = new double[numVertices];
		for (int i = 0; i < numVertices; i++)
			pathWeight[i] = Double.POSITIVE_INFINITY;

		boolean[] visited = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++)
			visited[i] = false;

		if (!indexIsValid(startIndex) || !indexIsValid(targetIndex) ||
				(startIndex == targetIndex) || isEmpty())
			return resultList.iterator();

		pathWeight[startIndex] = 0;
		predecessor[startIndex] = -1;
		visited[startIndex] = true;
		weight = 0;

		/**
		 * Update the pathWeight for each vertex except the
		 * startVertex. Notice that all vertices not adjacent
		 * to the startVertex will have a pathWeight of
		 * infinity for now.
		 */
		for (int i = 0; i < numVertices; i++) {
			if (!visited[i]) {
				pathWeight[i] = pathWeight[startIndex] +
						adjMatrix[startIndex][i];
				predecessor[i] = startIndex;
				traversalMinHeap.addElement(new Double(pathWeight[i]));
			}
		}

		do {
			weight = (traversalMinHeap.removeMin()).doubleValue();
			traversalMinHeap.heapifyRemove();
			if (weight == Double.POSITIVE_INFINITY) // no possible path
				return resultList.iterator();
			else {
				index = getIndexOfAdjVertexWithWeightOf(visited, pathWeight, weight);
				visited[index] = true;
			}

			/**
			 * Update the pathWeight for each vertex that has has not been
			 * visited and is adjacent to the last vertex that was visited.
			 * Also, add each unvisited vertex to the heap.
			 */
			for (int i = 0; i < numVertices; i++) {
				if (!visited[i]) {
					if ((adjMatrix[index][i] < Double.POSITIVE_INFINITY) &&
							(pathWeight[index] + adjMatrix[index][i]) < pathWeight[i]) {
						pathWeight[i] = pathWeight[index] + adjMatrix[index][i];
						predecessor[i] = index;
					}
					traversalMinHeap.addElement(new Double(pathWeight[i]));
				}
			}
		} while (!traversalMinHeap.isEmpty() && !visited[targetIndex]);

		index = targetIndex;
		stack.push(new Integer(index));
		do {
			index = predecessor[index];
			stack.push(new Integer(index));
		} while (index != startIndex);

		while (!stack.isEmpty())
			resultList.addToRear((stack.pop()));

		return resultList.iterator();
	}

	protected int getIndexOfAdjVertexWithWeightOf(boolean[] visited, 
                 double[] pathWeight, double weight)
   {
      for (int i = 0; i < numVertices; i++)
         if ((pathWeight[i] == weight) && !visited[i])
            for (int j = 0; j < numVertices; j++)
               if ((adjMatrix[i][j] < Double.POSITIVE_INFINITY) && 
                    visited[j])
                  return i;

      return -1;  // should never get to here
   }

	@Override
	public int size() {
		return numVertices;
	}

	@Override
	public double shortestPathWeight(Object startVertex, Object targetVertex) {
		return shortestPathWeight(getIndex((T) startVertex), getIndex((T) targetVertex));
	}

	public double shortestPathWeight(int startIndex, int targetIndex) {
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
	 * Returns a minimum spanning tree of the network.
	 *
	 * @return a minimum spanning tree of the network
	 */
	public Network mstNetwork() {
		int x, y;
		int index;
		double weight;
		int[] edge = new int[2];
		LinkedHeap<Double> minHeap = new LinkedHeap<Double>();
		Network<T> resultGraph = new Network<T>();

		if (isEmpty() || !isConnected())
			return resultGraph;

		resultGraph.adjMatrix = new double[numVertices][numVertices];
		for (int i = 0; i < numVertices; i++)
			for (int j = 0; j < numVertices; j++)
				resultGraph.adjMatrix[i][j] = Double.POSITIVE_INFINITY;
		resultGraph.vertices = (T[]) (new Object[numVertices]);

		boolean[] visited = new boolean[numVertices];
		for (int i = 0; i < numVertices; i++)
			visited[i] = false;

		edge[0] = 0;
		resultGraph.vertices[0] = this.vertices[0];
		resultGraph.numVertices++;
		visited[0] = true;

		/**
		 * Add all edges, which are adjacent to the starting vertex,
		 * to the heap
		 */
		for (int i = 0; i < numVertices; i++)
			minHeap.addElement(new Double(adjMatrix[0][i]));

		while ((resultGraph.size() < this.size()) && !minHeap.isEmpty()) {
			/**
			 * Get the edge with the smallest weight that has exactly
			 * one vertex already in the resultGraph
			 */
			do {
				weight = (minHeap.removeMin()).doubleValue();
				edge = getEdgeWithWeightOf(weight, visited);
			} while (!indexIsValid(edge[0]) || !indexIsValid(edge[1]));

			x = edge[0];
			y = edge[1];
			if (!visited[x])
				index = x;
			else
				index = y;

			/** Add the new edge and vertex to the resultGraph */
			resultGraph.vertices[index] = this.vertices[index];
			visited[index] = true;
			resultGraph.numVertices++;

			resultGraph.adjMatrix[x][y] = this.adjMatrix[x][y];
			resultGraph.adjMatrix[y][x] = this.adjMatrix[y][x];

			/**
			 * Add all edges, that are adjacent to the newly added vertex,
			 * to the heap
			 */
			for (int i = 0; i < numVertices; i++) {
				if (!visited[i] && (this.adjMatrix[i][index] < Double.POSITIVE_INFINITY)) {
					edge[0] = index;
					edge[1] = i;
					minHeap.addElement(new Double(adjMatrix[index][i]));
				}
			}
		}
		return resultGraph;
	}

	/**
	 * Returns a string representation of the adjacency matrix.
	 * 
	 * @param index
	 * @return
	 */
	public boolean indexIsValid(int index) {
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
