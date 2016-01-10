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

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Check NifSecurityMetadata generation
 */
public class SecurityMetadataGenerationTest {

    public SecurityMetadataGenerationTest() {
    }

    private void checkNITF21andNSIF10(NitfSecurityMetadata defaultSecurityMetadata) {
        assertNotNull(defaultSecurityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, defaultSecurityMetadata.getSecurityClassification());
        assertEquals("  ", defaultSecurityMetadata.getSecurityClassificationSystem());
        assertEquals("           ", defaultSecurityMetadata.getCodewords());
        assertEquals("  ", defaultSecurityMetadata.getControlAndHandling());
        assertEquals("                    ", defaultSecurityMetadata.getReleaseInstructions());
        assertEquals("  ", defaultSecurityMetadata.getDeclassificationType());
        assertEquals("        ", defaultSecurityMetadata.getDeclassificationDate());
        assertEquals("    ", defaultSecurityMetadata.getDeclassificationExemption());
        assertEquals(" ", defaultSecurityMetadata.getDowngrade());
        assertEquals("        ", defaultSecurityMetadata.getDowngradeDate());
        assertEquals("                                           ", defaultSecurityMetadata.getClassificationText());
        assertEquals(" ", defaultSecurityMetadata.getClassificationAuthorityType());
        assertEquals("                                        ", defaultSecurityMetadata.getClassificationAuthority());
        assertEquals(" ", defaultSecurityMetadata.getClassificationReason());
        assertEquals("        ", defaultSecurityMetadata.getSecuritySourceDate());
        assertEquals("               ", defaultSecurityMetadata.getSecurityControlNumber());
    }

    private void checkNITF20(NitfSecurityMetadata defaultSecurityMetadata) {
        assertNotNull(defaultSecurityMetadata);
        assertEquals(NitfSecurityClassification.UNCLASSIFIED, defaultSecurityMetadata.getSecurityClassification());
        assertEquals("                                        ", defaultSecurityMetadata.getCodewords());
        assertEquals("                                        ", defaultSecurityMetadata.getControlAndHandling());
        assertEquals("                                        ", defaultSecurityMetadata.getReleaseInstructions());
        assertEquals("                    ", defaultSecurityMetadata.getClassificationAuthority());
        assertEquals("                    ", defaultSecurityMetadata.getSecurityControlNumber());
        assertEquals("      ", defaultSecurityMetadata.getDowngradeDateOrSpecialCase());
        assertNull(defaultSecurityMetadata.getDowngradeEvent());
    }

    @Test
    public void checkDefaultGeneration() {
        NitfSecurityMetadata defaultSecurityMetadata = NitfSecurityMetadata.getDefaultMetadata(FileType.NITF_TWO_ONE);
        checkNITF21andNSIF10(defaultSecurityMetadata);
    }

    @Test
    public void checkDefaultGenerationNSIF() {
        NitfSecurityMetadata defaultSecurityMetadata = NitfSecurityMetadata.getDefaultMetadata(FileType.NSIF_ONE_ZERO);
        checkNITF21andNSIF10(defaultSecurityMetadata);
    }

    @Test
    public void checkDefaultGenerationNITF20() {
        NitfSecurityMetadata defaultSecurityMetadata = NitfSecurityMetadata.getDefaultMetadata(FileType.NITF_TWO_ZERO);
        checkNITF20(defaultSecurityMetadata);
    }

    @Test
    public void checkFileSecurityMetadataGeneration() {
        NitfFileSecurityMetadata fsm = NitfFileSecurityMetadata.getDefaultMetadata(FileType.NITF_TWO_ONE);
        assertNotNull(fsm);
        checkNITF21andNSIF10(fsm);
        assertEquals("     ", fsm.getFileCopyNumber());
        assertEquals("     ", fsm.getFileNumberOfCopies());
    }

    @Test
    public void checkFileSecurityMetadataGenerationNSIF() {
        NitfFileSecurityMetadata fsm = NitfFileSecurityMetadata.getDefaultMetadata(FileType.NSIF_ONE_ZERO);
        assertNotNull(fsm);
        checkNITF21andNSIF10(fsm);
        assertEquals("     ", fsm.getFileCopyNumber());
        assertEquals("     ", fsm.getFileNumberOfCopies());
    }

    @Test
    public void checkFileSecurityMetadataGenerationNITF20() {
        NitfFileSecurityMetadata fsm = NitfFileSecurityMetadata.getDefaultMetadata(FileType.NITF_TWO_ZERO);
        assertNotNull(fsm);
        checkNITF20(fsm);
        assertEquals("     ", fsm.getFileCopyNumber());
        assertEquals("     ", fsm.getFileNumberOfCopies());
    }
}
