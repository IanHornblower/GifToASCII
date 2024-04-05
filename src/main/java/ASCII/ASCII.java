package ASCII;

import java.awt.*;
import java.awt.image.BufferedImage;

// TODO: add b/w masking

public class ASCII {
    boolean invert = true;
    String calc = "RGBsum";  // Choices: "RGBsum", "R", "G", "B", "BW"
    boolean dither = false;
    boolean autocontrast = true;
    boolean noempty = false; // Replace spaces (u2800) with single dots
    String color = "htmlall"; // Choices "none", "ansi", "ansifg", "ansiall", "html", "htmlbg", "htmlall"
    boolean blank = false; // Use all MAX sized brail
    int width = 100 * 2; //char | mult by 2 cus it has to be to output 35 char
    BufferedImage input;
    
    String[] filter_options = new String[]{"RGBsum", "R", "G", "B", "BW"};
    String[] color_options = new String[]{"none", "ansi", "ansifg", "ansiall", "html", "htmlbg", "htmlall"};

    public static int getRedValue(BufferedImage img, int x, int y) {
        int pixel = img.getRGB(x, y);
        int red = (pixel >> 16) & 0xFF; // Extract red component
        return red;
    }

    public static int getBlueValue(BufferedImage img, int x, int y) {
        int pixel = img.getRGB(x, y);
        int blue = pixel & 0xFF; // Extract blue component
        return blue;
    }

    public static int getGreenValue(BufferedImage img, int x, int y) {
        int pixel = img.getRGB(x, y);
        int green = (pixel >> 8) & 0xFF; // Extract green component
        return green;
    }

    public static int getRGB(BufferedImage img, int x, int y, int pos) {
        switch (pos) {
            case 0:
                return getRedValue(img, x, y);
            case 1:
                return getGreenValue(img, x, y);
            case 2:
                return getBlueValue(img, x, y);
            default:
                return 0;
        }
    }

    public BufferedImage adjustToColor(BufferedImage input, int pos) {
        for(int y = 0; y < input.getHeight(); y++) {
            for(int x = 0; x < input.getWidth(); x++) {
                int rgb = getRGB(input, x,y, pos);
                input.setRGB(x,y, rgb);
            }
        }
        return input;
    }

    public BufferedImage convertToRGB(BufferedImage img) {
        BufferedImage newImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        newImg.getGraphics().drawImage(img, 0, 0, null);
        return newImg;
    }

    public BufferedImage applyAlgo(BufferedImage input, String algo) {
        switch (algo) {
            case "RGBsum":
            case "BW":
                input = convertToRGB(input);
                break;
            case "R":
                input = adjustToColor(input, 0);
                break;
            case "G":
                input = adjustToColor(input, 1);
                break;
            case "B":
                input = adjustToColor(input, 2);
                break;
        }
        return input;
    }

    public double calcAverage(BufferedImage input, String algo, boolean autocontrast) {
        if(autocontrast) {
            double average = 0;

            for(int y = 0; y < input.getHeight(); y++) {
                for(int x = 0; x < input.getWidth(); x++) {
                    switch (algo) {
                        case "R":
                            average += getRedValue(input, x, y);
                            // Fall-through
                        case "G":
                            average += getGreenValue(input, x, y);
                            // Fall-through
                        case "B":
                            average += getBlueValue(input, x, y);
                            break;
                        default:
                            // RGBsum
                            average += getRedValue(input, x, y) + getGreenValue(input, x, y) + getBlueValue(input, x, y);
                            break;
                    }

                }
            }

            return  average / (input.getWidth() * input.getHeight());
        }
        else return 382.5;
    }

    public boolean getDotValue(BufferedImage input, int[] pos, double average) {
        if(getRedValue(input, pos[0], pos[1]) + getGreenValue(input, pos[0], pos[1]) + getBlueValue(input, pos[0], pos[1]) < average) {
            return !invert;
        }
        else return invert;
    }

