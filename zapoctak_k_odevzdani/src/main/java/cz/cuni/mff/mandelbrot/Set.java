package cz.cuni.mff.mandelbrot;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.Math.*;

class ThreadSet implements Runnable{

    ThreadSet(int start, int end, double[][] data, double startPointX, double startPointY){
        startData = start;
        endData = end;
        this.startPointX = startPointX;
        this.startPointY = startPointY;
        this.data = data;
    }

    @Override
    public void run() {
        double x0, y0;
        int iterations = Global.countIterations();
        //System.out.println(iterations);
        for(int i = startData; i < endData; ++i) {
            double[] tempData = new double[Global.height];
            for (int j = 0; j < Global.height; ++j) {

                // antianalysing, 4x slower

                double gnu = 0;
                {
                    x0 = ((double) (i + 0.2) / Global.width) * Global.sizeX + startPointX;
                    y0 = ((double) (j + 0.2) / Global.height) * Global.sizeY - startPointY;
                    double mx = 0, my = 0, px;
                    long n = 0;
                    while (n < iterations && mx * mx + my * my < 4) {
                        px = mx * mx - my * my + x0;
                        my = 2 * mx * my + y0;
                        mx = px;
                        n++;
                    }
                    double nu = 1;
                    if (n < iterations) {
                        double log_zn = log(mx * mx + my * my) / 2;
                        nu = log(log_zn / log(2)) / log(2);
                    }
                    gnu += nu;
                    tempData[j] = n;
                }
                {
                    x0 = ((double) (i + 0.8) / Global.width) * Global.sizeX + startPointX;
                    y0 = ((double) (j + 0.2) / Global.height) * Global.sizeY - startPointY;
                    double mx = 0, my = 0, px;
                    long n = 0;
                    while (n < iterations && mx * mx + my * my < 4) {
                        px = mx * mx - my * my + x0;
                        my = 2 * mx * my + y0;
                        mx = px;
                        n++;
                    }
                    double nu = 1;
                    if (n < iterations) {
                        double log_zn = log(mx * mx + my * my) / 2;
                        nu = log(log_zn / log(2)) / log(2);
                    }
                    gnu += nu;
                    tempData[j] += n;
                }
                {
                    x0 = ((double) (i + 0.8) / Global.width) * Global.sizeX + startPointX;
                    y0 = ((double) (j + 0.8) / Global.height) * Global.sizeY - startPointY;
                    double mx = 0, my = 0, px;
                    long n = 0;
                    while (n < iterations && mx * mx + my * my < 4) {
                        px = mx * mx - my * my + x0;
                        my = 2 * mx * my + y0;
                        mx = px;
                        n++;
                    }
                    double nu = 1;
                    if (n < iterations) {
                        double log_zn = log(mx * mx + my * my) / 2;
                        nu = log(log_zn / log(2)) / log(2);
                    }
                    gnu += nu;
                    tempData[j] += n;
                }
                {
                    x0 = ((double) (i + 0.2) / Global.width) * Global.sizeX + startPointX;
                    y0 = ((double) (j + 0.8) / Global.height) * Global.sizeY - startPointY;
                    double mx = 0, my = 0, px;
                    long n = 0;
                    while (n < iterations && mx * mx + my * my < 4) {
                        px = mx * mx - my * my + x0;
                        my = 2 * mx * my + y0;
                        mx = px;
                        n++;
                    }
                    double nu = 1;
                    if (n < iterations) {
                        double log_zn = log(mx * mx + my * my) / 2;
                        nu = log(log_zn / log(2)) / log(2);
                    }
                    gnu += nu;
                    tempData[j] += n;
                }
                tempData[j] = (long)tempData[j] / 4 + 1 - gnu / 4;
                //System.out.println(tempData[j].fractionPart);
            }
            data[i] = tempData;
        }
    }

    private int startData, endData;
    private static double startPointX, startPointY;
    private static double[][] data;


}

public class Set {
    public Set(){
        this.data = new double[Global.width][Global.height];
        fillCanvas();
    }

    public static double getValueError(int i, int j){
        if(i < 0 || i >= Global.width || j < 0 || j >= Global.height)
            return -1;
        return data[i][j];
    }

    public static double getValue(int i , int j){
        return data[i][j];
    }

    public static void killThreads(){
        executor.shutdownNow();
    }

    private static void fillCanvas() {
        int subArraysForOneThread = 30;
        int pocetjader = Runtime.getRuntime().availableProcessors();
        executor = new ThreadPoolExecutor((int)pocetjader+4, pocetjader + pocetjader, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        for (int i = 0; i < Global.width; i += subArraysForOneThread){
            int end = i + subArraysForOneThread;
            if(end > Global.width)
                end = Global.width;
            executor.submit(new ThreadSet(i, end, data, Global.pointX - Global.sizeX / 2, Global.pointY + Global.sizeY / 2));
        }
        executor.shutdown();
        while (!executor.isTerminated()){Thread.yield();}

    }


    private static ThreadPoolExecutor executor;
    private static double[][] data;
}
