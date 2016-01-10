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

import java.time.Month;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Basic NITF date / time tests
 */
public class NitfDateTimeTest {

    public NitfDateTimeTest() {
    }

    /**
     * Test of set method, of class NitfDateTime.
     */
    @Test
    public void testSet() {
        int year = 2016;
        int month = 1;
        int day = 2;
        int hour = 3;
        int minute = 4;
        int second = 59;
        NitfDateTime instance = new NitfDateTime();
        instance.set(year, month, day, hour, minute, second);
        Date date = instance.toDate();
        assertEquals(1451703899000L, date.getTime());
    }

    /**
     * Test of setSourceString method, of class NitfDateTime.
     */
    @Test
    public void testSetSourceString() {
        String sourceValue = "20160102030459";
        NitfDateTime instance = new NitfDateTime();
        instance.setSourceString(sourceValue);
        assertEquals(sourceValue, instance.getSourceString());
    }

    /**
     * Test of toZonedDateTime method, of class NitfDateTime.
     */
    @Test
    public void testToZonedDateTime() {
        int year = 2016;
        int month = 1;
        int day = 2;
        int hour = 8;
        int minute = 6;
        int second = 59;
        NitfDateTime instance = new NitfDateTime();
        instance.set(year, month, day, hour, minute, second);
        ZonedDateTime zdt = instance.toZonedDateTime();
        assertEquals(2016, zdt.getYear());
        assertEquals(1, zdt.getMonthValue());
        assertEquals(Month.JANUARY, zdt.getMonth());
        assertEquals(2, zdt.getDayOfMonth());
        assertEquals(2, zdt.getDayOfYear());
        assertEquals(8, zdt.getHour());
        assertEquals(6, zdt.getMinute());
        assertEquals(59, zdt.getSecond());
        assertEquals(ZoneId.of("UTC"), zdt.getZone());
    }

}
