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
package org.codice.imaging.nitf.core.header;

import java.io.DataOutput;
import java.io.IOException;
import java.text.ParseException;
import org.codice.imaging.nitf.core.NitfDataSource;
import org.codice.imaging.nitf.core.RGBColour;
import org.codice.imaging.nitf.core.common.AbstractSegmentWriter;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.dataextension.DataExtensionSegment;
import org.codice.imaging.nitf.core.security.SecurityMetadataWriter;
import org.codice.imaging.nitf.core.text.TextSegment;
import org.codice.imaging.nitf.core.tre.TreParser;
import org.codice.imaging.nitf.core.tre.TreSource;

/**
 * Writer for the top level file header.
 */
public class NitfHeaderWriter extends AbstractSegmentWriter {

    private static final int BASIC_HEADER_LENGTH = 388;

    /**
     * Constructor.
     *
     * @param output the target to write the file header to.
     * @param treParser TreParser to use to serialise out the TREs.
     */
    public NitfHeaderWriter(final DataOutput output, final TreParser treParser) {
        super(output, treParser);
    }

    /**
     * Write out the file-level header.
     *
     * @param dataSource the data source to take NITF structure from.
     * @throws IOException on read or write problems
     * @throws ParseException on TRE parsing problems
     */
    public final void writeFileHeader(final NitfDataSource dataSource) throws IOException, ParseException {
        NitfHeader header = dataSource.getNitfHeader();
        writeBytes(header.getFileType().getTextEquivalent(), NitfHeaderConstants.FHDR_LENGTH + NitfHeaderConstants.FVER_LENGTH);
        writeFixedLengthNumber(header.getComplexityLevel(), NitfHeaderConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfHeaderConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfHeaderConstants.OSTAID_LENGTH);
        writeDateTime(header.getFileDateTime());
        writeFixedLengthString(header.getFileTitle(), NitfHeaderConstants.FTITLE_LENGTH);
        SecurityMetadataWriter securityWriter = new SecurityMetadataWriter(mOutput, mTreParser);
        securityWriter.writeFileSecurityMetadata(header.getFileSecurityMetadata());
        writeENCRYP();
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            mOutput.writeByte(header.getFileBackgroundColour().getRed());
            mOutput.writeByte(header.getFileBackgroundColour().getGreen());
            mOutput.writeByte(header.getFileBackgroundColour().getBlue());
            writeFixedLengthString(header.getOriginatorsName(), NitfHeaderConstants.ONAME_LENGTH);
        } else {
            writeFixedLengthString(header.getOriginatorsName(), NitfHeaderConstants.ONAME20_LENGTH);
        }
        writeFixedLengthString(header.getOriginatorsPhoneNumber(), NitfHeaderConstants.OPHONE_LENGTH);

        long headerLength = getBasicHeaderLength(header);

        int numberOfImageSegments = dataSource.getImageSegments().size();
        int numberOfGraphicSegments = dataSource.getGraphicSegments().size();
        int numberOfSymbolSegments = dataSource.getSymbolSegments().size();
        int numberOfLabelSegments = dataSource.getLabelSegments().size();
        int numberOfTextSegments = dataSource.getTextSegments().size();
        headerLength += numberOfImageSegments * (NitfHeaderConstants.LISH_LENGTH + NitfHeaderConstants.LI_LENGTH);
        headerLength += numberOfLabelSegments * (NitfHeaderConstants.LLSH_LENGTH + NitfHeaderConstants.LL_LENGTH);
        headerLength += numberOfGraphicSegments * (NitfHeaderConstants.LSSH_LENGTH + NitfHeaderConstants.LS_LENGTH);
        headerLength += numberOfSymbolSegments * (NitfHeaderConstants.LSSH_LENGTH + NitfHeaderConstants.LS_LENGTH);
        headerLength += numberOfTextSegments * (NitfHeaderConstants.LTSH_LENGTH + NitfHeaderConstants.LT_LENGTH);

        int numberOfDataExtensionSegments = 0;
        for (DataExtensionSegment desHeader : dataSource.getDataExtensionSegments()) {
            if (!desHeader.isStreamingMode()) {
                headerLength += NitfHeaderConstants.LDSH_LENGTH + NitfHeaderConstants.LD_LENGTH;
                numberOfDataExtensionSegments++;
            }
        }

