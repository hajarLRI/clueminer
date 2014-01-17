package org.clueminer.hts.fluorescence;

import java.awt.Color;
import java.awt.Font;
import org.clueminer.dataset.api.Instance;
import org.clueminer.dataset.api.Plotter;
import org.math.plot.Plot2DPanel;
import org.math.plot.plotObjects.BaseLabel;

/**
 *
 * @author Tomas Barton
 */
public class FluorescencePlot extends Plot2DPanel implements Plotter {

    private static final long serialVersionUID = 9134124279294818651L;

    public FluorescencePlot() {
        super();
    }

    @Override
    public void addInstance(Instance instance) {
        this.addLinePlot(instance.getName(), instance.arrayCopy());
    }

    @Override
    public void clearAll() {
        this.removeAllPlots();
    }

    @Override
    public void setTitle(String title) {
        BaseLabel label = new BaseLabel(title, Color.BLACK, 0.5, 1.1);
        label.setFont(new Font("Courier", Font.BOLD, 20));
        this.addPlotable(label);
    }

    @Override
    public void setXBounds(double min, double max) {
        this.setFixedBounds(0, min, max);
    }

    @Override
    public void setYBounds(double min, double max) {
        this.setFixedBounds(1, min, max);
    }
}
