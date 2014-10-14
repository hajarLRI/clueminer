package org.clueminer.clustering.aggl;

import org.clueminer.clustering.api.AgglParams;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clueminer.clustering.algorithm.HClustResult;
import org.clueminer.clustering.api.AbstractClusteringAlgorithm;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterLinkage;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.clustering.api.dendrogram.DendroNode;
import org.clueminer.clustering.api.dendrogram.DendroTreeData;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.hclust.DLeaf;
import org.clueminer.hclust.DTreeNode;
import org.clueminer.hclust.DynamicTreeData;
import org.clueminer.math.Matrix;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 * Hierarchical agglomerative clustering
 *
 * @see
 * http://nlp.stanford.edu/IR-book/html/htmledition/time-complexity-of-hac-1.html
 * @author Tomas Barton
 */
@ServiceProvider(service = ClusteringAlgorithm.class)
public class HAC extends AbstractClusteringAlgorithm implements AgglomerativeClustering {

    private final static String name = "HAC";
    private DendroNode nodes[];
    private Dataset<? extends Instance> dataset;
    private ClusterLinkage linkage;
    private AgglParams params;
    private static final Logger logger = Logger.getLogger(HAC.class.getName());

    /**
     * number of items to cluster
     */
    private int n;

    @Override
    public String getName() {
        return name;
    }

    @Override
    public HierarchicalResult hierarchy(Dataset<? extends Instance> dataset, Props pref) {
        this.dataset = dataset;
        HierarchicalResult result = new HClustResult(dataset);
        params = new AgglParams(pref);
        Matrix similarityMatrix;
        distanceMeasure = params.getDistanceMeasure();
        linkage = params.getLinkage();
        if (params.clusterRows()) {
            n = dataset.size();
        } else {
            //columns clustering
            n = dataset.attributeCount();
        }
        logger.log(Level.INFO, "clustering rows: {0} size: {1}", new Object[]{params.clusterRows(), n});
        int items = triangleSize(n);
        //TODO: we might track clustering by estimated time (instead of counters)
        PriorityQueue<Element> pq = new PriorityQueue<>(items);

        Matrix input = dataset.asMatrix();
        if (params.clusterRows()) {
            similarityMatrix = AgglClustering.rowSimilarityMatrix(input, distanceMeasure, pq);
        } else {
            similarityMatrix = AgglClustering.columnSimilarityMatrix(input, distanceMeasure, pq);
        }
        result.setProximityMatrix(similarityMatrix);

        DendroTreeData treeData = computeLinkage(pq, similarityMatrix);
        treeData.createMapping(n, treeData.getRoot());
        result.setTreeData(treeData);
        return result;
    }

    /**
     * Find most closest items and merges them into one cluster (subtree)
     *
     * @param pq
     * @param similarityMatrix
     * @return
     */
    private DendroTreeData computeLinkage(PriorityQueue<Element> pq, Matrix similarityMatrix) {
        //each instance will form a cluster
        Map<Integer, Set<Integer>> assignments = initialAssignment(n);

        Element curr;
        HashSet<Integer> blacklist = new HashSet<>();
        DendroNode node = null;
        Set<Integer> left, right;
        int nodeId = n;
        /**
         * queue of distances, each time join 2 items together, we should remove
         * (n-1) items from queue (but removing is too expensive)
         */
        while (!pq.isEmpty() && assignments.size() > 1) {
            curr = pq.poll();
            //System.out.println(curr.toString() + " remain: " + pq.size());
            if (!blacklist.contains(curr.getRow()) && !blacklist.contains(curr.getColumn())) {
                node = getOrCreate(nodeId++);
                node.setLeft(nodes[curr.getRow()]);
                node.setRight(nodes[curr.getColumn()]);
                node.setHeight(curr.getValue());

                //System.out.println("node " + node.getId() + " left: " + node.getLeft() + " right: " + node.getRight());
                blacklist.add(curr.getRow());
                blacklist.add(curr.getColumn());

                //remove old clusters
                left = assignments.remove(curr.getRow());
                right = assignments.remove(curr.getColumn());
                //merge together and add as a new cluster
                left.addAll(right);
                updateDistances(node.getId(), left, similarityMatrix, assignments, pq);
                //when assignment have size == 1, all clusters are merged into one
            }
        }

        //last node is the root
        DendroTreeData treeData = new DynamicTreeData(node);
        return treeData;
    }

    /**
     * Compute size of triangular matrix (n x n) minus diagonal
     *
     * @param n
     * @return
     */
    protected int triangleSize(int n) {
        return ((n - 1) * n) >>> 1;
    }

    private DendroNode getOrCreate(int id) {
        if (nodes[id] == null) {
            DendroNode node = new DTreeNode(id);
            nodes[id] = node;
        }
        return nodes[id];
    }

    private void updateDistances(int mergedId, Set<Integer> mergedCluster, Matrix similarityMatrix, Map<Integer, Set<Integer>> assignments, PriorityQueue<Element> pq) {
        Element current;
        double distance;
        for (Map.Entry<Integer, Set<Integer>> cluster : assignments.entrySet()) {
            distance = linkage.similarity(similarityMatrix, cluster.getValue(), mergedCluster);
            current = new Element(distance, mergedId, cluster.getKey());
            pq.add(current);
        }
        //finaly add merged cluster
        assignments.put(mergedId, mergedCluster);
    }

    /**
     * Each data point forms an individual cluster
     *
     * @param n the number of data points
     * @return
     */
    protected Map<Integer, Set<Integer>> initialAssignment(int n) {
        //binary tree, we know how many nodes we have
        nodes = new DendroNode[(2 * n - 1)];
        Map<Integer, Set<Integer>> clusterAssignment = new HashMap<Integer, Set<Integer>>(n);
        for (int i = 0; i < n; i++) {
            HashSet<Integer> cluster = new HashSet<Integer>();
            cluster.add(i);
            clusterAssignment.put(i, cluster);
            //each cluster is also a dendrogram leaf
            if (params.clusterRows()) {
                nodes[i] = new DLeaf(i, dataset.get(i));
            } else {
                nodes[i] = new DLeaf(i, dataset.getAttribute(i));
            }

        }
        return clusterAssignment;
    }

    @Override
    public Clustering<Cluster> cluster(Dataset<? extends Instance> dataset) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Clustering<Cluster> cluster(Dataset<? extends Instance> dataset, Props props) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
