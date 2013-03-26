package org.clueminer.xcalibour.files;

import java.util.Iterator;
import org.clueminer.dataset.api.AbstractTimeInstance;
import org.clueminer.dataset.api.ContinuousInstance;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.Plotter;
import org.clueminer.interpolation.InterpolationSearch;
import org.clueminer.math.Interpolator;
import org.clueminer.math.Numeric;

/**
 *
 * @author Tomas Barton
 */
public class MassSpectrum<E extends MassItem> extends AbstractTimeInstance<E> implements Instance, ContinuousInstance, Iterable<E> {

    private static final long serialVersionUID = 974569129848252471L;
    protected MassItem[] data;
    private int count = 0;
    private Interpolator interpolator;

    public MassSpectrum(int capacity) {
        data = new MassItem[capacity];
    }

    @Override
    public E get(int index) {
        return (E) data[index];
    }

    @Override
    public String getFullName() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void put(MassItem item) {
        checkMinMax(item.getIntensity());
        data[count++] = item;
    }

    @Override
    public int put(double value) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void remove(int i) {
        throw new UnsupportedOperationException("Currently we support only adding, removing not supported yet.");
    }

    @Override
    public double value(int index) {
        return data[index].getIntensity();
    }

    @Override
    public void put(int index, double value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setCapacity(int capacity) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getCapacity() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Plotter getPlotter() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double valueAt(double x) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public double zValueAt(double y, Numeric[] axisX) {
        int idx = InterpolationSearch.search(data, y);
        //System.out.println("getting y =" + y + " idx = " + idx + " found " + data[idx].getMass());
        /* int low, up;
         if (data[idx].getValue() > y) {
         up = idx;
         low = idx - 1;
         } else if (data[idx].getValue() < y) {
         low = idx;
         up = idx + 1;
         } else {
         //exact match
         return data[idx].getIntensity();
         }
         return interpolator.getValue(axisX, data, y, low, up);*/
        return data[idx].getIntensity();
    }

    @Override
    public double valueAt(double x, Interpolator interpolator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void crop(int begin, int size) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void normalize(int index) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ContinuousInstance copy() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Iterator<E> iterator() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int size() {
        return count;
    }

    @Override
    public String toString(String separator) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
