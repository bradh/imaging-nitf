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

import java.io.File;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test build of Header segment data
 */
public class TestImageSegmentDefaultBuild {

    public TestImageSegmentDefaultBuild() {
    }

    @Test
    public void writeFileWithImage() throws ParseException {
        final String OUTFILE_NAME = "fileWithImage.ntf";
        if (new File(OUTFILE_NAME).exists()) {
            new File(OUTFILE_NAME).delete();
        }

        SlottedMemoryNitfStorage store = new SlottedMemoryNitfStorage();

        Nitf nitf = Nitf.getDefault(FileType.NITF_TWO_ONE);
        assertNotNull(nitf);
        assertEquals(FileType.NITF_TWO_ONE, nitf.getFileType());

        store.setNitfFileLevelHeader(nitf);

        NitfImageSegmentHeader imageHeader = NitfImageSegmentHeader.getDefault(nitf.getFileType());
        store.getImageSegmentHeaders().add(imageHeader);
        store.getImageSegmentData().add(new byte[0]);

        NitfFileWriter writer = new NitfFileWriter(store, OUTFILE_NAME);
        writer.write();

        FileReader reader = new FileReader(OUTFILE_NAME);
        assertNotNull(reader);
        SlottedNitfParseStrategy parseStrategy = new HeaderOnlyNitfParseStrategy();
        NitfFileParser.parse(reader, parseStrategy);
        assertEquals(FileType.NITF_TWO_ONE, parseStrategy.getNitfHeader().getFileType());

        new File(OUTFILE_NAME).delete();
    }

}
