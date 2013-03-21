package org.clueminer.xcalibour.files;

import java.io.IOException;
import org.clueminer.fixtures.XCalibourFixture;
import org.clueminer.utils.progress.ProgressTicket;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.util.Exceptions;

/**
 *
 * @author deric
 */
public class XCalibourImporterTest {

    public XCalibourImporterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
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
     * Test of cancel method, of class XCalibourImporter.
     */
    @Test
    public void testCancel() {

    }

    /**
     * Test of setProgressTicket method, of class XCalibourImporter.
     */
    @Test
    public void testSetProgressTicket() {

    }

    /**
     * Test of run method, of class XCalibourImporter.
     */
    @Test
    public void testRun() {
        System.out.println("run");

        XCalibourFixture tf = new XCalibourFixture();
        ProgressHandle ph = ProgressHandleFactory.createHandle("Importing dataset");
        XCalibourImporter importer = null;
        try {
            importer = new XCalibourImporter(tf.testData());
            importer.setProgressHandle(ph);
            importer.run();
            
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}