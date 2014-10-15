package org.clueminer.dataset.std;

import org.clueminer.dataset.api.DataStandardization;
import org.clueminer.dataset.api.DataStandardizationFactory;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.std.StdNone;

/**
 *
 * @author Tomas Barton
 */
public class DataScaler {

    public static Dataset<? extends Instance> standartize(Dataset<? extends Instance> dataset, String method, boolean logScale) {
        DataStandardizationFactory sf = DataStandardizationFactory.getInstance();
        Dataset<? extends Instance> res;
        if (method.equals(StdNone.name)) {
            //nothing to optimize
            res = dataset;
        } else {
            DataStandardization std = sf.getProvider(method);
            if (std == null) {
                throw new RuntimeException("Standartization method " + std + " was not found");
            }
            res = std.optimize(dataset);
        }
        if (logScale) {
            StdMinMax scale = new StdMinMax();
            scale.setTargetMin(1);
            double max = res.max();
            double min = res.min();
            scale.setTargetMax(-min + max + 1);
            //normalize values, so that we can apply logarithm
            res = scale.optimize(res);

            for (int i = 0; i < res.size(); i++) {
                for (int j = 0; j < res.attributeCount(); j++) {
                    res.set(i, j, Math.log(res.get(i, j)));
                }
            }
        }
        return res;
    }

}
