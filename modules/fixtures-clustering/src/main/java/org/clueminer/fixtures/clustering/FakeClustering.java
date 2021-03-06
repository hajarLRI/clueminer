package org.clueminer.fixtures.clustering;

import org.clueminer.attributes.BasicAttrType;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.struct.BaseCluster;
import org.clueminer.clustering.struct.ClusterList;
import org.clueminer.colors.RandomColorsGenerator;
import org.clueminer.dataset.api.ColorGenerator;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.plugin.SampleDataset;

/**
 *
 * @author Tomas Barton
 */
public class FakeClustering {

    private static Clustering<Cluster> irisClusters;
    private static Clustering<Cluster> irisWrong;
    private static Clustering<Cluster> irisWrong1;
    private static Clustering<Cluster> irisWrong2;
    private static Clustering<Cluster> irisWrong4;
    private static Clustering<Cluster> irisWrong5;
    private static Clustering<Cluster> simpleClustering;
    private static Clustering<Cluster> simpleResponse;
    private static Dataset<Instance> wine;

    public static Clustering iris() {
        if (irisClusters == null) {
            ColorGenerator cg = new RandomColorsGenerator();
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            /**
             * fictive clustering, create iris cluster based on class labels
             * (the dataset is sorted)
             */
            irisClusters = new ClusterList(3);
            Cluster a = new BaseCluster(50);
            a.setColor(cg.next());
            a.setName("cluster 1");
            a.setAttributes(irisData.getAttributes());
            Cluster b = new BaseCluster(50);
            b.setName("cluster 2");
            b.setAttributes(irisData.getAttributes());
            b.setColor(cg.next());
            Cluster c = new BaseCluster(50);
            c.setName("cluster 3");
            c.setColor(cg.next());
            c.setAttributes(irisData.getAttributes());
            for (int i = 0; i < 50; i++) {
                a.add(irisData.instance(i));
                b.add(irisData.instance(i + 50));
                c.add(irisData.instance(i + 100));
            }

            irisClusters.add(a);
            irisClusters.add(b);
            irisClusters.add(c);
            //add dataset to lookup
            irisClusters.lookupAdd(irisData);
        }
        return irisClusters;
    }

    public static Clustering irisWrong() {
        if (irisWrong == null) {
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            irisWrong = new ClusterList(3);
            Cluster a = new BaseCluster(50);
            a.setName("cluster 1");
            a.setAttributes(irisData.getAttributes());
            //add few instances to first cluster
            a.add(irisData.instance(0));
            a.add(irisData.instance(1));
            a.add(irisData.instance(2));
            a.add(irisData.instance(149));

            Cluster b = new BaseCluster(50);
            b.setName("cluster 2");
            b.setAttributes(irisData.getAttributes());
            b.add(irisData.instance(3));
            b.add(irisData.instance(4));
            b.add(irisData.instance(5));
            b.add(irisData.instance(6));
            Cluster c = new BaseCluster(50);
            c.setName("cluster 3");
            c.setAttributes(irisData.getAttributes());
            //rest goes to the last cluster
            for (int i = 7; i < 149; i++) {
                c.add(irisData.instance(i));
            }

            irisWrong.add(a);
            irisWrong.add(b);
            irisWrong.add(c);
            //add dataset to lookup
            irisWrong.lookupAdd(irisData);
        }
        return irisWrong;
    }

    /**
     * Pretty bad clustering result, one class contained in two clusters
     *
     * @return
     */
    public static Clustering irisWrong2() {
        if (irisWrong2 == null) {
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            irisWrong2 = new ClusterList(3);
            Cluster a = new BaseCluster(50);
            a.setName("cluster 1"); // Iris-setosa
            //will contain 30 elements of first class (Iris-setosa)
            a.setAttributes(irisData.getAttributes());
            for (int i = 0; i < 30; i++) {
                a.add(irisData.instance(i));
            }

            Cluster b = new BaseCluster(50);
            b.setName("cluster 2");
            //will contain 20 elements of Iris-setosa
            b.setAttributes(irisData.getAttributes());
            for (int i = 30; i < 50; i++) {
                b.add(irisData.instance(i));
            }
            Cluster c = new BaseCluster(50);
            c.setName("cluster 3");
            c.setAttributes(irisData.getAttributes());
            //the rest (100) goes to the last cluster
            for (int i = 50; i < 150; i++) {
                c.add(irisData.instance(i));
            }

            irisWrong2.add(a);
            irisWrong2.add(b);
            irisWrong2.add(c);
            //add dataset to lookup
            irisWrong2.lookupAdd(irisData);
        }
        return irisWrong2;
    }

