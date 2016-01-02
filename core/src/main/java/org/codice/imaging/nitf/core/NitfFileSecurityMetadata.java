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

import java.text.ParseException;

/**
    File security metadata.
    <p>
    The security metadata at the file level is the same as the subheaders, except for
    two extra fields (copy number, and number of copies).
*/
public class NitfFileSecurityMetadata extends NitfSecurityMetadata {
    private String nitfFileCopyNumber = null;
    private String nitfFileNumberOfCopies = null;

    private NitfFileSecurityMetadata() {
    }

    /**
        Constructor.
        <p>
        This builds a new object using the specified NitfReader.

        @param nitfReader the reader to use, positioned at the start of the file security metadata.
        @throws ParseException if any error in the metadata is detected.
    */
    public NitfFileSecurityMetadata(final NitfReader nitfReader) throws ParseException {
        NitfFileSecurityMetadataParser parser = new NitfFileSecurityMetadataParser();
        parser.parse(nitfReader, this);
    }

    /**
        Set the file copy number.
        <p>
        "This field shall contain the copy number of the file. If this field is all BCS zeros (0x30),
        it shall imply that there is no tracking of numbered file copies."

        @param copyNumber the copy number
    */
    public final void setFileCopyNumber(final String copyNumber) {
        nitfFileCopyNumber = copyNumber;
    }

    /**
        Return the file copy number.
        <p>
        "This field shall contain the copy number of the file. If this field is all BCS zeros (0x30),
        it shall imply that there is no tracking of numbered file copies."
        <p>
        An empty string is also common, especially in NITF 2.0 files, since the specification marked
        this field as optional.

        @return the copy number
    */
    public final String getFileCopyNumber() {
        return nitfFileCopyNumber;
    }

    /**
        Set the file number of copies.
        <p>
        "This field shall contain the total number of copies of the file. If this field is all BCS
        zeros (0x30), it shall imply that there is no tracking of numbered file copies."

        @param numberOfCopies the number of copies.
    */
    public final void setFileNumberOfCopies(final String numberOfCopies) {
        nitfFileNumberOfCopies = numberOfCopies;
    }

    /**
        Return the file number of copies.
        <p>
        "This field shall contain the total number of copies of the file. If this field is all BCS
        zeros (0x30), it shall imply that there is no tracking of numbered file copies."
        <p>
        An empty string is also common, especially in NITF 2.0 files, since the specification marked
        this field as optional.

        @return the number of copies.
    */
    public final String getFileNumberOfCopies() {
        return nitfFileNumberOfCopies;
    }

    /**
     * Create default file-level security metadata.
     *
     * This is intended for making base (default) level security metadata, and some changes will probably be required
     * depending on application needs. In particular, you probably need to set the security classification system to
     * reflect your national needs if you care about these fields.
     *
     * @param fileType the type (version) of NITF file to build the security metadata for.
     * @return default unclassified security metadata
     */
    public static NitfFileSecurityMetadata getDefaultMetadata(final FileType fileType) {
        NitfFileSecurityMetadata defaultMetadata = new NitfFileSecurityMetadata();
        fillDefaultMetadata(defaultMetadata, fileType);
        defaultMetadata.setFileCopyNumber(spaceFillForLength(NitfConstants.FSCOP_LENGTH));
        defaultMetadata.setFileNumberOfCopies(spaceFillForLength(NitfConstants.FSCPYS_LENGTH));
        return defaultMetadata;
    }
};

