
package sample;

import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.StrokeType;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class Transicion {
    //atributos para la interfaz
    List<Arrow> arrows = new ArrayList<Arrow>();
    private CubicCurve curve;
    private Label caracteres;
    private Anchor anchor;
    //atributos para backend
    private ArrayList<Character> transiciones;
    private Nodo estadoLlegada;

    public Transicion(Nodo llegada){
        this.transiciones = new ArrayList<>();
        this.estadoLlegada = llegada;
    }

    public Transicion(Nodo llegada, String nameOfTheTransition,CubicCurve curve) {
        this.transiciones = new ArrayList<>();
        this.estadoLlegada = llegada;
        transiciones = setListaTransiciones(nameOfTheTransition);
        this.curve= curve;
        this.anchor = new Anchor("LIGHTBLUE",curve.controlX1Property(), curve.controlY1Property(),curve.controlX2Property(), curve.controlY2Property(),nameOfTheTransition);

        double[] arrowShape = new double[] { 0,0,8,16,-8,16 }; //forma de la flechas
        arrows.add( new Arrow( curve, 0.96f, arrowShape));
    }

    public Transicion(Nodo llegada, String nameOfTheTransition,CubicCurve curve,boolean boll) {
        this.transiciones = new ArrayList<>();
        this.estadoLlegada = llegada;
        transiciones = setListaTransiciones(nameOfTheTransition);
        this.curve= curve;
        this.anchor = new Anchor("LIGHTBLUE",curve.controlX1Property(), curve.controlY1Property(),curve.controlX2Property(), curve.controlY2Property(),nameOfTheTransition,boll);

        double[] arrowShape = new double[] { 0,0,8,16,-8,16 };//forma de la flechas
        arrows.add( new Arrow( curve, 0.96f, arrowShape));
    }


    public ArrayList<Character> getTransiciones(){
        return transiciones;
    }

    public void setTransiciones(ArrayList<Character> transiciones) {
        this.transiciones = transiciones;
    }


    /**
     * Agrega un caracter a la lista de transiciones
     * en caso de existir en el arreglo no lo agrega
     * @param transicion (caracter) a agregar a la lista
     */
    public void addTransicion(char transicion){
        if(!existeTransicion(transicion)) {
            transiciones.add(transicion);
        }else{
            System.out.println("Alert Type N°1: ¡No se ingreso la transicion: "+transicion+" ,porque ya existe!");
        }
    }

    /**
     * Agrega los caracteres de la lista a la lista de transiciones
     * si existen algunos caracteres solo agregara los que no esten
     * @param transiciones lista de caracteres a agregar
     */
    public void addTransicion(char[] transiciones){
        for(Character c_temp: transiciones){
            if(!existeTransicion(c_temp)) {
                addTransicion(c_temp);
            }
        }
    }

    public boolean existeTransicion(char nueva){
        for(Character c: transiciones){
            if(c.equals(nueva)){
                return true;
            }
        }
        return false;
    }



    public Nodo getEstadoLlegada(){
        return estadoLlegada;
    }

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

            setFill(Color.BLACK);

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

    // a draggable anchor displayed around a point.
    class Anchor extends Circle {

        Anchor(String color, DoubleProperty x1, DoubleProperty y1,DoubleProperty x2, DoubleProperty y2,String letrasFondo) {
            super(x1.get(), y1.get(), 15);
            setFill(new ImagePattern(textToImage(letrasFondo,color)));
            setStroke(Color.LIGHTBLUE);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);

            x1.bind(centerXProperty());
            y1.bind(centerYProperty());
            x2.bind(centerXProperty());
            y2.bind(centerYProperty());
            this.setVisible(true);//true
            enableDrag();
        }

        Anchor(String color, DoubleProperty x1, DoubleProperty y1,DoubleProperty x2, DoubleProperty y2,String letrasFondo,boolean bool) {
            super(x1.get(), y1.get(), 15);
            setFill(new ImagePattern(textToImage(letrasFondo,color)));
            setStroke(Color.LIGHTBLUE);
            setStrokeWidth(2);
            setStrokeType(StrokeType.OUTSIDE);

            x1.bind(centerXProperty());
            y1.bind(centerYProperty().subtract(70d));
            x2.bind(centerXProperty().add(70d));
            y2.bind(centerYProperty().add(70d));
            this.setVisible(true);//true
            enableDrag();
        }

        // make a node movable by dragging it around with the mouse.
        private void enableDrag() {
            final Transicion.Anchor.Delta dragDelta = new Transicion.Anchor.Delta();
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


    public List<Arrow> getArrows() {
        return arrows;
    }

    public Anchor getAnchor() {
        return anchor;
    }

    public void setArrows(List<Arrow> arrows) {
        this.arrows = arrows;
    }

    public void setAnchor(Anchor anchor) {
        this.anchor = anchor;
    }

    public CubicCurve getCurve() {
        return curve;
    }

    public void updateAllArrows(){
        for (Arrow arrow: arrows){
            arrow.update();
        }
    }

    private Image textToImage(String text, String color) {
        Label label = new Label(text);
        label.setAlignment(Pos.CENTER);
        label.setMinSize(30, 30);
        label.setMaxSize(30, 30);
        label.setPrefSize(30, 30);
        label.setStyle("-fx-background-color: "+color+"; -fx-text-fill:black;");
        label.setWrapText(true);
        Scene scene = new Scene(new Group(label));
        WritableImage img = new WritableImage(30, 30) ;
        scene.snapshot(img);
        return img ;
    }

    public void updateAnchor(){
        this.anchor.setFill(new ImagePattern(textToImage(wordToDisplayInTransition(),"LIGHTBLUE")));
    }

    public String wordToDisplayInTransition(){
        String str="";
        int index=0;
        for(Character c_temp:transiciones ) {
            str = str + "" + c_temp;
            if (index != transiciones.size() - 1) {
                str = str + ",";
            }
            index++;
        }
        return str;
    }

    /**
     * setea el array de transiciones.
     * @param transiciones
     * @return
     */
    public ArrayList<Character> setListaTransiciones(String transiciones){
        transiciones= transiciones.replace(" ","");
        transiciones= transiciones.replace(",","");
        ArrayList<Character> lista = new ArrayList<>();
        for(int i = 0; i < transiciones.length(); i++){
            if(!existeTransicion(transiciones.charAt(i))) {
                lista.add(transiciones.charAt(i));
            }
        }
        return lista;
    }
}
