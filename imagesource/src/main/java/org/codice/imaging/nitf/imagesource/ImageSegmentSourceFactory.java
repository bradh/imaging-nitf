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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.ImageSegmentFactory;

/**
 * Factory methods for creating ImageSegments from ImageIO sources.
 */
public final class ImageSegmentSourceFactory {

    private ImageSegmentSourceFactory() {
    }

    /**
     * Create a new ImageSegment from the specified stream.
     *
     * The input stream is assumed to hold image data that can be handled by an
     * ImageIO reader.
     *
     * @param input the input stream providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the image data
     * @throws IOException if the stream could not be read from.
     */
    public static ImageSegment getImageSegmentFrom3ByteBGR(final InputStream input, final FileType fileType) throws IOException {
        return getImageSegmentFrom3ByteBGR(ImageIO.read(input), fileType);
    }

    /**
     * Create a new ImageSegment from the specified File.
     *
     * The file is assumed to hold image data that can be handled by an ImageIO
     * reader.
     *
     * @param file the file providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the image data
     * @throws IOException if the file could not be read from.
     */
    public static ImageSegment getImageSegmentFrom3ByteBGR(final File file, final FileType fileType) throws IOException {
        return getImageSegmentFrom3ByteBGR(ImageIO.read(file), fileType);
    }

    /**
     * Create a new ImageSegment from a BufferedImage.
     *
     * This version converts the input data to 3BYTE_BGR, if needed.
     *
     * @param bufferedImage the BufferedImage providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the buffered image
     */
    public static ImageSegment getImageSegmentFrom3ByteBGR(final BufferedImage bufferedImage, final FileType fileType) {
        ImageSource imageSource = new ImageSource3ByteBGR(bufferedImage, fileType);
        ImageSegment imageSegment = ImageSegmentFactory.getDefault(fileType);
        imageSource.updateValues(imageSegment);
        return imageSegment;
    }

    /**
     * Create a new ImageSegment from a BufferedImage.
     *
     * This version converts the input data to BYTE_GRAY, if needed.
     *
     * @param bufferedImage the BufferedImage providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the buffered image
     */
    public static ImageSegment getImageSegmentFromByteGray(final BufferedImage bufferedImage, final FileType fileType) {
        ImageSource imageSource = new ImageSourceByteGray(bufferedImage, fileType);
        ImageSegment imageSegment = ImageSegmentFactory.getDefault(fileType);
        imageSource.updateValues(imageSegment);
        return imageSegment;
    }

    /**
     * Create a new ImageSegment from a BufferedImage.
     *
     * This version converts the input data to USHORT_GRAY, if needed.
     *
     * @param bufferedImage the BufferedImage providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the buffered image
     */
    public static ImageSegment getImageSegmentFromUShortGray(final BufferedImage bufferedImage, final FileType fileType) {
        ImageSource imageSource = new ImageSourceUShortGray(bufferedImage, fileType);
        ImageSegment imageSegment = ImageSegmentFactory.getDefault(fileType);
        imageSource.updateValues(imageSegment);
        return imageSegment;
    }

    /**
     * Create a new ImageSegment from a BufferedImage.
     *
     * This version makes a "best effort" conversion to something similar to the
     * input data.
     *
     * Note that there is currently no support for TYPE_BYTE_BINARY or
     * TYPE_BYTE_INDEXED.
     *
     * @param bufferedImage the BufferedImage providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     * @return ImageSegment corresponding to the buffered image, or null if the
     * image could not be handled.
     */
    public static ImageSegment getImageSegment(final BufferedImage bufferedImage, final FileType fileType) {
        switch (bufferedImage.getType()) {
            case BufferedImage.TYPE_CUSTOM:
            case BufferedImage.TYPE_3BYTE_BGR:
            case BufferedImage.TYPE_INT_RGB:
            case BufferedImage.TYPE_INT_ARGB:
            case BufferedImage.TYPE_INT_ARGB_PRE:
            case BufferedImage.TYPE_INT_BGR:
            case BufferedImage.TYPE_4BYTE_ABGR:
            case BufferedImage.TYPE_4BYTE_ABGR_PRE:
            case BufferedImage.TYPE_USHORT_565_RGB:
            case BufferedImage.TYPE_USHORT_555_RGB:
                return getImageSegmentFrom3ByteBGR(bufferedImage, fileType);
            case BufferedImage.TYPE_BYTE_GRAY:
                return getImageSegmentFromByteGray(bufferedImage, fileType);
            case BufferedImage.TYPE_USHORT_GRAY:
                return getImageSegmentFromUShortGray(bufferedImage, fileType);
            default:
                throw new UnsupportedOperationException(String.format("Could not create ImageSegment for image type: %d", bufferedImage.getType()));
        }
    }
}
