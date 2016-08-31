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
import java.awt.image.DataBufferUShort;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageBand;
import org.codice.imaging.nitf.core.image.ImageRepresentation;

/**
 * ImageSource for TYPE_USHORT_GRAY.
 */
class ImageSourceUShortGray extends ImageIOImageSourceImpl {


    ImageSourceUShortGray(final BufferedImage bufferedImage, final FileType fileType) {
        super(fileType);
        convertToShortGray(bufferedImage);
    }

    private void convertToShortGray(final BufferedImage sourceImage) {
        mBufferedImage = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), BufferedImage.TYPE_USHORT_GRAY);
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
        DataBufferUShort dbs = (DataBufferUShort) mBufferedImage.getData().getDataBuffer();
        return dbs.getSize() * Short.BYTES;
    }

    @Override
    public final ImageInputStream getData() {
        DataBufferUShort dbs = (DataBufferUShort) mBufferedImage.getData().getDataBuffer();
        ByteBuffer byteBuffer = ByteBuffer.allocate((int) getDataLength());
        byteBuffer.asShortBuffer().put(dbs.getData());
        InputStream bais = new ByteArrayInputStream(byteBuffer.array());
        return new MemoryCacheImageInputStream(bais);
    }
}
