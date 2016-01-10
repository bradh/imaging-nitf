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

import java.io.ByteArrayOutputStream;
import java.io.DataOutput;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import org.codice.imaging.nitf.core.common.CommonConstants;
import org.codice.imaging.nitf.core.common.CommonNitfSegment;
import org.codice.imaging.nitf.core.dataextension.NitfDataExtensionSegmentHeader;
import org.codice.imaging.nitf.core.graphic.NitfGraphicSegmentHeader;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.NitfImageBandLUT;
import org.codice.imaging.nitf.core.image.NitfImageBand;

import static org.codice.imaging.nitf.core.image.ImageConstants.ABPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.COMRAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IALVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ICORDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IDLVL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IFC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IGEOLO_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NUM_PARTS_IN_IGEOLO;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID1_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IID2_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ILOC_HALF_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IM;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMAG_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMFLT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IMODE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IREPBAND_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISORCE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISUBCAT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.ISYNC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.IXSOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBANDS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPC_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPP_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NBPR_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NCOLS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NELUT_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NICOM_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NLUTS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBH_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NPPBV_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.NROWS_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PJUST_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.PVTYPE_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.TGTID_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDIDL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.UDOFL_LENGTH;
import static org.codice.imaging.nitf.core.image.ImageConstants.XBANDS_LENGTH;

import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSHDL_LENGTH;
import static org.codice.imaging.nitf.core.graphic.GraphicSegmentConstants.SXSOFL_LENGTH;

import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DE;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESID_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESITEM_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESOFLW_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESSHL_LENGTH;
import static org.codice.imaging.nitf.core.dataextension.DataExtensionConstants.DESVER_LENGTH;

/**
 * Output independent parts of a NitfWriter implementation.
 */
public abstract class SharedNitfWriter implements NitfWriter {

    private static final int BASIC_HEADER_LENGTH = 388;
    private static final String DOWNGRADE_EVENT_MAGIC = "999998";
    private static final String STREAMING_FILE_HEADER = "STREAMING_FILE_HEADER";
    private static final int MAX_NUM_BANDS_IN_NBANDS_FIELD = 9;

    private TreParser mTreParser = null;
    private NitfDataSource mDataSource = null;

    /**
     * The target to write the data to.
     */
    protected DataOutput mOutput = null;

    /**
     * Initialise shared state.
     *
     * @param dataSource the parseStrategy that provides the data to be written out.
     */
    protected SharedNitfWriter(final NitfDataSource dataSource) {
        mDataSource = dataSource;
    }

    private String padNumberToLength(final long number, final int length) {
        return String.format("%0" + length + "d", number);
    }

    private String padStringToLength(final String s, final int length) {
        return String.format("%1$-" + length + "s", s);
    }

    private void writeDESData(final byte[] desData) throws IOException {
        if (desData != null) {
            mOutput.write(desData);
        }
    }

