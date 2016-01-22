package org.codice.imaging.nitf.render.imagehandler;

import java.nio.IntBuffer;

/**
 * An ImageBlock represents a single block of a larger image.
 */
class ImageBlock {
    private int row;
    private int column;
    private IntBuffer data;

    /**
     *
     * @param row - the row position of this ImageBlock in the larger image.
     * @param column - the column position of this ImageBlock in the larger image.
     * @param blockSize - the size of this block.
     */
    public ImageBlock(int row, int column, int blockSize) {
        this.row = row;
        this.column = column;
        this.data = IntBuffer.allocate(blockSize);
    }

    /**
     *
     * @return the row position of this ImageBlock in the larger image.
     */
    public int getRow() {
        return row;
    }

    /**
     *
     * @return the column position of this ImageBlock in the larger image.
     */
    public int getColumn() {
        return column;
    }

    /**
     *
     * @return the IntBuffer that contains the data for this ImageBlock.
     */
    public IntBuffer getData() {
        return data;
    }
}