        byte[] userDefinedHeaderData = mTreParser.getTREs(header, TreSource.UserDefinedHeaderData);
        int userDefinedHeaderDataLength = userDefinedHeaderData.length;
        if ((userDefinedHeaderDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedHeaderDataLength += NitfHeaderConstants.UDHOFL_LENGTH;
        }
        headerLength += userDefinedHeaderDataLength;

        byte[] extendedHeaderData = mTreParser.getTREs(header, TreSource.ExtendedHeaderData);
        int extendedHeaderDataLength = extendedHeaderData.length;
        if ((extendedHeaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            extendedHeaderDataLength += NitfHeaderConstants.XHDLOFL_LENGTH;
        }
        headerLength += extendedHeaderDataLength;

        long fileLength = headerLength;
        for (int i = 0; i < numberOfImageSegments; ++i) {
            fileLength += header.getImageSegmentSubHeaderLengths().get(i);
            fileLength += header.getImageSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            fileLength += header.getGraphicSegmentSubHeaderLengths().get(i);
            fileLength += header.getGraphicSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfSymbolSegments; ++i) {
            fileLength += header.getSymbolSegmentSubHeaderLengths().get(i);
            fileLength += header.getSymbolSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfLabelSegments; ++i) {
            fileLength += header.getLabelSegmentSubHeaderLengths().get(i);
            fileLength += header.getLabelSegmentDataLengths().get(i);
        }
        for (TextSegment textSegment : dataSource.getTextSegments()) {
            fileLength += textSegment.getHeaderLength();
            fileLength += textSegment.getData().length();
        }
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = dataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                fileLength += header.getDataExtensionSegmentSubHeaderLengths().get(i);
                fileLength += header.getDataExtensionSegmentDataLengths().get(i);
            }
        }
        writeFixedLengthNumber(fileLength, NitfHeaderConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfHeaderConstants.HL_LENGTH);
        writeFixedLengthNumber(numberOfImageSegments, NitfHeaderConstants.NUMI_LENGTH);
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeFixedLengthNumber(header.getImageSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LISH_LENGTH);
            writeFixedLengthNumber(header.getImageSegmentDataLengths().get(i), NitfHeaderConstants.LI_LENGTH);
        }
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getGraphicSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getGraphicSegmentDataLengths().get(i), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(0, NitfHeaderConstants.NUMX_LENGTH);
        } else {
            writeFixedLengthNumber(numberOfSymbolSegments, NitfHeaderConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfSymbolSegments; ++i) {
                writeFixedLengthNumber(header.getSymbolSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getSymbolSegmentDataLengths().get(i), NitfHeaderConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(numberOfLabelSegments, NitfHeaderConstants.NUML20_LENGTH);
            for (int i = 0; i < numberOfLabelSegments; ++i) {
                writeFixedLengthNumber(header.getLabelSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LLSH_LENGTH);
                writeFixedLengthNumber(header.getLabelSegmentDataLengths().get(i), NitfHeaderConstants.LL_LENGTH);
            }
        }
        writeFixedLengthNumber(numberOfTextSegments, NitfHeaderConstants.NUMT_LENGTH);
        for (TextSegment textSegment : dataSource.getTextSegments()) {
            writeFixedLengthNumber(textSegment.getHeaderLength(), NitfHeaderConstants.LTSH_LENGTH);
            writeFixedLengthNumber(textSegment.getData().length(), NitfHeaderConstants.LT_LENGTH);
        }
        writeFixedLengthNumber(numberOfDataExtensionSegments, NitfHeaderConstants.NUMDES_LENGTH);
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            DataExtensionSegment desHeader = dataSource.getDataExtensionSegments().get(i);
            if (!desHeader.isStreamingMode()) {
                writeFixedLengthNumber(header.getDataExtensionSegmentSubHeaderLengths().get(i), NitfHeaderConstants.LDSH_LENGTH);
                writeFixedLengthNumber(header.getDataExtensionSegmentDataLengths().get(i), NitfHeaderConstants.LD_LENGTH);
            }
        }
        writeFixedLengthNumber(0, NitfHeaderConstants.NUMRES_LENGTH);
        writeFixedLengthNumber(userDefinedHeaderDataLength, NitfHeaderConstants.UDHDL_LENGTH);
        if (userDefinedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), NitfHeaderConstants.UDHOFL_LENGTH);
            writeBytes(userDefinedHeaderData, userDefinedHeaderDataLength - NitfHeaderConstants.UDHOFL_LENGTH);
        }
        writeFixedLengthNumber(extendedHeaderDataLength, NitfHeaderConstants.XHDL_LENGTH);
        if (extendedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfHeaderConstants.XHDLOFL_LENGTH);
            writeBytes(extendedHeaderData, extendedHeaderDataLength - NitfHeaderConstants.XHDLOFL_LENGTH);
        }
    }

    private long getBasicHeaderLength(final NitfHeader header) {
        long headerLength = NitfHeaderConstants.FHDR_LENGTH
                + NitfHeaderConstants.FVER_LENGTH
                + NitfHeaderConstants.CLEVEL_LENGTH
                + NitfHeaderConstants.STYPE_LENGTH
                + NitfHeaderConstants.OSTAID_LENGTH
                + CommonConstants.STANDARD_DATE_TIME_LENGTH
                + NitfHeaderConstants.FTITLE_LENGTH
                + header.getFileSecurityMetadata().getSerialisedLength()
                + CommonConstants.ENCRYP_LENGTH
                + RGBColour.RGB_COLOUR_LENGTH
                + NitfHeaderConstants.ONAME_LENGTH
                + NitfHeaderConstants.OPHONE_LENGTH
                + NitfHeaderConstants.FL_LENGTH
                + NitfHeaderConstants.HL_LENGTH
                + NitfHeaderConstants.NUMI_LENGTH
                + NitfHeaderConstants.NUMS_LENGTH
                + NitfHeaderConstants.NUMX_LENGTH
                + NitfHeaderConstants.NUMT_LENGTH
                + NitfHeaderConstants.NUMDES_LENGTH
                + NitfHeaderConstants.NUMRES_LENGTH
                + NitfHeaderConstants.UDHDL_LENGTH
                + NitfHeaderConstants.XHDL_LENGTH;

        return headerLength;
    }


}
