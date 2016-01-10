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

/**
 * A collection of internal utilities.
 *
 * This should not contain public API, and probably should not have internal state.
 */
public final class Utility {

    private Utility() {
    }

    /**
     * Create an empty (space-filled) string of specified length.
     *
     * @param length the length of the string to create
     * @return the space-filled string
     */
    public static String spaceFillForLength(final int length) {
        return String.format("%1$-" + length + "s", "");
    }

}
