package org.clueminer.perspective;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Icon;
import javax.swing.JToggleButton;
import org.clueminer.gui.UIUtils;
import org.clueminer.perspective.api.PerspectiveController;
import org.clueminer.perspective.spi.Perspective;
import org.openide.util.ImageUtilities;
import org.openide.util.Lookup;

/**
 *
 * @author deric
 */
public class BannerComponent extends javax.swing.JPanel {

    private static final long serialVersionUID = -3718526426806831618L;
    private transient JToggleButton[] buttons;
    private transient PerspectiveController perspectiveController;

    /**
     * Creates new form BannerComponent
     */
    public BannerComponent() {
        initComponents();

        perspectiveController = Lookup.getDefault().lookup(PerspectiveController.class);

        addGroupTabs();
    }

    public int getSelectedPerspectiveIndex() {
        int i = 0;
        for (Perspective p : perspectiveController.getPerspectives()) {
            if (p.equals(perspectiveController.getSelectedPerspective())) {
                return i;
            }
            i++;
        }
        return -1;
    }

    private void addGroupTabs() {
        buttons = new JPerspectiveButton[perspectiveController.getPerspectives().length];
        int i = 0;

        //Add tabs
        for (final Perspective perspective : perspectiveController.getPerspectives()) {
            JPerspectiveButton toggleButton = new JPerspectiveButton(perspective.getDisplayName(), perspective.getIcon());
            toggleButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    perspectiveController.selectPerspective(perspective);
                }
            });
            perspectivesButtonGroup.add(toggleButton);
            buttonsPanel.add(toggleButton);
            buttons[i++] = toggleButton;
        }

        //Set currently selected button
        perspectivesButtonGroup.setSelected(buttons[getSelectedPerspectiveIndex()].getModel(), true);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        perspectivesButtonGroup = new javax.swing.ButtonGroup();
        buttonsPanel = new javax.swing.JPanel();

        buttonsPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.LEFT));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(buttonsPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.ButtonGroup perspectivesButtonGroup;
    // End of variables declaration//GEN-END:variables

    private static class JPerspectiveButton extends JToggleButton {

        private static final long serialVersionUID = 5200221183133193253L;

        public JPerspectiveButton(String text, Icon icon) {
            setText(text);
            setBorder(null);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

            if (UIUtils.isWindowsLookAndFeel()) {
                setIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/vista-enabled.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setRolloverIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/vista-mousover.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setSelectedIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/vista-selected.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
            } else if (UIUtils.isAquaLookAndFeel()) {
                setIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/aqua-enabled.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setRolloverIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/aqua-mouseover.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setSelectedIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/aqua-selected.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
            } else {
                setIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/nimbus-enabled.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setRolloverIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/nimbus-mouseover.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
                setSelectedIcon(ImageUtilities.image2Icon(ImageUtilities.mergeImages(ImageUtilities.loadImage("org/clueminer/perspective/resources/nimbus-selected.png"),
                        ImageUtilities.icon2Image(icon), 6, 3)));
            }
        }
    }
}
