/**
 * Copyright (c) Codice Foundation
 *
 * This is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. A copy of the GNU Lesser General Public License is
 * distributed along with this program and can be found at
 * <http://www.gnu.org/licenses/lgpl.html>.
 *
 *
 */
package org.codice.imaging.nitf.core;

/**
 * Utility collection of shared constants.
 */
public final class NitfConstants {
    // Error Messages
    static final String GENERIC_READ_ERROR_MESSAGE = "Error reading from NITF file: ";
    static final String FILE_NOT_FOUND_EXCEPTION_MESSAGE = "File Not Found Exception opening file:";
    static final String NOT_FOUND_MESSAGE_JOINER = " not found: ";

    static final String READ_MODE = "r";
    static final String WRITE_MODE = "rw";

    // NITF file lengths
    // file header
    /**
     * Length of the File Profile name field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FHDR_LENGTH = 4;

    /**
     * Length of the File Version field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FVER_LENGTH = 5;

    /**
     * Length of the Complexity Level field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int CLEVEL_LENGTH = 2;

    /**
     * Length of the Standard Type field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int STYPE_LENGTH = 4;

    /**
     * Length of the Originating Station field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int OSTAID_LENGTH = 10;
    /**
     * Length of the File Title field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FTITLE_LENGTH = 80;

    /**
     * Length of the Originator's Name field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int ONAME_LENGTH = 24;

    /**
     * Length of the Originator's Name field in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final int ONAME20_LENGTH = 27;

    /**
     * Length of the Originator's Phone Number field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int OPHONE_LENGTH = 18;

    /**
     * Length of the File Length (FL) field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FL_LENGTH = 12;

    /**
     * Length of the NITF File Header Length (HL) field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int HL_LENGTH = 6;

    /**
     * Length of the Number of Image Segments field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMI_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Image Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LISH_LENGTH = 6;

    /**
     * Length of each of the "Length of Nth Image Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LI_LENGTH = 10;

    /**
     * Length of the Number of Graphic Segments field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMS_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Graphic Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LSSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Graphic Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LS_LENGTH = 6;

    /**
     * Length of the "Reserved for Future Use" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMX_LENGTH = 3;

    /**
     * Length of the Number of Label Segments field in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final int NUML20_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Label Subheader" fields in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final int LLSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Label Segment" fields in the NITF 2.0 file header.
     * <p>
     * See MIL-STD-2500A
     */
    protected static final int LL_LENGTH = 3;

    /**
     * Length of the "Number of Text Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMT_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Text Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LTSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Text Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LT_LENGTH = 5;

    /**
     * Length of the "Number of Data Extension Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMDES_LENGTH = 3;

    /**
     * Length of each of the "Length of Nth Data Extension Segment Subheader" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LDSH_LENGTH = 4;

    /**
     * Length of each of the "Length of Nth Data Extension Segment" fields in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int LD_LENGTH = 9;

    /**
     * Length of the "Number of Reserved Extension Segments" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int NUMRES_LENGTH = 3;

    /**
     * Length of the "User Defined Header Data Length" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int UDHDL_LENGTH = 5;

    /**
     * Length of the "User Defined Header Overflow" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int UDHOFL_LENGTH = 3;

    /**
     * Length of the "Extended Header Data Length" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int XHDL_LENGTH = 5;

    /**
     * Length of the "Extended Header Data Overflow" field in the NITF 2.1 file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int XHDLOFL_LENGTH = 3;

    /**
     * The minimum allowable complexity level value.
     */
    protected static final int MIN_COMPLEXITY_LEVEL = 0;

    /**
     * The maximum allowable complexity level value.
     * <p>
     * Actual complexity levels are only defined to 9.
     */
    protected static final int MAX_COMPLEXITY_LEVEL = 99;

    /**
     * The length of the "SFH Length 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    static final int SFH_L1_LENGTH = 7;

    /**
     * The length of the "SFH Delimiter 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    static final int SFH_DELIM1_LENGTH = 4;

    /**
     * The expected content of the "SFH Delimiter 1" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B).
     */
    static final byte[] SFH_DELIM1 = new byte[]{0x0a, (byte) 0x6e, 0x1d, (byte) 0x97};

    /**
     * The length of the "SFH Delimiter 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B)
     */
    static final int SFH_DELIM2_LENGTH = 4;

    /**
     * The expected content of the "SFH Delimiter 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8(B).
     */
    static final byte[] SFH_DELIM2 = new byte[]{0x0e, (byte) 0xca, 0x14, (byte) 0xbf};

