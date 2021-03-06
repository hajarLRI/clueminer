package org.clueminer.eval;

import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.InternalEvaluator;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.dataset.api.Instance;
import org.clueminer.distance.EuclideanDistance;
import org.clueminer.distance.api.DistanceMeasure;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = InternalEvaluator.class)
public class GPlus extends AbstractEvaluator {

    private static final String NAME = "G+";
    private static final long serialVersionUID = 558399535473028351L;

    public GPlus() {
        dm = EuclideanDistance.getInstance();
    }

    public GPlus(DistanceMeasure dist) {
        this.dm = dist;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public double score(Clustering<? extends Cluster> clusters, Props params) {
        double maxIntraDist = Double.MIN_VALUE;
        double sMin = 0;
        double fw = 0, fb = 0;
        double nd;

        Cluster clust;
        Instance x, y;
        // calculate max intra cluster distance
        for (int i = 0; i < clusters.size(); i++) {
            clust = clusters.get(i);
            for (int j = 0; j < clust.size(); j++) {
                x = clust.instance(j);
                for (int k = j + 1; k < clust.size(); k++) {
                    fw++;
                    y = clust.instance(k);
                    double distance = dm.measure(x, y);
                    if (maxIntraDist < distance) {
                        maxIntraDist = distance;
                    }
                }
            }
        }

        // calculate inter cluster distances
        // count sMin
        Cluster a, b;
        for (int i = 0; i < clusters.size(); i++) {
            a = clusters.get(i);
            for (int j = 0; j < a.size(); j++) {
                x = a.instance(j);
                for (int k = i + 1; k < clusters.size(); k++) {
                    b = clusters.get(k);
                    for (int l = 0; l < b.size(); l++) {
                        y = b.instance(l);
                        fb++;
                        double distance = dm.measure(x, y);
                        if (distance < maxIntraDist) {
                            sMin++;
                        }
                    }
                }
            }
        }
        nd = fw + fb;
        double gPlus = (2 * sMin) / (nd * (nd - 1));
        return gPlus;
    }

    /**
     * Should be minimized
     *
     * @param score1
     * @param score2
     * @return
     */
    @Override
    public boolean isBetter(double score1, double score2) {
        // should be minimized: range = [0,x] with x= fb/nd
        return score1 < score2;
    }

    @Override
    public boolean isMaximized() {
        return false;
    }

    @Override
    public double getMin() {
        return Double.POSITIVE_INFINITY;
    }

    @Override
    public double getMax() {
        return 0;
    }
}
