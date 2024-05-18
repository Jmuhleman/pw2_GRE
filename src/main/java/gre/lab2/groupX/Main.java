package gre.lab2.groupX;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraphReader;
import gre.lab2.graph.BFYResult;
import java.io.IOException;

/**
 * Main class to run the BellmanFordYensAlgorithm algorithm
 * It performs the Bellman-Ford-Yens algorithm to find the shortest path tree or a negative cycle
 * in a weighted directed graph.
 * @author Julien Muhlemann, Julien Holzer
 * @version 1.0
 */
public final class Main {
    public static void main(String[] args) throws IOException {


        //read the files
        final String[] FILES = new String[]{"data/test_5_4.txt", "data/test.txt", "data/reseau1.txt", "data/reseau2.txt", "data/reseau3.txt", "data/reseau4.txt"};

        for (var file : FILES) {
            System.out.println("\n\n>> Calculating the shortest path of the graph in " + file + ":");
            // Read the given graph file, parse it and return a DirectedGraph structure
            WeightedDigraph graph = WeightedDigraphReader.fromFile(file);

            BellmanFordYensAlgorithm bfy = new BellmanFordYensAlgorithm();
            // Apply the BellmanFordYensAlgorithm algorithm from the vertex 0
            BFYResult result = bfy.compute(graph, 0);


            if (result instanceof BFYResult.ShortestPathTree) {
                System.out.println("Shortest path tree:");
                BFYResult.ShortestPathTree spt = (BFYResult.ShortestPathTree) result;
                if (spt.predecessors().length < 25) {
                    for (int i = 0; i < spt.distances().length; i++) {
                        System.out.println("Vertex " + i + ": " + spt.distances()[i] + " (predecessor: " + spt.predecessors()[i] + ")");
                    }
                } else {
                    System.out.println("Too many vertices to display I.E. > 25 vertices)");
                }
            } else if (result instanceof BFYResult.NegativeCycle) {
                System.out.println("Negative cycle found:");
                BFYResult.NegativeCycle nc = (BFYResult.NegativeCycle) result;
                System.out.println("Cycle: " + nc.vertices());
            }


        }

        // TODO
        //  - Renommage du package ;
        //  - Écrire le code dans le package de votre groupe et UNIQUEMENT celui-ci ;
        //  - Documentation soignée comprenant :
        //    - la javadoc, avec auteurs et description des implémentations ;
        //    - des commentaires sur les différentes parties de vos algorithmes.
    }
}
