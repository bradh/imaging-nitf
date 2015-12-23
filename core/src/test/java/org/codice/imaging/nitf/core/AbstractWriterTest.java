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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.text.ParseException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import static org.junit.Assert.*;

/**
 * Shared writer test code
 */
class AbstractWriterTest {

    protected InputStream getInputStream(String testfile) {
        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }

    protected void roundTripFile(String sourceFileName) throws URISyntaxException, ParseException, IOException {
        String outputFile = FilenameUtils.getName(sourceFileName);
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream(sourceFileName)));
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(reader, parseStrategy);
        NitfWriter writer = new NitfFileWriter(parseStrategy, outputFile);
        writer.write();
        assertTrue(FileUtils.contentEquals(new File(getClass().getResource(sourceFileName).toURI()), new File(outputFile)));
        assertTrue(new File(outputFile).delete());
    }
}
