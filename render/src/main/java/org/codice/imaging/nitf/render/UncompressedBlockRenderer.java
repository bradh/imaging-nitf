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

package org.codice.imaging.nitf.render;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferUShort;
import java.awt.image.IndexColorModel;
import java.awt.image.WritableRaster;
import java.io.IOException;
import javax.imageio.stream.ImageInputStream;
import org.codice.imaging.nitf.core.image.ImageCompression;
import org.codice.imaging.nitf.core.image.ImageMode;
import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;
import org.codice.imaging.nitf.core.image.PixelJustification;

public class UncompressedBlockRenderer implements BlockRenderer {

    private NitfImageSegmentHeader mImageSegmentHeader = null;
    private ImageInputStream mImageData = null;
    private ImageMask mMask = null;
    private long mMaskOffset = 0;


    /**
     * Set the image segment to read from
     *
     * @param imageSegmentHeader the image segment
     * @param imageInputStream the source to read the segment image data from
     * @throws IOException if the read fails
     */
    @Override
    public final void setImageSegment(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream) throws IOException {
        mImageSegmentHeader = imageSegmentHeader;
        mImageData = imageInputStream;
        if (mImageSegmentHeader.getImageCompression() == ImageCompression.NOTCOMPRESSEDMASK) {
            mMask = new ImageMask(mImageSegmentHeader, mImageData);
            mMaskOffset = mImageData.getStreamPosition();
        } else {
            // We don't have a real mask, but we can create our own for an uncompressed image
            mMask = buildUncompressedImageMask();
        }
    }

    @Override
    public final BufferedImage getNextImageBlock() throws IOException {
        switch (mImageSegmentHeader.getImageRepresentation()) {
            case MONOCHROME:
                return getNextImageBlockMono();
            case RGBLUT:
                return getNextImageBlockRGBLUT();
            default:
                System.out.println("Unhandled image representation:" + mImageSegmentHeader.getImageRepresentation());
                return null;
        }
    }

    @Override
    public final BufferedImage getImageBlock(final int rowIndex, final int columnIndex) throws IOException {
        mImageData.seek(getOffsetForBlock(rowIndex, columnIndex) + mMaskOffset);
        return getNextImageBlock();
    }

    private BufferedImage getNextImageBlockMono() throws IOException {
        if ((mImageSegmentHeader.getImageMode() != ImageMode.BLOCKINTERLEVE) || (mImageSegmentHeader.getNumBands() != 1)) {
            System.out.println("Unsupported mode / band combination: " + mImageSegmentHeader.getImageMode() + ", " + mImageSegmentHeader.getNumBands());
            return null;
        }
        // TODO: masked image
        switch (mImageSegmentHeader.getNumberOfBitsPerPixelPerBand()) {
            case 1:
                return getNextImageBlockMono1();
            case 8:
                return getNextImageBlockMono8();
            case 16:
                return getNextImageBlockMono16();
            default:
                if (mImageSegmentHeader.getNumberOfBitsPerPixelPerBand() < 16) {
                    return getNextImageBlockMonoArbitrary();
                } else {
                    System.out.println("Unhandled Mono bit depth:" + mImageSegmentHeader.getNumberOfBitsPerPixelPerBand());
                }
                return null;
        }
    }
    private BufferedImage getNextImageBlockRGBLUT() throws IOException {
        if ((mImageSegmentHeader.getImageMode() != ImageMode.BLOCKINTERLEVE) || (mImageSegmentHeader.getNumBands() != 1)) {
            System.out.println("Unsupported mode / band combination: " + mImageSegmentHeader.getImageMode() + ", " + mImageSegmentHeader.getNumBands());
            return null;
        }
        // TODO: masked image
        switch (mImageSegmentHeader.getNumberOfBitsPerPixelPerBand()) {
            case 1:
                return getNextImageBlockRGBLUT1();
            case 8:
                return getNextImageBlockRGBLUT8();
            default:
                System.out.println("Unhandled RGBLUT bit depth:" + mImageSegmentHeader.getNumberOfBitsPerPixelPerBand());
                return null;
        }
    }

