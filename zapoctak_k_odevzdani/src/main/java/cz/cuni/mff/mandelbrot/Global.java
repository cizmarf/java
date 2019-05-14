package cz.cuni.mff.mandelbrot;

public class Global {
    public static int numberOfColors = 4096;
    public static int iterations = 150;
    public static double zoomNorm = 10;
    public static double originSizeX = 3, originSizeY = 2;
    public static double sizeX = originSizeX, sizeY = originSizeY;
    public static double pointX = -0.5, pointY = 0;
    public static int height, width;

    public static long getZoom(){
        return (long)(originSizeX / sizeX);
    }

    public static void setSize(long zoom){
        sizeX = originSizeX / zoom;
        sizeY = originSizeY / zoom;
    }

    public static int countIterations(){
        return (int) (iterations/Math.pow(sizeX/originSizeX, itPow));
    }

    public static void setIterations(int newIterations){
        iterations = (int)(Math.pow(sizeX/originSizeX, itPow) * newIterations);
    }

    public static void resetSize(){
        sizeX = originSizeX;
        sizeY = originSizeY;
    }

    private static double itPow = 0.1;
}
