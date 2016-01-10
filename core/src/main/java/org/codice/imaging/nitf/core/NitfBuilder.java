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
package org.codice.imaging.nitf.core;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Builder for NITF files.
 */
public class NitfBuilder {

    private final SlottedMemoryNitfStorage store;

    /**
     * Create new NitfBuilder.
     *
     * @param fileType the type (version) of NITF file to create.
     */
    public NitfBuilder(final FileType fileType) {
        this.store = new SlottedMemoryNitfStorage();
        store.setNitfFileLevelHeader(Nitf.getDefault(fileType));
    }

    /**
     * Create a new NitfBuilder, defaulting to NITF 2.1.
     */
    public NitfBuilder() {
        this(FileType.NITF_TWO_ONE);
    }

    /**
     * Add an image to the NITF file, loaded from a File.
     *
     * @param file the File to load from.
     *
     * @throws IOException if a valid image cannot be read from the File.
     */
    public final void addImage(final File file) throws IOException {
        BufferedImage img = ImageIO.read(file);
        NitfImageSegmentHeader imgHeader = NitfImageSegmentHeader.getDefault(store.getNitfHeader().getFileType());
        imgHeader.setNumberOfRows(img.getHeight());
        imgHeader.setNumberOfColumns(img.getWidth());
        // TODO: set other imgHeader parts.
        store.getImageSegmentHeaders().add(imgHeader);
        // TODO: add img data to nitf, including making it into blocks if needed.
        // TODO: update image header lengths on getNitfHeader()
        // TODO: update image data lengths on getNitfHeader
        // TODO: update other Nitf parts?
    }

    // TODO: we can probably make lots more addImage methods, just by using other ImageIO.read() variants
    // then sharing code in some private method.

    /**
     * Return a NitfDataSource for this NITF file.
     *
     * @return the built file.
     */
    public final NitfDataSource getNitfData() {
        // TODO: update nitf header complexity value
        // TODO: anything else we need to recalculate?
        return store;
    }
}
