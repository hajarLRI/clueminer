package org.clueminer.io;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.clueminer.attributes.TimePointAttribute;
import org.clueminer.dataset.api.ContinuousInstance;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.plugin.ArrayDataset;
import org.clueminer.dataset.plugin.TimeseriesDataset;
import org.clueminer.fixtures.CommonFixture;
import org.clueminer.fixtures.TimeseriesFixture;
import org.clueminer.types.TimePoint;
import org.clueminer.utils.Dump;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author deric
 */
public class CsvLoaderTest {

    private static CsvLoader subject;
    private static final CommonFixture fixture = new CommonFixture();
    private static final double delta = 1e-9;

    public CsvLoaderTest() {
    }

    @Before
    public void setUp() {
        subject = new CsvLoader();
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of load method, of class CsvLoader.
     *
     * @throws java.lang.Exception
     */
    @Test
    public void testLoad_File_Dataset() throws Exception {
        File file = fixture.insectCsv();
        Dataset<Instance> dataset = new ArrayDataset<>(30, 3);
        subject.setClassIndex(3);
        subject.load(file, dataset);
        assertEquals(3, dataset.attributeCount());
        assertEquals(30, dataset.size());
        assertEquals(true, dataset.getAttribute(0).isNumerical());
        assertEquals(191, dataset.instance(0).value(0), delta);
    }

    /**
     * Test of load method, of class CsvLoader.
     */
    @Test
    public void testLoad_File() {
    }

    /**
     * Test of hasHeader method, of class CsvLoader.
     */
    @Test
    public void testHasHeader() {
    }

    /**
     * Test of setHasHeader method, of class CsvLoader.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testSetHeader() throws IOException {
        File file = fixture.irisData();
        Dataset<Instance> dataset = new ArrayDataset<>(150, 4);
        subject.setClassIndex(4);
        subject.setHasHeader(false);
        //run
        subject.load(file, dataset);
        assertEquals(4, dataset.attributeCount());
        assertEquals(150, dataset.size());
        assertEquals(true, dataset.getAttribute(0).isNumerical());
        assertEquals(5.1, dataset.instance(0).value(0), delta);
    }

    @Test
    public void testLoadingNames() throws IOException {
        File file = fixture.irisData();
        Dataset<Instance> dataset = new ArrayDataset<>(150, 4);
        subject.setClassIndex(4);
        subject.setHasHeader(false);
        subject.addNameAttr(4);
        //run
        subject.load(file, dataset);
        assertEquals("Iris-setosa", dataset.instance(0).getName());
        assertEquals("Iris-virginica", dataset.instance(149).getName());
    }

    @Test
    public void testDatasetWithHeader() throws IOException {
        File file = fixture.legendreCsv();
        char separator = ',';
        Dataset<Instance> dataset = new ArrayDataset<>(39, 21);
        CsvLoader loader = new CsvLoader();
        ArrayList<Integer> skip = new ArrayList<>();
        //skip.add(0); //first one is ID
        //loader.addNameAttr(0);
        for (int i = 0; i < 7; i++) {
            loader.addNameAttr(i); //meta attributes
            skip.add(i + 1);
        }

        loader.setClassIndex(0);
        loader.setNameJoinChar(", ");

        loader.setSkipIndex(skip);
        loader.setSeparator(separator);
        //loader.setClassIndex(0);
        loader.setHasHeader(true);
        Dataset<Instance> d = (Dataset<Instance>) dataset;
        loader.setDataset(d);
        loader.load(file);
    }

    /**
     * Test of getSeparator method, of class CsvLoader.
     */
    @Test
    public void testGetSeparator() {
    }

    /**
     * Test of setSeparator method, of class CsvLoader.
     */
    @Test
    public void testSetSeparator() {
    }

    /**
     * Test of getClassIndex method, of class CsvLoader.
     */
    @Test
    public void testGetClassIndex() {
    }

    /**
     * Test of setClassIndex method, of class CsvLoader.
     */
    @Test
    public void testSetClassIndex() {
    }

    /**
     * Test of getSkipIndex method, of class CsvLoader.
     */
    @Test
    public void testGetSkipIndex() {
    }

    /**
     * Test of setSkipIndex method, of class CsvLoader.
     */
    @Test
    public void testSetSkipIndex() {
    }

    /**
     * Test of getDataset method, of class CsvLoader.
     */
    @Test
    public void testGetDataset() {
    }

    /**
     * Test of setDataset method, of class CsvLoader.
     */
    @Test
    public void testSetDataset() {
    }

    /**
     * Test of skip method, of class CsvLoader.
     */
    @Test
    public void testSkip() {
    }

    /**
     * Test of isSkipHeader method, of class CsvLoader.
     */
    @Test
    public void testIsSkipHeader() {
    }

    /**
     * Test of setSkipHeader method, of class CsvLoader.
     */
    @Test
    public void testSetSkipHeader() {
    }

    @Test
    public void testMeta() throws IOException {
        File file = fixture.schoolData();
        Dataset<Instance> dataset = new ArrayDataset<>(17, 5);
        subject.setHasHeader(true);
        subject.addNameAttr(4);
        subject.addMetaAttr(3);
        subject.addMetaAttr(4);
        subject.setSeparator(' ');
        //run
        subject.load(file, dataset);
        assertEquals(17, dataset.size());
        double[] m = dataset.instance(0).getMetaNum();
        assertEquals(605.3, m[0], delta);
    }

    @Test
    public void testSimpleTimeSeries() throws IOException {
        char separator = ',';
        TimeseriesFixture tf = new TimeseriesFixture();
        File file = tf.ts01();
        Dataset<? extends Instance> dataset = new TimeseriesDataset<>(254);
        CsvLoader loader = new CsvLoader();
        ArrayList<Integer> metaAttr = new ArrayList<>();
        //ArrayList<Integer> skipAttr = new ArrayList<Integer>();
        loader.setClassIndex(0);
        for (int i = 1; i < 7; i++) {
            metaAttr.add(i);
            //  skipAttr.add(i);
        }
        for (int j = 0; j < 7; j++) {
            loader.addNameAttr(j); //meta attributes
        }
        loader.setNameJoinChar(", ");

        String[] firstLine = CsvLoader.firstLine(file, String.valueOf(separator));
        int i = 0;
        int index;
        int last = firstLine.length;
        int offset = metaAttr.size() + 1;
        TimePoint tp[] = new TimePointAttribute[last - offset];
        System.out.println("allocated size: " + tp.length);
        double pos;
        Dump.array(firstLine, "first line");
        for (String item : firstLine) {
            if (i >= offset) {
                index = i - offset;
                pos = Double.valueOf(item);
                tp[index] = new TimePointAttribute(index, index, pos);
            }
            i++;
        }
        ((TimeseriesDataset<ContinuousInstance>) dataset).setTimePoints(tp);
        loader.setMetaAttr(metaAttr);
        //loader.setSkipIndex(skipAttr);
        loader.setSeparator(separator);
        loader.setSkipHeader(true);
        Dataset<Instance> d = (Dataset<Instance>) dataset;
        loader.setDataset(d);
        loader.load(file);
    }

    @Test
    public void testQuotedCsv() throws IOException {
        char separator = ',';
        TimeseriesFixture tf = new TimeseriesFixture();
        File file = tf.quoted();
        Dataset<? extends Instance> dataset = new ArrayDataset<>(100, 22);
        CsvLoader loader = new CsvLoader();
        ArrayList<Integer> skip = new ArrayList<>();
        //skipped.add(0); //first one is ID
        for (int i = 0; i < 7; i++) {
            skip.add(i);
            loader.addNameAttr(i); //meta attributes
        }

        loader.setNameJoinChar(", ");

        loader.setSkipIndex(skip);
        loader.setSeparator(separator);
        //loader.setClassIndex(0);
        loader.setSkipHeader(false);
        loader.setDataset(dataset);
        loader.load(file);
    }

}
