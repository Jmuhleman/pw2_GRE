package gre.lab2.groupX;
import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main class to run the BellmanFordYensAlgorithm algorithm
 * It performs the Bellman-Ford-Yens algorithm to find the shortest path tree or a negative cycle
 * in a weighted directed graph.
 * @author Julien Muhlemann, Julien Holzer
 * @version 1.0
 */
public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
    /**
     *
     * @param graph the weighted directed graph
     * @param from the source vertex
     * @return A {@link BFYResult} which contains either the shortest path tree
     *         or the first negative cycle reachable from the source vertex.
     *
     * @complexity The time complexity of this method is in the worst case O(V * E) where V is the number of vertices
     *             and E is the number of edges in the graph.
     *             The yens improvement does not change the time complexity but reduces the number of relaxations
     *             if we do not observe any improvements in the distance to a vertex */
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {


        int nVertices = graph.getNVertices();
        int[] distances = new int[nVertices];
        int[] predecessors = new int[nVertices];
        boolean[] inQueue = new boolean[nVertices];
        int sentinel = -2;

        // (1) Initialize distances and predecessors
        for (int i = 0; i < nVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
            predecessors[i] = BFYResult.UNREACHABLE;
        }
        distances[from] = 0;

        // (3) Create a queue with the source and the sentinel
        int[] queue = new int[nVertices * nVertices + 1]; // Extra space for the sentinel
        Arrays.fill(queue, -3);
        int front = 0;
        int rear = 0;
        queue[rear] = from;
        queue[++rear] = sentinel;

        inQueue[from] = true;
        int k = 0;

        // (4) While the queue is not empty
        while (queue[front] != -3) {
            // (5) Dequeue the first vertex
            int currentVertex = queue[front++];
            // (6) If i is the sentinel
            if (currentVertex == sentinel) {
                // (7) If the queue is not empty
                if (queue[front] != -3) {
                    ++k;
                    if (k == nVertices) {
                        // retourner circuit absorbant
                        // puisqu'on a fait n itérations avec améliorations
                        return detectNegativeCycle(graph, predecessors, distances);
                    } else {
                        // (8) Enqueue the sentinel
                        queue[++rear] = sentinel;
                    }
                }
            } else {
                //if it was not a sentinelle pop, update the bool queue regarding last vertex!
                inQueue[queue[front - 1]] = false;
                // (11) Process each successor of i
                for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(currentVertex)) {
                    int j = edge.to();
                    int weight = edge.weight();
                    // (13) If there is an improvement
                    if (distances[currentVertex] != Integer.MAX_VALUE && distances[currentVertex] + weight < distances[j]) {
                        // (14) Update the distance and predecessor
                        distances[j] = distances[currentVertex] + weight;
                        predecessors[j] = currentVertex;
                        // (15) Insert j into the queue if not already there
                        if (!inQueue[j]) {
                            queue[++rear] = j;
                            inQueue[j] = true;
                        }
                    }
                }
            }
        }

        // (16) Return the distances and predecessors
        return new BFYResult.ShortestPathTree(distances, predecessors);
    }

    /**
     * Detect a negative cycle in the graph using the predecessors and distances
     * It performs one more iteration of the BFY algo to find the starting point of the cycle
     * and then backtracks the predecessors to find the cycle
     * @param graph the weighted directed graph
     * @param predecessors the predecessors of each vertex
     * @param distances the distances of each vertex
     * @return A {@link BFYResult.NegativeCycle} if a negative cycle is found and accessible
     *         from the source, otherwise null.
     *
     * @complexity The time complexity of this method is O(V * E) where V is the number of vertices
     *             and E is the number of edges in the graph.     */
    private BFYResult detectNegativeCycle(WeightedDigraph graph, int[] predecessors, int[] distances) {
        int nVertices = graph.getNVertices();
        int cycleStart = -1;

        // Check for updates to find the starting point of a negative cycle
        // as if we performed one more iteration of the BFY algo
        // the improvement would be made on the cycle
        for (int u = 0; u < nVertices; u++) {
            for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(u)) {
                int v = edge.to();
                int weight = edge.weight();
                if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    cycleStart = v;
                    break;
                }
            }
            //if we found a potential absorbing cycle, we break the loop
            if (cycleStart != -1) break;
        }

        // Use predecessors to find the negative cycle
        if (cycleStart != -1) {
            int[] cycle = new int[nVertices];

            int cycleLength = 0;
            int current = cycleStart;

            //backtracking the predecessors to find the cycle
            for (int i = 0; i < nVertices; i++) {
                current = predecessors[current];
            }

            //start of the current absorbing cycle
            int cycleNode = current;

            do {
                //mark the current node as part of the absorbing cycle
                cycle[cycleLength++] = current;
                current = predecessors[current];
            } while (current != cycleNode);

            //just the close the cycle I.E. the starting node appears twice
            cycle[cycleLength++] = current;

            boolean isReachable = isCycleAccessibleFromSource(cycleNode, predecessors, cycleNode);
            if (isReachable) {
                List<Integer> cycleList = new ArrayList<>();
                for (int i = cycleLength - 1; i >= 0; i--) {
                    cycleList.add(cycle[i]);
                }
                return new BFYResult.NegativeCycle(cycleList, cycleLength);
            }
        }

        return null;
    }

    /**
     * Check if the cycle is accessible from the source by backtracking the predecessors
     * @param cycleNode the node of the cycle
     * @param predecessors the predecessors of each vertex
     * @param source the source vertex
     * @return true if the cycle is accessible from the source, false otherwise.
     * @complexity The time complexity of this method is O(V) where V is the number of vertices in the graph.
     * */
    private boolean isCycleAccessibleFromSource(int cycleNode, int[] predecessors, int source) {
        int current = cycleNode;
        boolean[] visited = new boolean[predecessors.length];
        while (current != source && !visited[current]) {
            visited[current] = true;
            current = predecessors[current];
        }
        return current == source;
    }
}