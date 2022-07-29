package sample;

import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;

import java.util.ArrayList;

/**
 *
 */
public class Nodo extends Circle {
    private String estado;
    private boolean esFinal;
    private boolean esInitial; //posiblemente necesario;
    private boolean esSumidero=false;
    private Polygon forInitial;
    private ArrayList<Transicion> transiciones;


    public Nodo(){
        this.estado = super.getId();
        this.esFinal = false;
        this.esInitial= false;
        this.transiciones = new ArrayList<>();
    }

    public Nodo(double x, double y){
        super(x, y, 20, Color.LIGHTGRAY);
        this.estado = super.getId();
        this.estado = "hello node!";
        forInitial= new Polygon(new double[]{(double)(x-30),(double)(y+10),(double)(x-20),(double)(y),(double)(x-30),(double)(y-10)});
        forInitial.setVisible(true);
        this.estado ="";
        this.esFinal = false;
        this.transiciones = new ArrayList<>();
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean getEsFinal() {
        return esFinal;
    }

    public void setEsFinal(boolean esFinal) {
        this.esFinal = esFinal;
    }

    public void addTransicion(Transicion nuevo){
        this.transiciones.add(nuevo);
    }

    public boolean comprobarTransicion(char aux){
        for(Transicion i : transiciones){
            if(i.getTransiciones().contains(aux)){
                return true;
            }
        }
        return false;
    }

    public int getSize(){
        return transiciones.size();
    }

    public int getIndiceTransicion(char aux){
        for(int i = 0; i < transiciones.size(); i++){
            if(transiciones.get(i).getTransiciones().contains(aux)){
                return i;
            }
        }
        return -1;
    }

    public ArrayList<Transicion> getTransiciones(){
        return transiciones;
    }

    public void setTransiciones(ArrayList<Transicion> transiciones) {
        this.transiciones = transiciones;
    }

    public boolean removeThisInTransition(Transicion transicion){
        return this.transiciones.remove(transicion);

    }

    public Nodo getEstadoLlegada(char aux){
        return transiciones.get(getIndiceTransicion(aux)).getEstadoLlegada();
    }

    public boolean isEsInitial() {
        return esInitial;
    }

    public void setEsInitial(boolean esInitial) {
        this.esInitial = esInitial;
    }

    public Polygon getForInitial() {
        return forInitial;
    }

    public void setForInitial(Polygon forInitial) {
        this.forInitial = forInitial;
    }

    public  void update(){
        for (Transicion t: transiciones){
            t.updateAllArrows();
        }
    }

    /**
     * comprueba si el estado tiene transiciones vacias.
     * @return
     */
    public boolean comprobarTransicionVacia(){
        for(Transicion i : this.transiciones){
            if(i.getTransiciones().contains('_')){
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Nodo{" +
                "estado='" + estado + '\'' +
                ", transiciones=" + transiciones +
                '}';
    }

    public boolean isEsSumidero() {
        return esSumidero;
    }

    public void setEsSumidero(boolean esSumidero) {
        this.esSumidero = esSumidero;
    }
}
