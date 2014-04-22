package org.clueminer.processor.ui;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import org.clueminer.processor.DefaultProcessor;
import org.netbeans.validation.api.Problems;
import org.netbeans.validation.api.Validator;
import org.netbeans.validation.api.ui.ValidationGroup;
import org.netbeans.validation.api.ui.swing.ValidationPanel;

/**
 *
 * @author deric
 */
public class DefaultProcessorPanel extends javax.swing.JPanel {

    /**
     * Creates new form DefaultProcessorPanel
     */
    public DefaultProcessorPanel() {
        initComponents();
    }

    public void setup(DefaultProcessor processor) {

    }

    public void unsetup(DefaultProcessor processor) {

    }

    public static ValidationPanel createValidationPanel(DefaultProcessorPanel innerPanel) {
        ValidationPanel validationPanel = new ValidationPanel();
        validationPanel.setInnerComponent(innerPanel);

        ValidationGroup group = validationPanel.getValidationGroup();

        //  final FullValidationListener fullValidationListener = new FullValidationListener(innerPanel);
        //  group.add(fullValidationListener);
        PropertyChangeListener listener = new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent e) {

            }
        };
//        innerPanel.datePicker.addPropertyChangeListener(listener);

        return validationPanel;
    }

    private static class FullValidator implements Validator<String> {

        private DefaultProcessorPanel panel;

        public FullValidator(DefaultProcessorPanel panel) {
            this.panel = panel;
        }

        @Override
        public void validate(Problems problems, String compName, String model) {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public Class<String> modelType() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

    }

    /**
     * This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}