package org.clueminer.chameleon;

import java.util.LinkedList;
import org.clueminer.graph.api.Graph;
import org.clueminer.graph.api.Node;
import org.clueminer.partitioning.api.Bisection;

/**
 *
 * @author Tomas Bruna
 */
public class ImprovedSimilarity extends PairMerger {

    public ImprovedSimilarity(Graph g, Bisection bisection, double closenessPriority, SimilarityMeasure similarityMeasure) {
        super(g, bisection, closenessPriority, similarityMeasure);
    }

    @Override
    protected void createNewCluster(int clusterIndex1, int clusterIndex2) {
        Cluster cluster1 = clusters.get(clusterIndex1);
        Cluster cluster2 = clusters.get(clusterIndex2);
        LinkedList<Node> clusterNodes = cluster1.getNodes();
        clusterNodes.addAll(cluster2.getNodes());
        // addIntoTree(clusterIndex1, clusterIndex2);
        int index1 = max(clusterIndex2, clusterIndex1);
        int index2 = min(clusterIndex2, clusterIndex1);
        double edgeCountSum = cluster1.getEdgeCount() + cluster2.getEdgeCount() + clusterMatrix.get(index1).get(index2).counter;

        double newACL = cluster1.getACL() * (cluster1.getEdgeCount() / edgeCountSum)
                + cluster2.getACL() * (cluster2.getEdgeCount() / edgeCountSum)
                + clusterMatrix.get(index1).get(index2).ECL * (clusterMatrix.get(index1).get(index2).counter / edgeCountSum);

        Cluster newCluster = new Cluster(clusterNodes, graph, idCounter++, bisection);
        newCluster.setACL(newACL);
        newCluster.setEdgeCount((int) edgeCountSum);
        clusters.set(clusterIndex1, newCluster);
        clusters.remove(clusterIndex2);
    }

    @Override
    protected double computeSimilarity(int i, int j) {
        if (j > i) {
            int temp = i;
            i = j;
            j = temp;
        }
        double ec1 = clusters.get(i).getEdgeCount();
        double ec2 = clusters.get(j).getEdgeCount();

        if (ec1 == 0 || ec2 == 0) {
            return clusterMatrix.get(i).get(j).ECL * 40;
        }

        double val = (clusterMatrix.get(i).get(j).counter / (min(ec1, ec2)))
                * Math.pow((clusterMatrix.get(i).get(j).ECL / ((clusters.get(i).getACL() * ec1) / (ec1 + ec2) + (clusters.get(j).getACL() * ec2) / (ec1 + ec2))), closenessPriority)
                * Math.pow((min(clusters.get(i).getACL(), clusters.get(j).getACL()) / max(clusters.get(i).getACL(), clusters.get(j).getACL())), 1);

        return val;
    }
}
