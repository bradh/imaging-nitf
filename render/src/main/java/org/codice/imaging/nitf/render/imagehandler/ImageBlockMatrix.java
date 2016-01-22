package org.codice.imaging.nitf.render.imagehandler;

/**
 * The ImageMatrix represents image data stored in a rowcount x columncount matrix.
 */
class ImageBlockMatrix {
    private ImageBlock[][] blocks;

    /**
     *
     * @param rowCount - the number of rows in the matrix.
     * @param columnCount - the number of columns in each matrix row.
     * @param blockSize - the size of a single block in the matrix.
     */
    public ImageBlockMatrix(int rowCount, int columnCount, int blockSize) {
        blocks = new ImageBlock[rowCount][columnCount];

        for (int i = 0; i < rowCount; i++) {
            for (int j = 0; j < columnCount; j++) {
                blocks[i][j] = new ImageBlock(i, j, blockSize);
            }
        }
    }

    /**
     *
     * @param row - the row to retrieve the ImageBlock from.
     * @param column - the column to retrieve the ImageBlock from.
     * @return - the ImageBlock referenced by (row, column).
     */
    public ImageBlock getImageBlock(int row, int column) {
        return blocks[row][column];
    }
}
