package org.clueminer.graph.api;

import java.util.ArrayList;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;

/**
 * Factory for nodes and edges.
 * <p>
 * All new nodes and edges are created by this factory.
 * <p>
 * Both nodes and edges have unique identifiers. If not provided, a unique id
 * will be automatically assigned to the elements.
 */
public interface GraphFactory {

    /**
     * Creates and returns a directed edge between source and target.
     *
     * @param source the source node
     * @param target the target node
     * @return the new edge
     */
    public Edge newEdge(Node source, Node target);

    /**
     * Creates and returns an edge between source and target.
     *
     * @param source the source node
     * @param target the target node
     * @param directed true if directed, false if undirected
     * @return the new edge
     */
    public Edge newEdge(Node source, Node target, boolean directed);

    /**
     * Creates and returns an edge between source and target.
     *
     * @param source the source node
     * @param target the target node
     * @param type the edge type
     * @param directed true if directed, false if undirected
     * @return the new edge
     */
    public Edge newEdge(Node source, Node target, int type, boolean directed);

    /**
     * Creates and returns an edge between source and target.
     *
     * @param source the source node
     * @param target the target node
     * @param type the edge type
     * @param weight the edge weight
     * @param directed true if directed, false if undirected
     * @return the new edge
     */
    public Edge newEdge(Node source, Node target, int type, double weight, boolean directed);

    /**
     * Creates and returns an edge between source and target.
     *
     * @param id the edge id
     * @param source the source node
     * @param target the target node
     * @param type the edge type
     * @param weight the edge weight
     * @param directed true if directed, false if undirected
     * @return the new edge
     */
    public Edge newEdge(Object id, Node source, Node target, int type, double weight, boolean directed);

    /**
     * Creates and returns a node.
     *
     * @return the new node
     */
    public Node newNode();

    /**
     * Creates and returns a node.
     *
     * @param label the node label
     * @return the new node
     */
    public Node newNode(Object label);

    /**
     * Creates and returns a node.
     *
     * @param i instance which the node represents
     * @return the new node
     */
    public Node newNode(Instance i);

    /**
     * Creates nodes from the dataset
     *
     * @param input input dataset
     * @return list of nodes
     */
    public ArrayList<Node> createNodesFromInput(Dataset<? extends Instance> input);

}
