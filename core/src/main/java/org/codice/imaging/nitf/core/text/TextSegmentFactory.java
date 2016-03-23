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
package org.codice.imaging.nitf.core.text;

import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.common.NitfDateTime;
import org.codice.imaging.nitf.core.security.SecurityMetadataFactory;

/**
 * Factory class for creating new TextSegment instances.
 */
public final class TextSegmentFactory {

    private TextSegmentFactory() {
    }

    /**
     * Create a default NITF text segment, without data.
     *
     * @param fileType the type (version) of NITF file this text segment is for
     * @return default valid text segment, containing no text data.
     */
    public static TextSegment getDefault(final FileType fileType) {
        TextSegment textSegment = new TextSegmentImpl();
        textSegment.setIdentifier("");
        textSegment.setAttachmentLevel(0);
        textSegment.setTextDateTime(NitfDateTime.getNitfDateTimeForNow());
        textSegment.setTextTitle("");
        textSegment.setSecurityMetadata(SecurityMetadataFactory.getDefaultMetadata(fileType));
        textSegment.setTextFormat(TextFormat.UTF8SUBSET);
        return textSegment;
    }

    /**
     * Create a default NITF text segment, specifying title and body.
     *
     * @param fileType the type (version) of NITF file this text segment is for
     * @param title the title (TXTITL field) of the segment.
     * @param body the body (segment data) of the segment.
     * @return valid text segment, containing the specified title and data.
     */
    public static TextSegment getDefault(final FileType fileType, final String title, final String body) {
        TextSegment textSegment = getDefault(fileType);
        textSegment.setTextTitle(title);
        textSegment.setTextFormat(TextFormat.getFormatUsedInString(body));
        textSegment.setData(body);
        return textSegment;
    }
}
