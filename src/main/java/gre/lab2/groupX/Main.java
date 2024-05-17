package gre.lab2.groupX;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraphReader;
import gre.lab2.graph.BFYResult;
import java.io.IOException;


public final class Main {
  public static void main(String[] args) throws IOException {



    //read the files
    final String[] FILES = new String[] { "data/test.txt", "data/reseau1.txt", "data/reseau2.txt",
                                          "data/reseau3.txt", "data/reseau4.txt"};

    for (var file : FILES){
        System.out.println("\n\n>> Calculating the shortest path of the graph in " + file + ":");
        // Read the given graph file, parse it and return a DirectedGraph structure
        WeightedDigraph graph = WeightedDigraphReader.fromFile(file);

        BellmanFordYensAlgorithm bfy = new BellmanFordYensAlgorithm();
        // Apply the BellmanFordYensAlgorithm algorithm from the vertex 0
        BFYResult result = bfy.compute(graph, 0);

         System.out.println("--");

        if (result instanceof BFYResult.ShortestPathTree) {
            System.out.println("Shortest path tree:");
            BFYResult.ShortestPathTree spt = (BFYResult.ShortestPathTree) result;
            for (int i = 0; i < spt.distances().length; i++) {
            System.out.println("Vertex " + i + ": " + spt.distances()[i] + " (predecessor: " + spt.predecessors()[i] + ")");
            }
        }
        /*else if (result instanceof BFYResult.NegativeCycle) {
            System.out.println("Negative cycle found:");
            BFYResult.NegativeCycle nc = (BFYResult.NegativeCycle) result;
            //System.out.println("Cycle: " + nc.cycle());
        }
*/



    }

    // TODO
    //  - Renommage du package ;
    //  - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
    //  - Documentation soignée comprenant :
    //    - la javadoc, avec auteurs et description des implémentations ;
    //    - des commentaires sur les différentes parties de vos algorithmes.
  }
}
