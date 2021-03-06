package org.clueminer.clustering.api.config;

import org.clueminer.clustering.params.ParamType;

/**
 *
 * @author Tomas Barton
 * @param <T>
 */
public interface Parameter<T> {

    /**
     * Used for interactive configuration to retrieve a human readable parameter
     * name.
     *
     * @return a <code>String</code> containing a human readable name
     */
    String getName();

    /**
     * Used for interactive configuration to retrieve a human readable parameter
     * description.
     *
     * @return a <code>String</code> containing a human readable description
     */
    String getDescription();

    /**
     * Returns the current property value.
     *
     * @return current property value
     */
    T getValue();

    /**
     * Sets the property value.
     *
     * @param value to set the property to
     */
    void setValue(T value);

    /**
     *
     * @return type of this parameter
     */
    ParamType getType();

    /**
     * Factory for getting possible values
     *
     * @return String
     */
    String getFactory();
}
