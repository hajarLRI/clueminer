package org.math.plot.components;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.security.AccessControlException;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.filechooser.FileFilter;
import org.math.plot.PlotPanel;
import org.math.plot.canvas.Plot3DCanvas;
import org.math.plot.canvas.PlotCanvas;
import org.openide.util.ImageUtilities;

/**
 * BSD License
 *
 * @author Yann RICHET
 */
public class PlotToolBar extends JToolBar {

    private static final long serialVersionUID = -8678772186464428859L;
    // TODO redesign icons...
    protected ButtonGroup buttonGroup;
    protected JToggleButton buttonCenter;
    //protected JToggleButton buttonEdit;
    protected JToggleButton buttonZoom;
    protected JToggleButton buttonRotate;
    //protected JToggleButton buttonViewCoords;
    protected JButton buttonSetScales;
    protected JButton buttonDatas;
    protected JButton buttonSavePNGFile;
    protected JButton buttonReset;
    private boolean denySaveSecurity;
    private JFileChooser pngFileChooser;
    /**
     * the currently selected PlotPanel
     */
    private PlotCanvas plotCanvas;
    private PlotPanel plotPanel;

    public PlotToolBar(PlotPanel pp) {
        plotPanel = pp;
        plotCanvas = pp.plotCanvas;

        try {
            pngFileChooser = new JFileChooser();
            pngFileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File f) {
                    return f.isDirectory() || f.getName().endsWith(".png");
                }

                public String getDescription() {
                    return "Portable Network Graphic file";
                }
            });
        } catch (AccessControlException ace) {
            denySaveSecurity = true;
        }

        buttonGroup = new ButtonGroup();


        buttonCenter = new JToggleButton(ImageUtilities.loadImageIcon("org/math/plot/icons/center.png", false));
        buttonCenter.setToolTipText("Center axes");
        buttonCenter.setSelected(plotCanvas.ActionMode == PlotCanvas.TRANSLATION);

        buttonZoom = new JToggleButton(ImageUtilities.loadImageIcon("org/math/plot/icons/zoom.png", false));
        buttonZoom.setToolTipText("Zoom");
        buttonZoom.setSelected(plotCanvas.ActionMode == PlotCanvas.ZOOM);

        //buttonEdit = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/edit.png")));
        //buttonEdit.setToolTipText("Edit mode");

        //buttonViewCoords = new JToggleButton(new ImageIcon(PlotPanel.class.getResource("icons/position.png")));
        //buttonViewCoords.setToolTipText("Highlight coordinates / Highlight plot");

        buttonSetScales = new JButton(ImageUtilities.loadImageIcon("org/math/plot/icons/scale.png", false));
        buttonSetScales.setToolTipText("Set scales");

        buttonDatas = new JButton(ImageUtilities.loadImageIcon("org/math/plot/icons/data.png", false));
        buttonDatas.setToolTipText("Get datas");

        buttonSavePNGFile = new JButton(ImageUtilities.loadImageIcon("org/math/plot/icons/topngfile.png", false));
        buttonSavePNGFile.setToolTipText("Save graphics in a .PNG File");

        buttonReset = new JButton(ImageUtilities.loadImageIcon("org/math/plot/icons/back.png", false));
        buttonReset.setToolTipText("Reset zoom & axes");

        /*buttonEdit.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         plotCanvas.ActionMode = PlotCanvas.EDIT;
         }
         });*/

        buttonZoom.setSelected(true);
        buttonZoom.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.ZOOM;
            }
        });

        buttonCenter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.ActionMode = PlotCanvas.TRANSLATION;
            }
        });

        /*buttonViewCoords.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
         plotCanvas.setNoteCoords(buttonViewCoords.isSelected());
         }
         });*/

        buttonSetScales.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.displaySetScalesFrame();
            }
        });

        buttonDatas.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.displayDatasFrame();
            }
        });

        buttonSavePNGFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choosePNGFile();
            }
        });

        buttonReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                plotCanvas.resetBase();
            }
        });

        buttonGroup.add(buttonCenter);
        buttonGroup.add(buttonZoom);
        //buttonGroup.add(buttonEdit);

        add(buttonCenter, null);
        add(buttonZoom, null);
        add(buttonReset, null);
        //add(buttonViewCoords, null);
        add(buttonSetScales, null);
        //add(buttonEdit, null);
        add(buttonSavePNGFile, null);
        add(buttonDatas, null);

        if (!denySaveSecurity) {
            pngFileChooser.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    saveGraphicFile();
                }
            });
        } else {
            buttonSavePNGFile.setEnabled(false);
        }

        //buttonEdit.setEnabled(plotCanvas.getEditable());

        //buttonViewCoords.setEnabled(plotCanvas.getNotable());

        // allow mixed (2D/3D) plots managed by one toolbar
        if (plotCanvas instanceof Plot3DCanvas) {
            if (buttonRotate == null) {
                buttonRotate = new JToggleButton(ImageUtilities.loadImageIcon("org/math/plot/icons/rotation.png", false));
                buttonRotate.setToolTipText("Rotate axes");

                buttonRotate.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        plotCanvas.ActionMode = Plot3DCanvas.ROTATION;
                    }
                });
                buttonGroup.add(buttonRotate);
                add(buttonRotate, null, 2);
                buttonRotate.setSelected(plotCanvas.ActionMode == Plot3DCanvas.ROTATION);
            } else {
                buttonRotate.setEnabled(true);
            }
        } else {
            if (buttonRotate != null) {
                // no removal/disabling just disable
                if (plotCanvas.ActionMode == Plot3DCanvas.ROTATION) {
                    plotCanvas.ActionMode = PlotCanvas.ZOOM;
                }
                buttonRotate.setEnabled(false);
            }
        }
    }

    void choosePNGFile() {
        pngFileChooser.showSaveDialog(this);
    }

    void saveGraphicFile() {
        java.io.File file = pngFileChooser.getSelectedFile();
        try {
            plotPanel.toGraphicFile(file);
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(null, "Save failed : " + e.getMessage(), "Error", JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
        }
    }
}