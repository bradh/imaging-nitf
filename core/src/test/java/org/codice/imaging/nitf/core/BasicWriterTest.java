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
import java.io.InputStream;
import java.text.ParseException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for writing NITF file - common cases
 */
public class BasicWriterTest {

    public BasicWriterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void writeSimpleHeader() throws ParseException {
        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream("/WithBE.ntf")));
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(reader, parseStrategy);
        NitfWriter writer = new NitfFileWriter(parseStrategy, "testFile.ntf");
        writer.write();
        NitfReader testReader = new FileReader("testFile.ntf");
        SlottedNitfParseStrategy testParser = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(testReader, testParser);
        assertEquals(parseStrategy.nitfFileLevelHeader.getFileType(), testParser.nitfFileLevelHeader.getFileType());
        // TODO: check all contents are the same.
    }

    private InputStream getInputStream(String testfile) {

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }
}
