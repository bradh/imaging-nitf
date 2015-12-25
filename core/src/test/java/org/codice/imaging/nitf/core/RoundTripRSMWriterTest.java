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

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import org.junit.Test;

/**
 *
 * @author bradh
 */
public class RoundTripRSMWriterTest extends AbstractWriterTest {

    public RoundTripRSMWriterTest() {
    }

    @Test
    public void roundTripRSM_I6130A() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130a.ntf");
    }

    @Test
    public void roundTripRSM_I6130B() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130b.ntf");
    }

    @Test
    public void roundTripRSM_I6130C() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130c.ntf");
    }

    @Test
    public void roundTripRSM_I6130E() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130e.ntf");
    }

    @Test
    public void roundTripRSM_I6130F() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130f.ntf");
    }

    @Test
    public void roundTripRSM_I6130G() throws ParseException, URISyntaxException, IOException {
        roundTripFile("/JitcRSM/i_6130g.ntf");
    }
}
