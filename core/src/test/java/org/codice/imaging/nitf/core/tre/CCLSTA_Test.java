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
import org.codice.imaging.nitf.core.common.NitfFormatException;
import org.codice.imaging.nitf.core.common.NitfInputStreamReader;
import org.codice.imaging.nitf.core.common.NitfReader;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for CCLSTA TRE parsing.
 */
public class CCLSTA_Test {

    public CCLSTA_Test() {
    }

    @Test
    public void BasicCCLSTA() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream("CCLSTA00098014ISO3166-1:2013040ISO 3166 Maintenance Agency (ISO3166/MA)A525D54C-1858-4BC0-A5F2-57BB92F947E7000".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 109, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre cclsta = parseResult.getTREsWithName("CCLSTA").get(0);
        assertNotNull(cclsta);
        // Check it parsed - raw data is only for TREs we couldn't understand
        assertNull(cclsta.getRawData());
        // Check values
        assertEquals("CCLSTA", cclsta.getName());
        assertEquals(7, cclsta.getEntries().size());
        assertEquals("ISO3166-1:2013", cclsta.getFieldValue("CC_STD"));
        assertEquals("ISO3166-1:2013".length(), cclsta.getIntValue("CC_STD_LENGTH"));
        assertEquals("ISO 3166 Maintenance Agency (ISO3166/MA)", cclsta.getFieldValue("CC_ORG"));
        assertEquals("ISO 3166 Maintenance Agency (ISO3166/MA)".length(), cclsta.getIntValue("CC_ORG_LENGTH"));
        assertEquals("A525D54C-1858-4BC0-A5F2-57BB92F947E7", cclsta.getFieldValue("CC_UUID"));
        assertEquals(0, cclsta.getIntValue("SUPP_COUNT"));
        assertEquals(0, cclsta.getEntry("SUPPLEMENTAL_IDENTIFIERS").getGroups().size());
    }

    @Test
    public void BuildCCLSTA() throws NitfFormatException {
        Tre cclsta = TreFactory.getDefault("CCLSTA", TreSource.ExtendedHeaderData);
        assertNotNull(cclsta);
        cclsta.add(new TreEntry("CC_STD_LENGTH", "014"));
        cclsta.add(new TreEntry("CC_STD", "ISO3166-1:2013"));
        cclsta.add(new TreEntry("CC_ORG_LENGTH", "000"));
        cclsta.add(new TreEntry("CC_ORG", ""));
        cclsta.add(new TreEntry("CC_UUID", "A525D54C-1858-4BC0-A5F2-57BB92F947E7"));
        cclsta.add(new TreEntry("SUPP_COUNT", "000"));
        TreEntry supplementalIdentifiers = new TreEntry("SUPPLEMENTAL_IDENTIFIERS");
        cclsta.add(supplementalIdentifiers);

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(cclsta);
        assertNotNull(serialisedTre);
        assertEquals("CCLSTA", cclsta.getName());
        assertArrayEquals("014ISO3166-1:2013000A525D54C-1858-4BC0-A5F2-57BB92F947E7000".getBytes(), serialisedTre);
    }

    @Test
    public void BuildCCLSTA_factoryMethod() throws NitfFormatException {
        Tre cclsta = TreFactory.getCCLSTA(TreSource.ExtendedHeaderData, "ISO3166-1:2013", "", "A525D54C-1858-4BC0-A5F2-57BB92F947E7");

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(cclsta);
        assertNotNull(serialisedTre);
        assertEquals("CCLSTA", cclsta.getName());
        assertArrayEquals("014ISO3166-1:2013000A525D54C-1858-4BC0-A5F2-57BB92F947E7000".getBytes(), serialisedTre);
    }

    @Test
    public void BuildCCLSTA_factoryMethodWithMaintenanceOrg() throws NitfFormatException {
        Tre cclsta = TreFactory.getCCLSTA(TreSource.ExtendedHeaderData, "ISO3166-1:2013", "ISO 3166 Maintenance Agency (ISO3166/MA)", "A525D54C-1858-4BC0-A5F2-57BB92F947E7");

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(cclsta);
        assertNotNull(serialisedTre);
        assertEquals("CCLSTA", cclsta.getName());
        assertArrayEquals("014ISO3166-1:2013040ISO 3166 Maintenance Agency (ISO3166/MA)A525D54C-1858-4BC0-A5F2-57BB92F947E7000".getBytes(), serialisedTre);
    }

    @Test
    public void BuildCCLSTA_GENC() throws NitfFormatException {
        Tre cclsta = TreFactory.getCCLSTA(TreSource.UserDefinedHeaderData, "US GENC:ED3", "", "409B74EE-1572-4430-8B80-9BA6961B3CFC");
        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(cclsta);
        assertNotNull(serialisedTre);
        assertEquals("CCLSTA", cclsta.getName());
        assertArrayEquals("011US GENC:ED3000409B74EE-1572-4430-8B80-9BA6961B3CFC000".getBytes(), serialisedTre);
    }

