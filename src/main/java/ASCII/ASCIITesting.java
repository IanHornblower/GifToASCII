package ASCII;

import Gif.Gif;

import java.io.IOException;

import Gif.GifDecoder;

public class ASCIITesting {


    public static void main(String[] args) throws IOException {
        String duckStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/duck.gif";
        String amongusStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/mogusdude.gif";
        String twerkStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/twerk.gif";

        Gif amongus = null;
        Gif duck = null;
        Gif twek = null;

        duck = GifDecoder.decode(twerkStr);

        ASCII ascii = new ASCII();

        String duckASCII = ascii.getASCII(duck.frames().getFrame(0));

        System.out.println(duckASCII);
    }
}
