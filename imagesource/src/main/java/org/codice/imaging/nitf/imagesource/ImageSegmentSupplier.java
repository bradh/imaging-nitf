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
import java.util.function.Supplier;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageSegment;

/**
 * Supplier for ImageSegment instances.
 *
 * This is intended for use in the fluent API.
 */
public class ImageSegmentSupplier implements Supplier<ImageSegment> {

    private final BufferedImage mBufferedImage;
    private final FileType mFileType;

    /**
     * Create a new ImageSegment Supplier from a BufferedImage.
     *
     * This version makes a "best effort" conversion to something similar to the
     * input data.
     *
     * Note that there is currently no support for TYPE_BYTE_BINARY or
     * TYPE_BYTE_INDEXED.
     *
     * @param bufferedImage the BufferedImage providing image data.
     * @param fileType file type (NITF version) of the segment to create.
     */
    public ImageSegmentSupplier(final BufferedImage bufferedImage, final FileType fileType) {
        mBufferedImage = bufferedImage;
        mFileType = fileType;
    }

    @Override
    public final ImageSegment get() {
        return ImageSegmentSourceFactory.getImageSegment(mBufferedImage, mFileType);
    }
}