    @Test
    public void BuildCCLSTA_SupplementalIdents() throws NitfFormatException {
        Tre cclsta = TreFactory.getDefault("CCLSTA", TreSource.UserDefinedHeaderData);
        cclsta.add(new TreEntry("CC_STD_LENGTH", String.valueOf("STANAG 1059:ED8".length())));
        cclsta.add(new TreEntry("CC_STD", "STANAG 1059:ED8"));
        cclsta.add(new TreEntry("CC_ORG_LENGTH", "000"));
        cclsta.add(new TreEntry("CC_ORG", ""));
        cclsta.add(new TreEntry("CC_UUID", "2C68E226-A84A-49F3-A01E-2CE896B7E428"));
        cclsta.add(new TreEntry("SUPP_COUNT", "002"));
        TreEntry supplementalIdentifiers = new TreEntry("SUPPLEMENTAL_IDENTIFIERS");
        cclsta.add(supplementalIdentifiers);
        TreGroup ssGroup = new TreGroupImpl();
        ssGroup.add(new TreEntry("SUPP_IDENT", "SS"));
        ssGroup.add(new TreEntry("SUPP_NAME_LENGTH", String.valueOf("South Sudan".length())));
        ssGroup.add(new TreEntry("SUPP_NAME", "South Sudan"));
        ssGroup.add(new TreEntry("SUPP_LONG_NAME_LENGTH", String.valueOf("Republic of South Sudan".length())));
        ssGroup.add(new TreEntry("SUPP_LONG_NAME", "Republic of South Sudan"));
        supplementalIdentifiers.addGroup(ssGroup);
        TreGroup xkGroup = new TreGroupImpl();
        xkGroup.add(new TreEntry("SUPP_IDENT", "XK"));
        xkGroup.add(new TreEntry("SUPP_NAME_LENGTH", String.valueOf("User Assigned".length())));
        xkGroup.add(new TreEntry("SUPP_NAME", "User Assigned"));
        xkGroup.add(new TreEntry("SUPP_LONG_NAME_LENGTH", "000"));
        xkGroup.add(new TreEntry("SUPP_LONG_NAME", ""));
        supplementalIdentifiers.addGroup(xkGroup);

        TreParser parser = new TreParser();
        byte[] serialisedTre = parser.serializeTRE(cclsta);
        assertNotNull(serialisedTre);
        assertEquals("CCLSTA", cclsta.getName());
        assertArrayEquals("015STANAG 1059:ED80002C68E226-A84A-49F3-A01E-2CE896B7E428002SS011South Sudan023Republic of South SudanXK013User Assigned000".getBytes(), serialisedTre);
    }

    @Test
    public void ParseComplexCCLSTA() throws NitfFormatException {
        InputStream inputStream = new ByteArrayInputStream("CCLSTA00123015STANAG 1059:ED80002C68E226-A84A-49F3-A01E-2CE896B7E428002SS011South Sudan023Republic of South SudanXK013User Assigned000".getBytes());
        BufferedInputStream bufferedStream = new BufferedInputStream(inputStream);
        NitfReader nitfReader = new NitfInputStreamReader(bufferedStream);
        TreCollectionParser parser = new TreCollectionParser();
        TreCollection parseResult = parser.parse(nitfReader, 134, TreSource.ImageExtendedSubheaderData);
        assertEquals(1, parseResult.getTREs().size());
        Tre cclsta = parseResult.getTREsWithName("CCLSTA").get(0);
        assertNotNull(cclsta);
        // Check it parsed - raw data is only for TREs we couldn't understand
        assertNull(cclsta.getRawData());
        // Check values
        assertEquals("CCLSTA", cclsta.getName());
        assertEquals(7, cclsta.getEntries().size());
        assertEquals("STANAG 1059:ED8", cclsta.getFieldValue("CC_STD"));
        assertEquals("STANAG 1059:ED8".length(), cclsta.getIntValue("CC_STD_LENGTH"));
        assertEquals("", cclsta.getFieldValue("CC_ORG"));
        assertEquals("".length(), cclsta.getIntValue("CC_ORG_LENGTH"));
        assertEquals("2C68E226-A84A-49F3-A01E-2CE896B7E428", cclsta.getFieldValue("CC_UUID"));
        assertEquals(2, cclsta.getIntValue("SUPP_COUNT"));
        assertEquals(2, cclsta.getEntry("SUPPLEMENTAL_IDENTIFIERS").getGroups().size());

        TreGroup ssGroup = cclsta.getEntry("SUPPLEMENTAL_IDENTIFIERS").getGroups().get(0);
        assertNotNull(ssGroup);
        assertEquals("SS", ssGroup.getFieldValue("SUPP_IDENT"));
        assertEquals("South Sudan".length(), ssGroup.getIntValue("SUPP_NAME_LENGTH"));
        assertEquals("South Sudan", ssGroup.getFieldValue("SUPP_NAME"));
        assertEquals("Republic of South Sudan".length(), ssGroup.getIntValue("SUPP_LONG_NAME_LENGTH"));
        assertEquals("Republic of South Sudan", ssGroup.getFieldValue("SUPP_LONG_NAME"));

        TreGroup xkGroup = cclsta.getEntry("SUPPLEMENTAL_IDENTIFIERS").getGroups().get(1);
        assertNotNull(xkGroup);
        assertEquals("XK", xkGroup.getFieldValue("SUPP_IDENT"));
        assertEquals("User Assigned".length(), xkGroup.getIntValue("SUPP_NAME_LENGTH"));
        assertEquals("User Assigned", xkGroup.getFieldValue("SUPP_NAME"));
        assertEquals("".length(), xkGroup.getIntValue("SUPP_LONG_NAME_LENGTH"));
        assertEquals("", xkGroup.getFieldValue("SUPP_LONG_NAME"));
    }
}
