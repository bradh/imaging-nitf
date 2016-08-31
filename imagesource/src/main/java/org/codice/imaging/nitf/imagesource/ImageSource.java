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
package org.codice.imaging.nitf.imagesource;

import java.util.List;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinates;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import org.codice.imaging.nitf.core.image.TargetId;

/**
 * Source of data for NITF image segments.
 */
public interface ImageSource {

    /**
     * Get the File Type (NITF version) for this segment.
     *
     * @return FileType for this segment.
     */
    FileType getFileType();

    /**
     * Get the actual number of bits per pixel per band.
     *
     * See ImageSegment for meaning.
     *
     * @return actual number of bits per pixel per band.
     */
    int getActualBitsPerPixelPerBand();

    /**
     * Get image band information.
     *
     * See ImageSegment for meaning.
     *
     * @return list of image bands, in order.
     */
    List<ImageBand> getImageBands();

    /**
     * Get image category (ICAT).
     *
     * See ImageSegment for meaning.
     *
     * @return image category.
     */
    ImageCategory getImageCategory();

    /**
     * Get the image compression type.
     *
     * See ImageSegment for meaning.
     *
     * @return image compression type
     */
    ImageCompression getImageCompression();

    /**
     * Get the image coordinates representation.
     *
     * See ImageSegment for meaning.
     *
     * @return image coordinates representation.
     */
    ImageCoordinatesRepresentation getImageCoordinatesRepresentation();

    /**
     * Get the image mode.
     *
     * See ImageSegment for meaning.
     *
     * @return image mode.
     */
    ImageMode getImageMode();

    /**
     * Get the image representation (IREP).
     *
     * See ImageSegment for meaning.
     *
     * @return image representation.
     */
    ImageRepresentation getImageRepresentation();

    /**
     * Get the number of bits per pixel per band.
     *
     * See ImageSegment for meaning.
     *
     * @return number of bits per pixel per band.
     */
    int getNumberOfBitsPerPixelPerBand();

    /**
     * Get the number of pixel columns.
     *
     * See ImageSegment for meaning.
     *
     * @return number of columns in the image.
     */
    long getNumberOfColumns();

    /**
     * Get the number of pixel rows.
     *
     * See ImageSegment for meaning.
     *
     * @return number of rows in the image.
     */
    long getNumberOfRows();

    /**
     * Get pixel justification.
     *
     * See ImageSegment for meaning.
     *
     * @return pixel justification.
     */
    PixelJustification getPixelJustification();

    /**
     * Get pixel value type.
     *
     * See ImageSegment for meaning.
     *
     * @return pixel value type.
     */
    PixelValueType getPixelValueType();

    /**
     * Get the image data.
     *
     * Typically you want to set the image data on the segment to this stream.
     *
     * @return stream containing the image data.
     */
    ImageInputStream getData();

    /**
     * Get the length of the image data.
     *
     * @return stream data length.
     */
    long getDataLength();

    /**
     * Get the number of blocks per column.
     *
     * See ImageSegment for more info.
     *
     * @return the number of blocks per column.
     */
    int getNumberOfBlocksPerColumn();

    /**
     * Get the number of pixels per block vertical.
     *
     * See ImageSegment for more info.
     *
     * @return number of pixels per block vertical.
     */
    int getNumberOfPixelsPerBlockVerticalRaw();

    /**
     * Get the number of blocks per row.
     *
     * See ImageSegment for more info.
     *
     * @return the number of blocks per row.
     */
    int getNumberOfBlocksPerRow();

    /**
     * Get the number of pixels per block horizontal.
     *
     * See ImageSegment for more info.
     *
     * @return number of pixels per block horizontal.
     */
    int getNumberOfPixelsPerBlockHorizontalRaw();

    /**
     * Get the image magnification.
     *
     * See ImageSegment for more info.
     *
     * @return image magnification, as a string.
     */
    String getImageMagnification();


    /**
     * Get the compression rate (COMRAT).
     *
     * See ImageSegment for more info.
     *
     * @return the compression rate, as a string. May return an empty string if not applicable.
     */
    String getCompressionRate();

    /**
     * Get the image source (ISORCE).
     *
     * See ImageSegment for more info.
     *
     * @return the image source, or an empty string.
     */
    String getImageSource();

    /**
     * Get the image location column (ILOC, column part).
     *
     * See ImageSegment for more info.
     *
     * @return image location column.
     */
    int getImageLocationColumn();

    /**
     * Get the image location row (ILOC, row part).
     *
     * See ImageSegment for more info.
     *
     * @return image location row.
     */
    int getImageLocationRow();

    /**
     * Get the image coordinates.
     *
     * See ImageSegment for more info.
     *
     * @return image coordinates
     */
    ImageCoordinates getImageCoordinates();

    /**
     * Get the image comments
     *
     * See ImageSegment for more info.
     *
     * @return image comments
     */
    List<String> getImageComments();

    /**
     * Get the image date and time.
     *
     * See ImageSegment for more info.
     *
     * @return image date time
     */
    DateTime getImageDateTime();

    /**
     * Get the image display level.
     *
     * See ImageSegment for more info.
     *
     * @return image display level.
     */
    int getImageDisplayLevel();

    /**
     * Get the second image identifier (IID2).
     *
     * See ImageSegment for more info.
     *
     * @return image identifier 2.
     */
    String getImageIdentifier2();

    /**
     * Get the image target identifier (TGTID)
     *
     * See ImageSegment for more info.
     *
     * @return target id.
     */
    TargetId getImageTargetId();

    /**
     * Update the provided image segment with values from this source.
     *
     * @param imageSegment the image segment to update.
     */
    void updateValues(final ImageSegment imageSegment);
}
