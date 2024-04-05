package Gif;

import java.io.IOException;
import java.util.function.IntSupplier;

public class TestGifDecoder {



    public static void main(String[] args) throws IOException {
        boolean running = true;
        IntSupplier frame = ()-> 0;

        String duckStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/duck.gif";
        String amongusStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/mogusdude.gif";
        String twerkStr = "C:/Users/ianmh/OneDrive/Documents/FTC/Center Stage/GifRenderer/Testing/Gif To Ascii/twerk.gif";

        Gif amongus = null;
        Gif duck = null;
        Gif twek = null;

        float start = 0;
        float end = 0;

        while (running) {
            start = System.nanoTime();
            //amongus = GifDecoder.decode(amongusStr);
            duck = GifDecoder.decode(duckStr);
            //twek = GifDecoder.decode(twerkStr);

            end = System.nanoTime();
            running = false;
        }

        System.out.println("Duck Meta Data" + duck.metadata().toString());
    }
}
