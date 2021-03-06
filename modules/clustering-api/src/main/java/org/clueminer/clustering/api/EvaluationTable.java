package org.clueminer.clustering.api;

import java.util.HashMap;
import java.util.Map;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.utils.Props;

/**
 * Table with evaluation results, might perform caching or other optimizations
 *
 * @author Tomas Barton
 */
public interface EvaluationTable {

    void setData(Clustering<? extends Cluster> clusters, Dataset<? extends Instance> dataset);

    /**
     * Get score for given evaluator
     *
     * @param evaluator
     * @return
     */
    double getScore(ClusterEvaluation evaluator);

    /**
     *
     * @param evaluator
     * @param params optional parameters of evaluation metric
     * @return
     */
    double getScore(ClusterEvaluation evaluator, Props params);

    /**
     * Get score for given evaluator
     *
     * @param evaluator name of the evaluator
     * @return
     */
    double getScore(String evaluator);

    /**
     *
     * @param evaluator
     * @param params optional parameters of evaluation metric
     * @return
     */
    double getScore(String evaluator, Props params);

    /**
     * Names of all available evaluators
     *
     * @return
     */
    String[] getEvaluators();

    /**
     *
     * @return map of internal evaluation scores
     */
    Map<String, Double> getInternal();

    /**
     *
     * @return map of external evaluations scores
     */
    Map<String, Double> getExternal();

    /**
     *
     * @return all currently computed scores
     */
    HashMap<String, Double> getAll();

    /**
     * Count score that have not been evaluated so far
     *
     * @return map with all scores
     */
    HashMap<String, Double> countAll();
}