    public static Clustering irisWrong4() {
        if (irisWrong4 == null) {
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            irisWrong4 = new ClusterList(4);
            Cluster a = new BaseCluster(50);
            a.setName("cluster 1");
            //will contain 30 elements of first class
            a.setAttributes(irisData.getAttributes());
            for (int i = 0; i < 30; i++) {
                a.add(irisData.instance(i));
            }

            Cluster b = new BaseCluster(50);
            b.setName("cluster 2");
            //will contain 20 elements of first class
            b.setAttributes(irisData.getAttributes());
            for (int i = 30; i < 50; i++) {
                b.add(irisData.instance(i));
            }
            Cluster c = new BaseCluster(50);
            c.setName("cluster 3");
            c.setAttributes(irisData.getAttributes());
            //the rest (100) goes to the last cluster
            for (int i = 50; i < 100; i++) {
                c.add(irisData.instance(i));
            }

            Cluster d = new BaseCluster(50);
            d.setName("cluster 4");
            d.setAttributes(irisData.getAttributes());
            //the rest (100) goes to the last cluster
            for (int i = 100; i < 150; i++) {
                d.add(irisData.instance(i));
            }

            irisWrong4.add(a);
            irisWrong4.add(b);
            irisWrong4.add(c);
            irisWrong4.add(d);
            //add dataset to lookup
            irisWrong4.lookupAdd(irisData);
        }
        return irisWrong4;
    }

    public static Clustering irisWrong5() {
        if (irisWrong5 == null) {
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            irisWrong5 = new ClusterList(5);
            Cluster a = new BaseCluster(49);
            a.setName("cluster 1");
            a.setAttributes(irisData.getAttributes());
            for (int i = 0; i < 48; i++) {
                a.add(irisData.instance(i));
            }

            Cluster b = new BaseCluster(1);
            b.setName("cluster 2");
            b.setAttributes(irisData.getAttributes());
            b.add(irisData.instance(48));

            Cluster c = new BaseCluster(97);
            c.setName("cluster 3");
            c.setAttributes(irisData.getAttributes());
            for (int i = 49; i < 147; i++) {
                c.add(irisData.instance(i));
            }

            Cluster d = new BaseCluster(2);
            d.setName("cluster 4");
            d.setAttributes(irisData.getAttributes());
            for (int i = 147; i < 149; i++) {
                d.add(irisData.instance(i));
            }

            Cluster e = new BaseCluster(1);
            e.setName("cluster 5");
            e.setAttributes(irisData.getAttributes());
            e.add(irisData.instance(149));

            irisWrong5.add(a);
            irisWrong5.add(b);
            irisWrong5.add(c);
            irisWrong5.add(d);
            irisWrong5.add(e);
            //add dataset to lookup
            irisWrong5.lookupAdd(irisData);
        }
        return irisWrong5;
    }

    /**
     * Very bad clustering result, one item in singleton cluster, rest together
     *
     * @return
     */
    public static Clustering irisMostlyWrong() {
        if (irisWrong1 == null) {
            Dataset<? extends Instance> irisData = FakeDatasets.irisDataset();
            irisWrong1 = new ClusterList(2);
            Cluster a = new BaseCluster(1);
            a.setName("cluster 1"); // Iris-setosa
            //will contain single setosa item
            a.setAttributes(irisData.getAttributes());
            a.add(irisData.instance(0));

            Cluster b = new BaseCluster(149);
            b.setName("cluster 2");
            //will all the remaining items
            b.setAttributes(irisData.getAttributes());
            for (int i = 1; i < 150; i++) {
                b.add(irisData.instance(i));
            }

            irisWrong1.add(a);
            irisWrong1.add(b);
            //add dataset to lookup
            irisWrong1.lookupAdd(irisData);
        }
        return irisWrong1;
    }

