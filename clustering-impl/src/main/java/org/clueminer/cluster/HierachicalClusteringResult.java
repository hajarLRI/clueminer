package org.clueminer.cluster;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.CutoffStrategy;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.clustering.api.Merge;
import org.clueminer.dataset.api.Attribute;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.hclust.NaiveCutoff;
import org.clueminer.hclust.TreeDataImpl;
import org.clueminer.math.Matrix;

/**
 *
 * @author Tomas Barton
 */
public class HierachicalClusteringResult implements HierarchicalResult {

    private static final long serialVersionUID = 2779535800981843584L;
    private Matrix proximity;
    private Matrix similarity;
    private TreeDataImpl treeData;
    private Map<String, Map<Integer, Double>> scores = new HashMap<String, Map<Integer, Double>>();
    private CutoffStrategy cutoffStrategy = new NaiveCutoff();
    private int[] itemsMapping;    
    private Matrix inputData;
    private Clustering clustering = null;
    /**
     * original dataset
     */
    private Dataset<? extends Instance> dataset;

    public HierachicalClusteringResult(Dataset<? extends Instance> dataset) {
        this.dataset = dataset;
    }

    @Override
    public Matrix getProximityMatrix() {
        return proximity;
    }

    @Override
    public void setProximityMatrix(Matrix m) {
        this.proximity = m;
    }

    public TreeDataImpl getTreeData() {
        return treeData;
    }

    public void setTreeData(TreeDataImpl tree) {
        this.treeData = tree;
        createDefaultMapping();
    }

    @Override
    public Matrix getSimilarityMatrix() {
        return similarity;
    }

    @Override
    public void setSimilarityMatrix(Matrix m) {
        this.similarity = m;
    }

    public void createDefaultMapping() {
        itemsMapping = treeData.createTreeOrder();
    }

    @Override
    public int[] getMapping() {
        return itemsMapping;
    }

    @Override
    public int getNumClusters() {
        return treeData.getNumberOfClusters();
    }

    @Override
    public Clustering getClustering() {
        if (clustering == null) {
            updateClustering();
        }
        return clustering;
    }

    public void updateClustering() {
        clustering = getClustering(dataset);
        /**
         * TODO: fire result?
         */
    }

    /**
     * @TODO adjust also for columns clustering
     *
     * @param parent
     *
     * @return
     */
    @Override
    public Clustering getClustering(Dataset<? extends Instance> parent) {
        setDataset(parent);
        if (itemsMapping == null) {
            createDefaultMapping();
        }

        //we need number of instances in dataset
        int[] clusters = treeData.getClusters(parent.size());
        Clustering result = new ClusterList(treeData.getNumberOfClusters());
        if (treeData.getNumberOfClusters() <= 0) {
            return result;
        }
        //estimated capacity
        int perCluster = (int) (parent.size() / (float) treeData.getNumberOfClusters());
        int num, idx;
        Cluster<Instance> clust;
        //Dump.array(clusters, "clusters-assignment");
        //Dump.array(itemsMapping, "items-mapping");
        int cnt = 1;
        for (int i = 0; i < clusters.length; i++) {
            num = clusters[i] - 1; //numbering starts from 1
            if (!result.hasAt(num)) {
                clust = new BaseCluster<Instance>(perCluster);
                clust.setName("Cluster " + (cnt++));
                clust.setParent(parent);

                Attribute[] attr = parent.copyAttributes();
                for (int j = 0; j < attr.length; j++) {
                    clust.setAttribute(j, attr[j]);
                }
                //result.put(num, clust);
                result.put(clust);
            } else {
                clust = result.get(num);
            }
            idx = itemsMapping[i];
            //mapping is tracked in cluster
            clust.add(parent.instance(idx), idx);
        }

        return result;
    }

    @Override
    public Map<Integer, Double> getScores(String evaluator) {
        if (scores.containsKey(evaluator)) {
            return scores.get(evaluator);
        }
        return null;
    }

    @Override
    public double getScore(String evaluator, int clustNum) {
        return this.scores.get(evaluator).get(clustNum);
    }

    @Override
    public void setScores(String evaluator, int clustNum, double sc) {
        if (this.scores.containsKey(evaluator)) {
            this.scores.get(evaluator).put(clustNum, sc);
        } else {
            Map<Integer, Double> hm = new HashMap<Integer, Double>();
            hm.put(clustNum, sc);
            this.scores.put(evaluator, hm);
        }
    }

    @Override
    public boolean isScoreCached(String evaluator, int clustNum) {
        if (this.scores.containsKey(evaluator)) {
            if (this.scores.get(evaluator).containsKey(clustNum)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void setCutoff(double cutoff) {
        treeData.setCutoff(cutoff);
        //maximum number of clusters is number of instances
        treeData.formClusters(dataset.size());
        updateClustering();
    }

    @Override
    public double getCutoff() {
        return treeData.getCutoff();
    }

    @Override
    public double findCutoff() {
        double cut = cutoffStrategy.findCutoff(this);
        setCutoff(cut);
        System.out.println(treeData.toString());
        return cut;
    }

    @Override
    public double findCutoff(CutoffStrategy strategy) {
        double cut = strategy.findCutoff(this);
        setCutoff(cut);
        return cut;
    }

    @Override
    public double cutTreeByLevel(int level) {
        double cut = treeData.treeCutByLevel(level);
        setCutoff(cut);
        return cut;
    }

    @Override
    public Dataset<? extends Instance> getDataset() {
        return dataset;
    }

    public void setDataset(Dataset<? extends Instance> dataset) {
        this.dataset = dataset;
    }

    @Override
    public int treeLevels() {
        return treeData.treeLevels();
    }

    @Override
    public double treeHeightAt(int idx) {
        return treeData.getHeight(idx);
    }

    @Override
    public int treeOrder(int idx) {
        return treeData.getOrder(idx);
    }

    @Override
    public int[] getClusters(int terminalsNum) {
        return treeData.getClusters(terminalsNum);
    }

    @Override
    public double getMaxTreeHeight() {
        return treeData.getMaxHeight();
    }

    @Override
    public int getMappedIndex(int idx) {
        return itemsMapping[idx];
    }

    @Override
    public void setMappedIndex(int pos, int idx) {
        itemsMapping[pos] = idx;
    }

    /**
     * Mapping between dendrogram order and input matrix
     *
     * @param mapping
     */
    @Override
    public void setMapping(int[] mapping) {
        this.itemsMapping = mapping;
    }

    @Override
    public void setNumClusters(int num) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Matrix getInputData() {
        return inputData;
    }

    @Override
    public void setInputData(Matrix inputData) {
        this.inputData = inputData;
    }

    @Override
    public List<Merge> getMerges() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setMerges(List<Merge> merges) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Return Instance at given position in dendrogram
     *
     * @param index either row or column index in dendrogram
     * @return
     */
    @Override
    public Instance getInstance(int index) {
        return dataset.get(this.getMappedIndex(index));
    }

    @Override
    public int assignedCluster(int idx) {
        if (clustering == null) {
            return 0;
        }
        int assig = clustering.assignedCluster(idx);
        if (assig != -1) {
            return assig;
        }
        //this shouldn't happen :)
        return 0;
    }

    public CutoffStrategy getCutoffStrategy() {
        return cutoffStrategy;
    }

    /**
     *  Strategy for cutting dendrogram tree
     *
     *  @param cutoffStrategy
     */
    public void setCutoffStrategy(CutoffStrategy cutoffStrategy) {
        this.cutoffStrategy = cutoffStrategy;
    }

}
