package org.codice.imaging.nitf.render.imagehandler;

import java.awt.Graphics2D;
import java.io.IOException;
import java.nio.IntBuffer;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

public class BandSequentialImageModeHandler extends SharedImageModeHandler implements ImageModeHandler {
    private static final String NULL_ARG_ERROR_MESSAGE =
            "BandSequentialImageModeHandler(): argument '%s' may not be null.";


    @Override
    void handleMatrix(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, Graphics2D targetImage) {
        for (int bandIndex = 0; bandIndex < imageSegmentHeader.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            forEachBlock(imageSegmentHeader, matrix, block -> readBlock(imageSegmentHeader, block, imageInputStream,
                    imageRepresentationHandler, index));
        }

        forEachBlock(imageSegmentHeader, matrix, block -> { renderBlock(imageSegmentHeader, targetImage, block);
            block.getData().clear();
        });
    }

    private void readBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block,
            ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, int bandIndex) {

        final IntBuffer data = block.getData();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();

        try {
            for (int row = 0; row < blockWidth; row++) {
                for (int column = 0; column < blockHeight; column++) {
                    int i = row * blockWidth + column;
                    int bandValue = imageInputStream.read();
                    int mappedBand = mapImageBand(imageSegmentHeader, bandIndex);
                    if (mappedBand != NOT_VISIBLE_MAPPED) {
                        data.put(i, imageRepresentationHandler.renderPixel(data.get(i), bandValue, mappedBand));
                    }
                }
            }

            applyMask(block);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyMask(ImageBlock block) throws IOException {

        final IntBuffer data = block.getData();
        final int dataSize = data.array().length;

        if (imageMask != null) {
            for (int pixel = 0; pixel < dataSize; ++pixel) {
                data.put(pixel, data.get(pixel) | 0xFF000000);

                if (imageMask.isPadPixel(data.get(pixel))) {
                    data.put(pixel, 0x00000000);
                }
            }
        } else {
            for (int pixel = 0; pixel < dataSize; ++pixel) {
                data.put(pixel, data.get(pixel) | 0xFF000000);
            }
        }
    }

    @Override
    String getNullArgErrorMessage() {
        return NULL_ARG_ERROR_MESSAGE;
    }

    @Override
    ImageMode getExpectedImageMode() {
        return ImageMode.BANDSEQUENTIAL;
    }
}
