package sample;

/**
 * Created by Miguel on 01-07-2017.
 */
public class Recorrido {
    private Nodo inicio;
    private Nodo llegada;
    private Character transicion;

    public Recorrido(Nodo inicio, Nodo llegada, Character transicion){
        this.inicio = inicio;
        this.llegada = llegada;
        this.transicion = transicion;
    }

    public Nodo getInicio() {
        return inicio;
    }

    public void setInicio(Nodo inicio) {
        this.inicio = inicio;
    }

    public Nodo getLlegada() {
        return llegada;
    }

    public void setLlegada(Nodo llegada) {
        this.llegada = llegada;
    }

    public Character getTransicion() {
        return transicion;
    }

    public void setTransicion(Character transicion) {
        this.transicion = transicion;
    }
}
