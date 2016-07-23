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

import org.codice.imaging.nitf.core.common.NitfFormatException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Tests for CSEXRB TRE.
 *
 * See RFC_082A, draft release.
 */
public class CSEXRB_Test extends SharedTreTest {

    public CSEXRB_Test() {
    }

    @Test
    public void SimpleParse() throws NitfFormatException {
        String testData = "CSEXRB00211b7dff383-ef6e-4e20-928a-c7b83bf7c716000PLATxxPAYyyySENSzz -04474518.00+02674763.00-03663732.00012.1023.2034.3010.3045.6035.3038.9097.312345002000015328141.80812.765321.5431013287.632-73.3764.3400.2540.3034100000";
        Tre csexrb = parseTRE(testData, 222, "CSEXRB");
        assertEquals(37, csexrb.getEntries().size());
        assertEquals("b7dff383-ef6e-4e20-928a-c7b83bf7c716", csexrb.getFieldValue("IMAGE_UUID"));
        assertEquals(0, csexrb.getIntValue("NUM_ASSOC_DES"));
        assertEquals(0, csexrb.getEntry("ASSOC_DES").getGroups().size());
        assertEquals("PLATxx", csexrb.getFieldValue("PLATFORM_ID"));
        assertEquals("PAYyyy", csexrb.getFieldValue("PAYLOAD_ID"));
        assertEquals("SENSzz", csexrb.getFieldValue("SENSOR_ID"));
        assertEquals(" ", csexrb.getFieldValue("SENSOR_TYPE"));
        assertEquals("-04474518.00", csexrb.getFieldValue("GROUND_REF_POINT_X"));
        assertEquals("+02674763.00", csexrb.getFieldValue("GROUND_REF_POINT_Y"));
        assertEquals("-03663732.00", csexrb.getFieldValue("GROUND_REF_POINT_Z"));
        assertEquals("012.1", csexrb.getFieldValue("MAX_GSD"));
        assertEquals("023.2", csexrb.getFieldValue("ALONG_SCAN_GSD"));
        assertEquals("034.3", csexrb.getFieldValue("CROSS_SCAN_GSD"));
        assertEquals("010.3", csexrb.getFieldValue("GEO_MEAN_GSD"));
        assertEquals("045.6", csexrb.getFieldValue("A_S_VERT_GSD"));
        assertEquals("035.3", csexrb.getFieldValue("C_S_VERT_GSD"));
        assertEquals("038.9", csexrb.getFieldValue("GEO_MEAN_VERT_GSD"));
        assertEquals("097.3", csexrb.getFieldValue("GSD_BETA_ANGLE"));
        assertEquals(12345, csexrb.getIntValue("DYNAMIC_RANGE"));
        assertEquals(20000, csexrb.getIntValue("NUM_LINES"));
        assertEquals(15328, csexrb.getIntValue("NUM_SAMPLES"));
        assertEquals("141.808", csexrb.getFieldValue("ANGLE_TO_NORTH"));
        assertEquals("12.765", csexrb.getFieldValue("OBLIQUITY_ANGLE"));
        assertEquals("321.543", csexrb.getFieldValue("AZ_OF_OBLIQUITY"));
        assertEquals("1", csexrb.getFieldValue("ATM_REFR_FLAG"));
        assertEquals("0", csexrb.getFieldValue("VEL_ABER_FLAG"));
        assertEquals("1", csexrb.getFieldValue("GRD_COVER"));
        assertEquals("3", csexrb.getFieldValue("SNOW_DEPTH_CATEGORY"));
        assertEquals("287.632", csexrb.getFieldValue("SUN_AZIMUTH"));
        assertEquals("-73.376", csexrb.getFieldValue("SUN_ELEVATION"));
        assertEquals("4.3", csexrb.getFieldValue("PREDICTED_NIIRS"));
        assertEquals("400.2", csexrb.getFieldValue("CIRCL_ERR"));
        assertEquals("540.3", csexrb.getFieldValue("LINEAR_ERR"));
        assertEquals("034", csexrb.getFieldValue("CLOUD_COVER"));
        assertEquals("1", csexrb.getFieldValue("UE_TIME_FLAG"));
        assertEquals(0, csexrb.getIntValue("RESERVED_LEN"));
        assertEquals(0, csexrb.getFieldValue("RESERVED").length());
    }
}