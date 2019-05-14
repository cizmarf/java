package cz.cuni.mff.mandelbrot;

import javafx.scene.paint.Color;

public class ControllerCode{
    public static int actualTranslationX = 0, actualTranslationY = 0;
    public static int actualZoom = (int) Global.zoomNorm;
    public static int mouse_lastX, mouse_lastY;

    public static void addToAbsoluteTranslationAndNullActual(){
        Global.pointX += Global.sizeX * (actualTranslationX/(double) Global.width);
        Global.pointY -= Global.sizeY * (actualTranslationY/(double) Global.height);
        actualTranslationX = 0;
        actualTranslationY = 0;
    }

    public static double getNormZoom(){
        if(actualZoom > 1){
            return Math.pow(actualZoom, 1.0/1) / Global.zoomNorm + 1;

        }
        if(actualZoom < -1){
            return (Math.pow(actualZoom, 1.0/1) / Global.zoomNorm - 1);
        }
        return 1;
    }

    public static void zoom(){
        double normZoom = getNormZoom();
        if(normZoom > 0) {
            Global.sizeX /= normZoom;
            Global.sizeY /= normZoom;
        }else{
            Global.sizeX *= -normZoom;
            Global.sizeY *= -normZoom;
        }
        if(Global.sizeX > 3 || Global.sizeY > 2){
            Global.resetSize();
        }
        actualZoom = 0;
    }

    public static Color pixelToColor(double pixel) {
        int normalizedValue = (int)pixel % Global.iterations;
        normalizedValue = (int) (normalizedValue /(double) Global.countIterations() * Global.numberOfColors);
        if((int)pixel == Global.countIterations())
            return Color.rgb(0,0,0);

        /**
         12 bits     CBA_987_654_321
         R =         0907_4321
         G =         0B80_5321
         B =         C090_6321
         */
        //       CBA_987_654_321
        int R =  (normalizedValue & 0b000_000_001_111) |        //0000_1111
                ((normalizedValue & 0b000_001_000_000) >> 2) |  //0001_0000
                ((normalizedValue & 0b000_100_000_000) >> 2);   //0100_0000
        int G =  (normalizedValue & 0b000_000_000_111) |        //0000_0111
                ((normalizedValue & 0b000_000_010_000) >> 1) |  //0000_1000
                ((normalizedValue & 0b000_010_000_000) >> 2) |  //0010_0000
                ((normalizedValue & 0b010_000_000_000) >> 4);   //0100_0000
        int B =  (normalizedValue & 0b000_000_000_111) |        //0000_0111
                ((normalizedValue & 0b000_000_100_000) >> 2) |  //0000_1000
                ((normalizedValue & 0b000_100_000_000) >> 3) |  //0010_0000
                ((normalizedValue & 0b100_000_000_000) >> 4);   //1000_0000
        return Color.rgb(B, G, R);


    }

}
