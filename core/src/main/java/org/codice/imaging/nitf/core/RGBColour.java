/*
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details. A copy of the GNU Lesser General Public License
 * is distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 */
package org.codice.imaging.nitf.core;

import java.awt.Color;
import java.text.ParseException;

/**
    Red / Green / Blue colour representation.
*/
public class RGBColour {

    private byte red = 0x00;
    private byte green = 0x00;
    private byte blue = 0x00;

    private static final int REQUIRED_DATA_LENGTH = 3;
    private static final int UNSIGNED_BYTE_MASK = 0xFF;
    private static final int RED_OFFSET = 0;
    private static final int GREEN_OFFSET = 1;
    private static final int BLUE_OFFSET = 2;

    /**
        Constructor.

        @param rgb three element array of the red, green and blue component values for the colour
        @throws ParseException if the array does not have the right length.
    */
    public RGBColour(final byte[] rgb) throws ParseException {
        if (rgb.length != REQUIRED_DATA_LENGTH) {
            throw new ParseException("Incorrect number of bytes in RGB constructor array", 0);
        }
        red = rgb[RED_OFFSET];
        green = rgb[GREEN_OFFSET];
        blue = rgb[BLUE_OFFSET];
    }

    /**
        Return the red component of the colour.

        @return red component of the colour
    */
    public final byte getRed() {
        return red;
    }

    /**
        Return the green component of the colour.

        @return green component of the colour
    */
    public final byte getGreen() {
        return green;
    }

    /**
        Return the blue component of the colour.

        @return blue component of the colour
    */
    public final byte getBlue() {
        return blue;
    }

    @Override
    public final String toString() {
        return String.format("[0x%02x,0x%02x,0x%02x]",
                            (int) (red & UNSIGNED_BYTE_MASK),
                            (int) (green & UNSIGNED_BYTE_MASK),
                            (int) (blue & UNSIGNED_BYTE_MASK));
    }

    /**
     * Return the RGBColour as an AWT Color.
     *
     * @return the colour as a Color.
     */
    public final Color toColour() {
        return new Color((int) (red & UNSIGNED_BYTE_MASK),
                         (int) (green & UNSIGNED_BYTE_MASK),
                         (int) (blue & UNSIGNED_BYTE_MASK));
    }

    /**
     * Return the RGBColour as a byte array.
     *
     * This is the same format as was read in (so is suitable for writing out).
     *
     * @return the colour as a byte array
     */
    public final byte[] toByteArray() {
        byte[] bytes = new byte[REQUIRED_DATA_LENGTH];
        bytes[RED_OFFSET] = red;
        bytes[GREEN_OFFSET] = green;
        bytes[BLUE_OFFSET] = blue;
        return bytes;
    }
}
