package Gif;

public class Gif {

    private final Metadata metadata;
    private final Frames frames;

    public Gif(Metadata metadata, Frames frames) {
        this.metadata = metadata;
        this.frames = frames;
    }

    public Gif(Metadata metadata, Frames frames, Metadata metadata1, Frames frames1) {
        this.metadata = metadata1;
        this.frames = frames1;
    }

    public Metadata metadata() {
        return metadata;
    }

    public Frames frames() {
        return frames;
    }
}
