package org.clueminer.dataset;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.clueminer.instance.Instance;

/**
 *
 * @author Tomas Barton
 */
public abstract class AbstractAttribute implements Attribute {

    private static final long serialVersionUID = -210441685616484746L;
    /**
     * The basic information about the attribute. Will only be shallowly cloned.
     */
    private AttributeDescription attributeDescription;
    /**
     * Contains all attribute statistics calculation algorithms.
     */
    protected List<Statistics> statistics = new LinkedList<Statistics>();
    /**
     * Mapping of attributes to its providers
     */
    protected HashMap<IStats, Statistics> statisticsProviders = new HashMap<IStats, Statistics>();
    
    protected Dataset<? extends Instance> dataset;

    /**
     * Creates a simple attribute which is not part of a series and does not
     * provide a unit string. This constructor should only be used for
     * attributes which were not generated with help of a generator, i.e. this
     * attribute has no function arguments. Only the last transformation is
     * cloned, the other transformations are cloned by reference.
     */
    protected AbstractAttribute(AbstractAttribute attribute) {
        this.attributeDescription = attribute.attributeDescription;

        // copy statistics
        this.statistics = new LinkedList<Statistics>();
        for (Statistics st : attribute.statistics) {
            this.statistics.add((Statistics) st.clone());
        }
    }

    protected AbstractAttribute(String name, IAttributeType type) {
        this.attributeDescription = new AttributeDescription(name, type, 0.0d);
    }

    /**
     * Returns the name of the attribute.
     */
    @Override
    public String getName() {
        return this.attributeDescription.getName();
    }

    /**
     * Sets the name of the attribute.
     */
    @Override
    public void setName(String v) {
        if (v.equals(this.attributeDescription.getName())) {
            return;
        }
        this.attributeDescription.setName(v);
    }

    /**
     * @return index of column in dataset
     */
    @Override
    public int getIndex() {
        return this.attributeDescription.getIndex();
    }

    @Override
    public void setIndex(int index) {
        this.attributeDescription.setIndex(index);
    }
    
    @Override
    public void setDataset(Dataset<? extends Instance> dataset){
        this.dataset = dataset;
    }

    /**
     * Returns the statistics type of this attribute.
     *
     */
    public IAttributeType getType() {
        return this.attributeDescription.getType();
    }

    /**
     * Returns the attribute statistics.
     */
    @Override
    public Iterator<Statistics> getAllStatistics() {
        return this.statistics.iterator();
    }

    @Override
    public void registerStatistics(Statistics statistics) {
        this.statistics.add(statistics);
        IStats[] stats = statistics.provides();
        for (int i = 0; i < stats.length; i++) {
            statisticsProviders.put(stats[i], statistics);
        }
    }

    @Override
    public double statistics(IStats name) {
        if(statisticsProviders.containsKey(name)){
            return statisticsProviders.get(name).statistics(name);
        }
        throw new RuntimeException("statistics "+name+" was not registered");
    }

    @Override
    public void setDefault(double value) {
        this.attributeDescription = (AttributeDescription) this.attributeDescription.clone();
        this.attributeDescription.setDefault(value);
    }

    @Override
    public double getDefault() {
        return this.attributeDescription.getDefault();
    }

    /**
     * Clones this attribute.
     */
    @Override
    public abstract Object clone();

    /**
     * Returns true if the given attribute has the same name and the same table
     * index.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof AbstractAttribute)) {
            return false;
        }
        AbstractAttribute a = (AbstractAttribute) o;
        return this.attributeDescription.equals(a.attributeDescription);
    }

    @Override
    public int hashCode() {
        return attributeDescription.hashCode();
    }

    /**
     * Returns a human readable string that describes this attribute.
     */
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append(": ");
        result.append(this.attributeDescription.getName());
        return result.toString();
    }
}
