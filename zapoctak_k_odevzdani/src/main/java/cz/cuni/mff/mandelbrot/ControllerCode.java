package cz.cuni.mff.mandelbrot;

import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

 class ControllerCode{
	 static int actualTranslationX = 0, actualTranslationY = 0;
	 static int actualZoom = (int) Global.zoomNorm;
	 static int mouse_lastX, mouse_lastY;

	/**
	 * Function actualizes values of center of set on mouse release.
	 * This is computed by #actualTranslationX and #actualTranslationY
	 * and according to width and height of canvas and actual size of zoomed area.
	 * In these variables is stored value of translation until mouse press.
	 */
	 static void addToAbsoluteTranslationAndNullActual(){
		Global.pointX += Global.sizeX * (actualTranslationX/(double) Global.width);
		Global.pointY -= Global.sizeY * (actualTranslationY/(double) Global.height);

		actualTranslationX = 0;
		actualTranslationY = 0;
	}

	/**
	 * Function return normalized zoom.
	 * The zoom value is normalized by #Global.zoomNorm variable.
	 * The Function discriminates between zoomin and zoomout.
	 * If zoom is immeasurable function returns 1.
	 */
	 private static double getNormZoom(){
		if(actualZoom > 1){
			return Math.pow(actualZoom, 1.0/1) / Global.zoomNorm + 1;

		}

		if(actualZoom < -1){
			return (Math.pow(actualZoom, 1.0/1) / Global.zoomNorm - 1);
		}

		return 1;
	}

	/**
	 * Function sets global parametres according to zoom.
	 * If zoomout overlap the maximum height and width values it sets them to initial values.
	 */
	 static void zoom(){
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

	/**
	 * Function gives real value of pixel and compute the RGB representation of this value.
	 * The function proccess 12 bits from integer part of pixel variable and place corresponding bits
	 * to designated places in output RGB variable.
	 * The bits are placed by the following key:
	 * 12 bits		CBA_987_654_321
	 * R =			0907_4321
	 * G =			0B80_5321
	 * B =			C090_6321
	 */
	 private static Color pixelToColor(double pixel) {
		int normalizedValue = (int)pixel % Global.iterations;
		normalizedValue = (int) (normalizedValue /(double) Global.countIterations() * Global.numberOfColors);

		if((int)pixel == Global.countIterations())
			return Color.rgb(0,0,0);

		int R =  (normalizedValue & 0b000_000_001_111) |		//0000_1111
				((normalizedValue & 0b000_001_000_000) >> 2) |	//0001_0000
				((normalizedValue & 0b000_100_000_000) >> 2);	//0100_0000

		int G =  (normalizedValue & 0b000_000_000_111) |		//0000_0111
				((normalizedValue & 0b000_000_010_000) >> 1) |	//0000_1000
				((normalizedValue & 0b000_010_000_000) >> 2) |	//0010_0000
				((normalizedValue & 0b010_000_000_000) >> 4);	//0100_0000

		int B =  (normalizedValue & 0b000_000_000_111) |		//0000_0111
				((normalizedValue & 0b000_000_100_000) >> 2) |	//0000_1000
				((normalizedValue & 0b000_100_000_000) >> 3) |	//0010_0000
				((normalizedValue & 0b100_000_000_000) >> 4);	//1000_0000

		return Color.rgb(B, G, R);
	}

	/**
	 * Computes color of pixel.
	 * Firstly it gets two colors and then it chooses an color between them using
	 * linear interpolation. Linear interpolation depends on decimal part of
	 * <code>pixel</code> variable.
	 *
	 * @param pixel		the value represents pixel computed by definition of mandelbrot set
	 * @return			the color representing the value of <code>pixel</code>
	 */
	 static Color valueToColor(double pixel) {
		Color color1 = ControllerCode.pixelToColor(pixel);
		Color color2 = ControllerCode.pixelToColor(pixel + 1);

		double dif = pixel - (int)pixel;
		double R = ((color2.getRed() - color1.getRed()) * (dif));
		double B = ((color2.getBlue() - color1.getBlue()) * (dif));
		double G = ((color2.getGreen() - color1.getGreen()) * (dif));

		return Color.rgb((int)((color1.getRed() + R) * 255 * RSlider_val), (int)((color1.getGreen() + G) * 255 * GSlider_val), (int)((color1.getBlue() + B) * 255 * BSlider_val));
	}

	/**
	 * Reset mouse position variables to values of actual position.
	 *
	 * @param mouseEvent	provides position of mouse on drag event
	 */
	 static void upDateMouse(MouseEvent mouseEvent){
		ControllerCode.mouse_lastX = (int)mouseEvent.getX();
		ControllerCode.mouse_lastY = (int)mouseEvent.getY();
	}


	static double RSlider_val = 1;
	 static double GSlider_val = 1;
	 static double BSlider_val = 1;


}
