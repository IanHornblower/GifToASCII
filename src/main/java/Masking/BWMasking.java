package Masking;

import java.awt.image.BufferedImage;

public class BWMasking {

    private double lowerBound = 0, uppperBound = 255;

    public BWMasking(double lowerBound, double upperBound) {
        this.lowerBound = lowerBound;
        this.uppperBound = upperBound;
    }

    public BufferedImage process(BufferedImage image) {

    }


}
