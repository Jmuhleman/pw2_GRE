package gre.lab2.groupX;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;


public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
    @Override
    public BFYResult compute(WeightedDigraph graph, int from) {


        int nVertices = graph.getNVertices();
        int[] distances = new int[nVertices];
        int[] predecessors = new int[nVertices];
        boolean[] inQueue = new boolean[nVertices*nVertices];
        int sentinel = -2;

        // (1) Initialize distances and predecessors
        for (int i = 0; i < nVertices; i++) {
            distances[i] = Integer.MAX_VALUE;
            predecessors[i] = BFYResult.UNREACHABLE;
        }
        distances[from] = 0;

        // (3) Create a queue with the source and the sentinel
        int[] queue = new int[nVertices * nVertices + 1]; // Extra space for the sentinel
        int front = 0;
        int rear = 0;
        queue[rear] = from;
        queue[++rear] = sentinel;

        inQueue[from] = true;
        int k = 0;

        // (4) While the queue is not empty
        while (front != rear) {
            // (5) Dequeue the first vertex

            inQueue[front] = false;
            int currentVertex = queue[front++];
            // (6) If i is the sentinel
            if (currentVertex == sentinel) {
                // (7) If the queue is not empty
                if (front+1 != rear) {
                    ++k;
                    if (k == nVertices) {
                        // retourner circuit absorbant
                        // puisqu'on a fait n itérations avec améliorations


                    } else {
                        // (8) Enqueue the sentinel
                        queue[++rear] = sentinel;
                    }
                }

            } else {
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



    private BFYResult detectNegativeCycle(WeightedDigraph graph, int[] predecessors, int[] distances) {
        int nVertices = graph.getNVertices();
        boolean[] visited = new boolean[nVertices];
        int cycleStart = -1;

        // Check for updates to find the starting point of a negative cycle
        for (int u = 0; u < nVertices; u++) {
            for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(u)) {
                int v = edge.to();
                int weight = edge.weight();
                if (distances[u] != Integer.MAX_VALUE && distances[u] + weight < distances[v]) {
                    cycleStart = v;
                    break;
                }
            }
            if (cycleStart != -1) break;
        }

        // Use predecessors to find the negative cycle
        if (cycleStart != -1) {
            int[] cycle = new int[nVertices];
            int cycleLength = 0;
            int current = cycleStart;
            for (int i = 0; i < nVertices; i++) {
                current = predecessors[current];
            }
            int cycleNode = current;
            do {
                cycle[cycleLength++] = current;
                current = predecessors[current];
            } while (current != cycleNode);
            cycle[cycleLength++] = current;

          //  List<Integer> cycleList = new ArrayList<>();
            for (int i = cycleLength - 1; i >= 0; i--) {
           //     cycleList.add(cycle[i]);
            }
           // return new BFYResult.NegativeCycle(cycleList, cycleLength);
        }

        return null;
    }




}