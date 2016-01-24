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
package org.codice.imaging.nitf.render.imagehandler;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.render.ImageMask;

/**
 *
 * @author bradh
 */
abstract class SharedImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE
            = "%s: argument '%s' may not be null.";

    protected abstract String getHandlerClassName();

    protected abstract ImageMode getExpectedImageMode();

    protected ImageMask imageMask = null;

    private void checkNull(Object value, String valueName) {
        if (value == null) {
            throw new IllegalArgumentException(String.format(NULL_ARG_ERROR_MESSAGE, getHandlerClassName(), valueName));
        }
    }

    protected void forEachBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, Consumer<ImageBlock> intBufferConsumer) {
        for (int i = 0; i < imageSegmentHeader.getNumberOfBlocksPerColumn(); i++) {
            for (int j = 0; j < imageSegmentHeader.getNumberOfBlocksPerRow(); j++) {
                ImageBlock currentBlock = matrix.getImageBlock(i, j);
                intBufferConsumer.accept(currentBlock);
            }
        }
    }

    protected void renderBlock(NitfImageSegmentHeader imageSegmentHeader, Graphics2D targetImage, ImageBlock block) {
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        if (block.getData().getDataType() == DataBuffer.TYPE_INT) {
            RenderBlockInt(blockWidth, blockHeight, block, targetImage);
        } else if (block.getData().getDataType() == DataBuffer.TYPE_BYTE) {
            RenderBlockByte(imageSegmentHeader, block, targetImage);
        } else {
            throw new UnsupportedOperationException("Cannot handle type:" + block.getData().getDataType());
        }
    }

    private void RenderBlockInt(final int blockWidth, final int blockHeight, ImageBlock block, Graphics2D targetImage) {
        BufferedImage img = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
        int[] imgData = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        DataBufferInt sourceBlockData = (DataBufferInt) block.getData();
        System.arraycopy(sourceBlockData.getData(), 0, imgData, 0, sourceBlockData.getData().length);
        targetImage.drawImage(img, block.getColumn() * blockHeight, block.getRow() * blockWidth, null);
    }

    private void RenderBlockByte(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block, Graphics2D targetImage) {
        BufferedImage img;
        if (imageSegmentHeader.getImageRepresentation() == ImageRepresentation.MONOCHROME) {
            img = new BufferedImage(imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                    imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                    BufferedImage.TYPE_BYTE_GRAY);
        } else if (imageSegmentHeader.getImageRepresentation() == ImageRepresentation.RGBLUT) {
            IndexColorModel colourModel = new IndexColorModel(imageSegmentHeader.getActualBitsPerPixelPerBand(),
                    imageSegmentHeader.getImageBandZeroBase(0).getNumLUTEntries(),
                    imageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                    imageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                    imageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
            img = new BufferedImage(imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                    imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                    BufferedImage.TYPE_BYTE_INDEXED, colourModel);
        } else {
            throw new UnsupportedOperationException("No rendering support for " + imageSegmentHeader.getImageRepresentation().getTextEquivalent());
        }
        byte[] imgData = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
        DataBufferByte sourceBlockData = (DataBufferByte) block.getData();
        System.arraycopy(sourceBlockData.getData(), 0, imgData, 0, sourceBlockData.getData().length);
        targetImage.drawImage(img,
                block.getColumn() * imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                block.getRow() * imageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                null);

    }
    /**
     *
     * {@inheritDoc}
     */
    public void handleImage(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream, Graphics2D targetImage, ImageRepresentationHandler imageRepresentationHandler) throws IOException {
        validateHandleImageArgs(imageInputStream, imageSegmentHeader, targetImage, imageRepresentationHandler);
        readOrBuildImageMask(imageSegmentHeader, imageInputStream);
        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegmentHeader);
        handleMatrix(imageSegmentHeader, matrix, imageInputStream, imageRepresentationHandler, targetImage);
    }

    private void validateHandleImageArgs(ImageInputStream imageInputStream, NitfImageSegmentHeader imageSegmentHeader, Graphics2D targetImage, ImageRepresentationHandler imageRepresentationHandler) {
        checkNull(imageInputStream, "imageInputStream");
        checkNull(imageSegmentHeader, "imageSegmentHeader");
        checkNull(targetImage, "targetImage");
        checkNull(imageRepresentationHandler, "imageRepresentationHandler");
        ImageMode expectedImageMode = getExpectedImageMode();
        if (!expectedImageMode.equals(imageSegmentHeader.getImageMode())) {
            throw new IllegalStateException(String.format("PixelInterleaveImageModeHandler(): argument 'imageSegmentHeader' must have an ImageMode of '%s'.", expectedImageMode.getTextEquivalent()));
        }
    }

    private void readOrBuildImageMask(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream) throws IOException {
        if (ImageCompression.NOTCOMPRESSEDMASK.equals(imageSegmentHeader.getImageCompression())) {
            imageMask = new ImageMask(imageSegmentHeader, imageInputStream);
        } else {
            imageMask = new ImageMask(imageSegmentHeader);
        }
    }

    abstract void handleMatrix(NitfImageSegmentHeader imageSegmentHeader,
            ImageBlockMatrix matrix,
            ImageInputStream imageInputStream,
            ImageRepresentationHandler imageRepresentationHandler,
            Graphics2D targetImage);
}
