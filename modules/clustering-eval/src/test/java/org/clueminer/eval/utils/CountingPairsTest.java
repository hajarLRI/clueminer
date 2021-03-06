package org.clueminer.eval.utils;

import com.google.common.collect.Table;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import org.clueminer.clustering.algorithm.KMeans;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.PartitioningClustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.plugin.SampleDataset;
import org.clueminer.distance.EuclideanDistance;
import org.clueminer.eval.external.ExternalTest;
import org.clueminer.fixtures.CommonFixture;
import org.clueminer.fixtures.clustering.FakeClustering;
import org.clueminer.io.ARFFHandler;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deric
 */
public class CountingPairsTest extends ExternalTest {

    private static Clustering clusters;
    private static final CommonFixture tf = new CommonFixture();
    private static Clustering iris;

    public CountingPairsTest() {
    }

    @BeforeClass
    public static void setUpClass() throws FileNotFoundException, IOException {
        clusters = FakeClustering.iris();

        //now try some real clustering
        PartitioningClustering km = new KMeans(3, 100, new EuclideanDistance());
        ARFFHandler arff = new ARFFHandler();
        Dataset<Instance> irisDataset = new SampleDataset();
        arff.load(tf.irisArff(), irisDataset, 4);
        iris = km.partition(irisDataset);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of contingencyTable method, of class CountingPairs.
     */
    @Test
    public void testCountPairs() {
        //this clustering has all assignments correct
        Table<String, String, Integer> table = CountingPairs.contingencyTable(clusters);
        for (String row : table.rowKeySet()) {
            Map<String, Integer> map = table.row(row);
            for (String klass : map.keySet()) {
                int cnt = map.get(klass);
                //each class has 50 members
                assertEquals(50, cnt);
            }
        }
        System.out.println(table.toString());
    }

    /**
     * Test of findMatching method, of class CountingPairs.
     */
    @Test
    public void testFindMatching() {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(iris);
        Matching matching = CountingPairs.findMatching(table);
        //we have 3 different classes
        assertEquals(3, matching.size());

        //let's test stupid clustering
        // Dataset<Instance> irisData = FakeClustering.irisDataset();
        Clustering<Cluster> irisClusters = FakeClustering.irisWrong();

        table = CountingPairs.contingencyTable(irisClusters);
        matching = CountingPairs.findMatching(table);
        //we have 3 different classes
        assertEquals(3, matching.size());
        System.out.println("table: " + table);
        System.out.println("matching: " + matching);
        //Iris-setosa will be in all 3 clusters
        //cluster 3 should be mapped to Iris-versicolor
        System.out.println("versicolor: " + matching.get("Iris-versicolor"));
        assertNotNull(matching.get("Iris-versicolor"));
    }

    @Test
    public void testEmptyClass() {
        System.out.println("===test empty class");
        Clustering<Cluster> irisClusters = FakeClustering.irisWrong2();

        Table<String, String, Integer> table = CountingPairs.contingencyTable(irisClusters);
        Matching matching = CountingPairs.findMatching(table);

        System.out.println("table: " + table);
        System.out.println("matching: " + matching);
        //only 2 classes could be paired, third one is paired in a random way
        assertEquals(3, matching.size());
        //Iris-setosa will be in all 3 clusters
        //cluster 3 should be mapped to Iris-versicolor
        System.out.println("versicolor: " + matching.get("Iris-versicolor"));

        Map<String, Integer> res;

        for (String cluster : matching.values()) {
            res = CountingPairs.countAssignments(table, matching.getByCluster(cluster), cluster);
            System.out.println("wrong table: " + res);
        }

        //assertNotNull(matching.get("Iris-versicolor"));
    }

    /**
     * Test of countAssignments method, of class CountingPairs.
     */
    @Test
    public void testCountAssignments() {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(iris);
        Matching matching = CountingPairs.findMatching(table);
        Map<String, Integer> res;

        int tp, fp, fn, tn, sum;
        for (String cluster : matching.values()) {
            //System.out.println(cluster + " corresponds to " + matching.inverse().get(cluster));
            res = CountingPairs.countAssignments(table, matching.getByCluster(cluster), cluster);
            assertEquals(4, res.size());
            tp = res.get("tp");
            fp = res.get("fp");
            fn = res.get("fn");
            tn = res.get("tn");
            System.out.println("tp = " + tp + " | fp = " + fp);
            System.out.println("fn = " + fn + " | tn = " + tn);
            sum = tp + fp + fn + tn;
            assertEquals(iris.instancesCount(), sum);
        }

    }

    @Test
    public void testCountPairs2() {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(FakeClustering.irisMostlyWrong());
        Matching matching = CountingPairs.findMatching(table);
        //just 2 clusters, so 2 classes must belong to 1 cluster
        assertEquals(3, matching.size());
    }

    @Test
    public void testCountPairs3() {
        Table<String, String, Integer> table = CountingPairs.contingencyTable(FakeClustering.irisWrong4());
        Matching matching = CountingPairs.findMatching(table);
        System.out.println("iris wrong4:");
        System.out.println("table: " + table);
        System.out.println("matching: " + matching);
        //only 2 classes could be paired, third one is paired in a random way
        assertEquals(3, matching.size());
    }

    @Test
    public void testContingencyTable_Clustering() {
    }

    @Test
    public void testContingencyTable_Clustering_Clustering() {
    }

    @Test
    public void testClusteringFromClasses() {
    }

    @Test
    public void testDumpTable() {
    }

    @Test
    public void testMatchPairs_Clustering_Clustering() {
    }

    @Test
    public void testMatchPairs_Clustering() {
        Clustering<? extends Cluster> clust = pcaData();

        assertEquals(10, clust.instancesCount());
        assertEquals(2, clust.get(0).size());
        assertEquals(3, clust.get(1).size());
        assertEquals(5, clust.get(2).size());

        PairMatch pm = CountingPairs.matchPairs(clust);
        assertEquals(7, pm.tp);
        assertEquals(6, pm.fp);
        assertEquals(7, pm.fn);
        assertEquals(25, pm.tn);
    }
}
