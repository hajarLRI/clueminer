package org.clueminer.chart.renderer;

import org.clueminer.chart.api.ChartConfig;
import org.clueminer.chart.api.ChartRenderer;
import org.clueminer.chart.api.ChartData;
import org.clueminer.chart.api.Range;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.openide.util.lookup.ServiceProvider;

/**
 * Simple rendering of a ChartRenderer - line that connects all points
 *
 * @author Tomas Barton
 */
@ServiceProvider(service = ChartRenderer.class)
public class Line implements ChartRenderer {

    private static final long serialVersionUID = -3078916604543993282L;

    public Line() {
    }

    @Override
    public String getName() {
        return "Line";
    }

    @Override
    public void paint(Graphics2D g, ChartConfig cf) {
        ChartData cd = cf.getChartData();
        //System.out.println("chart data size= " + cd.getTimePointsCnt());
        Rectangle rect = cf.getChartPanel().getBounds();
        rect.grow(-2, -2);
        Range range = cf.getRange();
        //System.out.println("rectangle "+rect);
        //System.out.println("range "+range);
        double x, y, value, yPrev, xPrev;
        int i;
        if (!cd.isVisibleNull()) {
            //long start = System.currentTimeMillis();
            cd.setMinY(Double.MAX_VALUE);
            cd.setMaxY(Double.MIN_VALUE);

            int itemCnt = cd.getTimePointsCnt();
            //  System.out.println("items in dataset "+itemCnt);
            //  g.drawString("items in dataset "+itemCnt, 20, 20);
            //  System.out.println("canvas size "+g.getClipBounds());
            Dataset<? extends Instance> dataset = cd.getDataset();
            for (Instance inst : dataset) {
                yPrev = 0;
                i = 0;
                xPrev = Double.NaN;
                g.setPaint(inst.getColor());
                while (i < itemCnt) {
                    value = inst.value(i);
                    cd.checkMax(value);
                    cd.checkMin(value);
                    x = cd.getX(i, rect);
                    y = cd.getY(value, rect, range);
                    if (xPrev != Double.NaN) {
                        g.draw(new Line2D.Double(x, y, xPrev, yPrev));
                    }
                    xPrev = x;
                    yPrev = y;
                    i++;
                }
            }
            //long end = System.currentTimeMillis();
            //System.out.println("line chart rendered in " + (end - start) + "ms last index " + i);
        }
    }
}
