package sample;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;

/**
 * Example of how a cubic curve works, drag the anchors around to change the curve.
 * Extended with arrows with the help of José Pereda: http://stackoverflow.com/questions/26702519/javafx-line-curve-with-arrow-head
 * Original code by jewelsea: http://stackoverflow.com/questions/13056795/cubiccurve-javafx
 */
public class CubicCurveManipulatorWithArrows extends Application {

    List<Arrow> arrows = new ArrayList<Arrow>();
    private Button checkCollisions;
    private Group root;

    public static class Arrow extends Polygon {

        public double rotate;
        public float t;
        CubicCurve curve;
        Rotate rz;

        public Arrow( CubicCurve curve, float t) {
            super();
            this.curve = curve;
            this.t = t;
            init();
        }

        public Arrow( CubicCurve curve, float t, double... arg0) {
            super(arg0);
            this.curve = curve;
            this.t = t;
            init();
        }

        private void init() {

            setFill(Color.web("#ff0900"));

            rz = new Rotate();
            {
                rz.setAxis(Rotate.Z_AXIS);
            }
            getTransforms().addAll(rz);

            update();
        }

        public void update() {
            double size = Math.max(curve.getBoundsInLocal().getWidth(), curve.getBoundsInLocal().getHeight());
            double scale = size / 4d;

            Point2D ori = eval(curve, t);
            Point2D tan = evalDt(curve, t).normalize().multiply(scale);

            setTranslateX(ori.getX());
            setTranslateY(ori.getY());

            double angle = Math.atan2( tan.getY(), tan.getX());

            angle = Math.toDegrees(angle);

            // arrow origin is top => apply offset
            double offset = -90;
            if( t > 0.5)
                offset = +90;

            rz.setAngle(angle + offset);

        }

        /**
         * Evaluate the cubic curve at a parameter 0<=t<=1, returns a Point2D
         * @param c the CubicCurve
         * @param t param between 0 and 1
         * @return a Point2D
         */
        private Point2D eval(CubicCurve c, float t){
            Point2D p=new Point2D(Math.pow(1-t,3)*c.getStartX()+
                    3*t*Math.pow(1-t,2)*c.getControlX1()+
                    3*(1-t)*t*t*c.getControlX2()+
                    Math.pow(t, 3)*c.getEndX(),
                    Math.pow(1-t,3)*c.getStartY()+
                            3*t*Math.pow(1-t, 2)*c.getControlY1()+
                            3*(1-t)*t*t*c.getControlY2()+
                            Math.pow(t, 3)*c.getEndY());
            return p;
        }

        /**
         * Evaluate the tangent of the cubic curve at a parameter 0<=t<=1, returns a Point2D
         * @param c the CubicCurve
         * @param t param between 0 and 1
         * @return a Point2D
         */
        private Point2D evalDt(CubicCurve c, float t){
            Point2D p=new Point2D(-3*Math.pow(1-t,2)*c.getStartX()+
                    3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlX1()+
                    3*((1-t)*2*t-t*t)*c.getControlX2()+
                    3*Math.pow(t, 2)*c.getEndX(),
                    -3*Math.pow(1-t,2)*c.getStartY()+
                            3*(Math.pow(1-t, 2)-2*t*(1-t))*c.getControlY1()+
                            3*((1-t)*2*t-t*t)*c.getControlY2()+
                            3*Math.pow(t, 2)*c.getEndY());
            return p;
        }
    }



    public static void main(String[] args) throws Exception { launch(args); }
    @Override public void start(final Stage stage) throws Exception {
        // primera curva
        CubicCurve curve = createStartingCurve(100d,300d,300d,100d);

        Anchor start    = new Anchor(Color.PALEGREEN, curve.startXProperty(),    curve.startYProperty());
        Anchor control1 = new Anchor(Color.GOLD,      curve.controlX1Property(), curve.controlY1Property(),curve.controlX2Property(), curve.controlY2Property());
        Anchor end      = new Anchor(Color.TOMATO,    curve.endXProperty(),      curve.endYProperty());

        this.root = new Group();
        root.getChildren().addAll(curve, start, control1, end);

        double[] arrowShape = new double[] { 0,0,3,30,-3,30 };//forma de la flechas

        for(float i= 0.0f;i<=1f;i+=0.01f) {
            arrows.add(new Arrow(curve, i, arrowShape));
        }

        //segunda curva
        CubicCurve curve2 = createStartingCurve(100d,100d,300d,300d);

        Anchor start2   = new Anchor(Color.PALEGREEN, curve2.startXProperty(),    curve2.startYProperty());
        Anchor control12 = new Anchor(Color.GOLD,      curve2.controlX1Property(), curve2.controlY1Property(),curve2.controlX2Property(), curve2.controlY2Property());
        Anchor end2      = new Anchor(Color.TOMATO,    curve2.endXProperty(),      curve2.endYProperty());

        root.getChildren().addAll(curve2, start2, control12, end2);

        double[] arrowShape2 = new double[] { 0,0,3,30,-3,30 };//forma de la flechas

        for(float i= 0.0f;i<=1f;i+=0.01f) {
            arrows.add(new Arrow(curve2, i, arrowShape2));
        }
        root.getChildren().addAll( arrows);


        this.checkCollisions= new Button("check collisions");
        this.checkCollisions.setOnMouseClicked(event -> {
            for(CubicCurve curbeA: getCurves()){
                for(CubicCurve curveB: getCurves()){
                    if(curbeA!=curveB) {
                        if (collisionCurves(curbeA, curveB)) {
                            autohideAlert("they are collisions", 2000);
                        } else {
                            autohideAlert("they aren't collisions", 2000);
                        }
                    }
                }
            }
        });
        root.getChildren().add(checkCollisions);
        stage.setTitle("Cubic Curve Manipulation Sample");
        stage.setScene(new Scene( root, 400, 400, Color.ALICEBLUE));
        stage.show();
    }