    /**
     * Length of the "SFH Length 2" field.
     * <p>
     * See MIL-STD-2500C Table A-8 (B).
     */
    static final int SFH_L2_LENGTH = 7;

    /**
     * The "magic flag" value used in length fields to indicate streaming mode.
     * <p>
     * See MIL-STD-2500C Section 5.2.1 for more information.
     */
    protected static final long STREAMING_FILE_MODE = 999999999999L;

    // file security
    /**
     * Length of the "File Copy Number" field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FSCOP_LENGTH = 5;

    /**
     * Length of the "File Number of Copies" field in the NITF file header.
     * <p>
     * See MIL-STD-2500C Table A-1.
     */
    protected static final int FSCPYS_LENGTH = 5;

    // graphics segment
    /**
     * Marker string for NITF Graphic subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final String SY = "SY";

    /**
     * Length of the Graphic Identifier in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SID_LENGTH = 10;

    /**
     * Length of the "Graphic name" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SNAME_LENGTH = 20;

    /**
     * Expected content of the "Graphic Type" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final String SFMT_CGM = "C";

    /**
     * Default content for the the "Reserved for Future Use" SSTRUCT field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final String SSTRUCT = "0000000000000";

    /**
     * Length of the "Graphic Display Level" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SDLVL_LENGTH = 3;

    /**
     * Length of the "Graphic Attachment Level" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SALVL_LENGTH = 3;

    /**
     * Length of the half of the "Graphic Location" field in the NITF Graphic Subheader
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is RRRRRCCCCC, but we parse rows then columns.
     */
    protected static final int SLOC_HALF_LENGTH = 5;

    /**
     * Length of half of the "First Graphic Bound Location" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    protected static final int SBND1_HALF_LENGTH = 5;

    /**
     * Length of the "Graphic Colour" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SCOLOR_LENGTH = 1;

    /**
     * Length of half of the "Second Graphic Bound Location" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    protected static final int SBND2_HALF_LENGTH = 5;

    /**
     * Default value of the "Reserved for Figure Use" SRES2 field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final String SRES = "00";

    /**
     * Length of the "Graphic Extended Subheader Data Length" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SXSHDL_LENGTH = 5;

    /**
     * Length of the "Graphic Extended Subheader Data" field in the NITF Graphic Subheader.
     * <p>
     * See MIL-STD-2500C Table A-5.
     */
    protected static final int SXSOFL_LENGTH = 3;

    // Symbol (Graphic) Segment
    /**
     * Length of the "Symbol Type" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYTYPE_LENGTH = 1;

    /**
     * Length of the "Number of Lines Per Symbol" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NLIPS_LENGTH = 4;

    /**
     * Length of the "Number of Pixels Per Line" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NPIXPL_LENGTH = 4;

    /**
     * Length of the "Line Width" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int NWDTH_LENGTH = 4;

    /**
     * Length of the "Number of Bits Per Pixel" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYNBPP_LENGTH = 1;

    /**
     * Length of the "Symbol Number" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SNUM_LENGTH = 6;

    /**
     * Length of the "Symbol Rotation" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SROT_LENGTH = 3;

    /**
     * Length of the "Number of LUT Entries" field in the NITF 2.0 symbol header.
     * <p>
     * See MIL-STD-2500A Table VI and VII.
     */
    protected static final int SYNELUT_LENGTH = 3;

    // label segment
    /**
     * Marker string for NITF Label subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final String LA = "LA";

    /**
     * Length of the "Label Identifier" field in the NITF 2.0 Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LID_LENGTH = 10;

    /**
     * Length of the "Label Font Style" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LFS_LENGTH = 1;

    /**
     * Length of the "Label Cell Width" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LCW_LENGTH = 2;

    /**
     * Length of the "Label Cell Height" field in the NITF Label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LCH_LENGTH = 2;

    /**
     * Length of the "Label Display Level" field in the NITF 2.0 label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LDLVL_LENGTH = 3;

    /**
     * Length of the "Label Attachment Level" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LALVL_LENGTH = 3;

    /**
     * Length of half of the "Label Location" field in the NITF label Subheader.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     * <p>
     * The actual length is twice this, since the format is rrrrrccccc, but we parse rows then columns.
     */
    protected static final int LLOC_HALF_LENGTH = 5;

    /**
     * Length of the "Extended Subheader Data Length" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LXSHDL_LENGTH = 5;

    /**
     * Length of the "Extended Subheader Overflow" field in the NITF label header.
     * <p>
     * See MIL-STD-2500A Table XI and XII.
     */
    protected static final int LXSOFL_LENGTH = 3;