    public char blockFromCursor(BufferedImage input, int[] pos, double average, boolean noempty, boolean blank) {
        if(blank) return 0x28ff;
        char blockValue = 0x2800;
        if(getDotValue(input, pos, average)) blockValue += 0x0001;
        if(getDotValue(input, new int[]{pos[0] + 1, pos[1]}, average)) blockValue += 0x0008;
        if(getDotValue(input, new int[]{pos[0], pos[1] + 1}, average)) blockValue += 0x0002;
        if(getDotValue(input, new int[]{pos[0] + 1, pos[1] + 1}, average)) blockValue += 0x0010;
        if(getDotValue(input, new int[]{pos[0], pos[1] + 2}, average)) blockValue += 0x0004;
        if(getDotValue(input, new int[]{pos[0] + 1, pos[1] + 2}, average)) blockValue += 0x0020;
        if(getDotValue(input, new int[]{pos[0], pos[1] + 3}, average)) blockValue += 0x0040;
        if(getDotValue(input, new int[]{pos[0] + 1, pos[1] + 3}, average)) blockValue += 0x0080;
        if(noempty && blockValue == 0x2800) blockValue = 0x2801;
        return blockValue;
    }

    public String colorAverageAtCursor(BufferedImage originalImage, int[] pos, String colorstyle) {
        int px = originalImage.getRGB(pos[0], pos[1]);

        Color color = new Color(px);

        switch (colorstyle) {
            case "ansi":
                return String.format("\u001B[48;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue());
            case "ansifg":
                return String.format("\u001B[38;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue());
            case "ansiall":
                return String.format("\u001B[38;2;%d;%d;%d;48;2;%d;%d;%dm", color.getRed(), color.getGreen(), color.getBlue(), color.getRed(), color.getGreen(), color.getBlue());
            case "html":
                return String.format("<font color=\"#%02x%02x%02x\">", color.getRed(), color.getGreen(), color.getBlue());
            case "htmlbg":
                return String.format("<font style=\"background-color:#%02x%02x%02x\">", color.getRed(), color.getGreen(), color.getBlue());
            case "htmlall":
                return String.format("<font style=\"color:#%02x%02x%02x;background-color:#%02x%02x%02x\">", color.getRed(), color.getGreen(), color.getBlue(), color.getRed(), color.getGreen(), color.getBlue());
            case "htmlnocolor\"":
                return "<font>";

            default:
                return "";
        }
    }

    public static BufferedImage convertToBinary(BufferedImage img) {
        BufferedImage binaryImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        Graphics2D g2d = binaryImg.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();
        return binaryImg;
    }

    public static BufferedImage resize(BufferedImage img, int width, int height) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    public String getRawASCII(BufferedImage input, BufferedImage originalImage, boolean dither, boolean autocontrast, boolean noempty, String colorstyle, boolean blank) {
        StringBuilder output = new StringBuilder();

        input = applyAlgo(input, calc);
        input = convertToRGB(input);

        double average = calcAverage(input, calc, autocontrast);

        if(dither) {
            input = convertToBinary(input);
        }
        input = convertToRGB(input);

        int ySize = input.getHeight();
        int xSize = input.getWidth();

        int yPos = 0;
        int xPos = 0;

        StringBuilder line = new StringBuilder("");

        while (yPos < ySize - 3) {
            xPos = 0;
            while (xPos < xSize) {
                line.append(colorAverageAtCursor(originalImage, new int[]{xPos, yPos}, colorstyle));
                line.append(blockFromCursor(input, new int[]{xPos, yPos}, average, noempty, blank));

                if(colorstyle.equals("html") || colorstyle.equals("htmlbg") || colorstyle.equals("htmlnocolor")) line.append("</font>");
                xPos += 2;
            }
            if(colorstyle.equals("ansi") || colorstyle.equals("ansifg") || colorstyle.equals("ansiall")) line.append("\\u001B[0m");
            if(colorstyle.equals("html") || colorstyle.equals("htmlbg") || colorstyle.equals("htmlall") || colorstyle.equals("htmlnocolor")) line.append("</br>");
            output.append(line);
            line = new StringBuilder("");
            yPos += 4;
        }

        return output.toString();
    }

    public static BufferedImage copyImage(BufferedImage source){
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), source.getType());
        Graphics g = b.getGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public String getASCII(BufferedImage frame) {
        BufferedImage image = frame;

        // Could cause issue due to floating point math
        image = resize(image, width, Math.round((width * image.getHeight()) / image.getWidth()));

        int offX = image.getWidth() % 2;
        int offY = image.getHeight() % 4;

        if((offX + offY) > 0) image = resize(image, image.getWidth() + offX, image.getHeight() + offY);
        BufferedImage originalImage = copyImage(image);

        return getRawASCII(image, originalImage, dither, autocontrast, noempty, color, blank);
    }
}