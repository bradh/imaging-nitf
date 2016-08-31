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
import java.awt.image.SampleModel;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.common.DateTime;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinates;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import org.codice.imaging.nitf.core.image.TargetId;

/**
 * Shared implementation for ImageSource based on ImageIO.
 */
public abstract class ImageIOImageSourceImpl implements ImageSource {

    /**
     * Buffered image, in a format we know we can handle.
     */
    protected BufferedImage mBufferedImage;

    /**
     * File type (NITF version) of the image segment that will be created.
     */
    protected FileType mFileType;

    /**
     * Constructor.
     *
     * @param fileType the file type of the image segment that will be created.
     */
    protected ImageIOImageSourceImpl(final FileType fileType) {
        mFileType = fileType;
    }

    @Override
    public final FileType getFileType() {
        return mFileType;
    }

    // TODO: IID1
    // TODO: TREs?
    @Override
    public final String getImageSource() {
        return "";
    }

    @Override
    public final long getNumberOfColumns() {
        return mBufferedImage.getWidth();
    }

    @Override
    public final int getNumberOfBlocksPerColumn() {
        return mBufferedImage.getNumYTiles();
    }

    @Override
    public final long getNumberOfRows() {
        return mBufferedImage.getHeight();
    }

    @Override
    public final int getNumberOfBlocksPerRow() {
        return mBufferedImage.getNumXTiles();
    }

    @Override
    public final ImageCategory getImageCategory() {
        return ImageCategory.VISUAL;
    }

    @Override
    public final PixelValueType getPixelValueType() {
        return PixelValueType.INTEGER;
    }

    @Override
    public final int getNumberOfBitsPerPixelPerBand() {
        // TODO: this probably needs to round up if we ever support non-byte / short samples.
        return getActualBitsPerPixelPerBand();
    }

    @Override
    public final int getActualBitsPerPixelPerBand() {
        SampleModel sampleModel = mBufferedImage.getSampleModel();
        int sampleSize = 0;
        for (int band = 0; band < sampleModel.getNumBands(); band++) {
            sampleSize = Math.max(sampleSize, sampleModel.getSampleSize(band));
        }
        return sampleSize;
    }

    @Override
    public final ImageMode getImageMode() {
        return ImageMode.PIXELINTERLEVE;
    }

    @Override
    public final ImageCompression getImageCompression() {
        return ImageCompression.NOTCOMPRESSED;
    }

    @Override
    public final String getCompressionRate() {
        return "";
    }

    @Override
    public final PixelJustification getPixelJustification() {
        return PixelJustification.RIGHT;
    }

    @Override
    public final ImageCoordinatesRepresentation getImageCoordinatesRepresentation() {
        return ImageCoordinatesRepresentation.NONE;
    }

    @Override
    public final ImageCoordinates getImageCoordinates() {
        return null;
    }

    @Override
    public final int getNumberOfPixelsPerBlockHorizontalRaw() {
        return mBufferedImage.getTileWidth();
    }

    @Override
    public final int getNumberOfPixelsPerBlockVerticalRaw() {
        return mBufferedImage.getTileHeight();
    }

    @Override
    public final String getImageMagnification() {
        return "1.0";
    }

    @Override
    public final int getImageLocationColumn() {
        return 0;
    }

    @Override
    public final int getImageLocationRow() {
        return 0;
    }

    @Override
    public final TargetId getImageTargetId() {
        TargetId tgtid = new TargetId();
        return tgtid;
    }

    @Override
    public final String getImageIdentifier2() {
        return "";
    }

    @Override
    public final int getImageDisplayLevel() {
        return 1;
    }

    @Override
    public final DateTime getImageDateTime() {
        // TODO: try harder to find a real date
        DateTime dateTime = new DateTime();
        dateTime.set(ZonedDateTime.now());
        // TODO: set the source format if the file type is NITF 2.0.
        return dateTime;
    }

    @Override
    public final List<String> getImageComments() {
        return new ArrayList<>();
    }

    /**
     * The image bands.
     *
     * @return list of image bands, in order corresponding to data.
     */
    @Override
    public abstract List<ImageBand> getImageBands();

    @Override
    public final void updateValues(final ImageSegment imageSegment) {
        imageSegment.setFileType(getFileType());
        // TODO: IID1
        if (getImageDateTime() != null) {
            imageSegment.setImageDateTime(getImageDateTime());
        }
        imageSegment.setImageTargetId(getImageTargetId());
        imageSegment.setImageIdentifier2(getImageIdentifier2());
        imageSegment.setImageSource(getImageSource());
        imageSegment.setNumberOfRows(getNumberOfRows());
        imageSegment.setNumberOfColumns(getNumberOfColumns());
        imageSegment.setPixelValueType(getPixelValueType());
        imageSegment.setImageRepresentation(getImageRepresentation());
        imageSegment.setImageCategory(getImageCategory());
        imageSegment.setActualBitsPerPixelPerBand(getActualBitsPerPixelPerBand());
        imageSegment.setPixelJustification(getPixelJustification());
        imageSegment.setImageCoordinatesRepresentation(getImageCoordinatesRepresentation());
        // TODO: IGEOLO
        for (String imageComment : getImageComments()) {
            imageSegment.addImageComment(imageComment);
        }
        imageSegment.setImageCompression(getImageCompression());
        imageSegment.setCompressionRate(getCompressionRate());
        for (ImageBand imageBand : getImageBands()) {
            imageSegment.addImageBand(imageBand);
        }
        imageSegment.setImageMode(getImageMode());
        imageSegment.setNumberOfBlocksPerRow(getNumberOfBlocksPerRow());
        imageSegment.setNumberOfBlocksPerColumn(getNumberOfBlocksPerColumn());
        imageSegment.setNumberOfPixelsPerBlockHorizontalRaw(getNumberOfPixelsPerBlockHorizontalRaw());
        imageSegment.setNumberOfPixelsPerBlockVerticalRaw(getNumberOfPixelsPerBlockVerticalRaw());
        imageSegment.setNumberOfBitsPerPixelPerBand(getNumberOfBitsPerPixelPerBand());
        imageSegment.setImageDisplayLevel(getImageDisplayLevel());
        imageSegment.setImageLocationColumn(getImageLocationColumn());
        imageSegment.setImageLocationRow(getImageLocationRow());
        imageSegment.setImageMagnification(getImageMagnification());
        // TODO: TREs?
        imageSegment.setDataLength(getDataLength());
        imageSegment.setData(getData());
    }
}
