package org.clueminer.gui;

import java.awt.AlphaComposite;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * Buffered panel
 *
 * @author Tomas Barton
 */
public abstract class BPanel extends JPanel {

    private static final long serialVersionUID = -5425978383189367748L;

    protected Dimension reqSize = new Dimension(0, 0);
    protected Dimension realSize = new Dimension(0, 0);
    protected BufferedImage bufferedImage;
    protected Graphics2D g;
    protected boolean preserveAlpha = false;
    protected boolean fitToSpace = true;

    public BPanel() {
        setDoubleBuffered(false);
        this.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                reqSize = getSize();
                sizeUpdated(reqSize);
            }

            @Override
            public void componentMoved(ComponentEvent e) {
            }

            @Override
            public void componentShown(ComponentEvent e) {
                reqSize = getSize();
                sizeUpdated(reqSize);
                recalculate();
            }

            @Override
            public void componentHidden(ComponentEvent e) {

            }
        });
    }

    public abstract void render(Graphics2D g);

    /**
     * Called when component's dimension changes
     *
     * @param size new component size
     */
    public abstract void sizeUpdated(Dimension size);

    public abstract boolean hasData();

    /**
     * Recalculate dimension of component. YOU MUST UPDATE realSize variable
     */
    public abstract void recalculate();

    public abstract boolean isAntiAliasing();

    private void createBufferedGraphics() {
        if (!hasData()) {
            return;
        }

        if (fitToSpace) {
            if (realSize.width <= 0 || realSize.height <= 0) {
                return;
            }
            //create smaller image, then upscale or downscale
            bufferedImage = new BufferedImage(realSize.width, realSize.height, BufferedImage.TYPE_INT_ARGB);
        } else {
            if (reqSize.width <= 0 || reqSize.height <= 0) {
                return;
            }
            bufferedImage = new BufferedImage(reqSize.width, reqSize.height, BufferedImage.TYPE_INT_ARGB);
        }

        g = bufferedImage.createGraphics();
        //this.setOpaque(false);
        // clear the panel
        //g.setColor(getBackground());
        if (preserveAlpha) {
            this.setOpaque(false);
            g.setComposite(AlphaComposite.Clear);
        } else {
            g.setComposite(AlphaComposite.Src);
            if (fitToSpace) {
                g.fillRect(0, 0, realSize.width, realSize.width);
            } else {
                g.fillRect(0, 0, reqSize.width, reqSize.width);
            }
        }
        drawComponent(g);
    }

    /**
     * Draws componet on given graphics.
     *
     * @param g
     */
    public void drawComponent(Graphics2D g) {
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        if (isAntiAliasing()) {
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        }

        if (preserveAlpha) {
            g.setComposite(AlphaComposite.Src);
        }
        render(g);
        g.dispose();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (bufferedImage == null) {
            createBufferedGraphics();
        }
        //if no data, bufferedImage is null
        if (bufferedImage != null) {
            int dx = Math.abs(reqSize.width - realSize.width);
            int dy = Math.abs(reqSize.height - realSize.height);

            //requested size is different from buffered one, resize it
            if (dx > 1 || dy > 1) {
                if (fitToSpace) {
                    if (reqSize.width <= 0 || reqSize.height <= 0) {
                        return;
                    }
                    //resize buffered image
                    BufferedImage scaledBI = new BufferedImage(reqSize.width, reqSize.height, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D gr = scaledBI.createGraphics();
                    if (preserveAlpha) {
                        gr.setComposite(AlphaComposite.Clear);
                        gr.fillRect(0, 0, dx, dy);
                        //gr.setComposite(AlphaComposite.Src);
                    }
                    //AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
                    //gr.drawRenderedImage(scaledBI, at);
                    g.drawImage(bufferedImage, 0, 0, reqSize.width, reqSize.height, null);
                    gr.dispose();
                    bufferedImage = scaledBI;
                } else {
                    //difference between real and requested is too big
                    createBufferedGraphics();
                }
            }

            //cached image
            g.drawImage(bufferedImage,
                    0, 0,
                    reqSize.width, reqSize.height,
                    null);
        }
    }

    public void resetCache() {
        recalculate();

        //invoke painting from EDT thread
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                bufferedImage = null;
                createBufferedGraphics();
                validate();
                revalidate();
                repaint();
            }
        });
    }

    public Dimension getRealSize() {
        return realSize;
    }

}
