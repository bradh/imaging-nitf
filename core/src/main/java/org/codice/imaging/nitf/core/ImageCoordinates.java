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

/**
    Coordinates of an image.
*/
public class ImageCoordinates {

    private ImageCoordinatePair coordinate00;
    private ImageCoordinatePair coordinate0MaxCol;
    private ImageCoordinatePair coordinateMaxRowMaxCol;
    private ImageCoordinatePair coordinateMaxRow0;

    private static final int COORDINATE00_INDEX = 0;
    private static final int COORDINATE0MAXCOL_INDEX = 1;
    private static final int COORDINATEMAXROWMAXCOL_INDEX = 2;
    private static final int COORDINATEMAXROW0_INDEX = 3;

    private ImageCoordinates() {
    }

    /**
        Constructor.

        @param coord array of four coordinate pairs, in the order [0,0], [0, MaxCol], [MaxRow, MaxCol], [MaxRow, 0].
    */
    public ImageCoordinates(final ImageCoordinatePair[] coord) {
        coordinate00 = coord[COORDINATE00_INDEX];
        coordinate0MaxCol = coord[COORDINATE0MAXCOL_INDEX];
        coordinateMaxRowMaxCol = coord[COORDINATEMAXROWMAXCOL_INDEX];
        coordinateMaxRow0 = coord[COORDINATEMAXROW0_INDEX];
    }

    /**
        Get the coordinate pair for [0,0].

        @return corresponding coordinate pair.
    */
    public final ImageCoordinatePair getCoordinate00() {
        return coordinate00;
    }

    /**
        Get the coordinate pair for [0,MaxCol].

        @return corresponding coordinate pair.
    */
    public final ImageCoordinatePair getCoordinate0MaxCol() {
        return coordinate0MaxCol;
    }

    /**
        Get the coordinate pair for [MaxRow,MaxCol].

        @return corresponding coordinate pair.
    */
    public final ImageCoordinatePair getCoordinateMaxRowMaxCol() {
        return coordinateMaxRowMaxCol;
    }

    /**
        Get the coordinate pair for [MaxRow,0].

        @return corresponding coordinate pair.
    */
    public final ImageCoordinatePair getCoordinateMaxRow0() {
        return coordinateMaxRow0;
    }

    static ImageCoordinates getDefault() {
        ImageCoordinates coordinates = new ImageCoordinates();
        coordinates.coordinate00 = ImageCoordinatePair.getDefault();
        coordinates.coordinate0MaxCol = ImageCoordinatePair.getDefault();
        coordinates.coordinateMaxRow0 = ImageCoordinatePair.getDefault();
        coordinates.coordinateMaxRowMaxCol = ImageCoordinatePair.getDefault();
        return coordinates;
    }
}