    private BufferedImage getNextImageBlockRGBLUT1() throws IOException {
        IndexColorModel colourModel = new IndexColorModel(mImageSegmentHeader.getActualBitsPerPixelPerBand(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getNumLUTEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_BINARY, colourModel);
        copy1BitValuesIntoImage(img);
        return img;
    }

    private BufferedImage getNextImageBlockMono1() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_BINARY);
        copy1BitValuesIntoImage(img);
        return img;
    }

    private void copy1BitValuesIntoImage(final BufferedImage img) throws IOException {
        WritableRaster raster = img.getRaster();
        for (int rowIndex = 0; rowIndex < mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(); ++rowIndex) {
            for (int columnIndex = 0; columnIndex < mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(); ++columnIndex) {
                raster.setSample(columnIndex, rowIndex, 0, (int)mImageData.readBits(1));
            }
        }
    }

    private BufferedImage getNextImageBlockRGBLUT8() throws IOException {
        IndexColorModel colourModel = new IndexColorModel(mImageSegmentHeader.getActualBitsPerPixelPerBand(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getNumLUTEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(0).getEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(1).getEntries(),
                                                          mImageSegmentHeader.getImageBandZeroBase(0).getLUTZeroBase(2).getEntries());
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_INDEXED, colourModel);
        byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        mImageData.readFully(imgData);
        return img;
    }

    private BufferedImage getNextImageBlockMono8() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_BYTE_GRAY);
        byte[] imgData = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
        mImageData.readFully(imgData);
        return img;
    }

    private BufferedImage getNextImageBlockMono16() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_USHORT_GRAY);
        short[] imgData = ((DataBufferUShort)img.getRaster().getDataBuffer()).getData();
        if ((mImageSegmentHeader.getNumberOfBitsPerPixelPerBand() == mImageSegmentHeader.getActualBitsPerPixelPerBand()) || (mImageSegmentHeader.getPixelJustification() == PixelJustification.LEFT)) {
            mImageData.readFully(imgData, 0, mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal() * mImageSegmentHeader.getNumberOfPixelsPerBlockVertical());
        } else {
            short[] data = new short[mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal() * mImageSegmentHeader.getNumberOfPixelsPerBlockVertical()];
            mImageData.readFully(data, 0, mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal() * mImageSegmentHeader.getNumberOfPixelsPerBlockVertical());
            for (int i = 0; i < data.length; ++i) {
                data[i] = (short) (data[i] << (mImageSegmentHeader.getNumberOfBitsPerPixelPerBand() - mImageSegmentHeader.getActualBitsPerPixelPerBand()));
            }
            System.arraycopy(data, 0, imgData, 0, data.length);
        }
        return img;
    }

    /*
        Allow rendering for any bit depth (up to 16 bit max), probably slow reading
    */
    private BufferedImage getNextImageBlockMonoArbitrary() throws IOException {
        BufferedImage img = new BufferedImage(mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal(),
                                              mImageSegmentHeader.getNumberOfPixelsPerBlockVertical(),
                                              BufferedImage.TYPE_USHORT_GRAY);

        short[] data = new short[mImageSegmentHeader.getNumberOfPixelsPerBlockHorizontal() * mImageSegmentHeader.getNumberOfPixelsPerBlockVertical()];
        for (int i = 0; i < data.length; ++i) {
            int pixelRawValue = (int)mImageData.readBits(mImageSegmentHeader.getNumberOfBitsPerPixelPerBand());
            if ((mImageSegmentHeader.getNumberOfBitsPerPixelPerBand() == mImageSegmentHeader.getActualBitsPerPixelPerBand()) || (mImageSegmentHeader.getPixelJustification() == PixelJustification.LEFT)) {
                data[i] = (short) (pixelRawValue << (img.getColorModel().getPixelSize() - mImageSegmentHeader.getNumberOfBitsPerPixelPerBand()));
            } else {
                data[i] = (short) (pixelRawValue << (img.getColorModel().getPixelSize() - mImageSegmentHeader.getActualBitsPerPixelPerBand()));
            }
        }

        short[] imgData = ((DataBufferUShort)img.getRaster().getDataBuffer()).getData();
        System.arraycopy(data, 0, imgData, 0, data.length);
        return img;
    }

    private long getOffsetForBlock(int rowIndex, int columnIndex) {
        // TODO: this should handle mask blocks.
        int numberOfBlocks = rowIndex * mImageSegmentHeader.getNumberOfBlocksPerRow() + columnIndex;
        if (mMask.isMaskedBlock(numberOfBlocks, 0)) {
            System.out.println("Some masked block");
        }
        return numberOfBlocks * mImageSegmentHeader.getNumberOfBytesPerBlock();
    }

    private ImageMask buildUncompressedImageMask() {
        return new ImageMask(mImageSegmentHeader);
    }
}
