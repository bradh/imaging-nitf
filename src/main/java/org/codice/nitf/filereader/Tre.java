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
package org.codice.nitf.filereader;

import java.util.ArrayList;
import java.util.List;

public class Tre {
    private String prefix = null;
    private String treName = null;
    List<TreField> fields = new ArrayList<TreField>();

    public void setName(String name) {
        treName = name;
    }

    public String getName() {
        return treName;
    }

    public void setPrefix(String mdPrefix) {
        prefix = mdPrefix;

    }

    public String getPrefix() {
        return prefix;
    }

    public List<TreField> getFields() {
        return fields;
    }
}