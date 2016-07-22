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
 * Tests for CSWPRB TRE.
 * 
 * See RFC_082A, draft release.
 */
public class CSWRPB_Test extends SharedTreTest {

    public CSWRPB_Test() {
    }

    @Test
    public void SimpleParseScanner() throws NitfFormatException {
        String testData = "CSWRPB000671S00000020000003000000400000050000006000000700000080000009000000000";
        Tre cswrpb = parseTRE(testData, 78, "CSWRPB");
        assertEquals(5, cswrpb.getEntries().size());
        assertEquals(1, cswrpb.getIntValue("NUM_SETS_WARP_DATA"));
        assertEquals("S", cswrpb.getFieldValue("SENSOR_TYPE"));
        assertEquals(1, cswrpb.getEntry("WARPING_SETS").getGroups().size());
        assertEquals(14, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntries().size());
        assertEquals(2, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_LINE"));
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_SAMP"));
        assertEquals(4, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_LINE"));
        assertEquals(5, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_SAMP"));
        assertEquals(6, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_LINE_UNWRP"));
        assertEquals(7, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_SAMP_UNWRP"));
        assertEquals(8, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_LINE_UNWRP"));
        assertEquals(9, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_SAMP_UNWRP"));
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("LINE_POLY_ORDER_M1"));
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("LINE_POLY_ORDER_M2"));
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SAMP_POLY_ORDER_N1"));
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SAMP_POLY_ORDER_N2"));
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().size());
        assertEquals(0, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().size());
        assertEquals(0, cswrpb.getIntValue("RESERVED_LEN"));
        assertEquals(0, cswrpb.getFieldValue("RESERVED").length());
    }

    @Test
    public void MultiTermsParseScanner() throws NitfFormatException {
        String testData = "CSWRPB003611S000000200000030000004000000500000060000007000000800000091234"
                + "2.123456789012345E-043.123456789012345E-03"
                + "4.123456789012345E-025.123456789012345E-025.123456789012345E-016.123456789012345E+01"
                + "7.123456789012345E-021.123456789992345E-023.123456789013345E-046.124456789012345E+03"
                + "9.123456789012345E-022.123456789012345E+033.123456789112345E-087.123556789012345E+07"
                + "00000";
        Tre cswrpb = parseTRE(testData, 372, "CSWRPB");
        assertEquals(5, cswrpb.getEntries().size());
        assertEquals(1, cswrpb.getIntValue("NUM_SETS_WARP_DATA"));
        assertEquals("S", cswrpb.getFieldValue("SENSOR_TYPE"));
        assertEquals(1, cswrpb.getEntry("WARPING_SETS").getGroups().size());
        assertEquals(14, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntries().size());
        assertEquals(2, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_LINE"));
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_SAMP"));
        assertEquals(4, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_LINE"));
        assertEquals(5, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_SAMP"));
        assertEquals(6, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_LINE_UNWRP"));
        assertEquals(7, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("OFFSET_SAMP_UNWRP"));
        assertEquals(8, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_LINE_UNWRP"));
        assertEquals(9, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SCALE_SAMP_UNWRP"));
        assertEquals(1, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("LINE_POLY_ORDER_M1"));
        assertEquals(2, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("LINE_POLY_ORDER_M2"));
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SAMP_POLY_ORDER_N1"));
        assertEquals(4, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getIntValue("SAMP_POLY_ORDER_N2"));
        assertEquals(2, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().size());
        assertEquals(1, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("LINE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals("2.123456789012345E-04", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("LINE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("A"));
        assertEquals(1, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("LINE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals("3.123456789012345E-03", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("LINE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("LINE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("A"));
        assertEquals(4, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().size());
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(2).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals(3, cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(3).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().size());
        assertEquals("4.123456789012345E-02", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("B"));
        assertEquals("5.123456789012345E-02", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(1).getFieldValue("B"));
        assertEquals("5.123456789012345E-01", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(2).getFieldValue("B"));
        assertEquals("6.123456789012345E+01", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("B"));
        assertEquals("7.123456789012345E-02", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(1).getFieldValue("B"));
        assertEquals("1.123456789992345E-02", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(1).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(2).getFieldValue("B"));
        assertEquals("3.123456789013345E-04", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(2).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("B"));
        assertEquals("6.124456789012345E+03", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(2).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(1).getFieldValue("B"));
        assertEquals("9.123456789012345E-02", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(2).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(2).getFieldValue("B"));
        assertEquals("2.123456789012345E+03", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(3).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(0).getFieldValue("B"));
        assertEquals("3.123456789112345E-08", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(3).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(1).getFieldValue("B"));
        assertEquals("7.123556789012345E+07", cswrpb.getEntry("WARPING_SETS").getGroups().get(0).getEntry("SAMPLE_POLYNOMIALS_SAMPLE").getGroups().get(3).getEntry("SAMPLE_POLYNOMIALS_LINE").getGroups().get(2).getFieldValue("B"));
        assertEquals(0, cswrpb.getIntValue("RESERVED_LEN"));
        assertEquals(0, cswrpb.getFieldValue("RESERVED").length());
    }
}
