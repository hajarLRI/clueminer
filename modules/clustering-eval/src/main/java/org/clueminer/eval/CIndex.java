package org.clueminer.eval;

import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.InternalEvaluator;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.EuclideanDistance;
import org.clueminer.distance.api.DistanceMeasure;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 * The C-index was reviewed in Hubert and Levin (1976). It is computed as
 *
 * C_index = [d_w - min(d_w)] / [max(d_w) - min(d_w)],
 *
 * where d_w is the sum of the within cluster distances. The index was found to
 * exhibit excellent recovery characteristics by Milligan (1981a). The minimum
 * value across the hierarchy levels was used to indicate the optimal number of
 * clusters
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = InternalEvaluator.class)
public class CIndex extends AbstractEvaluator {

    private static final long serialVersionUID = -4725798362682980138L;
    private static String NAME = "C-index";

    public CIndex() {
        dm = EuclideanDistance.getInstance();
    }

    public CIndex(DistanceMeasure dist) {
        this.dm = dist;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double score(Clustering<? extends Cluster> clusters, Props params) {
        double dw = 0;
        double minDw = Double.MAX_VALUE, maxDw = Double.MIN_VALUE;

        Instance x, y;
        // calculate intra cluster distances and sum of all
        //for each cluster
        for (int i = 0; i < clusters.size(); i++) {
            Dataset d = clusters.get(i);
            for (int j = 0; j < d.size(); j++) {
                x = d.instance(j);
                for (int k = j + 1; k < d.size(); k++) {
                    y = d.instance(k);
                    double distance = dm.measure(x, y);
                    dw += distance;
                    if (maxDw < distance) {
                        maxDw = distance;
                    }
                    if (minDw > distance) {
                        minDw = distance;
                    }
                }

            }
        }

        // calculate C Index
        double cIndex = (dw - minDw) / (maxDw - minDw);
        return cIndex;
    }

    @Override
    public boolean isBetter(double score1, double score2) {
        // should be minimized ( smallest intra cluster distances)
        return score1 < score2;
    }

    @Override
    public boolean isMaximized() {
        return false;
    }

    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public double getMax() {
        return Double.POSITIVE_INFINITY;
    }
}
