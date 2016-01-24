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
import java.awt.image.DataBuffer;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * ImageModeHandler implementation that handles RowInterleave image mode
 */
public class BlockInterleaveImageModeHandler extends SharedImageModeHandler implements ImageModeHandler {

    @Override
    protected ImageMode getExpectedImageMode() {
        return ImageMode.BLOCKINTERLEVE;
    }

    @Override
    // TODO: see if we can move this to the superclass
    void handleMatrix(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, Graphics2D targetImage) {
        forEachBlock(imageSegmentHeader, matrix, block -> {
            readBlock(imageSegmentHeader, block, imageInputStream, imageRepresentationHandler);
            renderBlock(imageSegmentHeader, targetImage, block);
        });
    }

    private void readBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block,
            ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler) {

        final DataBuffer data = block.getData();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();

        if (imageMask.isMaskedBlock(block.getRow() * imageSegmentHeader.getNumberOfBlocksPerRow() + block.getColumn(), 0)) {
            return;
        }

        try {
            for (int bandIndex = 0; bandIndex < imageSegmentHeader.getNumBands(); ++bandIndex) {
                for (int row = 0; row < blockWidth; row++) {
                    for (int column = 0; column < blockHeight; column++) {
                        int i = row * blockWidth + column;
                        int bandValue = imageInputStream.read();
                        data.setElem(i, imageRepresentationHandler.renderPixel(imageSegmentHeader, data.getElem(i), bandValue, bandIndex));
                        if (!imageMask.isPadPixel(i)) {
                            data.setElem(i, data.getElem(i) | 0xFF000000);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String getHandlerClassName() {
        return this.getClass().getName();
    }
}
