package Gif;

import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class GifDecoder {

    public static Gif decode(String string) throws IOException {
        return decode(new File(string));
    }

    public static Gif decode(File file) throws IOException {
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
        reader.setInput(imageInputStream, false);

        Metadata metadata = generateMetadata(file, reader);
        Frames frames;

        try {
            frames= splitFrames(reader);
        }
        catch (IOException e) {
            throw new IOException("Gif could not be split, Try another Gif");
        }


        reader.dispose();
        imageInputStream.close();

        return new Gif(metadata, frames);
    }

    private static Frames splitFrames(ImageReader reader) throws IOException {
        ArrayList<BufferedImage> frameList = new ArrayList<>();

        for (int frameIndex = 0; frameIndex < reader.getNumImages(true); frameIndex++) {
            frameList.add(reader.read(frameIndex));
        }

        return new Frames(frameList);
    }

    private static Metadata generateMetadata(File file, ImageReader reader) throws IOException {
        String name = file.getName().replaceAll(".gif","");
        int width = reader.getWidth(0);
        int height = reader.getHeight(0);
        float aspectRatio = reader.getAspectRatio(0);
        int frames = reader.getNumImages(true);
        long totalDuration = getGifDuration(reader);

        return new Metadata(name, width, height, aspectRatio, frames, totalDuration);
    }

    private static long getGifDuration(ImageReader reader) throws IOException {
        long totalDuration = 0;

        try {
            int numFrames = reader.getNumImages(true); // Get the number of frames in the GIF
            for (int i = 0; i < numFrames; i++) {
                // Get the metadata for the current frame
                IIOMetadata metadata = reader.getImageMetadata(i);

                // Calculate the duration of the current frame
                long frameDuration = getFrameDuration(metadata);

                // Add the frame duration to the total duration
                totalDuration += frameDuration;
            }
        } finally {
            // Close resources
            reader.dispose();
        }

        return totalDuration;
    }

    private static long getFrameDuration(IIOMetadata metadata) {
        String[] metadataNames = metadata.getMetadataFormatNames();

        for (String name : metadataNames) {
            if (name.equalsIgnoreCase("javax_imageio_gif_image_1.0")) {
                IIOMetadataNode root = (IIOMetadataNode) metadata.getAsTree(name);

                // Locate the Graphic Control Extension (GCE) nodes
                NodeList gceNodes = root.getElementsByTagName("GraphicControlExtension");

                if (gceNodes.getLength() > 0) {
                    IIOMetadataNode gceNode = (IIOMetadataNode) gceNodes.item(0);

                    // Get the duration (frame delay) attribute from the GCE
                    String duration = gceNode.getAttribute("delayTime");

                    // Convert the duration from hundredths of a second to milliseconds
                    return Integer.parseInt(duration) * 10L;
                }
            }
        }

        return 0; // Default to 0 if no frame duration information is found
    }
}
