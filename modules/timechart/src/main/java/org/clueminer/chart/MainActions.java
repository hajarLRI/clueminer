package org.clueminer.chart;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.*;
import org.clueminer.chart.api.Annotation;

import org.clueminer.chart.api.ChartRenderer;
import org.clueminer.chart.api.ChartData;
import org.clueminer.chart.factory.AnnotationFactory;
import org.clueminer.chart.factory.ChartRendererFactory;
import org.clueminer.dialogs.AnnotationProperties;
import org.clueminer.dialogs.Overlays;
import org.clueminer.dialogs.SettingsPanel;
import org.clueminer.factory.TemplateFactory;
import org.clueminer.export.impl.ImageExporter;
import org.netbeans.api.print.PrintManager;
import org.openide.DialogDescriptor;
import org.openide.DialogDisplayer;
import org.openide.NotifyDescriptor.Confirmation;
import org.openide.NotifyDescriptor.InputLine;
import org.openide.util.ImageUtilities;
import org.openide.util.NbBundle;

/**
 *
 * @author Tomas Barton
 */
public final class MainActions {

    private MainActions() {
    }

    /*
     * ChartFrame popup actions & ProjectToolbar actions
     */
    public static Action zoomIn(ChartFrame chartFrame) {
        return ZoomIn.getAction(chartFrame);
    }

    public static Action zoomOut(ChartFrame chartFrame) {
        return ZoomOut.getAction(chartFrame);
    }

    public static Action zoomShowAll(ChartFrame chartFrame) {
        return ZoomShowAll.getAction(chartFrame);
    }

    public static Action chartPopup(ChartFrame chartFrame) {
        return ChartPopup.getAction(chartFrame);
    }

    public static Action openOverlays(ChartFrame chartFrame) {
        return OpenOverlays.getAction(chartFrame);
    }

    public static Action annotationPopup(ChartFrame chartFrame) {
        return AnnotationPopup.getAction(chartFrame);
    }

    public static Action toggleMarker(ChartFrame chartFrame) {
        return ToggleMarker.getAction(chartFrame);
    }

    public static Action exportImage(ChartFrame chartFrame) {
        return ExportImage.getAction(chartFrame);
    }

    public static Action printChart(ChartFrame chartFrame) {
        return PrintChart.getAction(chartFrame);
    }

    public static Action chartProperties(ChartFrame chartFrame) {
        return ChartProps.getAction(chartFrame);
    }

    public static Action toggleToolbarVisibility(ChartFrame chartFrame) {
        return ToggleToolbarVisibility.getAction(chartFrame);
    }

    public static Action saveToTemplate(ChartFrame chartFrame) {
        return SaveToTemplate.getAction(chartFrame);
    }

    /*
     * Submenu actions
     */
    public static Action changeChart(ChartFrame chartFrame, String chartName, boolean current) {
        return ChangeChart.getAction(chartFrame, chartName, current);
    }

    public static Action addAnnotation(String annotationName) {
        return AddAnnotation.getAction(annotationName);
    }

    public static Action removeAllAnnotations(ChartFrame chartFrame) {
        return RemoveAllAnnotations.getAction(chartFrame);
    }

    public static Action annotationProperties(ChartFrame chartFrame, AnnotationImpl annotation) {
        return AnnotationProps.getAction(chartFrame, annotation);
    }

    /*
     * ProjectToolbar popup actions
     */
    public static Action toggleToolbarSmallIcons(ChartFrame chartFrame, ChartToolbar chartToolbar) {
        return ToggleToolbarSmallIcons.getAction(chartFrame, chartToolbar);
    }

    public static Action toggleToolbarShowLabels(ChartFrame chartFrame, ChartToolbar chartToolbar) {
        return ToggleToolbarShowLabels.getAction(chartFrame, chartToolbar);
    }


    /*
     * Abstract MainAction
     */
    private static abstract class MainAction extends AbstractAction {

        private static final long serialVersionUID = -1667284822435980124L;

        public MainAction(String name, boolean flag) {
            putValue(NAME,
                     NbBundle.getMessage(MainActions.class, "ACT_" + name));
            putValue(SHORT_DESCRIPTION,
                     NbBundle.getMessage(MainActions.class, "TOOL_" + name));
            if (flag) {
                putValue(SMALL_ICON, ImageUtilities.loadImage("org/clueminer/chart/" + name + "16.png", true));
                putValue(LONG_DESCRIPTION, name);
                putValue(LARGE_ICON_KEY, ImageUtilities.loadImage("org/clueminer/chart/" + name + "24.png", true));
            }
        }
    }

    private static class ZoomShowAll extends MainAction {

