package Gif;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

public class Frames {

    ArrayList<BufferedImage> frameList = new ArrayList<>();

    public Frames(ArrayList<BufferedImage> frameList) {
        this.frameList = frameList;
    }

    public Frames(BufferedImage... frames) {
        this.frameList.addAll(Arrays.asList(frames));
    }

    public void setAll(ArrayList<BufferedImage> frameList) {
        this.frameList = frameList;
    }

    public void setAll(BufferedImage... frames) {
        this.frameList.addAll(Arrays.asList(frames));
    }

    public void setFrame(int i, BufferedImage frames) {
        frameList.set(i, frames);
    }

    public BufferedImage getFrame(int i) {
        return frameList.get(i);
    }

}
