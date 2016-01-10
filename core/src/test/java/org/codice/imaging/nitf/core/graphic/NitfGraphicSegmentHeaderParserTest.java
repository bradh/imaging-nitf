/**
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
 **/
package org.codice.imaging.nitf.core.graphic;

import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.Is.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.util.LinkedList;

import org.codice.imaging.nitf.core.FileType;
import org.codice.imaging.nitf.core.NitfParseStrategy;
import org.codice.imaging.nitf.core.NitfReader;
import org.codice.imaging.nitf.core.NitfSecurityClassification;
import org.codice.imaging.nitf.core.NitfSecurityMetadata;
import org.codice.imaging.nitf.core.TreCollection;
import org.junit.Before;
import org.junit.Test;

public class NitfGraphicSegmentHeaderParserTest {
    private NitfParseStrategy strategy;
    private NitfReader nitfReader;
    private LinkedList<String> stringValues = new LinkedList<String>();
    private LinkedList<Integer> intValues = new LinkedList<Integer>();

    private static final String SSCLAS = "U";
    private static final String SSCLSY = "S1";
    private static final String SSCODE = "S2";
    private static final String SSCTLH = "S3";
    private static final String SSREL = "S4";
    private static final String SSDCTP = "S5";
    private static final String SSDCDT = "S6";
    private static final String SSDCXM = "S7";
    private static final String SSDG = "S8";
    private static final String SSDGDT = "20150101";
    private static final String SSCLTX = "S10";
    private static final String SSCATP = "S11";
    private static final String SSCAUT = "S12";
    private static final String SSCRSN = "S13";
    private static final String SSSRDT = "S14";
    private static final String SSCTLN = "S15";

    private static final String ENCRYP = "0";
    private static final String SCOLOR = "C";
    private static final String SNAME = "12345678901234567890";
    private static final String SY = "";

    private static final int SXSOFL = 0;
    private static final int SXSHDL = 10;
    private static final int SBND2_ROW = 20;
    private static final int SBND2_COL = 21;
    private static final int SBND1_ROW = 30;
    private static final int SBND1_COL = 31;
    private static final int SLOC_ROW = 40;
    private static final int SLOC_COL = 41;
    private static final int SALVL = 50;
    private static final int SDLVL = 60;

    @Before
    public void setup() throws ParseException {
        stringValues.clear();
        intValues.clear();
        this.nitfReader = mock(NitfReader.class);
        when(nitfReader.getFileType()).thenReturn(FileType.NITF_TWO_ONE);

        stringValues.push(SCOLOR);
        stringValues.push(ENCRYP);

        stringValues.push(SSCTLN);
        stringValues.push(SSSRDT);
        stringValues.push(SSCRSN);
        stringValues.push(SSCAUT);
        stringValues.push(SSCATP);
        stringValues.push(SSCLTX);
        stringValues.push(SSDGDT);
        stringValues.push(SSDG);
        stringValues.push(SSDCXM);
        stringValues.push(SSDCDT);
        stringValues.push(SSDCTP);
        stringValues.push(SSREL);
        stringValues.push(SSCTLH);
        stringValues.push(SSCODE);
        stringValues.push(SSCLSY);
        stringValues.push(SSCLAS);

        stringValues.push(SNAME);
        stringValues.push(SY);
        when(nitfReader.readBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());
        when(nitfReader.readTrimmedBytes(any(Integer.class))).thenAnswer(a -> stringValues.pop());

        intValues.push(SXSOFL);
        intValues.push(SXSHDL);
        intValues.push(SBND2_COL);
        intValues.push(SBND2_ROW);
        intValues.push(SBND1_COL);
        intValues.push(SBND1_ROW);
        intValues.push(SLOC_ROW);
        intValues.push(SLOC_COL);
        intValues.push(SALVL);
        intValues.push(SDLVL);
        when(nitfReader.readBytesAsInteger(any(Integer.class))).thenAnswer(a -> intValues.pop());

        strategy = mock(NitfParseStrategy.class);
        when(strategy.parseTREs(any(NitfReader.class), any(Integer.class))).thenReturn(new TreCollection());
    }

    @Test
    public void testParse() throws ParseException {
        NitfGraphicSegmentHeaderParser parser = new NitfGraphicSegmentHeaderParser();
        NitfGraphicSegmentHeader header = parser.parse(nitfReader, strategy);
        NitfSecurityMetadata securityMetaData = header.getSecurityMetadata();

        assertThat(header, notNullValue());
        assertThat(header.getBoundingBox1Column(), is(SBND1_COL));
        assertThat(header.getBoundingBox1Row(), is(SBND1_ROW));
        assertThat(header.getBoundingBox2Column(), is(SBND2_COL));
        assertThat(header.getBoundingBox2Row(), is(SBND2_ROW));
        assertThat(header.getGraphicColour(), is(GraphicColour.COLOUR));
        assertThat(header.getGraphicDataLength(), is(0));
        assertThat(header.getGraphicDisplayLevel(), is(SDLVL));
        assertThat(header.getGraphicName(), is(SNAME));
        assertThat(header.getAttachmentLevel(), is(SALVL));
        assertThat(header.getExtendedHeaderDataOverflow(), is(SXSOFL));

        assertThat(securityMetaData, notNullValue());
        assertThat(securityMetaData.getClassificationReason(), is(SSCRSN));
        assertThat(securityMetaData.getClassificationAuthority(), is(SSCAUT));
        assertThat(securityMetaData.getClassificationAuthorityType(), is(SSCATP));
        assertThat(securityMetaData.getClassificationText(), is(SSCLTX));
        assertThat(securityMetaData.getCodewords(), is(SSCODE));
        assertThat(securityMetaData.getControlAndHandling(), is(SSCTLH));
        assertThat(securityMetaData.getDeclassificationDate(), is(SSDCDT));
        assertThat(securityMetaData.getDeclassificationExemption(), is(SSDCXM));
        assertThat(securityMetaData.getDeclassificationType(), is(SSDCTP));
        assertThat(securityMetaData.getDowngrade(), is(SSDG));
        assertThat(securityMetaData.getDowngradeDate(), is(SSDGDT));
        assertThat(securityMetaData.getDowngradeDateOrSpecialCase(), nullValue());
        assertThat(securityMetaData.getReleaseInstructions(), is(SSREL));
        assertThat(securityMetaData.getDowngradeEvent(), nullValue());
        assertThat(securityMetaData.getSecurityClassification(), is(NitfSecurityClassification.UNCLASSIFIED));
        assertThat(securityMetaData.getSecurityClassificationSystem(), is(SSCLSY));
        assertThat(securityMetaData.getSecurityControlNumber(), is(SSCTLN));
        assertThat(securityMetaData.getSecuritySourceDate(), is(SSSRDT));
    }
}
