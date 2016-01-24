package org.codice.imaging.nitf.render.imagehandler;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * The ImageBlockMatrix represents image data stored in a rowcount x columncount matrix of blocks.
 */
class ImageBlockMatrix {
    final private ImageBlock[][] blocks;

    /**
     * Construct a new ImageBlockMatrix.
     *
     * @param imageSegmentHeader the image segment header specifying the blocked image matrix.
     */
    ImageBlockMatrix(NitfImageSegmentHeader imageSegmentHeader) {
        int rowCount = imageSegmentHeader.getNumberOfBlocksPerColumn();
        int columnCount = imageSegmentHeader.getNumberOfBlocksPerRow();
        int blockSize = imageSegmentHeader.getNumberOfPixelsPerBlockHorizontal()
                * imageSegmentHeader.getNumberOfPixelsPerBlockVertical();

        blocks = new ImageBlock[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                blocks[i][j] = new ImageBlock(i, j, blockSize);
            }
        }
    }

    /**
     * Getter for image blocks.
     *
     * @param row - the row to retrieve the ImageBlock from.
     * @param column - the column to retrieve the ImageBlock from.
     * @return - the ImageBlock referenced by (row, column).
     */
    public ImageBlock getImageBlock(int row, int column) {
        return blocks[row][column];
    }
}