    /**
     * Retorna a forma correcta del poligono que componen la flecha
     * @return double[] que representa el poligono
     */
    public static double[] arrowForm(){
        return new double[] { 0,0,10,20,-10,20 };
    }

    public ArrayList<CubicCurve> getCurves(){
        ArrayList<CubicCurve> curves= new ArrayList<>();
        for(Node node:root.getChildren()){
            if(node instanceof CubicCurve){
                curves.add((CubicCurve)node);
            }
        }
        return curves;
    }

    public boolean collisionCurves(CubicCurve curveA,CubicCurve curveB){
        if(curveA==curveB)
            return false;
        return curveA.intersects(curveB.getLayoutBounds());
    }

    public void autohideAlert(String title, int wait){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(title);
        //alert.setContentText(content);
        //ButtonType buttonTypeOne = new ButtonType("Yes");
        //ButtonType buttonTypeCancel = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        //alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

        Thread thread = new Thread(() -> {
            try {
                // Wait for 5 secs
                Thread.sleep(wait);
                if (alert.isShowing()) {
                    Platform.runLater(() -> alert.close());
                }
            } catch (Exception exp) {
                exp.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
        Optional<ButtonType> result = alert.showAndWait();
    }


    private CubicCurve createStartingCurve(Double x1,Double y1,Double x2,Double y2) {
        CubicCurve curve = new CubicCurve();
        //aqui conectar a los nodos
        curve.setStartX(x1);
        curve.setStartY(y1);
        curve.setEndX(x2);
        curve.setEndY(y2);
        double distance= Math.sqrt(Math.pow(x1-x2,2)+Math.pow(y2-y1,2));
        double tetha=Math.toDegrees(Math.asin((Math.sqrt(Math.pow(y2-y1,2))/distance)));
        double hipo= distance/Math.cos(Math.toRadians(45));
        curve.setControlX1((x1+x2)/2);
        curve.setControlY1((y1+y2)/2);
        curve.setControlX2((x1+x2)/2);
        curve.setControlY2((y1+y2)/2);

        curve.setStroke(Color.FORESTGREEN);
        curve.setStrokeWidth(4);
        curve.setStrokeLineCap(StrokeLineCap.ROUND);
        curve.setFill(null);
        curve.setFocusTraversable(false);
        return curve;
    }


    // a draggable anchor displayed around a point.
    class Anchor extends Circle {
        Anchor(Color color, DoubleProperty x, DoubleProperty y) {
            super(x.get(), y.get(), 10);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);

            x.bind(centerXProperty());
            y.bind(centerYProperty());
            this.setVisible(true);//true
            enableDrag();
        }

        Anchor(Color color, DoubleProperty x1, DoubleProperty y1,DoubleProperty x2, DoubleProperty y2) {
            super(x1.get(), y1.get(), 10);
            setFill(color.deriveColor(1, 1, 1, 0.5));
            setStroke(color);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);

            x1.bind(centerXProperty());
            y1.bind(centerYProperty());
            x2.bind(centerXProperty());
            y2.bind(centerYProperty());
            this.setVisible(true);//true
            enableDrag();
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final Delta dragDelta = new Delta();
            setOnMousePressed(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    // record a delta distance for the drag and drop operation.
                    dragDelta.x = getCenterX() - mouseEvent.getX();
                    dragDelta.y = getCenterY() - mouseEvent.getY();
                    getScene().setCursor(Cursor.MOVE);
                }
            });
            setOnMouseReleased(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    getScene().setCursor(Cursor.HAND);
                }
            });
            setOnMouseDragged(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    double newX = mouseEvent.getX() + dragDelta.x;
                    if (newX > 0 && newX < getScene().getWidth()) {
                        setCenterX(newX);
                    }
                    double newY = mouseEvent.getY() + dragDelta.y;
                    if (newY > 0 && newY < getScene().getHeight()) {
                        setCenterY(newY);
                    }

                    // update arrow positions
                    for( Arrow arrow: arrows) {
                        arrow.update();
                    }
                }
            });
            setOnMouseEntered(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.HAND);
                    }
                }
            });
            setOnMouseExited(new EventHandler<MouseEvent>() {
                @Override public void handle(MouseEvent mouseEvent) {
                    if (!mouseEvent.isPrimaryButtonDown()) {
                        getScene().setCursor(Cursor.DEFAULT);
                    }
                }
            });
        }

        // records relative x and y co-ordinates.
        private class Delta { double x, y; }
    }
}