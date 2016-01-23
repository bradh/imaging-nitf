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
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * ImageModeHandler implementation that handles PixelInterleave image mode
 */
public class PixelInterleaveImageModeHandler extends SharedImageModeHandler implements ImageModeHandler {

    private static final String NULL_ARG_ERROR_MESSAGE
            = "PixelInterleaveImageModeHandler(): argument '%s' may not be null.";

    public PixelInterleaveImageModeHandler() {
    }


    private void readBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block,
            ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler) {

        final IntBuffer data = block.getData();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();

        if (imageMask.isMaskedBlock(block.getRow() * imageSegmentHeader.getNumberOfBlocksPerRow() + block.getColumn(), 0)) {
            return;
        }

        try {
            for (int row = 0; row < blockWidth; row++) {
                for (int column = 0; column < blockHeight; column++) {
                    int i = row * blockWidth + column;
                    for (int bandIndex = 0; bandIndex < imageSegmentHeader.getNumBands(); ++bandIndex) {
                        int bandValue = imageInputStream.read();
                        data.put(i, imageRepresentationHandler.renderPixel(data.get(i), bandValue, bandIndex));
                    }
                    if (!imageMask.isPadPixel(i)) {
                        data.put(i, data.get(i) | 0xFF000000);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getNullArgErrorMessage() {
        return NULL_ARG_ERROR_MESSAGE;
    }

    @Override
    protected ImageMode getExpectedImageMode() {
        return ImageMode.PIXELINTERLEVE;
    }

    @Override
    void handleMatrix(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, Graphics2D targetImage) {
        forEachBlock(imageSegmentHeader, matrix, block -> {
            readBlock(imageSegmentHeader, block, imageInputStream, imageRepresentationHandler);
            renderBlock(imageSegmentHeader, targetImage, block);
            block.getData().clear();
        });
    }
}
