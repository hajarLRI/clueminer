package org.clueminer.clustering.aggl.linkage;

import java.util.Set;
import org.clueminer.clustering.api.AbstractLinkage;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.ClusterLinkage;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.EuclideanDistance;
import org.clueminer.distance.api.DistanceMeasure;
import org.clueminer.math.Matrix;
import org.openide.util.lookup.ServiceProvider;

/**
 * Group Averaged Agglomerative Clustering (GAAC)
 *
 * Clusters will be compared using the similarity of the computed mean data
 * point (or <i>centroid</i>) for each cluster. This comparison method is also
 * known as UPGMA or Mean Linkage.
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = ClusterLinkage.class)
public class AverageLinkage extends AbstractLinkage implements ClusterLinkage {

    private static final long serialVersionUID = 1357290267936276833L;
    public static String name = "Average Linkage";

    public AverageLinkage() {
        super(EuclideanDistance.getInstance());
    }

    public AverageLinkage(DistanceMeasure dm) {
        super(dm);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double distance(Cluster<? extends Instance> cluster1, Cluster<? extends Instance> cluster2) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double similarity(Matrix similarityMatrix, Set<Integer> cluster, Set<Integer> toAdd) {
        double similaritySum = 0;
        for (int i : cluster) {
            for (int j : toAdd) {
                similaritySum += similarityMatrix.get(i, j);
            }
        }
        return similaritySum / (cluster.size() * toAdd.size());
    }

    @Override
    public double alphaA(int ma, int mb, int mq) {
        return (double) ma / (ma + mb);
    }

    @Override
    public double alphaB(int ma, int mb, int mq) {
        return (double) mb / (ma + mb);
    }

    @Override
    public double beta(int ma, int mb, int mq) {
        return 0;
    }

    @Override
    public double gamma() {
        return 0;
    }
}
