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

import static org.codice.imaging.nitf.core.SymbolConstants.NLIPS_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.NPIXPL_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.NWDTH_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.SNUM_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.SROT_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.SYNBPP_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.SYNELUT_LENGTH;
import static org.codice.imaging.nitf.core.SymbolConstants.SYTYPE_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SALVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SCOLOR_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SDLVL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SID_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SLOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SNAME_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SY;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.AbstractNitfSegmentParser;

/**
    Parser for a symbol segment subheader in a NITF 2.0 file.
*/
class NitfSymbolSegmentHeaderParser extends AbstractNitfSegmentParser {

    private int numberOfEntriesInLUT = 0;
    private int symbolExtendedSubheaderLength = 0;

    private NitfSymbolSegmentHeader segment = null;

    NitfSymbolSegmentHeaderParser() {
    }

    final NitfSymbolSegmentHeader parse(final NitfReader nitfReader, final NitfParseStrategy parseStrategy) throws ParseException {

        reader = nitfReader;
        segment = new NitfSymbolSegmentHeader();
        parsingStrategy = parseStrategy;

        readSY();
        readSID();
        readSNAME();
        segment.setSecurityMetadata(new NitfSecurityMetadata(reader));
        readENCRYP();
        readSTYPE();
        readNLIPS();
        readNPIXPL();
        readNWDTH();
        readNBPP();
        readSDLVL();
        readSALVL();
        readSLOC();
        readSLOC2();
        readSCOLOR();
        readSNUM();
        readSROT();
        readNELUT();
        for (int i = 0; i < numberOfEntriesInLUT; ++i) {
            throw new UnsupportedOperationException("TODO: Implement LUT parsing when we have an example");
        }
        readSXSHDL();
        if (symbolExtendedSubheaderLength > 0) {
            readSXSOFL();
            readSXSHD();
        }
        return segment;
    }

    private void readSY() throws ParseException {
        reader.verifyHeaderMagic(SY);
    }

    private void readSID() throws ParseException {
        segment.setIdentifier(reader.readTrimmedBytes(SID_LENGTH));
    }

    private void readSNAME() throws ParseException {
        segment.setSymbolName(reader.readTrimmedBytes(SNAME_LENGTH));
    }

    private void readSTYPE() throws ParseException {
        String stype = reader.readTrimmedBytes(SYTYPE_LENGTH);
        segment.setSymbolType(SymbolType.getEnumValue(stype));
    }

    private void readNLIPS() throws ParseException {
        segment.setNumberOfLinesPerSymbol(reader.readBytesAsInteger(NLIPS_LENGTH));
    }

    private void readNPIXPL() throws ParseException {
        segment.setNumberOfPixelsPerLine(reader.readBytesAsInteger(NPIXPL_LENGTH));
    }

    private void readNWDTH() throws ParseException {
        segment.setLineWidth(reader.readBytesAsInteger(NWDTH_LENGTH));
    }

    private void readNBPP() throws ParseException {
        segment.setNumberOfBitsPerPixel(reader.readBytesAsInteger(SYNBPP_LENGTH));
    }

    private void readSDLVL() throws ParseException {
        segment.setSymbolDisplayLevel(reader.readBytesAsInteger(SDLVL_LENGTH));
    }

    private void readSALVL() throws ParseException {
        segment.setAttachmentLevel(reader.readBytesAsInteger(SALVL_LENGTH));
    }

    private void readSLOC() throws ParseException {
        segment.setSymbolLocationRow(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocationColumn(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSLOC2() throws ParseException {
        segment.setSymbolLocation2Row(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
        segment.setSymbolLocation2Column(reader.readBytesAsInteger(SLOC_HALF_LENGTH));
    }

    private void readSCOLOR() throws ParseException {
        String scolor = reader.readTrimmedBytes(SCOLOR_LENGTH);
        segment.setSymbolColourFormat(SymbolColour.getEnumValue(scolor));
    }

    private void readSNUM() throws ParseException {
        segment.setSymbolNumber(reader.readBytes(SNUM_LENGTH));
    }

    private void readSROT() throws ParseException {
        segment.setSymbolRotation(reader.readBytesAsInteger(SROT_LENGTH));
    }

    private void readNELUT() throws ParseException {
        numberOfEntriesInLUT = reader.readBytesAsInteger(SYNELUT_LENGTH);
    }

    private void readSXSHDL() throws ParseException {
        symbolExtendedSubheaderLength = reader.readBytesAsInteger(SXSHDL_LENGTH);
    }

    private void readSXSOFL() throws ParseException {
        segment.setExtendedHeaderDataOverflow(reader.readBytesAsInteger(SXSOFL_LENGTH));
    }

    private void readSXSHD() throws ParseException {
        TreCollection extendedSubheaderTREs = parsingStrategy.parseTREs(reader,
                symbolExtendedSubheaderLength - SXSOFL_LENGTH,
                TreSource.SymbolExtendedSubheaderData);
        segment.mergeTREs(extendedSubheaderTREs);
    }
}
