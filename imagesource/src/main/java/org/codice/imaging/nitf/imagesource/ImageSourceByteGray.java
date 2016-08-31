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
 * ImageSegmentSource implementation for TYPE_BYTE_GRAY.
 */
class ImageSourceByteGray extends ImageIOImageSourceImpl {


    ImageSourceByteGray(final BufferedImage bufferedImage, final FileType fileType) {
        super(fileType);
        convertToByteGray(bufferedImage);
    }

    private void convertToByteGray(final BufferedImage sourceImage) {
        mBufferedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp converter = new ColorConvertOp(null);
        converter.filter(sourceImage, mBufferedImage);
    }

    @Override
    public final ImageRepresentation getImageRepresentation() {
        return ImageRepresentation.MONOCHROME;
    }

    @Override
    public final List<ImageBand> getImageBands() {
        List<ImageBand> imageBands = new ArrayList<>();
        ImageBand mono = new ImageBand();
        mono.setImageRepresentation("M");
        imageBands.add(mono);
        return imageBands;
    }

    @Override
    public final long getDataLength() {
        DataBufferByte dbb = (DataBufferByte) mBufferedImage.getData().getDataBuffer();
        return dbb.getSize();
    }

    @Override
    public final ImageInputStream getData() {
        DataBufferByte dbb = (DataBufferByte) mBufferedImage.getData().getDataBuffer();
        InputStream bais = new ByteArrayInputStream(dbb.getData());
        return new MemoryCacheImageInputStream(bais);
    }
}
