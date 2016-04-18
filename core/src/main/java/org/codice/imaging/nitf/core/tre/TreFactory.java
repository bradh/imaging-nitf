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

/**
 * Factory class for creating new Tre (Tagged Registered Extension) instances.
 */
public final class TreFactory {

    private static final int CC_ORG_MAXLENGTH = 999;
    private static final int CC_STD_MAXLENGTH = 999;
    private static final int CC_UUID_LENGTH = 36;
    private TreFactory() {
    }

    /**
     * Create a new Tre instance.
     *
     * This instance will need to have the required entries added.
     *
     * @param tag the name of the TRE (i.e. the six letter tag)
     * @param source the location of this TRE (intended location in the file)
     * @return TRE with no content.
     */
    public static Tre getDefault(final String tag, final TreSource source) {
        Tre tre = new TreImpl(tag, source);
        return tre;
    }

    /**
     * Create a new CCLSTA Tre instance.
     *
     * @param source the location of this TRE (intended location in the file)
     * @param standard the country code standard to use.
     * @param namingAuthority the naming authority that manages the standard
     * @param uuid UUID for the country code
     *
     * Usual UUID values include 409B74EE-1572-4430-8B80-9BA6961B3CFC (for US GENC:Ed3 Update 2),
     * 2C68E226-A84A-49F3-A01E-2CE896B7E428 (for STANAG 1059:Ed8) and A525D54C-1858-4BC0-A5F2-57BB92F947E7 (for ISO
     * 3166:2013).
     *
     * @return the CCLSTA TRE containing the serialised values (excluding the CETAG and CEL)
     * @throws NitfFormatException if the input values are not in range
     */
    public static Tre getCCLSTA(final TreSource source, final String standard, final String namingAuthority, final String uuid)
            throws NitfFormatException {
        if ((standard == null) || (standard.length() < 0) || (standard.length() > CC_STD_MAXLENGTH)) {
            throw new NitfFormatException("CCLSTA CC_STD field length must be between 1 and 999 characters long");
        }
        if ((namingAuthority == null) || (namingAuthority.length() > CC_ORG_MAXLENGTH)) {
            throw new NitfFormatException("CCLSTA CC_ORG field length must be non null and less than 999 characters long");
        }
        if ((uuid == null) || (uuid.length() != CC_UUID_LENGTH)) {
            throw new NitfFormatException(String.format("CCLSTA CC_UUID field length must be %d characters long", CC_UUID_LENGTH));
        }
        Tre cclsta = getDefault("CCLSTA", source);
        cclsta.add(new TreEntry("CC_STD_LENGTH", String.valueOf(standard.length())));
        cclsta.add(new TreEntry("CC_STD", standard));
        cclsta.add(new TreEntry("CC_ORG_LENGTH", String.valueOf(namingAuthority.length())));
        cclsta.add(new TreEntry("CC_ORG", namingAuthority));
        cclsta.add(new TreEntry("CC_UUID", uuid));
        cclsta.add(new TreEntry("SUPP_COUNT", "0"));
        TreEntry supplementalIdentifiers = new TreEntry("SUPPLEMENTAL_IDENTIFIERS");
        cclsta.add(supplementalIdentifiers);
        return cclsta;
    }

}