    public static Dataset<Instance> wine() {
        if (wine == null) {
            wine = new SampleDataset(27);
            wine.attributeBuilder().create("x", BasicAttrType.INTEGER);

            String klass = "cabernet";
            for (int i = 0; i < 13; i++) {
                wine.builder().create(new double[]{i}, klass);
            }

            String klass2 = "syrah";
            for (int i = 0; i < 9; i++) {
                wine.builder().create(new double[]{i * 3 + 13}, klass2);
            }

            String klass3 = "pinot";
            for (int i = 0; i < 5; i++) {
                wine.builder().create(new double[]{i * 4 + 50}, klass3);
            }
        }

        return wine;
    }

    public static Clustering wineCorrect() {
        if (simpleClustering == null) {

            simpleClustering = new ClusterList(3);
            Cluster a = new BaseCluster(12);
            a.setName("cabernet");
            a.attributeBuilder().create("x", BasicAttrType.INTEGER);
            Cluster b = new BaseCluster(9);
            b.setName("syrah");
            b.attributeBuilder().create("x", BasicAttrType.INTEGER);
            Cluster c = new BaseCluster(6);
            c.setName("pinot");
            c.attributeBuilder().create("x", BasicAttrType.INTEGER);

            Dataset<Instance> data = wine();
            for (int i = 0; i < 13; i++) {
                a.add(data.instance(i));
            }

            for (int i = 13; i < 22; i++) {
                b.add(data.instance(i));
            }

            for (int i = 22; i < 27; i++) {
                c.add(data.instance(i));
            }

            simpleClustering.add(a);
            simpleClustering.add(b);
            simpleClustering.add(c);
        }

        return simpleClustering;
    }

    /**
     * @see
     * http://alias-i.com/lingpipe/docs/api/com/aliasi/classify/PrecisionRecallEvaluation.html
     * @return
     */
    public static Clustering wineClustering() {

        if (simpleResponse == null) {
            simpleResponse = new ClusterList(3);
            Cluster a = new BaseCluster(13);
            a.setName("cluster A");
            a.attributeBuilder().create("x", BasicAttrType.INTEGER);
            Cluster b = new BaseCluster(9);
            b.setName("cluster B");
            b.attributeBuilder().create("x", BasicAttrType.INTEGER);

            Cluster c = new BaseCluster(5);
            c.setName("cluster C");
            c.attributeBuilder().create("x", BasicAttrType.INTEGER);

            Dataset<Instance> data = wine();
            System.out.println("dataset size " + data.size());
            // cabernet 9x -> a
            for (int i = 0; i < 9; i++) {
                a.add(data.instance(i));
            }

            // cabernet 2x => b
            b.add(data.instance(9));
            // cabernet 1x => c
            c.add(data.instance(10));
            b.add(data.instance(11));
            b.add(data.instance(12));

            // syrah 2x -> a
            for (int i = 13; i < 15; i++) {
                a.add(data.instance(i));
            }

            // syrah 2x -> c
            c.add(data.instance(15));

            // syrah 5x -> b
            for (int i = 16; i < 21; i++) {
                b.add(data.instance(i));
            }
            a.add(data.instance(21));
            // pinot 4x -> c
            for (int i = 22; i < 26; i++) {
                c.add(data.instance(i));
            }

            // pinot -> cabernet cluster
            b.add(data.instance(26));

            simpleResponse.add(a);
            simpleResponse.add(b);
            simpleResponse.add(c);
            //add dataset to lookup
            simpleResponse.lookupAdd(data);
        }

        return simpleResponse;
    }

}