    // security metadata
    /**
     * Length of the "Security Classification" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLAS_LENGTH = 1;

    /**
     * Length of the "Security Classification System" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLSY_LENGTH = 2;

    /**
     * Length of the "Codewords" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */

    protected static final int XSCODE_LENGTH = 11;

    /**
     * Length of the "Control and Handling" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCTLH_LENGTH = 2;

    /**
     * Length of the "Releasing Instructions" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSREL_LENGTH = 20;

    /**
     * Length of the "Declassification Type" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCTP_LENGTH = 2;

    /**
     * Length of the "Declassification Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCDT_LENGTH = 8;

    /**
     * Length of the "Declassification Exemption" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDCXM_LENGTH = 4;

    /**
     * Length of the "Downgrade" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDG_LENGTH = 1;

    /**
     * Length of the "Downgrade Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSDGDT_LENGTH = 8;

    /**
     * Length of the "Classification Text" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCLTX_LENGTH = 43;

    /**
     * Length of the "Classification Authority Type" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCATP_LENGTH = 1;

    /**
     * Length of the "Classification Authority" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCAUT_LENGTH = 40;

    /**
     * Length of the "Classification Reason" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCRSN_LENGTH = 1;

    /**
     * Length of the "Security Source Date" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSSRDT_LENGTH = 8;

    /**
     * Length of the "Security Control Number" field used in the NITF file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500C Annex A for the various uses.
     */
    protected static final int XSCTLN_LENGTH = 15;

    // NITF 2.0 field lengths
    /**
     * Length of the "Codewords" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCODE20_LENGTH = 40;

    /**
     * Length of the "Control and Handling" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCTLH20_LENGTH = 40;

    /**
     * Length of the "Releasing Instructions" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSREL20_LENGTH = 40;

    /**
     * Length of the "Classification Authority" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCAUT20_LENGTH = 20;

    /**
     * Length of the "Control Number" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSCTLN20_LENGTH = 20;

    /**
     * Length of the "Security Downgrade" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSDWNG20_LENGTH = 6;

    /**
     * Length of the "Downgrade Event" field used in the NITF 2.0 file header and subheaders.
     * <p>
     * This field is called different things in different places. For example, in the file header
     * it starts with F, while in the image subheader it starts with I. To make it more general,
     * we use 'X' to represent these various prefixes.
     * See MIL-STD-2500A Tables for the various uses.
     */
    protected static final int XSDEVT20_LENGTH = 40;

    /**
     * Marker field used in the "Security Downgrade" field to indicate that the "Downgrade Event" field is present.
     * <p>
     * NITF 2.0 only. See MIL-STD-2500A Tables for usage.
     */
    public static final String DOWNGRADE_EVENT_MAGIC = "999998";

    // text segment
    /**
     * Marker string for NITF Text subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final String TE = "TE";

    /**
     * Length of the "Text Identifier" field in the NITF 2.1 Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TEXTID_LENGTH = 7;

    /**
     * Length of the "Text Attachment Level" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTALVL_LENGTH = 3;

    /**
     * Length of the "Text Identifier" field in the NITF 2.0 Text Subheader.
     * <p>
     * See MIL-STD-2500A.
     */
    protected static final int TEXTID20_LENGTH = 10;

    /**
     * Length of the "Text Title" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTITL_LENGTH = 80;

    /**
     * Length of the "Text Format" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXTFMT_LENGTH = 3;

    /**
     * Length of the "Text Extended Subheader Data Length" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXSHDL_LENGTH = 5;

    /**
     * Length of the "Text Extended Subheader Overflow" field in the NITF Text Subheader.
     * <p>
     * See MIL-STD-2500C Table A-6.
     */
    protected static final int TXSOFL_LENGTH = 3;

    // Tagged Record Extension (TRE) segment
    /**
     * Length of the "Unique Extension Type Identifier" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    public static final int TAG_LENGTH = 6;

    /**
     * Length of the "Length of REDATA Field" field in the Registered and controlled TRE format.
     * <p>
     * See MIL-STD-2500C Table A-7.
     */
    public static final int TAGLEN_LENGTH = 5;

    static final String AND_CONDITION = " AND ";
    static final String UNSUPPORTED_IFTYPE_FORMAT_MESSAGE = "Unsupported format for iftype:";

    private NitfConstants() {
    }
}
