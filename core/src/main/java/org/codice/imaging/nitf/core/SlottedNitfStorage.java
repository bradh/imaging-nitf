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

import java.util.ArrayList;
import java.util.List;
import org.codice.imaging.nitf.core.common.dataextension.NitfDataExtensionSegmentHeader;

/**
 * A data structure collection that holds the various parts of a NITF file.
 */
public class SlottedNitfStorage implements NitfDataSource {

    /**
     * The file level header.
     */
    protected Nitf nitfFileLevelHeader;
    /**
     * The list of image segment headers.
     */
    protected final List<NitfImageSegmentHeader> imageSegmentHeaders = new ArrayList<>();
    /**
     * The list of image segment data.
     */
    protected final List<byte[]> imageSegmentData = new ArrayList<>();
    /**
     * The list of graphic segment headers.
     */
    protected final List<NitfGraphicSegmentHeader> graphicSegmentHeaders = new ArrayList<>();
    /**
     * The list of graphic segment data.
     */
    protected final List<byte[]> graphicSegmentData = new ArrayList<>();
    /**
     * The list of symbol segment headers.
     */
    protected final List<NitfSymbolSegmentHeader> symbolSegmentHeaders = new ArrayList<>();
    /**
     * The list of symbol segment data.
     */
    protected final List<byte[]> symbolSegmentData = new ArrayList<>();
    /**
     * The list of label segment headers.
     */
    protected final List<NitfLabelSegmentHeader> labelSegmentHeaders = new ArrayList<>();
    /**
     * The list of label segment data.
     */
    protected final List<String> labelSegmentData = new ArrayList<>();
    /**
     * The list of text segment headers.
     */
    protected final ArrayList<NitfTextSegmentHeader> textSegmentHeaders = new ArrayList<>();
    /**
     * The list of text segment data.
     */
    protected final ArrayList<String> textSegmentData = new ArrayList<>();
    /**
     * The list of DES headers.
     */
    protected final List<NitfDataExtensionSegmentHeader> dataExtensionSegmentHeaders = new ArrayList<>();
    /**
     * The list of DES data.
     */
    protected final List<byte[]> dataExtensionSegmentData = new ArrayList<>();

    @Override
    public final Nitf getNitfHeader() {
        return nitfFileLevelHeader;
    }

    @Override
    public final List<NitfImageSegmentHeader> getImageSegmentHeaders() {
        return imageSegmentHeaders;
    }

    @Override
    public final List<byte[]> getImageSegmentData() {
        return imageSegmentData;
    }

    @Override
    public final List<NitfGraphicSegmentHeader> getGraphicSegmentHeaders() {
        return graphicSegmentHeaders;
    }

    @Override
    public final List<byte[]> getGraphicSegmentData() {
        return graphicSegmentData;
    }

    @Override
    public final List<NitfSymbolSegmentHeader> getSymbolSegmentHeaders() {
        return symbolSegmentHeaders;
    }

    @Override
    public final List<byte[]> getSymbolSegmentData() {
        return symbolSegmentData;
    }

    @Override
    public final List<NitfLabelSegmentHeader> getLabelSegmentHeaders() {
        return labelSegmentHeaders;
    }

    @Override
    public final List<String> getLabelSegmentData() {
        return labelSegmentData;
    }

    @Override
    public final List<NitfTextSegmentHeader> getTextSegmentHeaders() {
        return textSegmentHeaders;
    }

    @Override
    public final List<String> getTextSegmentData() {
        return textSegmentData;
    }

    @Override
    public final List<NitfDataExtensionSegmentHeader> getDataExtensionSegmentHeaders() {
        return dataExtensionSegmentHeaders;
    }

    @Override
    public final List<byte[]> getDataExtensionSegmentData() {
        return dataExtensionSegmentData;
    }
}
