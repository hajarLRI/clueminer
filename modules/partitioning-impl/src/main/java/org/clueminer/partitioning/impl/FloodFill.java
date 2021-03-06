package org.clueminer.partitioning.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import org.clueminer.graph.api.Graph;
import org.clueminer.graph.api.Node;

/**
 *
 * @author Tomas Bruna
 */
public class FloodFill {

    private Graph graph;
    private boolean marked[];
    private ArrayList<LinkedList<Node>> result;

    /**
     * Finds disconnected subgraphs in the given graph
     *
     * @param g Graph to find subgraphs in
     * @return Lists of nodes in different subgraphs
     */
    public ArrayList<LinkedList<Node>> findSubgraphs(Graph g) {
        graph = g;
        marked = new boolean[graph.getNodeCount()];
        Node nodes[] = graph.getNodes().toArray();

        for (Node node : nodes) {
            marked[graph.getIndex(node)] = false;
        }

        boolean allMarked = false;
        int clusterCounter = 0;
        result = new ArrayList<>();
        while (!allMarked) {
            allMarked = true;
            for (Node node : nodes) {
                if (marked[graph.getIndex(node)] == false) {
                    allMarked = false;
                    result.add(new LinkedList<Node>());
                    markSubgraph(node, clusterCounter++);
                }
            }
        }
        return result;
    }

    /**
     * Recursively marks all nodes in subgraph
     *
     * @param node
     * @param clusterNumber
     */
    private void markSubgraph(Node node, int clusterNumber) {
        result.get(clusterNumber).add(node);
        marked[graph.getIndex(node)] = true;
        Iterator<Node> neighbors = graph.getNeighbors(node).iterator();
        while (neighbors.hasNext()) {
            Node neighbor = neighbors.next();
            if (marked[graph.getIndex(neighbor)] == false) {
                markSubgraph(neighbor, clusterNumber);
            }
        }
    }
}
