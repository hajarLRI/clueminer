/*
 * Copyright (C) 2011-2015 clueminer.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clueminer.chart.graphics;

import java.io.IOException;
import java.io.OutputStream;
import org.clueminer.chart.api.Drawable;

/**
 * Interface providing functions for rendering {@code Drawable} instances and
 * writing them to an output stream. As an example: a plot can be saved into a
 * bitmap file.
 *
 * @see DrawableWriterFactory
 */
public interface DrawableWriter {

    /**
     * unique identifier
     *
     * @return
     */
    String getName();

    /**
     * Returns the output format of this writer.
     *
     * @return String representing the MIME-Type.
     */
    public String getMimeType();

    /**
     * Stores the specified {@code Drawable} instance.
     *
     * @param d           {@code Drawable} to be written.
     * @param destination Stream to write to
     * @param width       Width of the image.
     * @param height      Height of the image.
     * @throws IOException if writing to stream fails
     */
    public void write(Drawable d, OutputStream destination,
            double width, double height) throws IOException;

    /**
     * Stores the specified {@code Drawable} instance.
     *
     * @param d           {@code Drawable} to be written.
     * @param destination Stream to write to
     * @param x           Horizontal position.
     * @param y           Vertical position.
     * @param width       Width of the image.
     * @param height      Height of the image.
     * @throws IOException if writing to stream fails
     */
    public void write(Drawable d, OutputStream destination,
            double x, double y, double width, double height) throws IOException;
}