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
 * Implementation of the Interface Serializable for BufferedImage class.
 *
 * @author Lukasz Bacik <mail@luka.sh>
 */
package sh.luka.gui;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;

/**
 * based on code that has been found in Internet.
 */
public class BufferedImageSerializable implements Cloneable, Serializable {

    public BufferedImage image = null;

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        if (image != null) {
            ImageIO.write(image, "png", ImageIO.createImageOutputStream(out));
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        image = ImageIO.read(ImageIO.createImageInputStream(in));
    }

}
