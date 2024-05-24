package gre.lab2.groupJ;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.*;

/**
 * Main class to run the BellmanFordYensAlgorithm algorithm
 * It performs the Bellman-Ford-Yens algorithm to find the shortest path tree or a negative cycle
 * in a weighted directed graph.
 * The Bellman-Ford-Yens algorithm is a variant of the Bellman-Ford algorithm that can detect negative cycles.
 * The algorithm has a time complexity of O(n*m) where n is the number of vertices and m is the number of edges.
 *
 * @author Julien Muhlemann, Julien Holzer
 */
public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {

    // set an arbitrary value for the sentinel (-1 because it is not a valid vertex index (0 to nVertices-1))
    final static int SENTINEL = -1;

    /**
     * @param graph the weighted directed graph
     * @param from  the source vertex
     * @return A {@link BFYResult} which contains either the shortest path tree
     * or a negative cycle reachable from the source vertex.
     */
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {

        // initialize the distances and predecessors arrays to a size corresponding to the number of vertices
        int nVertices = graph.getNVertices();
        int[] distances = new int[nVertices];
        int[] predecessors = new int[nVertices];


        // initialize the inQueue array to keep track of the vertices in the queue (false by default)
        boolean[] inQueue = new boolean[nVertices];


        // initialize the iteration count to 0 (the maximum number of iterations being nVertices)
        int iterationCount = 0;

        // initialize distances to max value and predecessors to unreachable
        for (int i = 0; i < nVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
            predecessors[i] = BFYResult.UNREACHABLE;
        }

        // set the distance of the source to 0
        distances[from] = 0;

        // create a queue and add the source and the sentinel
        Queue<Integer> queue = new LinkedList<>();
        queue.add(from);
        queue.add(SENTINEL);

        // indicate that the source is in the queue
        inQueue[from] = true;

        // while the queue is not empty continue the algorithm
        while (!queue.isEmpty()) {

            // dequeue the first vertex (the inQueue array will be updated later because we need to check if it's a sentinel first)
            int currentVertex = queue.remove();

            // if currentVertex is the sentinel
            if (currentVertex == SENTINEL) {
                // if the queue is not empty
                if (!queue.isEmpty()) {
                    ++iterationCount;
                    // if we have done nVertices iterations, there is a negative cycle
                    if (iterationCount == nVertices) {
                        return detectNegativeCycle(graph, predecessors, queue);
                    } else {
                        // enqueue the sentinel
                        queue.add(SENTINEL);
                    }
                }
            } else {
                // here the current vertex is guaranteed to be a valid vertex index
                // the update is related with the removal of the vertex from the queue
                inQueue[currentVertex] = false;

                // process each successor of the current vertex
                for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(currentVertex)) {
                    int successorVertex = edge.to();
                    int weight = edge.weight();
                    // if there is an improvement update the distance and predecessor
                    if (distances[currentVertex] != Integer.MAX_VALUE && distances[currentVertex] + weight < distances[successorVertex]) {
                        distances[successorVertex] = distances[currentVertex] + weight;
                        predecessors[successorVertex] = currentVertex;

                        // insert successorVertex into the queue if not already there
                        if (!inQueue[successorVertex]) {
                            queue.add(successorVertex);
                            inQueue[successorVertex] = true;
                        }
                    }
                }
            }
        }

        // return the arrays of distances and predecessors
        return new BFYResult.ShortestPathTree(distances, predecessors);
    }


    /**
     * Detects a negative cycle in the graph
     *
     * @param graph       the weighted directed graph
     * @param predecessors the predecessors array
     * @param queue       the queue
     * @return A {@link BFYResult} which contains the negative cycle and its total weight
     */
    public static BFYResult detectNegativeCycle(WeightedDigraph graph, int[] predecessors, Queue<Integer> queue) {

        // initialize an array to keep track of the vertices in the cycle (false by default)
        int nVertices = graph.getNVertices();
        boolean[] inCycle = new boolean[nVertices];

        // get the current vertex from the queue, it could never be a NullPointer because the queue is not empty
        int currentVerticle = queue.element();

        // initialize the list of the potential vertices in the cycle
        List<Integer> cycleVertices = new LinkedList<>();


        // if the current vertex is not in the cycle potential vertices list, add it to the list and take the predecessor as the next vertex
        // if the current vertex is already in the cycle, we have found the cycle (and maybe some other vertices)
        while (!inCycle[currentVerticle]) {
            cycleVertices.addFirst(currentVerticle);
            inCycle[currentVerticle] = true;
            currentVerticle = predecessors[currentVerticle];
        }

        // remove the vertices that are not in the cycle by comparing the last element of the list with the current vertex
        while (cycleVertices.getLast() != currentVerticle) {
            cycleVertices.removeLast();
        }

        // now that we have the vertices in the cycle, we need to find the total weight of the cycle
        int cycleWeight = 0;
        // iterate over the vertices in the cycle and find the minimum edge weight between each pair of vertices
        for (int i = 0; i < cycleVertices.size(); i++) {
            int currentVertex = cycleVertices.get(i);
            int successorVertex = cycleVertices.get((i + 1) % (cycleVertices.size())); // modulo the size of cycleVertices for the last vertex of the list
            int minEdgeWeight = Integer.MAX_VALUE;

            // find the minimum edge weight between currentVertex and successorVertex
            for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(currentVertex)) {
                // if the edge goes to the successorVertex and the weight is less than the minimum edge weight, update the minimum edge weight
                if (edge.to() == successorVertex && edge.weight() < minEdgeWeight) {
                    minEdgeWeight = edge.weight();
                }
            }
            // add the minimum edge weight to the total weight of the cycle
            cycleWeight += minEdgeWeight;
        }

        // return the negative cycle as an array of the vertices in the cycle and the total weight of the cycle
        return new BFYResult.NegativeCycle(cycleVertices, cycleWeight);
    }
}