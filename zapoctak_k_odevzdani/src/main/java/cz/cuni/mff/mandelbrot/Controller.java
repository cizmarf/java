package cz.cuni.mff.mandelbrot;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;


public class Controller implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetCanvas();
        Global.height = (int)canvas.getHeight();
        Global.width = (int)canvas.getWidth();
        et = new Set();
        textFieldIterations.setText(String.valueOf(Global.iterations));
        textFieldZoom.setText(String.valueOf(Global.getZoom()));

        RSlider.setValue(100);
        RSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            RSlider_val = RSlider.getValue() / 100;
            fillCanvas();
        });

        GSlider.setValue(100);
        GSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            GSlider_val = GSlider.getValue() / 100;
            fillCanvas();
        });

        BSlider.setValue(100);
        BSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            BSlider_val = BSlider.getValue() / 100;
            fillCanvas();
        });

        fillCanvas();
    }

    @FXML
    public void textFieldIterationsKeyTyped(KeyEvent keyEvent) {
        try {
            int it = Integer.parseInt(textFieldIterations.getText());
            Global.setIterations(it);
            et = new Set();
            fillCanvas();
        }catch (Exception e){}
    }

    public void textFieldZoomKeyTyped(KeyEvent keyEvent) {
        try {
            long zoom = Long.parseLong(textFieldZoom.getText());
            Global.setSize(zoom);
            textFieldIterations.setText(String.valueOf(Global.countIterations()));
            et = new Set();
            fillCanvas();
        }catch (Exception e){}


    }

    @FXML
    public void textFieldReKeyTyped(KeyEvent keyEvent) {
        try {
            double pointX = Double.parseDouble(textFieldRe.getText());
            double pointY = Double.parseDouble(textFieldIm.getText());
            Global.pointX = pointX;
            Global.pointY = pointY;
            et = new Set();
            fillDraggedCanvas();
        }catch (Exception e){}

    }

    @FXML
    public void textFieldImKeyTyped(KeyEvent keyEvent) {
        textFieldReKeyTyped(keyEvent);
    }

    @FXML
    public void canvasMouseDragged(MouseEvent mouseEvent) {
        resetCanvas();
        ControllerCode.actualTranslationX += ControllerCode.mouse_lastX - (int)mouseEvent.getX();
        ControllerCode.actualTranslationY += ControllerCode.mouse_lastY - (int)mouseEvent.getY();
        fillDraggedCanvas();
        upDateMouse(mouseEvent);
    }

    @FXML
    public void canvasOnMousePressed(MouseEvent mouseEvent) {
        upDateMouse(mouseEvent);
    }

    @FXML
    public void canvasOnMouseRelease(MouseEvent mouseEvent) {
        ControllerCode.addToAbsoluteTranslationAndNullActual();
        et = new Set();
        fillCanvas();
    }

    @FXML
    public void canvasOnScroll(ScrollEvent scrollEvent) {
        Set.killThreads();
        ControllerCode.actualZoom += scrollEvent.getDeltaY();
        resetCanvas();
        redrawZoomCanvas();

    }

    public void redrawZoomCanvas(){
        //resetCanvas();
        textFieldZoom.setText(String.valueOf(Global.getZoom()));
        ControllerCode.zoom();
        textFieldIterations.setText(String.valueOf(Global.countIterations()));
        et = new Set();
        fillDraggedCanvas();
    }

    private void fillCanvas(){
        textFieldRe.setText(String.valueOf(Global.pointX));
        textFieldIm.setText(String.valueOf(Global.pointY));
        for(int i = 0; i < Global.width; ++i) {
            for (int j = 0; j < Global.height; ++j) {
                double realValue = et.getValue(i, j);
                valueToColor(realValue);
                canvas.getGraphicsContext2D().getPixelWriter().setColor(i,j, valueToColor(realValue));
            }
        }
    }

    private void fillDraggedCanvas(){
        for(int i = 0; i < Global.width; ++i) {
            for (int j = 0; j < Global.height; ++j) {
                double realValue = et.getValueError(i + ControllerCode.actualTranslationX, j + ControllerCode.actualTranslationY);
                if(realValue == -1)
                    continue;
                valueToColor(realValue);
                canvas.getGraphicsContext2D().getPixelWriter().setColor(i,j, valueToColor(realValue));
            }
        }
    }

    private Color valueToColor(double pixel) {
        Color color1 = ControllerCode.pixelToColor(pixel);
        Color color2 = ControllerCode.pixelToColor(pixel + 1);
        double dif = pixel - (int)pixel;
        double R = ((color2.getRed() - color1.getRed()) * (dif));
        double B = ((color2.getBlue() - color1.getBlue()) * (dif));
        double G = ((color2.getGreen() - color1.getGreen()) * (dif));
        return Color.rgb((int)((color1.getRed() + R) * 255 * RSlider_val), (int)((color1.getGreen() + G) * 255 * GSlider_val), (int)((color1.getBlue() + B) * 255 * BSlider_val));
    }




    private void upDateMouse(MouseEvent mouseEvent){
        ControllerCode.mouse_lastX = (int)mouseEvent.getX();
        ControllerCode.mouse_lastY = (int)mouseEvent.getY();
    }

    private void resetCanvas(){
        canvas.getGraphicsContext2D().setFill(Color.rgb(253, 253, 253));
        canvas.getGraphicsContext2D().fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

    }

    private Set et;

    private double RSlider_val = 1;
    private double GSlider_val = 1;
    private double BSlider_val = 1;

    @FXML
    private Canvas canvas;

    @FXML
    private TextField textFieldRe;

    @FXML
    private TextField textFieldIm;

    @FXML
    private TextField textFieldIterations;

    @FXML
    private TextField textFieldZoom;

    @FXML
    private Slider RSlider;

    @FXML
    private Slider GSlider;

    @FXML
    private Slider BSlider;
}