        private static final long serialVersionUID = -3792925153248356329L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ZoomShowAll(chartFrame);
        }

        private ZoomShowAll(ChartFrame chartFrame) {
            super("ZoomShowAll", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            System.out.println(e);
            chartFrame.setZoom(1.0);
        }
    }

    private static class ZoomIn extends MainAction {

        private static final long serialVersionUID = -251027548521263379L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ZoomIn(chartFrame);
        }

        private ZoomIn(ChartFrame chartFrame) {
            super("ZoomIn", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.zoomIn();
        }
    }

    private static class ZoomOut extends MainAction {

        private static final long serialVersionUID = -4941850457319143886L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ZoomOut(chartFrame);
        }

        private ZoomOut(ChartFrame chartFrame) {
            super("ZoomOut", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.zoomOut();
        }
    }

    private static class ChartPopup extends MainAction {

        private static final long serialVersionUID = 1025799964264193397L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ChartPopup(chartFrame);
        }

        private ChartPopup(ChartFrame chartFrame) {
            super("Charts", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String current = chartFrame.getChartData().getChart().getName();

            JButton button = (JButton) e.getSource();
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem item;
            for (String chart : ChartRendererFactory.getInstance().getProviders()) {
                popupMenu.add(item = new JMenuItem(
                        MainActions.changeChart(chartFrame, chart, chart.equals(current))));
                item.setMargin(new Insets(0, 0, 0, 0));
            }

            popupMenu.show(button, 0, button.getHeight());
        }
    }

    private static class ChangeChart extends MainAction {

        private static final long serialVersionUID = 7747883536720409416L;
        private ChartFrame chartFrame;
        private String chartName;

        public static Action getAction(ChartFrame chartFrame, String chartName, boolean current) {
            return new ChangeChart(chartFrame, chartName, current);
        }

        private ChangeChart(ChartFrame chartFrame, String chartName, boolean current) {
            super("Charts", current);
            this.chartFrame = chartFrame;
            this.chartName = chartName;
            putValue(NAME, chartName);
            putValue(SHORT_DESCRIPTION, chartName);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ChartRenderer chart = ChartRendererFactory.getInstance().getProvider(chartName);
            chartFrame.getChartData().setChart(chart);
            chartFrame.validate();
            chartFrame.repaint();
        }
    }

    private static class OpenOverlays extends MainAction {

        private static final long serialVersionUID = -1122763395539423344L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new OpenOverlays(chartFrame);
        }

        private OpenOverlays(ChartFrame chartFrame) {
            super("Overlays", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Overlays dialog = new Overlays(new JFrame(), true);
            dialog.setChartFrame(chartFrame);
            dialog.setLocationRelativeTo(chartFrame);
            dialog.initForm();
            dialog.setVisible(true);
        }
    }

    private static class AnnotationPopup extends MainAction {

        private static final long serialVersionUID = -4624007815882864810L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new AnnotationPopup(chartFrame);
        }

        private AnnotationPopup(ChartFrame chartFrame) {
            super("Annotations", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JButton button = (JButton) e.getSource();
            JPopupMenu popup = new JPopupMenu();

            JMenuItem item;

            for (String annotation : AnnotationFactory.getInstance().getProviders()) {
                popup.add(item = new JMenuItem(
                        MainActions.addAnnotation(annotation)));
                item.setMargin(new Insets(0, 0, 0, 0));
            }

            popup.addSeparator();

            popup.add(item = new JMenuItem(
                    MainActions.removeAllAnnotations(chartFrame)));
            item.setMargin(new Insets(0, 0, 0, 0));

            if (chartFrame.hasCurrentAnnotation()) {
                AnnotationImpl current = chartFrame.getCurrentAnnotation();
                if (current.isSelected()) {
                    popup.addSeparator();
                    popup.add(item = new JMenuItem(
                            MainActions.annotationProperties(chartFrame, current)));
                    item.setMargin(new Insets(0, 0, 0, 0));
                }
            }

            popup.show(button, 0, button.getHeight());
        }
    }

    private static class AddAnnotation extends MainAction {

        private static final long serialVersionUID = 144007177206705167L;
        private String annotationName;

        public static Action getAction(String annotationName) {
            return new AddAnnotation(annotationName);
        }

        private AddAnnotation(String annotationName) {
            super("Annotations", true);
            putValue(NAME, annotationName);
            putValue(SHORT_DESCRIPTION, annotationName);
            this.annotationName = annotationName;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Annotation annotation = AnnotationFactory.getInstance().getProvider(annotationName);
            AnnotationFactory.getInstance().setDefault(annotation);
        }
    }

    private static class RemoveAllAnnotations extends MainAction {

