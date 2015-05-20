/*
 * Copyright (C) 2015 Lukasz Bacik <mail@luka.sh>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/**
 *
 * !!! This code is still under heavy development
 * !!! (however, as usually - all comments are welcome :))
 *
 * @version 0.1 ALFA (prototype)
 *
 * @author Lukasz Bacik <mail@luka.sh>
 */
package sh.luka.gui;

import java.awt.Container;
import java.awt.Rectangle;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.Box;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
 * Coordinates Box it is a window showing some information about displayed
 * graphics file.
 *
 * Displayed information:
 * - original size of file (width, height in pixels)
 * - current scale
 * - size of file in current scale (width, height in pixels)
 * - currently visible part of file (rectangle as x,y of left-top corner
 *      and width and height of displayed part)
 * - original scale of the ruler - rectangle described by coordinates of
 *      the left-bottom (!) and the right-top (!) corner
 * - scale (on the ruler) of the currently displayed part of the file
 *
 * @author Lukasz Bacik <mail@luka.sh>
 */
public class CoordinatesBox extends JFrame {

    protected JImageViewer parentObject = null;

    public CoordinatesBox(JImageViewer p) {

        parentObject = p;

        setTitle("coordinates");
        setSize(200, 300);

        Container c = getContentPane();
        addPanels(c);
    }

    protected void addPanels(Container c) {
        c.add(imagePanel());
    }

    protected Box imagePanel() {

        Box panel = Box.createVerticalBox();
        Box level;

        BufferedImage im = parentObject.image.image;

        // image
        String s = Integer.toString(im.getWidth());
        s += " x " + Integer.toString(im.getHeight());
        JLabel imageSizeLabel = new JLabel("image oryginal size:");
        JLabel imageSizeText = new JLabel(s);

        level = Box.createHorizontalBox();
        level.add(imageSizeLabel);
        level.add(Box.createHorizontalStrut(10));
        level.add(imageSizeText);
        panel.add(level);

        // image scale
        JLabel scaleFieldLabel = new JLabel("image scale:");
        JTextField scaleField = new JTextField(Double.toString(parentObject.scale), 20);
        scaleField.setMaximumSize(scaleField.getPreferredSize());

        level = Box.createHorizontalBox();
        level.add(scaleFieldLabel);
        level.add(Box.createHorizontalStrut(10));
        level.add(scaleField);
        panel.add(level);

        Point2D p1 = new Point2D.Double(im.getWidth(), im.getHeight());
        Point2D p2 = new Point2D.Double();
        Point2D p3 = new Point2D.Double();

        p3 = parentObject.graphPanel.at.transform(p1, p2);

        JLabel imageVisibleSizeLabel4 = new JLabel("image current size:");
        JLabel imageVisibleSizeText4 = new JLabel(p3.getX() + " x " + p3.getY());

        level = Box.createHorizontalBox();
        level.add(imageVisibleSizeLabel4);
        level.add(Box.createHorizontalStrut(10));
        level.add(imageVisibleSizeText4);
        panel.add(level);

        // visible
        Rectangle rec = parentObject.scroll.getViewport().getViewRect();
        s = rec.x + " - " + rec.y + " - " + rec.width + " - " + rec.height;
        JLabel imageVisibleSizeLabel = new JLabel("visible size:");
        JLabel imageVisibleSizeText = new JLabel(s);

        level = Box.createHorizontalBox();
        level.add(imageVisibleSizeLabel);
        level.add(Box.createHorizontalStrut(10));
        level.add(imageVisibleSizeText);
        panel.add(level);

        JLabel l5label = new JLabel("scale oryginal:");
        JLabel l5 = new JLabel(
                ((JImageViewerRuler) parentObject).rulerScale.getP1().getX() + ", "
                + ((JImageViewerRuler) parentObject).rulerScale.getP1().getY() + ", "
                + ((JImageViewerRuler) parentObject).rulerScale.getP2().getX() + ", "
                + ((JImageViewerRuler) parentObject).rulerScale.getP2().getY());

        level = Box.createHorizontalBox();
        level.add(l5label);
        level.add(Box.createHorizontalStrut(10));
        level.add(l5);
        panel.add(level);

        Line2D rs = ((JImageViewerRuler) parentObject).getScale(parentObject.scroll.getViewport().getViewRect());
        JLabel l6label = new JLabel("scale visible:");
        JLabel l6 = new JLabel(
                rs.getP1().getX() + ", "
                + rs.getP1().getY() + ", "
                + rs.getP2().getX() + ", "
                + rs.getP2().getY());

        level = Box.createHorizontalBox();
        level.add(l6label);
        level.add(Box.createHorizontalStrut(10));
        level.add(l6);
        panel.add(level);

        return panel;
    }

    public void recalculate() {

        Container c = getContentPane();
        c.removeAll();
        c.revalidate();
        addPanels(c);
        repaint();
    }

}
