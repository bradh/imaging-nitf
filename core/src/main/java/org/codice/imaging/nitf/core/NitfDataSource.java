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

import java.util.List;
import org.codice.imaging.nitf.core.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.graphic.NitfGraphicSegmentHeader;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * Data source for NITF file parts.
 */
public interface NitfDataSource {

    /**
     * Return the file-level header for the parsed file.
     *
     * @return the file-level header
     */
    Nitf getNitfHeader();

    /**
     * Return the image segment headers associated with this file.
     *
     * @return image segment headers
     */
    List<NitfImageSegmentHeader> getImageSegmentHeaders();

    /**
     * Return the list of image segment data.
     *
     * @return image segment data
     */
     List<byte[]> getImageSegmentData();

    /**
     * Return the graphic segment headers associated with this file.
     *
     * @return graphic segment headers
     */
    List<NitfGraphicSegmentHeader> getGraphicSegmentHeaders();

    /**
     * Return list of graphic segment data.
     *
     * @return graphic segment data
     */
    List<byte[]> getGraphicSegmentData();

    /**
     * Return the symbol segment headers associated with this file.
     *
     * @return symbol segment headers
     */
    List<NitfSymbolSegmentHeader> getSymbolSegmentHeaders();

    /**
     * Return the list of symbol segment data.
     *
     * @return symbol segment data
     */
    List<byte[]> getSymbolSegmentData();

    /**
     * Return the label segment headers associated with this file.
     *
     * @return label segment headers
     */
    List<NitfLabelSegmentHeader> getLabelSegmentHeaders();

    /**
     * Return list of label segment data.
     *
     * @return label segment data
     */
    List<String> getLabelSegmentData();

    /**
     * Return the text segments associated with this file.
     *
     * @return text segments
     */
    List<NitfTextSegmentHeader> getTextSegmentHeaders();

    /**
     * Return list of text segment data.
     *
     * @return text segment data
     */
    List<String> getTextSegmentData();

    /**
     * Return the data extension segment headers associated with this file.
     *
     * @return data extension segment headers
     */
    List<NitfDataExtensionSegmentHeader> getDataExtensionSegmentHeaders();

    /**
     * Return list of data extension segment data.
     *
     * @return data extension segment data
     */
    List<byte[]> getDataExtensionSegmentData();
}
