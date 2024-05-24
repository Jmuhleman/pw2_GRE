package gre.lab2.groupJ;
import gre.lab2.graph.WeightedDigraph;
import gre.lab2.graph.WeightedDigraphReader;
import gre.lab2.graph.BFYResult;
import java.io.IOException;

/**
 * Main class to run the BellmanFordYensAlgorithm algorithm
 * It performs the Bellman-Ford-Yens algorithm to find the shortest path tree or a negative cycle
 * in a weighted directed graph.
 * @author Julien Muhlemann, Julien Holzer
 */
public final class Main {
    public static void main(String[] args) throws IOException {

        // list of files to test the algorithm on (from the lab's instructions and from the exercise)
        final String[] FILES = new String[]{"data/ex5_2.txt","data/ex5_3.txt", "data/reseau1.txt", "data/reseau2.txt", "data/reseau3.txt", "data/reseau4.txt"};

        // create an instance of the BellmanFordYensAlgorithm algorithm
        BellmanFordYensAlgorithm bfy = new BellmanFordYensAlgorithm();
        // The source vertex is 0 (according to this lab's instructions)
        final int SOURCE_VERTEX = 0;

        // for each file in the list apply the BFY algorithm
        for (var file : FILES) {
            System.out.println("\n\n>> Applying the Bellman-Ford algorithm on the graph in the file : " + file + ":");
            // read the given graph file, parse it and return a DirectedGraph structure
            WeightedDigraph graph = WeightedDigraphReader.fromFile(file);

            // apply the BellmanFordYensAlgorithm algorithm from the source vertex (vertex 0)
            BFYResult result = bfy.compute(graph, SOURCE_VERTEX);


            // if the result is not a negative cycle, print the shortest path tree
            if (!result.isNegativeCycle()) {
                System.out.println("Shortest path tree found:");
                if (graph.getNVertices() < 25) {
                    System.out.println(result);
                } else {
                    System.out.println("Too many vertices to display I.E. > 25 vertices)");
                }
                // if the result is a negative cycle, print the negative cycle
            } else {
                System.out.println("Negative cycle found:");
                System.out.println(result);
            }


        }
    }
}
