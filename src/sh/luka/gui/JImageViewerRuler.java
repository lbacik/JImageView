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
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

/**
 * Ruler - extension of the JImageViewer class allowing user to define and work
 * with own unit of the graphic width & height. Ruler has only an informational
 * meaning, it calculates the width and height of the graphics from pixels to...
 * something else.
 *
 * !!! Important !!!
 * The rulerScale variable defines "own scale" from the left-bottom (x1, y1)
 * to the right-top (x2,y2) corner of the image.
 *
 * @author Lukasz Bacik <mail@luka.sh>
 */
public class JImageViewerRuler extends JImageViewer implements Cloneable, Serializable {

    /**
     * the "view" can be described in two ways:
     * 1. the line points are describing the top-left and the bottom-right corner of the view
     * 2. the line points are describing the bottom-left and the top-right corner of the view
     *
     * by default we are using second one
     */
    protected boolean righttop = false;
    public Line2D rulerScale = null;

    public JImageViewerRuler(Line2D scale) {
        super();
        rulerScale = scale;
    }

    protected JMenuBar menu() {
        JMenuBar menu = super.menu();

        return menu;
    }

    protected void addContainerComponents(Container c) {
        super.addContainerComponents(c);

        /**
         * can be used to show ruler on graphics panel
         */

	// JPanel rule_h = new RulePanel();
        // JPanel rule_v = new JPanel();
        // c.add(rule_h, BorderLayout.NORTH);
        // c.add(rule_v, BorderLayout.WEST);
    }

    protected Line2D getScale(Rectangle2D viewRectangle) {
        Line2D result;

        BufferedImage im = image.image;

        // the image size after transormation
        Point2D p1 = new Point2D.Double(im.getWidth(), im.getHeight());
        Point2D p2 = new Point2D.Double();
        p2 = graphPanel.at.transform(p1, p2);
	// p2 - max size of x & y = 100%

        Double dx = rulerScale.getX2() - rulerScale.getX1();
        Double dy;
        if(righttop == true) {
            // type 1
            dy = rulerScale.getY1() - rulerScale.getY2();
        } else {
            // type 2
            dy = rulerScale.getY2() - rulerScale.getY1();
        }

        // unit size
        Double uvx = p2.getX() / 100.0;
        Double uvy = p2.getY() / 100.0;

        // how many units from left corner
        Double A = viewRectangle.getX() / uvx;
        Double B = (viewRectangle.getX() + viewRectangle.getWidth()) / uvx;

        Double C = viewRectangle.getY() / uvy;
        Double D = (viewRectangle.getY() + viewRectangle.getHeight()) / uvy;

        Double px1 = (dx / 100.0) * A;
        Double py1 = (dy / 100.0) * C;
        Double px2 = (dx / 100.0) * B;
        Double py2 = (dy / 100.0) * D;


        if(righttop == true) {
            // type 1
            result = new Line2D.Double(	rulerScale.getX1() + px1, rulerScale.getY1() - py1,
        			rulerScale.getX1() + px2, rulerScale.getY1() - py2);
        } else {
            // type 2
            result = new Line2D.Double(rulerScale.getX1() + px1, rulerScale.getY1() + py1,
                                rulerScale.getX1() + px2, rulerScale.getY1() + py2);
        }
        return result;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        // ruler is initialized with some default scale
        new JImageViewerRuler(new Line2D.Double(-2.5, 3.0, 2.5, -1.0));
    }

    /*
    private class RulePanel extends JPanel {

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;

            int weight = getWidth();
            int height = getHeight();

            Point2D start = new Point2D.Double();
            Point2D stop = new Point2D.Double();
            Line2D line = new Line2D.Double();

            for (int i = 1; i <= weight; i++) {
                if (i % 10 == 0) {
                    start.setLocation(i, 1);
                    stop.setLocation(i, 5);
                    line.setLine(start, stop);
                    g2.draw(line);
                }
            }

        }
    }
    */
}
