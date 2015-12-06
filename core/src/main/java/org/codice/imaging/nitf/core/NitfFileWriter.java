/*
 * Copyright 2015 (c) Codice Foundation
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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author bradh
 */
public class NitfFileWriter implements NitfWriter {

    private SlottedNitfParseStrategy mDataSource = null;
    private String mOutputFileName = null;
    private RandomAccessFile mOutputFile = null;

    private static final int BASIC_HEADER_LENGTH = 388;

    /**
     * Construct a file-based NITF writer.
     *
     * @param nitfDataSource the source of data to be written out
     * @param outputFileName the name (including path) of the target file
     */
    public NitfFileWriter(final SlottedNitfParseStrategy nitfDataSource, final String outputFileName) {
        mDataSource = nitfDataSource;
        mOutputFileName = outputFileName;
    }

    @Override
    public final void write() {
        try {
            mOutputFile = new RandomAccessFile(mOutputFileName, NitfConstants.WRITE_MODE);
            writeFileHeader(mDataSource.nitfFileLevelHeader);
            int numberOfImageSegments = mDataSource.imageSegmentHeaders.size();
            for (int i = 0; i < numberOfImageSegments; ++i) {
                writeImageHeader(mDataSource.imageSegmentHeaders.get(i), mDataSource.nitfFileLevelHeader.getFileType());
                writeImageData(mDataSource.imageSegmentData.get(i));
            }
            int numberOfGraphicSegments = mDataSource.graphicSegmentHeaders.size();
            for (int i = 0; i < numberOfGraphicSegments; ++i) {
                writeGraphicHeader(mDataSource.graphicSegmentHeaders.get(i));
                writeGraphicData(mDataSource.graphicSegmentData.get(i));
            }
            int numberOfTextSegments = mDataSource.textSegmentHeaders.size();
            for (int i = 0; i < numberOfTextSegments; ++i) {
                writeTextHeader(mDataSource.textSegmentHeaders.get(i));
                writeTextData(mDataSource.textSegmentData.get(i));
            }
            mOutputFile.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(NitfFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(NitfFileWriter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void writeFileHeader(final Nitf header) throws IOException {
        mOutputFile.writeBytes(header.getFileType().getTextEquivalent());
        writeFixedLengthNumber(header.getComplexityLevel(), NitfConstants.CLEVEL_LENGTH);
        writeFixedLengthString(header.getStandardType(), NitfConstants.STYPE_LENGTH);
        writeFixedLengthString(header.getOriginatingStationId(), NitfConstants.OSTAID_LENGTH);
        mOutputFile.writeBytes(header.getFileDateTime().getSourceString());
        writeFixedLengthString(header.getFileTitle(), NitfConstants.FTITLE_LENGTH);
        writeFileSecurityMetadata(header.getFileSecurityMetadata());
        writeFixedLengthString("0", NitfConstants.ENCRYP_LENGTH);
        mOutputFile.writeByte(header.getFileBackgroundColour().getRed());
        mOutputFile.writeByte(header.getFileBackgroundColour().getGreen());
        mOutputFile.writeByte(header.getFileBackgroundColour().getBlue());
        writeFixedLengthString(header.getOriginatorsName(), NitfConstants.ONAME_LENGTH);
        writeFixedLengthString(header.getOriginatorsPhoneNumber(), NitfConstants.OPHONE_LENGTH);

        int numberOfImageSegments = header.getImageSegmentDataLengths().size();
        int numberOfGraphicSegments = header.getGraphicSegmentDataLengths().size();
        int numberOfTextSegments = header.getTextSegmentDataLengths().size();

        int headerLength = BASIC_HEADER_LENGTH
                + numberOfImageSegments * (NitfConstants.LISH_LENGTH + NitfConstants.LI_LENGTH)
                + numberOfGraphicSegments * (NitfConstants.LSSH_LENGTH + NitfConstants.LS_LENGTH)
                + numberOfTextSegments * (NitfConstants.LTSH_LENGTH + NitfConstants.LT_LENGTH);

        int fileLength = headerLength;
        for (int i = 0; i < numberOfImageSegments; ++i) {
            fileLength += header.getImageSegmentSubHeaderLengths().get(i);
            fileLength += header.getImageSegmentDataLengths().get(i);
        }
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            fileLength += header.getGraphicSegmentSubHeaderLengths().get(i);
            fileLength += header.getGraphicSegmentDataLengths().get(i);
        }
        // TODO: handle symbol segments for NITF 2.0
        for (int i = 0; i < numberOfTextSegments; ++i) {
            fileLength += header.getTextSegmentSubHeaderLengths().get(i);
            fileLength += header.getTextSegmentDataLengths().get(i);
        }
        // TODO: DES and overflow

        writeFixedLengthNumber(fileLength, NitfConstants.FL_LENGTH);
        writeFixedLengthNumber(headerLength, NitfConstants.HL_LENGTH);

        writeFixedLengthNumber(numberOfImageSegments, NitfConstants.NUMI_LENGTH);
        for (int i = 0; i < numberOfImageSegments; ++i) {
            writeFixedLengthNumber(header.getImageSegmentSubHeaderLengths().get(i), NitfConstants.LISH_LENGTH);
            writeFixedLengthNumber(header.getImageSegmentDataLengths().get(i), NitfConstants.LI_LENGTH);
        }

        writeFixedLengthNumber(numberOfGraphicSegments, NitfConstants.NUMS_LENGTH);
        for (int i = 0; i < numberOfGraphicSegments; ++i) {
            writeFixedLengthNumber(header.getGraphicSegmentSubHeaderLengths().get(i), NitfConstants.LSSH_LENGTH);
            writeFixedLengthNumber(header.getGraphicSegmentDataLengths().get(i), NitfConstants.LS_LENGTH);
        }
        // TODO: handle properly, including NITF 2.0 case
        writeFixedLengthNumber(0, NitfConstants.NUMX_LENGTH);

        writeFixedLengthNumber(numberOfTextSegments, NitfConstants.NUMT_LENGTH);
        for (int i = 0; i < numberOfTextSegments; ++i) {
            writeFixedLengthNumber(header.getTextSegmentSubHeaderLengths().get(i), NitfConstants.LTSH_LENGTH);
            writeFixedLengthNumber(header.getTextSegmentDataLengths().get(i), NitfConstants.LT_LENGTH);
        }

        // TODO: handle properly
        writeFixedLengthNumber(0, NitfConstants.NUMDES_LENGTH);

        writeFixedLengthNumber(0, NitfConstants.NUMRES_LENGTH);

        // TODO: handle properly
        writeFixedLengthNumber(0, NitfConstants.UDHDL_LENGTH);
        writeFixedLengthNumber(0, NitfConstants.XHDL_LENGTH);
    }

    private void writeFixedLengthString(final String s, final int length) throws IOException {
        String paddedString = String.format("%1$-" + length + "s", s);
        mOutputFile.writeBytes(paddedString);
    }

    private void writeFixedLengthNumber(final long number, final int length) throws IOException {
        String paddedString = String.format("%0" + length + "d", number);
        mOutputFile.writeBytes(paddedString);
    }

    private void writeImageHeader(final NitfImageSegmentHeader header, final FileType fileType) throws IOException {
        writeFixedLengthString(NitfConstants.IM, NitfConstants.IM.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.IID1_LENGTH);
        writeFixedLengthString(header.getImageDateTime().getSourceString(), NitfConstants.STANDARD_DATE_TIME_LENGTH);
        writeFixedLengthString(header.getImageTargetId().toString(), NitfConstants.TGTID_LENGTH);
        writeFixedLengthString(header.getImageIdentifier2(), NitfConstants.IID2_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata());
        writeFixedLengthString("0", NitfConstants.ENCRYP_LENGTH);
        writeFixedLengthString(header.getImageSource(), NitfConstants.ISORCE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfRows(), NitfConstants.NROWS_LENGTH);
        writeFixedLengthNumber(header.getNumberOfColumns(), NitfConstants.NCOLS_LENGTH);
        writeFixedLengthString(header.getPixelValueType().getTextEquivalent(), NitfConstants.PVTYPE_LENGTH);
        writeFixedLengthString(header.getImageRepresentation().getTextEquivalent(), NitfConstants.IREP_LENGTH);
        writeFixedLengthString(header.getImageCategory().getTextEquivalent(), NitfConstants.ICAT_LENGTH);
        writeFixedLengthNumber(header.getActualBitsPerPixelPerBand(), NitfConstants.ABPP_LENGTH);
        writeFixedLengthString(header.getPixelJustification().getTextEquivalent(), NitfConstants.PJUST_LENGTH);
        writeFixedLengthString(header.getImageCoordinatesRepresentation().getTextEquivalent(fileType), NitfConstants.ICORDS_LENGTH);
        // if (header.getImageCoordinatesRepresentation() != ImageCoordinatesRepresentation.NONE) {
            // TODO: output IGEOLO
        // }
        writeFixedLengthNumber(header.getImageComments().size(), NitfConstants.NICOM_LENGTH);
        for (String comment : header.getImageComments()) {
            writeFixedLengthString(comment, NitfConstants.ICOM_LENGTH);
        }
        writeFixedLengthString(header.getImageCompression().getTextEquivalent(), NitfConstants.IC_LENGTH);
        writeFixedLengthString(header.getCompressionRate(), NitfConstants.COMRAT_LENGTH);
        writeFixedLengthNumber(header.getNumBands(), NitfConstants.NBANDS_LENGTH);
        for (int i = 0; i < header.getNumBands(); ++i) {
            NitfImageBand band = header.getImageBandZeroBase(i);
            writeFixedLengthString(band.getImageRepresentation(), NitfConstants.IREPBAND_LENGTH);
            writeFixedLengthString(band.getSubCategory(), NitfConstants.ISUBCAT_LENGTH);
            writeFixedLengthString("N", NitfConstants.IFC_LENGTH);
            writeFixedLengthString("", NitfConstants.IMFLT_LENGTH); // space filled
            writeFixedLengthNumber(band.getNumLUTs(), NitfConstants.NLUTS_LENGTH);
            if (band.getNumLUTs() != 0) {
                writeFixedLengthNumber(band.getNumLUTEntries(), NitfConstants.NELUT_LENGTH);
                for (int j = 0; j < band.getNumLUTs(); ++j) {
                    NitfImageBandLUT lut = band.getLUT(j);
                    mOutputFile.write(lut.getEntries());
                }
            }
        }
        writeFixedLengthNumber(0, NitfConstants.ISYNC_LENGTH);
        writeFixedLengthString(header.getImageMode().getTextEquivalent(), NitfConstants.IMODE_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerRow(), NitfConstants.NBPR_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBlocksPerColumn(), NitfConstants.NBPC_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockHorizontal(), NitfConstants.NPPBH_LENGTH);
        writeFixedLengthNumber(header.getNumberOfPixelsPerBlockVertical(), NitfConstants.NPPBV_LENGTH);
        writeFixedLengthNumber(header.getNumberOfBitsPerPixelPerBand(), NitfConstants.NBPP_LENGTH);
        writeFixedLengthNumber(header.getImageDisplayLevel(), NitfConstants.IDLVL_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.IALVL_LENGTH);
        writeFixedLengthNumber(header.getImageLocationRow(), NitfConstants.ILOC_HALF_LENGTH);
        writeFixedLengthNumber(header.getImageLocationColumn(), NitfConstants.ILOC_HALF_LENGTH);
        writeFixedLengthString(header.getImageMagnification(), NitfConstants.IMAG_LENGTH);
        // TODO: these need to be created based on the original TREs, which we don't maintain yet
        writeFixedLengthNumber(0, NitfConstants.UDIDL_LENGTH);
        writeFixedLengthNumber(0, NitfConstants.IXSHDL_LENGTH);
    }

    private void writeImageData(final byte[] imageData) throws IOException {
        mOutputFile.write(imageData);
    }

    private void writeGraphicHeader(final NitfGraphicSegmentHeader header) throws IOException {
        writeFixedLengthString(NitfConstants.SY, NitfConstants.SY.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.SID_LENGTH);
        writeFixedLengthString(header.getGraphicName(), NitfConstants.SNAME_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata());
        writeFixedLengthString("0", NitfConstants.ENCRYP_LENGTH);
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
        // TODO: Handle TREs properly
        writeFixedLengthNumber(0, NitfConstants.SXSHDL_LENGTH);
    }

    private void writeGraphicData(final byte[] graphicData) throws IOException {
        mOutputFile.write(graphicData);
    }

    private void writeTextHeader(final NitfTextSegmentHeader header) throws IOException {
        writeFixedLengthString(NitfConstants.TE, NitfConstants.TE.length());
        writeFixedLengthString(header.getIdentifier(), NitfConstants.TEXTID_LENGTH);
        writeFixedLengthNumber(header.getAttachmentLevel(), NitfConstants.TXTALVL_LENGTH);
        writeFixedLengthString(header.getTextDateTime().getSourceString(), NitfConstants.STANDARD_DATE_TIME_LENGTH);
        writeFixedLengthString(header.getTextTitle(), NitfConstants.TXTITL_LENGTH);
        writeSecurityMetadata(header.getSecurityMetadata());
        writeFixedLengthString("0", NitfConstants.ENCRYP_LENGTH);
        writeFixedLengthString(header.getTextFormat().getTextEquivalent(), NitfConstants.TXTFMT_LENGTH);
        // TODO: Handle TREs properly
        writeFixedLengthNumber(0, NitfConstants.TXSHDL_LENGTH);
    }

    private void writeTextData(final String textData) throws IOException {
        mOutputFile.writeBytes(textData);
    }

    private void writeFileSecurityMetadata(final NitfFileSecurityMetadata fsmeta) throws IOException {
        writeSecurityMetadata(fsmeta);
        writeFixedLengthString(fsmeta.getFileCopyNumber(), NitfConstants.FSCOP_LENGTH);
        writeFixedLengthString(fsmeta.getFileNumberOfCopies(), NitfConstants.FSCPYS_LENGTH);
    }

    private void writeSecurityMetadata(final NitfSecurityMetadata securityMetadata) throws IOException {
        // TODO: make this handle NITF 2.0 too.
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
}
