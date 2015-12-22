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
    public void roundTripSimpleFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/WithBE.ntf");
    }

    @Test
    public void roundTripTextFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3201a.nsf");
    }

    @Test
    public void roundTripHeaderFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3034c.ntf");
    }

    @Test
    public void roundTripGeoFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/i_3001a.ntf");
    }

    @Test
    public void roundTripImageCommentFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3010a.nsf");
    }

    @Test
    public void roundTripMultiImageFile() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcNitf21Samples/ns3361c.nsf");
    }

    @Test
    public void roundTripGraphicFile() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3051e.ntf");
    }

    @Test
    public void roundTripGraphic2File() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/ns3051v.nsf");
    }

    @Test
    public void roundTripDESFile() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/autzen-utm10.ntf");
    }

    @Test
    public void roundTripTREs() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf21Samples/i_3128b.ntf");
    }

    @Test
    public void roundTripGraphicSegmentExt() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/gdal3453.ntf");
    }

    @Test
    public void roundTripNitf20_U1001A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1001A.NTF");
    }

    @Test
    public void roundTripNitf20_U1034A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1034A.NTF");
    }

    @Test
    public void roundTripNitf20_U1036A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1036A.NTF");
    }

    @Test
    public void roundTripNitf20_U1050A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1050A.NTF");
    }

    @Test
    public void roundTripNitf20_U1060A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1060A.NTF");
    }

    @Test
    public void roundTripNitf20_U1101A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1101A.NTF");
    }

    @Test
    public void roundTripNitf20_U1114A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1114A.NTF");
    }

    @Test
    public void roundTripNitf20_U1122A() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1122A.NTF");
    }

    @Test
    public void roundTripNitf20_U1130F() throws IOException, ParseException, URISyntaxException {
        roundTripFile("/JitcNitf20Samples/U_1130F.NTF");
    }

    private void roundTripFile(String sourceFileName) throws URISyntaxException, ParseException, IOException {
        String outputFile = FilenameUtils.getName(sourceFileName);

        NitfReader reader = new NitfInputStreamReader(new BufferedInputStream(getInputStream(sourceFileName)));
        SlottedNitfParseStrategy parseStrategy = new AllDataExtractionParseStrategy();
        NitfFileParser.parse(reader, parseStrategy);
        NitfWriter writer = new NitfFileWriter(parseStrategy, outputFile);
        writer.write();
        assertTrue(FileUtils.contentEquals(new File(getClass().getResource(sourceFileName).toURI()), new File(outputFile)));
        assertTrue(new File(outputFile).delete());
    }

    private InputStream getInputStream(String testfile) {

        assertNotNull("Test file missing", getClass().getResource(testfile));
        return getClass().getResourceAsStream(testfile);
    }
}
