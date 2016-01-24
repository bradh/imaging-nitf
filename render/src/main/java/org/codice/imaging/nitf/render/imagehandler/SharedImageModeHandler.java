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
import java.awt.image.DataBufferInt;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.render.ImageMask;

/**
 *
 * @author bradh
 */
abstract class SharedImageModeHandler {
    protected static final int NOT_VISIBLE_MAPPED = -1;
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
        BufferedImage img = new BufferedImage(blockWidth, blockHeight, BufferedImage.TYPE_INT_ARGB);
        int[] imgData = ((DataBufferInt) img.getRaster().getDataBuffer()).getData();
        System.arraycopy(block.getData().array(), 0, imgData, 0, block.getData().array().length);
        targetImage.drawImage(img, block.getColumn() * blockHeight, block.getRow() * blockWidth, null);
    }

    /**
     *
     * {@inheritDoc}
     */
    public void handleImage(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream, Graphics2D targetImage, ImageRepresentationHandler imageRepresentationHandler) throws IOException {
        validateHandleImageArgs(imageInputStream, imageSegmentHeader, targetImage, imageRepresentationHandler);
        readOrBuildImageMask(imageSegmentHeader, imageInputStream);
        ImageBlockMatrix matrix = new ImageBlockMatrix(imageSegmentHeader.getNumberOfBlocksPerColumn(), imageSegmentHeader.getNumberOfBlocksPerRow(), imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal() * imageSegmentHeader.getNumberOfPixelsPerBlockVertical());
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

    protected int mapImageBand(NitfImageSegmentHeader imageSegmentHeader, int bandIndex) {
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
                mappedBand = RowInterleaveImageModeHandler.NOT_VISIBLE_MAPPED;
        }
        return mappedBand;
    }
}
