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

import static org.codice.imaging.nitf.core.TestUtils.checkDateTimeIsRecent;
import static org.codice.imaging.nitf.core.TestUtils.checkNitf21SecurityMetadataUnclasAndEmpty;
import org.codice.imaging.nitf.core.common.FileType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

/**
 * Tests for the TextSegmentFactory.
 */
public class TextSegmentFactoryTest {

    public TextSegmentFactoryTest() {
    }

    @Test
    public void NoText() {
        TextSegment noTextSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(noTextSegment);
        assertEquals("", noTextSegment.getIdentifier());
        assertEquals(0, noTextSegment.getAttachmentLevel());
        checkDateTimeIsRecent(noTextSegment.getTextDateTime().getZonedDateTime());
        assertEquals("", noTextSegment.getTextTitle());
        checkNitf21SecurityMetadataUnclasAndEmpty(noTextSegment.getSecurityMetadata());
        assertEquals(TextFormat.UTF8SUBSET, noTextSegment.getTextFormat());
    }

    @Test
    public void SimpleText() {
        TextSegment textSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE,
                "Some Title",
                "This is the body.\r\nIt has two lines.");
        assertNotNull(textSegment);
        assertEquals("", textSegment.getIdentifier());
        assertEquals(0, textSegment.getAttachmentLevel());
        checkDateTimeIsRecent(textSegment.getTextDateTime().getZonedDateTime());
        assertEquals("Some Title", textSegment.getTextTitle());
        checkNitf21SecurityMetadataUnclasAndEmpty(textSegment.getSecurityMetadata());
        assertEquals(TextFormat.BASICCHARACTERSET, textSegment.getTextFormat());
        assertEquals("This is the body.\r\nIt has two lines.", textSegment.getData());
    }
}
