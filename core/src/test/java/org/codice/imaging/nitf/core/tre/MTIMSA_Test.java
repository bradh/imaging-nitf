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
package org.codice.imaging.nitf.core.tre;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 * Tests for parsing MTIMSA.
 *
 * This is defined in NGA.STND.0044 1.1, as part of MIE4NITF. See Section 5.9.4.7 and Table 17.
 */
public class MTIMSA_Test {

    public MTIMSA_Test() {
    }

    @Test
    public void SimpleParse() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("MTIMSA0015000199fa238862-73ed-41fc-8d52-bfc7a954428c00295cb5511-7350-479b-9c8a-f028aba01e840000030045.0000000E+0100000000620160716215756.012345678\u0000\u0000\u0000\u0000\u0001\u0031\u002D\u0000\u0001\u0001\u0002\u0003\u0004\u0000\u0000\u0000\u0001\u004e".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 150, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre mtimsa = parseResult.getTREsWithName("MTIMSA").get(0);
        assertNotNull(mtimsa);
        assertNull(mtimsa.getRawData());
        assertEquals(15, mtimsa.getEntries().size());
        assertEquals("001", mtimsa.getFieldValue("IMAGE_SEG_INDEX"));
        assertEquals("99", mtimsa.getFieldValue("GEOCOORDS_STATIC"));
        assertEquals("fa238862-73ed-41fc-8d52-bfc7a954428c", mtimsa.getFieldValue("LAYER_ID"));
        assertEquals("002", mtimsa.getFieldValue("CAMERA_SET_INDEX"));
        assertEquals("95cb5511-7350-479b-9c8a-f028aba01e84", mtimsa.getFieldValue("CAMERA_ID"));
        assertEquals("000003", mtimsa.getFieldValue("TIME_INTERVAL_INDEX"));
        assertEquals("004", mtimsa.getFieldValue("TEMP_BLOCK_INDEX"));
        assertEquals("5.0000000E+01", mtimsa.getFieldValue("NOMINAL_FRAME_RATE"));
        assertEquals(50.0, mtimsa.getDoubleValue("NOMINAL_FRAME_RATE"), 0.000000001);
        assertEquals("000000006", mtimsa.getFieldValue("REFERENCE_FRAME_NUM"));
        assertEquals("20160716215756.012345678", mtimsa.getFieldValue("BASE_TIMESTAMP"));
        assertEquals(20000000, mtimsa.getIntValue("DT_MULTIPLIER"));
        assertEquals(1, mtimsa.getIntValue("DT_SIZE"));
        assertEquals(16909060, mtimsa.getIntValue("NUMBER_FRAMES"));
        assertEquals(1, mtimsa.getIntValue("NUMBER_DT"));
        TreGroup deltaTimes = mtimsa.getEntry("DELTA_TIME").getGroups().get(0);
        assertEquals(1, deltaTimes.getEntries().size());
        assertEquals(78, deltaTimes.getIntValue("DT"));
    }
}
