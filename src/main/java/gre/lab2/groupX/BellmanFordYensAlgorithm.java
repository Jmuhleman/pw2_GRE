package gre.lab2.groupX;

import gre.lab2.graph.BFYResult;
import gre.lab2.graph.IBellmanFordYensAlgorithm;
import gre.lab2.graph.WeightedDigraph;

import java.util.*;
import java.util.Collections;
import java.util.List;

public final class BellmanFordYensAlgorithm implements IBellmanFordYensAlgorithm {
  @Override
  public BFYResult compute(WeightedDigraph graph, int from) {


    int nVertices = graph.getNVertices();
    int[] distances = new int[nVertices];
    int[] predecessors = new int[nVertices];
    Arrays.fill(distances, Integer.MAX_VALUE);
    Arrays.fill(predecessors, BFYResult.UNREACHABLE);
    distances[from] = 0;

    // Relax edges |V| - 1 times
      for (int u = 0; u < nVertices; u++) {

        for (WeightedDigraph.Edge edge : graph.getOutgoingEdges(u)) {
          // If we find a better way
          if (distances[u] != Integer.MAX_VALUE && distances[u] + edge.weight() < distances[edge.to()]) {
            distances[edge.to()] = distances[u] + edge.weight();
            predecessors[edge.to()] = u;
          }
        }
      }

    return new BFYResult.ShortestPathTree(distances, predecessors);
  }


}