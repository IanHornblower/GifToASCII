package Gif;

public class Metadata {

    private final String name;
    private final int width;
    private final int height;
    private final float aspectRatio;
    private final int frames;
    private final float totalDuration;


    public Metadata(String name, int width, int height, float aspectRatio, int frames, float totalDuration) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.aspectRatio = aspectRatio;
        this.frames = frames;
        this.totalDuration = totalDuration;
    }

    public String name() {
        return name;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public float aspectRatio() {
        return aspectRatio;
    }

    public int frames() {
        return frames;
    }

    public float totalDuration() {
        return totalDuration;
    }

    @Override
    public String toString() {
        return "Metadata{" +
                "name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", aspectRatio=" + aspectRatio +
                ", frames=" + frames +
                ", totalDuration=" + totalDuration +
                '}';
    }
}
