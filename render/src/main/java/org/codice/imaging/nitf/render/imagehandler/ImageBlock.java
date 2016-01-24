package org.codice.imaging.nitf.render.imagehandler;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferInt;

/**
 * An ImageBlock represents a single block of a larger image.
 */
class ImageBlock {
    private int row;
    private int column;
    private DataBuffer data;

    /**
     *
     * @param row - the row position of this ImageBlock in the larger image.
     * @param column - the column position of this ImageBlock in the larger image.
     * @param blockSize - the size of this block.
     */
    public ImageBlock(int dataType, int row, int column, int blockSize) {
        this.row = row;
        this.column = column;
        createBufferByType(dataType, blockSize);

    }

    private void createBufferByType(int dataType, int blockSize) {
        switch (dataType) {
            case DataBuffer.TYPE_INT:
                data = new DataBufferInt(blockSize);
                break;
            default:
                throw new UnsupportedOperationException("No support for type: " + dataType);
        }
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
     * @return the data for this ImageBlock.
     */
    public DataBuffer getData() {
        return data;
    }

    void clear() {
        createBufferByType(data.getDataType(), data.getSize());
    }
}
