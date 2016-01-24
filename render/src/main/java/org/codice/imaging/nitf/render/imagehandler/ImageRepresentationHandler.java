package org.codice.imaging.nitf.render.imagehandler;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * An ImageRepresentationHandler calculates the values for a given pixel based on the current pixel value
 * and the value of the band being read.  This interface abstracts the calculation of a single
 * pixel value based on one or more band values for that pixel.
 */

@FunctionalInterface
public interface ImageRepresentationHandler {
    /**
     * Applies the bandValue to currentValue based on bandIndex.
     *
     * @param imageSegmentHeader the image segment header that provides the interpretation of this pixel
     * @param currentValue - the current value for the pixel.
     * @param bandValue - the value of the band being applied.
     * @param bandIndex - the index of the band being applied, zero-indexed.
     * @return - the new value for the current pixel.
     */
    int renderPixel(NitfImageSegmentHeader imageSegmentHeader, int currentValue, int bandValue, int bandIndex);
}
