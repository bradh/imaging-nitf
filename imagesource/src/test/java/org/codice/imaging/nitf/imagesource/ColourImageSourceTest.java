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
package org.codice.imaging.nitf.imagesource;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Test for JPEG source
 */
public class ColourImageSourceTest {

    public ColourImageSourceTest() {
    }

    @Test
    public void parseJPEG1pixel() throws IOException {
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegmentFrom3ByteBGR(getClass().getResourceAsStream("/JPEG/1pixel.jpg"), FileType.NITF_TWO_ONE);
        assertEquals(1, imageSegment.getNumberOfColumns());
        assertEquals(1, imageSegment.getNumberOfRows());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(8, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(ImageRepresentation.RGBTRUECOLOUR, imageSegment.getImageRepresentation());
        assertEquals(3, imageSegment.getNumBands());
        assertEquals("B", imageSegment.getImageBandZeroBase(0).getImageRepresentation());
        assertEquals("G", imageSegment.getImageBandZeroBase(1).getImageRepresentation());
        assertEquals("R", imageSegment.getImageBandZeroBase(2).getImageRepresentation());

        assertEquals(3 * 1 * 1, imageSegment.getDataLength());
        byte[] testbytes = new byte[3 * 1 * 1];
        imageSegment.getData().readFully(testbytes);
    }

    @Test
    public void parseJPEGImageSegment() throws IOException {
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegmentFrom3ByteBGR(getClass().getResourceAsStream("/JPEG/grevillea.jpg"), FileType.NITF_TWO_ONE);
        checkGrevilleaResults(imageSegment);
    }

    @Test
    public void parsePNG() throws IOException {
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegmentFrom3ByteBGR(getClass().getResourceAsStream("/PNG/grevillea.png"), FileType.NITF_TWO_ONE);
        checkGrevilleaResults(imageSegment);
    }

    @Test
    public void parsePNGFile() throws IOException, URISyntaxException {
        File pngFile = new File(getClass().getResource("/PNG/grevillea.png").toURI());
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegmentFrom3ByteBGR(pngFile, FileType.NITF_TWO_ONE);
        checkGrevilleaResults(imageSegment);
    }

    private void checkGrevilleaResults(ImageSegment imageSegment) throws IOException {
        assertEquals(479, imageSegment.getNumberOfColumns());
        assertEquals(438, imageSegment.getNumberOfRows());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(8, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(ImageRepresentation.RGBTRUECOLOUR, imageSegment.getImageRepresentation());
        assertEquals(3, imageSegment.getNumBands());
        assertEquals("B", imageSegment.getImageBandZeroBase(0).getImageRepresentation());
        assertEquals("G", imageSegment.getImageBandZeroBase(1).getImageRepresentation());
        assertEquals("R", imageSegment.getImageBandZeroBase(2).getImageRepresentation());

        assertEquals(3 * 479 * 438, imageSegment.getDataLength());
        byte[] testbytes = new byte[3 * 479 * 438];
        imageSegment.getData().readFully(testbytes);
    }


}
