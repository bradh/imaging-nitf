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

import java.io.File;
import java.text.ParseException;
import org.codice.imaging.nitf.core.AllDataExtractionParseStrategy;
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.NitfFileWriter;
import org.codice.imaging.nitf.core.SlottedMemoryNitfStorage;
import org.codice.imaging.nitf.core.SlottedNitfParseStrategy;
import org.codice.imaging.nitf.core.common.FileReader;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.header.NitfFileParser;
import org.codice.imaging.nitf.core.header.NitfHeader;
import org.codice.imaging.nitf.core.header.NitfHeaderFactory;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

/**
 * Test for adding / modifying test segments.
 */
public class TestTextSegmentAddition {

    public TestTextSegmentAddition() {
    }

    @Test
    public void writeSimpleHeader() throws ParseException {
        final String OUTFILE_NAME = "testsegment1.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedMemoryNitfStorage store = createBasicStore();
        TextSegment textSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE,
                "Some Title",
                "This is the body.\r\nIt has two lines.");
        store.getTextSegments().add(textSegment);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();
        NitfDataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(1, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment);
        assertEquals("Some Title", testSegment.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment.getData());
        assertEquals(textSegment.getTextDateTime().getSourceString(), testSegment.getTextDateTime().getSourceString());

        new File(OUTFILE_NAME).delete();
    }

    @Test
    public void writeHeaderWithId() throws ParseException {
        final String OUTFILE_NAME = "testsegment2.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedMemoryNitfStorage store = createBasicStore();
        TextSegment textSegment = TextSegmentFactory.getDefault(FileType.NITF_TWO_ONE,
                "Some Title",
                "This is the body.\r\nIt has two lines.");
        textSegment.setIdentifier("XyZZy03");
        store.getTextSegments().add(textSegment);
        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();
        NitfDataSource dataSource = verifyBasicHeader(OUTFILE_NAME);
        assertEquals(0, dataSource.getImageSegments().size());
        assertEquals(0, dataSource.getGraphicSegments().size());
        assertEquals(1, dataSource.getTextSegments().size());
        assertEquals(0, dataSource.getDataExtensionSegments().size());

        TextSegment testSegment = dataSource.getTextSegments().get(0);
        assertNotNull(testSegment);
        assertEquals("XyZZy03", testSegment.getIdentifier());
        assertEquals("Some Title", testSegment.getTextTitle());
        assertTrue(TextFormat.BASICCHARACTERSET.equals(testSegment.getTextFormat()));
        assertEquals("This is the body.\r\nIt has two lines.", testSegment.getData());
        assertEquals(textSegment.getTextDateTime().getSourceString(), testSegment.getTextDateTime().getSourceString());

        new File(OUTFILE_NAME).delete();
    }

    private SlottedMemoryNitfStorage createBasicStore() {
        SlottedMemoryNitfStorage store = new SlottedMemoryNitfStorage();
        NitfHeader nitf = NitfHeaderFactory.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(FileType.NITF_TWO_ONE, nitf.getFileType());
        store.setNitfHeader(nitf);
        return store;
    }

    private NitfDataSource verifyBasicHeader(final String OUTFILE_NAME) throws ParseException {
        FileReader reader = new FileReader(OUTFILE_NAME);
        assertNotNull(reader);
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(reader, parseStrategy);
        assertEquals(FileType.NITF_TWO_ONE, parseStrategy.getNitfHeader().getFileType());
        return parseStrategy.getNitfDataSource();
    }
}
