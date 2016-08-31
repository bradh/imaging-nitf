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
import java.awt.image.ColorConvertOp;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageRepresentation;

/**
 * ImageSegmentSource implementation for TYPE_3BYTE_BGR.
 */
class ImageSource3ByteBGR extends ImageIOImageSourceImpl implements ImageSource {

    ImageSource3ByteBGR(final BufferedImage bufferedImage, final FileType fileType) {
        super(fileType);
        convertTo3ByteBGR(bufferedImage);
    }

    public final void convertTo3ByteBGR(final BufferedImage sourceImage) {
        mBufferedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        ColorConvertOp converter = new ColorConvertOp(null);
        converter.filter(sourceImage, mBufferedImage);
    }

    @Override
    public final ImageRepresentation getImageRepresentation() {
        return ImageRepresentation.RGBTRUECOLOUR;
    }

    @Override
    public List<ImageBand> getImageBands() {
        List<ImageBand> imageBands = new ArrayList<>();
        ImageBand blue = new ImageBand();
        blue.setImageRepresentation("B");
        imageBands.add(blue);
        ImageBand green = new ImageBand();
        green.setImageRepresentation("G");
        imageBands.add(green);
        ImageBand red = new ImageBand();
        red.setImageRepresentation("R");
        imageBands.add(red);
        return imageBands;
    }

    @Override
    public final long getDataLength() {
        DataBufferByte dbb = (DataBufferByte) mBufferedImage.getData().getDataBuffer();
        return dbb.getSize();
    }

    @Override
    public final ImageInputStream getData() {
        // TODO: this should handle actual data types better
        DataBufferByte dbb = (DataBufferByte) mBufferedImage.getData().getDataBuffer();
        InputStream bais = new ByteArrayInputStream(dbb.getData());
        return new MemoryCacheImageInputStream(bais);
    }
}
