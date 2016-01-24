/*
 * Copyright (C) 2016 bradh.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301  USA
 */
package org.codice.imaging.nitf.render.imagehandler;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 *
 * @author bradh
 */
public class MappedBandRepresentationHandler implements ImageRepresentationHandler {
    private static final int NOT_VISIBLE_MAPPED = -1;

    private int mapImageBand(NitfImageSegmentHeader imageSegmentHeader, int bandIndex) {
        int mappedBand;
        switch (imageSegmentHeader.getImageBandZeroBase(bandIndex).getImageRepresentation()) {
            case "R":
                mappedBand = 2;
                break;
            case "G":
                mappedBand = 1;
                break;
            case "B":
                mappedBand = 0;
                break;
            default:
                mappedBand = NOT_VISIBLE_MAPPED;
        }
        return mappedBand;
    }

    @Override
    public int renderPixel(NitfImageSegmentHeader imageSegmentHeader, int currentValue, int bandValue, int bandIndex) {
        int mappedBand = mapImageBand(imageSegmentHeader, bandIndex);
        if (mappedBand != NOT_VISIBLE_MAPPED) {
            return (currentValue | (bandValue << (8 * mappedBand)));
        }
        return currentValue;
    }

}
