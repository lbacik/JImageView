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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Base class of the project, it contains only simple menu and panel
 * (uses scrollbars) to show graphics. All what it can do is to open and show
 * a graphics file (with zoom feature only).
 *
 * @todo to move "coordinates" do Ruler class
 *
 * @author Lukasz Bacik <mail@luka.sh>
 */
public class JImageViewer extends JFrame implements Cloneable, Serializable {

    protected BufferedImageSerializable image;
    protected GraphPanel graphPanel;
    protected JScrollPane scroll;
    protected CoordinatesBox coordinatesBox = null;
    private JFileChooser file;

    protected JMenuItem zoom_p;
    protected JMenuItem zoom_m;

    protected double scale = 1.0;

    public JImageViewer() {

        image = new BufferedImageSerializable();

        Toolkit tools = Toolkit.getDefaultToolkit();
        Dimension screenSize = tools.getScreenSize();

        setSize(screenSize.width / 2, screenSize.height / 2);
        setLocation(screenSize.width / 4, screenSize.height / 4);

        Container c = getContentPane();
        setJMenuBar(menu());

        addContainerComponents(c);

        setVisible(true);
        repaint();
    }

    /**
     * Menu
     *
     * @return JMenuBar
     */
    protected JMenuBar menu() {
        JMenuBar menu = new JMenuBar();

        JMenu menuFile = new JMenu("File");

        JMenuItem open = new JMenuItem("Open...");
        menuFile.add(open);
        open.addActionListener(new OpenFileListener());

        /*
        JMenuItem save = new JMenuItem("Save...");
        menuFile.add(save);
        save.addActionListener(new SaveFileListener());
        */

        menuFile.addSeparator();

        JMenuItem exit = new JMenuItem("Exit");
        menuFile.add(exit);

        menu.add(menuFile);

        /*
        JMenu menuEdit = new JMenu("Edit");

        JMenuItem scale = new JMenuItem("Scale");
        menuEdit.add(scale);

        JMenuItem preferences = new JMenuItem("Preferences");
        menuEdit.add(preferences);
        preferences.addActionListener(new PreferencesListener());

        menu.add(menuEdit);
        */

        JMenu menuImage = new JMenu("Image");

        //JMenuItem
        zoom_p = new JMenuItem("Zoom +");
        menuImage.add(zoom_p);
        zoom_p.addActionListener(new ZoomListener());

        //JMenuItem
        zoom_m = new JMenuItem("Zoom -");
        menuImage.add(zoom_m);
        zoom_m.addActionListener(new ZoomListener());

        JMenuItem zoom_s = new JMenuItem("Zoom...");
        menuImage.add(zoom_s);

        JMenuItem coordinates = new JMenuItem("Coordinates");
        menuImage.add(coordinates);
        coordinates.addActionListener(new CoordinatesListener());

        menu.add(menuImage);

        return menu;
    }

    /**
     * Graphics panel with scrollbars
     *
     * @param c
     */
    protected void addContainerComponents(Container c) {

        JPanel mainPanel = new JPanel();
        graphPanel = new GraphPanel();
        scroll = new JScrollPane(graphPanel);

        scroll.addComponentListener(new ScrollComponentListener());
        ScrollAdjustmentListener l = new ScrollAdjustmentListener();
        scroll.getHorizontalScrollBar().addAdjustmentListener(l);
        scroll.getVerticalScrollBar().addAdjustmentListener(l);

        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(scroll, BorderLayout.CENTER);
        c.add(mainPanel, BorderLayout.CENTER);
    }

    /**
     *
     * @param image BufferedImage to show
     */
    protected void showImage(BufferedImage image) {

        graphPanel.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));
        graphPanel.repaint();
        scroll.getViewport().revalidate();

    }

    /**
     *
     * @param file Graphics file to open
     */
    protected void action(File file) {

        if (file.isFile()) {
            try {

                image.image = ImageIO.read(file);

            } catch (IOException e) {
                JOptionPane.showMessageDialog(getParent(),
                        "Does not compute !", "No file read or found",
                        JOptionPane.INFORMATION_MESSAGE);
                // e.printStackTrace();
            }

            showImage(image.image);
        }

    }

    /**
     * MAIN
     *
     * @param args
     */
    public static void main(String[] args) {
        new JImageViewer();
    }

    /**
     * Open file action
     */
    protected class OpenFileListener implements ActionListener, Cloneable, Serializable {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            file = new JFileChooser();
            file.setCurrentDirectory(new File("."));
            int result = file.showOpenDialog(getParent());
            if (result == JFileChooser.APPROVE_OPTION) {

                File f = file.getSelectedFile();

                action(f);
            }

        }

    }

    /*
    private class SaveFileListener implements ActionListener, Cloneable, Serializable {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // TODO Auto-generated method stub

        }

    }
    */

    /*
    private class PreferencesListener implements ActionListener, Cloneable, Serializable {

        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showConfirmDialog(getParent(), "Test");

        }
    }
    */

    /**
     * Zoom +/- action
     */
    private class ZoomListener implements ActionListener, Cloneable, Serializable {

        @Override
        public void actionPerformed(ActionEvent e) {
            // JOptionPane.showConfirmDialog(getParent(), e.getActionCommand());
            Object source = e.getSource();
            // double scale = 1;
            if (source == zoom_p) {
                scale = scale + 0.1;
            } else if (source == zoom_m) {
                scale = scale - 0.1;
            }

            graphPanel.reScale(scale);
            scroll.getViewport().revalidate();
        }

    }

    /**
     * Graphics panel
     */
    protected class GraphPanel extends JPanel implements Cloneable, Serializable {

        public AffineTransform at = new AffineTransform(); // identity transform

        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            BufferedImage im = image.image;

            Graphics2D g2 = (Graphics2D) g;
            g2.transform(at);
            if (im != null) {
                g.drawImage(im, 0, 0, null);
            }

        }

        public void reScale(double scale) {

            at = AffineTransform.getScaleInstance(scale, scale);
            BufferedImage im = image.image;

            int width = (int) (scale * im.getWidth());
            int height = (int) (scale * im.getHeight());
            setPreferredSize(new Dimension(width, height));
            repaint();
        }

    }

    /**
     * Coordinates window
     * @todo this code should be moved to Ruler class
     */
    protected class CoordinatesListener implements ActionListener, Cloneable, Serializable {

        @Override
        public void actionPerformed(ActionEvent e) {
            /*
             if(coordinatesBox == null) {
             coordinatesBox = new CoordinatesBox(JImageViewer.this);
             }
             */

            coordinatesBox = new CoordinatesBox(JImageViewer.this);
            // graphPanel.
            coordinatesBox.setVisible(true);
            coordinatesBox.repaint();
        }
    }

    protected class ScrollComponentListener extends ComponentAdapter implements Cloneable, Serializable {

        public void componentResized(ComponentEvent e) {
            // System.out.println("resized!");
            if (coordinatesBox != null) {
                coordinatesBox.recalculate();
            }

        }
    }

    protected class ScrollAdjustmentListener implements AdjustmentListener, Cloneable, Serializable {

        @Override
        public void adjustmentValueChanged(AdjustmentEvent arg0) {
            // System.out.println("scroll - adjust!");
            if (coordinatesBox != null) {
                coordinatesBox.recalculate();
            }

        }

    }

}
