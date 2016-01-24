package org.codice.imaging.nitf.render.imagehandler;

import java.awt.Graphics2D;
import java.awt.image.DataBufferInt;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

public class BandSequentialImageModeHandler extends SharedImageModeHandler implements ImageModeHandler {

    @Override
    void handleMatrix(NitfImageSegmentHeader imageSegmentHeader, ImageBlockMatrix matrix, ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, Graphics2D targetImage) {
        for (int bandIndex = 0; bandIndex < imageSegmentHeader.getNumBands(); bandIndex++) {
            final int index = bandIndex;

            forEachBlock(imageSegmentHeader, matrix, block -> readBlock(imageSegmentHeader, block, imageInputStream,
                    imageRepresentationHandler, index));
        }
        forEachBlock(imageSegmentHeader, matrix, block -> {
            renderBlock(imageSegmentHeader, targetImage, block);
            block.clear();
        });

    }

    private void readBlock(NitfImageSegmentHeader imageSegmentHeader, ImageBlock block,
            ImageInputStream imageInputStream, ImageRepresentationHandler imageRepresentationHandler, int bandIndex) {

        final DataBufferInt data = block.getData();
        final int blockHeight = imageSegmentHeader.getNumberOfPixelsPerBlockVertical();
        final int blockWidth = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal();

        try {
            for (int row = 0; row < blockWidth; row++) {
                for (int column = 0; column < blockHeight; column++) {
                    int i = row * blockWidth + column;
                    int bandValue = imageInputStream.read();
                    data.setElem(i, imageRepresentationHandler.renderPixel(imageSegmentHeader, data.getElem(i), bandValue, bandIndex));
                }
            }

            applyMask(block);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void applyMask(ImageBlock block) throws IOException {

        final DataBufferInt data = block.getData();

        if (imageMask != null) {
            for (int pixel = 0; pixel < data.getSize(); ++pixel) {
                data.setElem(pixel, data.getElem(pixel) | 0xFF000000);

                if (imageMask.isPadPixel(data.getElem(pixel))) {
                    data.setElem(pixel, 0x00000000);
                }
            }
        } else {
            for (int pixel = 0; pixel < data.getSize(); ++pixel) {
                data.setElem(pixel, data.getElem(pixel) | 0xFF000000);
            }
        }
    }

    @Override
    protected ImageMode getExpectedImageMode() {
        return ImageMode.BANDSEQUENTIAL;
    }

    @Override
    protected String getHandlerClassName() {
        return this.getClass().getName();
    }
}