    private void writeDESHeader(final NitfDataExtensionSegmentHeader header, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(DE, DE.length());
        writeFixedLengthString(header.getIdentifier(), DESID_LENGTH);
        writeFixedLengthNumber(header.getDESVersion(), DESVER_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), fileType);
        if (header.isTreOverflow(fileType)) {
            writeFixedLengthString(header.getOverflowedHeaderType(), DESOFLW_LENGTH);
            writeFixedLengthNumber(header.getItemOverflowed(), DESITEM_LENGTH);
        }
        writeFixedLengthNumber(header.getUserDefinedSubheaderField().length(), DESSHL_LENGTH);
        if (header.getUserDefinedSubheaderField().length() > 0) {
            mOutput.writeBytes(header.getUserDefinedSubheaderField());
        } else {
            byte[] treData = getTREs(header, TreSource.TreOverflowDES);
            mOutput.write(treData);
        }
    }

    /**
     * Write out the data to the specified target.
     *
     * @throws ParseException if there is a problem reading data
     * @throws IOException if there is a problem writing data
     */
    protected final void writeData() throws ParseException, IOException {
        mTreParser = new TreParser();
        writeFileHeader();
        writeImageSegments();
        writeGraphicSegments();
        writeSymbolSegments();
        writeLabelSegments();
        writeTextSegments();
        writeDataExtensionSegments();
    }

    private void writeDataExtensionSegments() throws ParseException, IOException {
        int numberOfDataExtensionSegments = mDataSource.getDataExtensionSegmentHeaders().size();
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            if (STREAMING_FILE_HEADER.equals(mDataSource.getDataExtensionSegmentHeaders().get(i).getIdentifier().trim())) {
                continue;
            }
            writeDESHeader(mDataSource.getDataExtensionSegmentHeaders().get(i), mDataSource.getNitfHeader().getFileType());
            writeDESData(mDataSource.getDataExtensionSegmentData().get(i));
        }
    }

    private void writeTextSegments() throws ParseException, IOException {
        int numberOfTextSegments = mDataSource.getTextSegmentHeaders().size();
        for (int i = 0; i < numberOfTextSegments; ++i) {
            writeTextHeader(mDataSource.getTextSegmentHeaders().get(i), mDataSource.getNitfHeader().getFileType());
            writeTextData(mDataSource.getTextSegmentData().get(i));
        }
    }

    private void writeLabelSegments() throws IOException, ParseException {
        int numberOfLabelSegments = mDataSource.getLabelSegmentHeaders().size();
        for (int i = 0; i < numberOfLabelSegments; ++i) {
            writeLabelHeader(mDataSource.getLabelSegmentHeaders().get(i));
            writeLabelData(mDataSource.getLabelSegmentData().get(i));
        }
    }

    private void writeSymbolSegments() throws ParseException, IOException {
        int numberOfSymbolSegments = mDataSource.getSymbolSegmentHeaders().size();
        for (int i = 0; i < numberOfSymbolSegments; ++i) {
            writeSymbolHeader(mDataSource.getSymbolSegmentHeaders().get(i));
            writeSymbolData(mDataSource.getSymbolSegmentData().get(i));
        }
    }

    private void writeGraphicSegments() throws IOException, ParseException {
        int numberOfGraphicSegments = mDataSource.getGraphicSegmentHeaders().size();
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            writeGraphicHeader(mDataSource.getGraphicSegmentHeaders().get(i));
            writeGraphicData(mDataSource.getGraphicSegmentData().get(i));
        }
    }

    private void writeImageSegments() throws ParseException, IOException {
        int numberOfImageSegments = mDataSource.getImageSegmentHeaders().size();
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeImageHeader(mDataSource.getImageSegmentHeaders().get(i), mDataSource.getNitfHeader().getFileType());
            writeImageData(mDataSource.getImageSegmentData().get(i));
        }
    }

    private void writeFileHeader() throws IOException, ParseException {
        Nitf header = mDataSource.getNitfHeader();
        mOutput.writeBytes(header.getFileType().getTextEquivalent());
        writeFixedLengthNumber(header.getComplexityLevel(), NitfConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfConstants.OSTAID_LENGTH);
        mOutput.writeBytes(header.getFileDateTime().getSourceString());
        writeFixedLengthString(header.getFileTitle(), NitfConstants.FTITLE_LENGTH);
        writeFileSecurityMetadata(header.getFileSecurityMetadata(), header.getFileType());
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            mOutput.writeByte(header.getFileBackgroundColour().getRed());
            mOutput.writeByte(header.getFileBackgroundColour().getGreen());
            mOutput.writeByte(header.getFileBackgroundColour().getBlue());
            writeFixedLengthString(header.getOriginatorsName(), NitfConstants.ONAME_LENGTH);
        } else {
            writeFixedLengthString(header.getOriginatorsName(), NitfConstants.ONAME20_LENGTH);
        }
        writeFixedLengthString(header.getOriginatorsPhoneNumber(), NitfConstants.OPHONE_LENGTH);
        int numberOfImageSegments = header.getImageSegmentSubHeaderLengths().size();
        int numberOfGraphicSegments = header.getGraphicSegmentSubHeaderLengths().size();
        int numberOfLabelSegments = header.getLabelSegmentSubHeaderLengths().size();
        int numberOfTextSegments = header.getTextSegmentSubHeaderLengths().size();
        byte[] userDefinedHeaderData = getTREs(header, TreSource.UserDefinedHeaderData);
        int userDefinedHeaderDataLength = userDefinedHeaderData.length;
        if ((userDefinedHeaderDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedHeaderDataLength += NitfConstants.UDHOFL_LENGTH;
        }
        byte[] extendedHeaderData = getTREs(header, TreSource.ExtendedHeaderData);
        int extendedHeaderDataLength = extendedHeaderData.length;
        if ((extendedHeaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            extendedHeaderDataLength += NitfConstants.XHDLOFL_LENGTH;
        }
        int headerLength = BASIC_HEADER_LENGTH
                + numberOfImageSegments * (NitfConstants.LISH_LENGTH + NitfConstants.LI_LENGTH)
                + numberOfGraphicSegments * (NitfConstants.LSSH_LENGTH + NitfConstants.LS_LENGTH)
                + numberOfTextSegments * (NitfConstants.LTSH_LENGTH + NitfConstants.LT_LENGTH)
                + numberOfLabelSegments * (NitfConstants.LLSH_LENGTH + NitfConstants.LL_LENGTH)
                + userDefinedHeaderDataLength
                + extendedHeaderDataLength;

        int numberOfDataExtensionSegments = 0;
        for (NitfDataExtensionSegmentHeader desHeader : mDataSource.getDataExtensionSegmentHeaders()) {
            // TODO: move this test to NitfDataExtensionSegmentHeader
            if (!STREAMING_FILE_HEADER.equals(desHeader.getIdentifier().trim())) {
                headerLength += NitfConstants.LDSH_LENGTH + NitfConstants.LD_LENGTH;
                numberOfDataExtensionSegments++;
            }
        }
        if (header.getFileType() == FileType.NITF_TWO_ZERO) {
            if (DOWNGRADE_EVENT_MAGIC.equals(header.getFileSecurityMetadata().getDowngradeDateOrSpecialCase())) {
                headerLength += NitfConstants.XSDEVT20_LENGTH;
            }
        }
        int fileLength = headerLength;
        for (int i = 0; i < numberOfImageSegments; ++i) {
            fileLength += header.getImageSegmentSubHeaderLengths().get(i);
            fileLength += header.getImageSegmentDataLengths().get(i);
        }
        // Also handles symbol segments in NITF 2.0 files.
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            fileLength += header.getGraphicSegmentSubHeaderLengths().get(i);
            fileLength += header.getGraphicSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfLabelSegments; ++i) {
            fileLength += header.getLabelSegmentSubHeaderLengths().get(i);
            fileLength += header.getLabelSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfTextSegments; ++i) {
            fileLength += header.getTextSegmentSubHeaderLengths().get(i);
            fileLength += header.getTextSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            NitfDataExtensionSegmentHeader desHeader = mDataSource.getDataExtensionSegmentHeaders().get(i);
            if (!STREAMING_FILE_HEADER.equals(desHeader.getIdentifier().trim())) {
                fileLength += header.getDataExtensionSegmentSubHeaderLengths().get(i);
                fileLength += header.getDataExtensionSegmentDataLengths().get(i);
            }
        }
        writeFixedLengthNumber(fileLength, NitfConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfConstants.HL_LENGTH);
        writeFixedLengthNumber(numberOfImageSegments, NitfConstants.NUMI_LENGTH);
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeFixedLengthNumber(header.getImageSegmentSubHeaderLengths().get(i), NitfConstants.LISH_LENGTH);
            writeFixedLengthNumber(header.getImageSegmentDataLengths().get(i), NitfConstants.LI_LENGTH);
        }
        if ((header.getFileType() == FileType.NITF_TWO_ONE) || (header.getFileType() == FileType.NSIF_ONE_ZERO)) {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getGraphicSegmentSubHeaderLengths().get(i), NitfConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getGraphicSegmentDataLengths().get(i), NitfConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(0, NitfConstants.NUMX_LENGTH);
        } else {
            writeFixedLengthNumber(numberOfGraphicSegments, NitfConstants.NUMS_LENGTH);
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeFixedLengthNumber(header.getSymbolSegmentSubHeaderLengths().get(i), NitfConstants.LSSH_LENGTH);
                writeFixedLengthNumber(header.getSymbolSegmentDataLengths().get(i), NitfConstants.LS_LENGTH);
            }
            writeFixedLengthNumber(numberOfLabelSegments, NitfConstants.NUML20_LENGTH);
            for (int i = 0; i < numberOfLabelSegments; ++i) {
                writeFixedLengthNumber(header.getLabelSegmentSubHeaderLengths().get(i), NitfConstants.LLSH_LENGTH);
                writeFixedLengthNumber(header.getLabelSegmentDataLengths().get(i), NitfConstants.LL_LENGTH);
            }
        }
        writeFixedLengthNumber(numberOfTextSegments, NitfConstants.NUMT_LENGTH);
        for (int i = 0; i < numberOfTextSegments; ++i) {
            writeFixedLengthNumber(header.getTextSegmentSubHeaderLengths().get(i), NitfConstants.LTSH_LENGTH);
            writeFixedLengthNumber(header.getTextSegmentDataLengths().get(i), NitfConstants.LT_LENGTH);
        }
        writeFixedLengthNumber(numberOfDataExtensionSegments, NitfConstants.NUMDES_LENGTH);
        for (int i = 0; i < numberOfDataExtensionSegments; ++i) {
            NitfDataExtensionSegmentHeader desHeader = mDataSource.getDataExtensionSegmentHeaders().get(i);
            if (!STREAMING_FILE_HEADER.equals(desHeader.getIdentifier().trim())) {
                writeFixedLengthNumber(header.getDataExtensionSegmentSubHeaderLengths().get(i), NitfConstants.LDSH_LENGTH);
                writeFixedLengthNumber(header.getDataExtensionSegmentDataLengths().get(i), NitfConstants.LD_LENGTH);
            }
        }
        writeFixedLengthNumber(0, NitfConstants.NUMRES_LENGTH);
        writeFixedLengthNumber(userDefinedHeaderDataLength, NitfConstants.UDHDL_LENGTH);
        if (userDefinedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), NitfConstants.UDHOFL_LENGTH);
            mOutput.write(userDefinedHeaderData);
        }
        writeFixedLengthNumber(extendedHeaderDataLength, NitfConstants.XHDL_LENGTH);
        if (extendedHeaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfConstants.XHDLOFL_LENGTH);
            mOutput.write(extendedHeaderData);
        }
    }

    private void writeFileSecurityMetadata(final NitfFileSecurityMetadata fsmeta, final FileType fileType) throws IOException {
        writeSecurityMetadata(fsmeta, fileType);
        writeFixedLengthString(fsmeta.getFileCopyNumber(), NitfConstants.FSCOP_LENGTH);
        writeFixedLengthString(fsmeta.getFileNumberOfCopies(), NitfConstants.FSCPYS_LENGTH);
    }

    private void writeFixedLengthNumber(final long number, final int length) throws IOException {
        mOutput.writeBytes(padNumberToLength(number, length));
    }

    private void writeFixedLengthString(final String s, final int length) throws IOException {
        mOutput.writeBytes(padStringToLength(s, length));
    }

    private void writeGraphicData(final byte[] graphicData) throws IOException {
        mOutput.write(graphicData);
    }

    private void writeGraphicHeader(final NitfGraphicSegmentHeader header) throws IOException, ParseException {
        writeFixedLengthString(NitfConstants.SY, NitfConstants.SY.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.SID_LENGTH);
        writeFixedLengthString(header.getGraphicName(), NitfConstants.SNAME_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ONE);
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        writeFixedLengthString(NitfConstants.SFMT_CGM, NitfConstants.SFMT_CGM.length());
        writeFixedLengthString(NitfConstants.SSTRUCT, NitfConstants.SSTRUCT.length());
        writeFixedLengthNumber(header.getGraphicDisplayLevel(), NitfConstants.SDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.SALVL_LENGTH);
        writeFixedLengthNumber(header.getGraphicLocationRow(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getGraphicLocationColumn(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox1Row(), NitfConstants.SBND1_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox1Column(), NitfConstants.SBND1_HALF_LENGTH);
        writeFixedLengthString(header.getGraphicColour().getTextEquivalent(), NitfConstants.SCOLOR_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox2Row(), NitfConstants.SBND2_HALF_LENGTH);
        writeFixedLengthNumber(header.getBoundingBox2Column(), NitfConstants.SBND2_HALF_LENGTH);
        writeFixedLengthString(NitfConstants.SRES, NitfConstants.SRES.length()); // SRES2
        byte[] graphicExtendedSubheaderData = getTREs(header, TreSource.GraphicExtendedSubheaderData);
        int graphicExtendedSubheaderDataLength = graphicExtendedSubheaderData.length;
        if ((graphicExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            graphicExtendedSubheaderDataLength += SXSOFL_LENGTH;
        }
        writeFixedLengthNumber(graphicExtendedSubheaderDataLength, SXSHDL_LENGTH);
        if (graphicExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), SXSOFL_LENGTH);
            mOutput.write(graphicExtendedSubheaderData);
        }
    }

    private void writeImageData(final byte[] imageData) throws IOException {
        if (imageData != null && imageData.length > 0) {
            mOutput.write(imageData);
        }
    }

    private void writeImageHeader(final NitfImageSegmentHeader header, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(IM, IM.length());
        writeFixedLengthString(header.getIdentifier(), IID1_LENGTH);
        writeFixedLengthString(header.getImageDateTime().getSourceString(), CommonConstants.STANDARD_DATE_TIME_LENGTH);
        writeFixedLengthString(header.getImageTargetId().toString(), TGTID_LENGTH);
        writeFixedLengthString(header.getImageIdentifier2(), IID2_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), fileType);
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        writeFixedLengthString(header.getImageSource(), ISORCE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfRows(), NROWS_LENGTH);
        writeFixedLengthNumber(header.getNumberOfColumns(), NCOLS_LENGTH);
        writeFixedLengthString(header.getPixelValueType().getTextEquivalent(), PVTYPE_LENGTH);
        writeFixedLengthString(header.getImageRepresentation().getTextEquivalent(), IREP_LENGTH);
        writeFixedLengthString(header.getImageCategory().getTextEquivalent(), ICAT_LENGTH);
        writeFixedLengthNumber(header.getActualBitsPerPixelPerBand(), ABPP_LENGTH);
        writeFixedLengthString(header.getPixelJustification().getTextEquivalent(), PJUST_LENGTH);
        writeFixedLengthString(header.getImageCoordinatesRepresentation().getTextEquivalent(fileType), ICORDS_LENGTH);
        if (header.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE) {
            writeFixedLengthString(header.getImageCoordinates().getCoordinate00().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinate0MaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinateMaxRowMaxCol().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
            writeFixedLengthString(header.getImageCoordinates().getCoordinateMaxRow0().getSourceFormat(),
                    IGEOLO_LENGTH / NUM_PARTS_IN_IGEOLO);
        }
        writeFixedLengthNumber(header.getImageComments().size(), NICOM_LENGTH);
        for (String comment : header.getImageComments()) {
            writeFixedLengthString(comment, ICOM_LENGTH);
        }
        writeFixedLengthString(header.getImageCompression().getTextEquivalent(), IC_LENGTH);
        if ((header.getImageCompression() != ImageCompression.NOTCOMPRESSED)
                && (header.getImageCompression() != ImageCompression.NOTCOMPRESSEDMASK)) {
            writeFixedLengthString(header.getCompressionRate(), COMRAT_LENGTH);
        }

        if (header.getNumBands() <= MAX_NUM_BANDS_IN_NBANDS_FIELD) {
            writeFixedLengthNumber(header.getNumBands(), NBANDS_LENGTH);
        } else {
            writeFixedLengthNumber(0, NBANDS_LENGTH);
            writeFixedLengthNumber(header.getNumBands(), XBANDS_LENGTH);
        }

        for (int i = 0; i < header.getNumBands(); ++i) {
            NitfImageBand band = header.getImageBandZeroBase(i);
            writeFixedLengthString(band.getImageRepresentation(), IREPBAND_LENGTH);
            writeFixedLengthString(band.getSubCategory(), ISUBCAT_LENGTH);
            writeFixedLengthString("N", IFC_LENGTH);
            writeFixedLengthString("", IMFLT_LENGTH); // space filled
            writeFixedLengthNumber(band.getNumLUTs(), NLUTS_LENGTH);
            if (band.getNumLUTs() != 0) {
                writeFixedLengthNumber(band.getNumLUTEntries(), NELUT_LENGTH);
                for (int j = 0; j < band.getNumLUTs(); ++j) {
                    NitfImageBandLUT lut = band.getLUTZeroBase(j);
                    mOutput.write(lut.getEntries());
                }
            }
        }
        writeFixedLengthNumber(0, ISYNC_LENGTH);
        writeFixedLengthString(header.getImageMode().getTextEquivalent(), IMODE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerRow(), NBPR_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerColumn(), NBPC_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockHorizontal(), NPPBH_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockVertical(), NPPBV_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBitsPerPixelPerBand(), NBPP_LENGTH);
        writeFixedLengthNumber(header.getImageDisplayLevel(), IDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), IALVL_LENGTH);
        writeFixedLengthNumber(header.getImageLocationRow(), ILOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getImageLocationColumn(), ILOC_HALF_LENGTH);
        writeFixedLengthString(header.getImageMagnification(), IMAG_LENGTH);
        byte[] userDefinedImageData = getTREs(header, TreSource.UserDefinedImageData);
        int userDefinedImageDataLength = userDefinedImageData.length;
        if ((userDefinedImageDataLength > 0) || (header.getUserDefinedHeaderOverflow() != 0)) {
            userDefinedImageDataLength += UDOFL_LENGTH;
        }
        writeFixedLengthNumber(userDefinedImageDataLength, UDIDL_LENGTH);
        if (userDefinedImageDataLength > 0) {
            writeFixedLengthNumber(header.getUserDefinedHeaderOverflow(), UDOFL_LENGTH);
            mOutput.write(userDefinedImageData);
        }
        byte[] imageExtendedSubheaderData = getTREs(header, TreSource.ImageExtendedSubheaderData);
        int imageExtendedSubheaderDataLength = imageExtendedSubheaderData.length;
        if ((imageExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            imageExtendedSubheaderDataLength += IXSOFL_LENGTH;
        }
        writeFixedLengthNumber(imageExtendedSubheaderDataLength, IXSHDL_LENGTH);
        if (imageExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), IXSOFL_LENGTH);
            mOutput.write(imageExtendedSubheaderData);
        }
    }

    private void writeLabelData(final String labelData) throws IOException {
        mOutput.writeBytes(labelData);
    }

    private void writeLabelHeader(final NitfLabelSegmentHeader header) throws IOException, ParseException {
        writeFixedLengthString(NitfConstants.LA, NitfConstants.LA.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.LID_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ZERO);
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        writeFixedLengthString(" ", NitfConstants.LFS_LENGTH);
        writeFixedLengthNumber(header.getLabelCellWidth(), NitfConstants.LCW_LENGTH);
        writeFixedLengthNumber(header.getLabelCellHeight(), NitfConstants.LCH_LENGTH);
        writeFixedLengthNumber(header.getLabelDisplayLevel(), NitfConstants.LDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.LALVL_LENGTH);
        writeFixedLengthNumber(header.getLabelLocationRow(), NitfConstants.LLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getLabelLocationColumn(), NitfConstants.LLOC_HALF_LENGTH);
        mOutput.write(header.getLabelTextColour().toByteArray());
        mOutput.write(header.getLabelBackgroundColour().toByteArray());
        byte[] labelExtendedSubheaderData = getTREs(header, TreSource.LabelExtendedSubheaderData);
        int labelExtendedSubheaderDataLength = labelExtendedSubheaderData.length;
        if ((labelExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            labelExtendedSubheaderDataLength += NitfConstants.LXSOFL_LENGTH;
        }
        writeFixedLengthNumber(labelExtendedSubheaderDataLength, NitfConstants.LXSHDL_LENGTH);
        if (labelExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfConstants.LXSOFL_LENGTH);
            mOutput.write(labelExtendedSubheaderData);
        }
    }

    private void writeSecurityMetadata(final NitfSecurityMetadata securityMetadata, final FileType fileType) throws IOException {
        if (fileType == FileType.NITF_TWO_ZERO) {
            writeSecurityMetadata20(securityMetadata);
        } else {
            writeSecurityMetadata21(securityMetadata);
        }
    }

    private void writeSecurityMetadata20(final NitfSecurityMetadata securityMetadata) throws IOException {
        writeFixedLengthString(securityMetadata.getSecurityClassification().getTextEquivalent(), NitfConstants.XSCLAS_LENGTH);
        writeFixedLengthString(securityMetadata.getCodewords(), NitfConstants.XSCODE20_LENGTH);
        writeFixedLengthString(securityMetadata.getControlAndHandling(), NitfConstants.XSCTLH20_LENGTH);
        writeFixedLengthString(securityMetadata.getReleaseInstructions(), NitfConstants.XSREL20_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthority(), NitfConstants.XSCAUT20_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityControlNumber(), NitfConstants.XSCTLN20_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngradeDateOrSpecialCase(), NitfConstants.XSDWNG20_LENGTH);
        if (DOWNGRADE_EVENT_MAGIC.equals(securityMetadata.getDowngradeDateOrSpecialCase())) {
            writeFixedLengthString(securityMetadata.getDowngradeEvent(), NitfConstants.XSDEVT20_LENGTH);
        }
    }

    private void writeSecurityMetadata21(final NitfSecurityMetadata securityMetadata) throws IOException {
        writeFixedLengthString(securityMetadata.getSecurityClassification().getTextEquivalent(), NitfConstants.XSCLAS_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityClassificationSystem(), NitfConstants.XSCLSY_LENGTH);
        writeFixedLengthString(securityMetadata.getCodewords(), NitfConstants.XSCODE_LENGTH);
        writeFixedLengthString(securityMetadata.getControlAndHandling(), NitfConstants.XSCTLH_LENGTH);
        writeFixedLengthString(securityMetadata.getReleaseInstructions(), NitfConstants.XSREL_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationType(), NitfConstants.XSDCTP_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationDate(), NitfConstants.XSDCDT_LENGTH);
        writeFixedLengthString(securityMetadata.getDeclassificationExemption(), NitfConstants.XSDCXM_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngrade(), NitfConstants.XSDG_LENGTH);
        writeFixedLengthString(securityMetadata.getDowngradeDate(), NitfConstants.XSDGDT_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationText(), NitfConstants.XSCLTX_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthorityType(), NitfConstants.XSCATP_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationAuthority(), NitfConstants.XSCAUT_LENGTH);
        writeFixedLengthString(securityMetadata.getClassificationReason(), NitfConstants.XSCRSN_LENGTH);
        writeFixedLengthString(securityMetadata.getSecuritySourceDate(), NitfConstants.XSSRDT_LENGTH);
        writeFixedLengthString(securityMetadata.getSecurityControlNumber(), NitfConstants.XSCTLN_LENGTH);
    }

    private void writeSymbolData(final byte[] graphicData) throws IOException {
        mOutput.write(graphicData);
    }

    private void writeSymbolHeader(final NitfSymbolSegmentHeader header) throws IOException, ParseException {
        writeFixedLengthString(NitfConstants.SY, NitfConstants.SY.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.SID_LENGTH);
        writeFixedLengthString(header.getSymbolName(), NitfConstants.SNAME_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), FileType.NITF_TWO_ZERO);
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        writeFixedLengthString(header.getSymbolType().getTextEquivalent(), NitfConstants.SYTYPE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfLinesPerSymbol(), NitfConstants.NLIPS_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerLine(), NitfConstants.NPIXPL_LENGTH);
        writeFixedLengthNumber(header.getLineWidth(), NitfConstants.NWDTH_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBitsPerPixel(), NitfConstants.SYNBPP_LENGTH);
        writeFixedLengthNumber(header.getSymbolDisplayLevel(), NitfConstants.SDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.SALVL_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocationRow(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocationColumn(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocation2Row(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getSymbolLocation2Column(), NitfConstants.SLOC_HALF_LENGTH);
        writeFixedLengthString(header.getSymbolColour().getTextEquivalent(), NitfConstants.SCOLOR_LENGTH);
        writeFixedLengthString(header.getSymbolNumber(), NitfConstants.SNUM_LENGTH);
        writeFixedLengthNumber(header.getSymbolRotation(), NitfConstants.SROT_LENGTH);
        // TODO: need to have a LUT list or similar in the symbol segment subheader
        writeFixedLengthNumber(0, NitfConstants.SYNELUT_LENGTH);
        byte[] symbolExtendedSubheaderData = getTREs(header, TreSource.SymbolExtendedSubheaderData);
        int symbolExtendedSubheaderDataLength = symbolExtendedSubheaderData.length;
        if ((symbolExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            symbolExtendedSubheaderDataLength += SXSOFL_LENGTH;
        }
        writeFixedLengthNumber(symbolExtendedSubheaderDataLength, SXSHDL_LENGTH);
        if (symbolExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), SXSOFL_LENGTH);
            mOutput.write(symbolExtendedSubheaderData);
        }
    }

    private void writeTextData(final String textData) throws IOException {
        mOutput.writeBytes(textData);
    }

    private void writeTextHeader(final NitfTextSegmentHeader header, final FileType fileType) throws IOException, ParseException {
        writeFixedLengthString(NitfConstants.TE, NitfConstants.TE.length());
        if (fileType == FileType.NITF_TWO_ZERO) {
            writeFixedLengthString(header.getIdentifier(), NitfConstants.TEXTID20_LENGTH);
        } else {
            writeFixedLengthString(header.getIdentifier(), NitfConstants.TEXTID_LENGTH);
            writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.TXTALVL_LENGTH);
        }
        writeFixedLengthString(header.getTextDateTime().getSourceString(), CommonConstants.STANDARD_DATE_TIME_LENGTH);
        writeFixedLengthString(header.getTextTitle(), NitfConstants.TXTITL_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata(), fileType);
        writeFixedLengthString("0", CommonConstants.ENCRYP_LENGTH);
        writeFixedLengthString(header.getTextFormat().getTextEquivalent(), NitfConstants.TXTFMT_LENGTH);
        byte[] textExtendedSubheaderData = getTREs(header, TreSource.TextExtendedSubheaderData);
        int textExtendedSubheaderDataLength = textExtendedSubheaderData.length;
        if ((textExtendedSubheaderDataLength > 0) || (header.getExtendedHeaderDataOverflow() != 0)) {
            textExtendedSubheaderDataLength += NitfConstants.TXSOFL_LENGTH;
        }
        writeFixedLengthNumber(textExtendedSubheaderDataLength, NitfConstants.TXSHDL_LENGTH);
        if (textExtendedSubheaderDataLength > 0) {
            writeFixedLengthNumber(header.getExtendedHeaderDataOverflow(), NitfConstants.TXSOFL_LENGTH);
            mOutput.write(textExtendedSubheaderData);
        }
    }

    private byte[] getTREs(final CommonNitfSegment header, final TreSource source) throws ParseException, IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (Tre tre : header.getTREsRawStructure().getTREsForSource(source)) {
            String name = padStringToLength(tre.getName(), NitfConstants.TAG_LENGTH);
            baos.write(name.getBytes(StandardCharsets.UTF_8));
            if (tre.getRawData() != null) {
                String tagLen = padNumberToLength(tre.getRawData().length, NitfConstants.TAGLEN_LENGTH);
                baos.write(tagLen.getBytes(StandardCharsets.UTF_8));
                baos.write(tre.getRawData());
            } else {
                byte[] treData = mTreParser.serializeTRE(tre);
                baos.write(padNumberToLength(treData.length, NitfConstants.TAGLEN_LENGTH).getBytes(StandardCharsets.UTF_8));
                baos.write(treData);
            }
        }
        return baos.toByteArray();
    }

}