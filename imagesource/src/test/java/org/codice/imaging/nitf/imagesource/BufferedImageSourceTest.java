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

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import org.codice.imaging.nitf.core.common.FileType;
import org.codice.imaging.nitf.core.image.ImageCategory;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageCoordinatesRepresentation;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.ImageRepresentation;
import org.codice.imaging.nitf.core.image.ImageSegment;
import org.codice.imaging.nitf.core.image.PixelJustification;
import org.codice.imaging.nitf.core.image.PixelValueType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Test;

/**
 * Tests for creation from a BufferedImage.
 */
public class BufferedImageSourceTest {

    public BufferedImageSourceTest() {
    }

    @Test
    public void redFill_RGB() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void SupplierCheck() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_RGB);
        fillWithRed(sourceImage);
        ImageSegmentSupplier supplier = new ImageSegmentSupplier(sourceImage, FileType.NITF_TWO_ONE);
        ImageSegment imageSegment = supplier.get();
        checkImageSegmentRedBlock(imageSegment);
    }

    @Test
    public void redFill_ARGB() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_ARGB);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_ARGB_Pre() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_ARGB_PRE);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_IntBGR() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_INT_BGR);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_4BYTE_ABGR() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_4BYTE_ABGR);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_4BYTE_ABGR_PRE() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_4BYTE_ABGR_PRE);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_TYPE_USHORT_565_RGB() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_USHORT_565_RGB);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    @Test
    public void redFill_TYPE_USHORT_555_RGB() throws IOException {
        BufferedImage sourceImage = new BufferedImage(200, 300, BufferedImage.TYPE_USHORT_555_RGB);
        fillWithRed(sourceImage);
        checkRedBlock(sourceImage);
    }

    private void fillWithRed(BufferedImage sourceImage) {
        Graphics2D graphics = sourceImage.createGraphics();
        graphics.setBackground(Color.RED);
        graphics.clearRect(0, 0, sourceImage.getWidth(), sourceImage.getHeight());
    }

    private void checkRedBlock(BufferedImage sourceImage) throws IOException {
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegment(sourceImage, FileType.NITF_TWO_ONE);
        checkImageSegmentRedBlock(imageSegment);
    }

    private void checkImageSegmentRedBlock(ImageSegment imageSegment) throws IOException {
        assertEquals("", imageSegment.getImageSource());
        assertEquals("", imageSegment.getImageIdentifier2());
        assertEquals(200, imageSegment.getNumberOfColumns());
        assertEquals(300, imageSegment.getNumberOfRows());
        assertEquals(1, imageSegment.getNumberOfBlocksPerRow());
        assertEquals(1, imageSegment.getNumberOfBlocksPerColumn());
        assertEquals(200, imageSegment.getNumberOfPixelsPerBlockHorizontal());
        assertEquals(300, imageSegment.getNumberOfPixelsPerBlockVertical());
        assertEquals(200, imageSegment.getNumberOfPixelsPerBlockHorizontalRaw());
        assertEquals(300, imageSegment.getNumberOfPixelsPerBlockVerticalRaw());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(8, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertNotNull(imageSegment.getCompressionRate());
        assertEquals(ImageRepresentation.RGBTRUECOLOUR, imageSegment.getImageRepresentation());
        assertEquals(ImageCoordinatesRepresentation.NONE, imageSegment.getImageCoordinatesRepresentation());
        // Dubious test - we perhaps should do something better.
        assertNull(imageSegment.getImageCoordinates());
        assertEquals("1.0", imageSegment.getImageMagnification());
        assertEquals(1.0, imageSegment.getImageMagnificationAsDouble(), 0.000001);
        assertEquals(0, imageSegment.getImageLocationColumn());
        assertEquals(0, imageSegment.getImageLocationRow());
        assertEquals(1, imageSegment.getImageDisplayLevel());
        assertEquals(0, imageSegment.getImageComments().size());
        assertEquals("", imageSegment.getImageTargetId().getBasicEncyclopediaNumber());
        assertEquals("", imageSegment.getImageTargetId().getOSuffix());
        assertEquals("", imageSegment.getImageTargetId().getCountryCode());
        assertEquals(3, imageSegment.getNumBands());
        assertEquals("B", imageSegment.getImageBandZeroBase(0).getImageRepresentation());
        assertEquals("G", imageSegment.getImageBandZeroBase(1).getImageRepresentation());
        assertEquals("R", imageSegment.getImageBandZeroBase(2).getImageRepresentation());

        assertEquals(0, imageSegment.getUserDefinedHeaderOverflow());

        assertEquals(3 * 200 * 300, imageSegment.getDataLength());
        byte[] testbytes = new byte[3 * 200 * 300];
        imageSegment.getData().readFully(testbytes);
        assertEquals(0, testbytes[0]); // blue band
        assertEquals(0, testbytes[1]); // green band
        assertEquals((byte)0xFF, testbytes[2]); // red band
    }

    @Test
    public void grayFill() throws IOException {
        BufferedImage sourceImage = new BufferedImage(400, 300, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = sourceImage.createGraphics();
        graphics.setBackground(Color.LIGHT_GRAY);
        graphics.clearRect(0, 0, sourceImage.getWidth(), sourceImage.getHeight());
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegment(sourceImage, FileType.NITF_TWO_ONE);
        assertEquals(400, imageSegment.getNumberOfColumns());
        assertEquals(300, imageSegment.getNumberOfRows());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(8, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(8, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment.getImageRepresentation());
        assertEquals(1, imageSegment.getNumBands());
        assertEquals("M", imageSegment.getImageBandZeroBase(0).getImageRepresentation());

        assertEquals(400 * 300, imageSegment.getDataLength());
        byte[] testbytes = new byte[400 * 300];
        imageSegment.getData().readFully(testbytes);
        assertEquals((byte)Color.LIGHT_GRAY.getBlue(), testbytes[0]);
        assertEquals((byte)Color.LIGHT_GRAY.getGreen(), testbytes[0]);
        assertEquals((byte)Color.LIGHT_GRAY.getRed(), testbytes[0]);
    }

    @Test
    public void uShortGrayFill() throws IOException {
        BufferedImage sourceImage = new BufferedImage(400, 300, BufferedImage.TYPE_USHORT_GRAY);
        for (int y = 0; y < sourceImage.getHeight(); ++y) {
            for (int x = 0; x < sourceImage.getWidth(); ++x) {
                sourceImage.getRaster().setSample(x, y, 0, 0x7FFF);
            }
        }
        ImageSegment imageSegment = ImageSegmentSourceFactory.getImageSegment(sourceImage, FileType.NITF_TWO_ONE);
        assertEquals(400, imageSegment.getNumberOfColumns());
        assertEquals(300, imageSegment.getNumberOfRows());
        assertEquals(ImageCategory.VISUAL, imageSegment.getImageCategory());
        assertEquals(PixelValueType.INTEGER, imageSegment.getPixelValueType());
        assertEquals(16, imageSegment.getNumberOfBitsPerPixelPerBand());
        assertEquals(16, imageSegment.getActualBitsPerPixelPerBand());
        assertEquals(PixelJustification.RIGHT, imageSegment.getPixelJustification());
        assertEquals(ImageMode.PIXELINTERLEVE, imageSegment.getImageMode());
        assertEquals(ImageCompression.NOTCOMPRESSED, imageSegment.getImageCompression());
        assertEquals(ImageRepresentation.MONOCHROME, imageSegment.getImageRepresentation());
        assertEquals(1, imageSegment.getNumBands());
        assertEquals("M", imageSegment.getImageBandZeroBase(0).getImageRepresentation());

        assertEquals(400 * 300 * Short.BYTES, imageSegment.getDataLength());
        byte[] testbytes = new byte[400 * 300 * Short.BYTES];
        imageSegment.getData().readFully(testbytes);
        assertEquals(0x7F, testbytes[0]);
        assertEquals((byte)0xFF, testbytes[1]);
        assertEquals(0x7F, testbytes[2]);
        assertEquals((byte)0xFF, testbytes[3]);
     }
}
