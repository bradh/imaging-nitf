package org.codice.imaging.nitf.render.imagehandler;

import java.awt.*;
import java.io.IOException;

import javax.imageio.stream.ImageInputStream;

import org.codice.imaging.nitf.core.image.NitfImageSegmentHeader;

/**
 * An ImageModeHandler abstracts the processing of an ImageInputStream based on the Nitf Image Mode.
 * Pixel-by-pixel rendering is delegated to the supplied ImageRepresentationHandler.
 */
public interface ImageModeHandler {

    /**
     *
     * @param imageSegmentHeader - the NitfImageSegmentHeader for the image being rendered.
     * @param imageInputStream - the ImageInputStream containing the image data.
     * @param targetImage - the Graphic2D that the image will be rendered to.
     * @param imageRepresentationHandler - the ImageRepresentationHandler which will render a single pixel.
     * @throws IOException - propagated from the ImageInputStream.
     */
    void handleImage(NitfImageSegmentHeader imageSegmentHeader, ImageInputStream imageInputStream,
            Graphics2D targetImage, ImageRepresentationHandler imageRepresentationHandler)
            throws IOException;
}