        private static final long serialVersionUID = -5339023832954629827L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new RemoveAllAnnotations(chartFrame);
        }

        private RemoveAllAnnotations(ChartFrame chartFrame) {
            super("AnnotationsRemoveAll", false);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.removeAllAnnotations();
        }
    }

    private static class AnnotationProps extends MainAction {

        private static final long serialVersionUID = -5903365476818541598L;
        private ChartFrame chartFrame;
        private AnnotationImpl annotation;

        public static Action getAction(ChartFrame chartFrame, AnnotationImpl annotation) {
            return new AnnotationProps(chartFrame, annotation);
        }

        private AnnotationProps(ChartFrame chartFrame, AnnotationImpl annotation) {
            super("AnnotationsProperties", false);
            this.chartFrame = chartFrame;
            this.annotation = annotation;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            AnnotationProperties dialog = new AnnotationProperties(new JFrame(), true);
            dialog.initializeForm(annotation);
            dialog.setLocationRelativeTo(chartFrame);
            dialog.setVisible(true);
        }
    }

    private static class ToggleMarker extends MainAction {

        private static final long serialVersionUID = -2781164463038323948L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ToggleMarker(chartFrame);
        }

        private ToggleMarker(ChartFrame chartFrame) {
            super("Marker", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() instanceof JToggleButton) {
                JToggleButton button = (JToggleButton) e.getSource();
                boolean enable = button.isSelected();
                chartFrame.getChartProperties().setMarkerVisibility(enable);
                chartFrame.validate();
                chartFrame.repaint();
                chartFrame.componentFocused();
            }
        }
    }

    private static class ExportImage extends MainAction {

        private static final long serialVersionUID = -7302035462577469031L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ExportImage(chartFrame);
        }

        private ExportImage(ChartFrame chartFrame) {
            super("ExportImage", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            ImageExporter.getDefault().export(chartFrame.getMainPanel());
            chartFrame.componentFocused();
        }
    }

    private static class PrintChart extends MainAction {

        private static final long serialVersionUID = 2490934543157114684L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new PrintChart(chartFrame);
        }

        private PrintChart(ChartFrame chartFrame) {
            super("Print", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            PrintManager.printAction(chartFrame.getMainPanel()).actionPerformed(e);
        }
    }

    private static class ChartProps extends MainAction {

        private static final long serialVersionUID = -7538585839977472162L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ChartProps(chartFrame);
        }

        private ChartProps(ChartFrame chartFrame) {
            super("ChartProperties", true);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SettingsPanel.getDefault().openSettingsWindow(chartFrame);
        }
    }

    private static class ToggleToolbarVisibility extends MainAction {

        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new ToggleToolbarVisibility(chartFrame);
        }

        private ToggleToolbarVisibility(ChartFrame chartFrame) {
            super(
                    chartFrame.getChartProperties().getToolbarVisibility()
                    ? "HideToolbar"
                    : "ShowToolbar",
                    false);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.getChartProperties().toggleToolbarVisibility();
            chartFrame.setToolbarVisibility();
        }
    }

    private static class ToggleToolbarSmallIcons extends MainAction {

        private ChartFrame chartFrame;
        private ChartToolbar chartToolbar;

        public static Action getAction(ChartFrame chartFrame, ChartToolbar chartToolbar) {
            return new ToggleToolbarSmallIcons(chartFrame, chartToolbar);
        }

        private ToggleToolbarSmallIcons(ChartFrame chartFrame, ChartToolbar chartToolbar) {
            super("SmallToolbarIcons", false);
            this.chartFrame = chartFrame;
            this.chartToolbar = chartToolbar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.getChartProperties().toggleToolbarSmallIcons();
            chartToolbar.toggleIcons();
        }
    }

    private static class ToggleToolbarShowLabels extends MainAction {

        private ChartFrame chartFrame;
        private ChartToolbar chartToolbar;

        public static Action getAction(ChartFrame chartFrame, ChartToolbar chartToolbar) {
            return new ToggleToolbarShowLabels(chartFrame, chartToolbar);
        }

        private ToggleToolbarShowLabels(ChartFrame chartFrame, ChartToolbar chartToolbar) {
            super("HideLabels", false);
            this.chartFrame = chartFrame;
            this.chartToolbar = chartToolbar;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.getChartProperties().toggleShowLabels();
            chartToolbar.toggleLabels();
        }
    }

    public static JMenu generateChartsMenu(ChartFrame chartFrame) {
        JMenuItem menuItem;

        JMenu menu = new JMenu(NbBundle.getMessage(MainActions.class, "ACT_Charts"));
        menu.setIcon(ImageUtilities.loadImageIcon("org/clueminer/chart/chart16.png", true));

        ChartData chartData = chartFrame.getChartData();
        String current = chartData.getChart().getName();

        for (String chart : ChartRendererFactory.getInstance().getProviders()) {
            menu.add(menuItem = new JMenuItem(MainActions.changeChart(chartFrame, chart, current.equals(chart))));
            menuItem.setMargin(new Insets(0, 0, 0, 0));
        }

        return menu;
    }

    public static JMenu generateAnnotationsMenu(ChartFrame chartFrame) {
        JMenuItem menuItem;
        JMenu menu = new JMenu(NbBundle.getMessage(MainActions.class, "ACT_Annotations"));
        menu.setIcon(ImageUtilities.loadImageIcon("org/clueminer/chart/line16.png", true));

        for (String annotation : AnnotationFactory.getInstance().getProviders()) {
            menu.add(menuItem = new JMenuItem(
                    MainActions.addAnnotation(annotation)));
            menuItem.setMargin(new Insets(0, 0, 0, 0));
        }

        menu.addSeparator();

        menu.add(menuItem = new JMenuItem(
                MainActions.removeAllAnnotations(chartFrame)));
        menuItem.setMargin(new Insets(0, 0, 0, 0));

        if (chartFrame.hasCurrentAnnotation()) {
            AnnotationImpl current = chartFrame.getCurrentAnnotation();
            if (current.isSelected()) {
                menu.addSeparator();
                menu.add(menuItem = new JMenuItem(
                        MainActions.annotationProperties(chartFrame, current)));
                menuItem.setMargin(new Insets(0, 0, 0, 0));
            }
        }

        return menu;
    }

    private static JMenu generateTempMenu(ChartFrame chartFrame) {
        JMenu menu = new JMenu(NbBundle.getMessage(MainActions.class, "ACT_SelectTemplate"));
        for (Object template : TemplateFactory.getDefault().getTemplateNames()) {
            if ((chartFrame.getTemplate() == null) || (!template.equals(chartFrame.getTemplate().getName()))) {
                menu.add(new JMenuItem(ChangeTemplate.getAction(chartFrame, (String) template)));
            }
        }
        return menu;
    }

    public static JMenu generateTemplatesMenu(ChartFrame chartFrame) {
        JMenu menu = new JMenu(NbBundle.getMessage(MainActions.class, "ACT_Templates"));
        menu.add(generateTempMenu(chartFrame));
        menu.add(new JMenuItem(MainActions.saveToTemplate(chartFrame)));
        return menu;
    }

    private static class ChangeTemplate extends MainAction {

        private static final long serialVersionUID = 8854588040661174473L;
        private ChartFrame chartFrame;
        private String template;

        public static Action getAction(ChartFrame chartFrame, String template) {
            return new ChangeTemplate(chartFrame, template);
        }

        private ChangeTemplate(ChartFrame chartFrame, String template) {
            super("SaveToTemplate", false);
            this.chartFrame = chartFrame;
            this.template = template;
            putValue(NAME, template);
            putValue(SHORT_DESCRIPTION, template);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            chartFrame.setTemplate(TemplateFactory.getDefault().getTemplate(template));
        }
    }

    private static class SaveToTemplate extends MainAction {

        private static final long serialVersionUID = 1L;
        private ChartFrame chartFrame;

        public static Action getAction(ChartFrame chartFrame) {
            return new SaveToTemplate(chartFrame);
        }

        private SaveToTemplate(ChartFrame chartFrame) {
            super("SaveToTemplate", false);
            this.chartFrame = chartFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            InputLine descriptor = new DialogDescriptor.InputLine(
                    "Template Name:", "Save to Template");
            descriptor.setOptions(new Object[]{
                DialogDescriptor.OK_OPTION,
                DialogDescriptor.CANCEL_OPTION
            });
            Object ret = DialogDisplayer.getDefault().notify(descriptor);
            if (ret.equals(DialogDescriptor.OK_OPTION)) {
                String name = descriptor.getInputText();
                if (!TemplateFactory.getDefault().templateExists(name)) {
                    TemplateFactory.getDefault().saveToTemplate(name, chartFrame);
                } else {
                    Confirmation confirmation = new DialogDescriptor.Confirmation(
                            "<html>This template already exists!<br>Do you want to overwrite this template?</html>", "Overwrite");
                    Object obj = DialogDisplayer.getDefault().notify(confirmation);
                    if (obj.equals(DialogDescriptor.OK_OPTION)) {
                        TemplateFactory.getDefault().removeTemplate(name);
                        TemplateFactory.getDefault().saveToTemplate(name, chartFrame);
                    }
                }
            }
        }
    }
}
