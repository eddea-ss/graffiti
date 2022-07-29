package sample;

import javafx.scene.control.Control;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * It represents a non-deterministic finite automata.
 */
public class Afnd {
    private String alfabeto;
    private Nodo estadoInicial;
    private ArrayList<Nodo> estados;
    private ArrayList<Recorrido> recorrido;
    private boolean esAfd;

    public Afnd(String alfabeto) {
        this.alfabeto = alfabeto;
        this.estados = new ArrayList<>();
        this.estadoInicial = null;
        this.recorrido = new ArrayList<>();
        this.esAfd = false;
    }

    public Afnd() {
        //this.alfabeto = alfabeto;
        this.estados = new ArrayList<>();
        this.estadoInicial = null;
        this.recorrido = new ArrayList<>();
        this.esAfd = false;
    }

    public boolean getAfd() {
        return esAfd;
    }

    public void setAfd(boolean esAfd) {
        this.esAfd = esAfd;
    }

    public void setEstados(ArrayList<Nodo> estados) {
        this.estados = estados;
    }

    public String getAlfabeto() {
        return alfabeto;
    }

    public void setAlfabeto(String alfabeto) {
        this.alfabeto = alfabeto;
    }


    public Nodo getEstadoInicial() {
        return estadoInicial;
    }

    public void setEstadoInicial(Nodo estadoInicial) {//agregar
        this.estadoInicial = estadoInicial;
    }

    public void addEstado(Nodo nuevo) {
        this.estados.add(nuevo);
    }

    public boolean comprobarAlfabeto(char letra) {
        if (alfabeto.contains(Character.toString(letra))) {
            return true;
        }
        return false;
    }

    public boolean comprobarAlfabeto2(String palabra){
        palabra = palabra.trim();
        if(palabra != null && !comprobarPalabraVacia(palabra)) {
            return true;
        }else {
            if(alfabeto != null && alfabeto.length() != 0) {
                if (palabra != null && palabra.length() > 0) {
                    for (int i = 0; i < palabra.length(); i++) {
                        System.out.println(i);
                        if (!alfabeto.contains(Character.toString(palabra.charAt(i)))) {
                            return false;
                        }
                    }
                }
            }else{
                return false;
            }

        }

        return true;
    }

    /**
     * Funcion que consume una palabra y verifica si es valida
     * segun el lenguaje descrito por el automata
     *
     * @param palabra String con la palabra a verificar
     * @return true si la palabra es valida
     */
    public boolean comprobarPalabra(String palabra) {
        char letra;
        String aux = palabra;
        Nodo estadoActual = new Nodo();

        letra = palabra.charAt(0);
        aux = aux.substring(1);

        if (comprobarAlfabeto(letra)) {
            if (estadoInicial != null && estadoInicial.comprobarTransicion(letra)) {
                estadoInicial.getIndiceTransicion(letra);
                estadoActual = estadoInicial.getEstadoLlegada(letra);
                System.out.println("esta aqui: " + estadoActual.getEstado());

                while (!aux.trim().isEmpty()) {
                    letra = aux.charAt(0);
                    aux = aux.substring(1);

                    if (estadoActual.comprobarTransicion(letra)) {
                        estadoActual.getIndiceTransicion(letra);
                        estadoActual = estadoActual.getEstadoLlegada(letra);
                        System.out.println("esta aqui: " + estadoActual.getEstado());
                    } else {
                        return false;
                    }
                }
            }
        }

        if (estadoActual.getEsFinal()) {
            return true;
        }
        return false;
    }

    /**
     * Funcion que consume una palabra y verifica si es valida
     * segun el lenguaje descrito por el automata
     *
     * @param palabra String con la palabra a verificar
     * @return true si la palabra es valida
     */

    /*
    public boolean comprobarPalabra2(String palabra){
        ArrayList<String> colaPalabras = new ArrayList<>();
        ArrayList<Nodo> colaNodos = new ArrayList<>();
        char letra;
        String aux, palabraActual;
        Nodo estadoActual = new Nodo();

        letra = palabra.charAt(0);
        aux = palabra.substring(1);
        if(comprobarAlfabeto(letra)){
            estadoActual = estadoInicial;
            palabraActual = aux;
            for(Transicion i : estadoActual.getTransiciones()){
                if(i.getTransiciones().contains(letra)){
                    colaNodos.add(i.getEstadoLlegada());
                    colaPalabras.add(aux);
                }
            }

            while(!aux.trim().isEmpty()){
                letra = aux.charAt(0);
                aux = aux.substring(1);
                while(colaPalabras.contains(palabraActual) && palabraActual != ""){
                    //System.out.println(colaPalabras);
                    for(Transicion i : colaNodos.get(colaPalabras.indexOf(palabraActual)).getTransiciones()){
                        if(i.getTransiciones().contains(letra) && comprobarAlfabeto(letra)){
                            colaNodos.add(i.getEstadoLlegada());
                            colaPalabras.add(aux);

                        }
                    }

                    colaNodos.remove(colaPalabras.indexOf(palabraActual));
                    colaPalabras.remove(colaPalabras.indexOf(palabraActual));
                }
                System.out.println(palabraActual+"!!");
                palabraActual = aux;
                System.out.println(colaPalabras+"???");
            }
        }
        System.out.println(colaPalabras);
        for(Nodo j : colaNodos){
            System.out.println(j.getEstado()+"???");
            if(j.getEsFinal()){
                return true;
            }
        }
        return false;
    }
*/

    public boolean comprobarPalabra2(String palabra){
        ArrayList<Nodo> colaN = new ArrayList<>();
        ArrayList<Nodo> colaA = new ArrayList<>();
        ArrayList<String> colaP = new ArrayList<>();
        ArrayList<Integer> colaC = new ArrayList<>();
        int cont = 0;

        if(recorrido != null && !recorrido.isEmpty()) {
            recorrido.clear();
        }

        if(!comprobarPalabraVacia(palabra)){
            if(estadoInicial != null) {
                if(estadoInicial.getEsFinal()){
                    return true;
                }

                for (Transicion i : estadoInicial.getTransiciones()) {
                    if (i.getTransiciones().contains('_')) {
                        colaA.add(estadoInicial);
                        colaN.add(i.getEstadoLlegada());
                    }
                }
            }
            while(!colaN.isEmpty()){
                if(!colaN.isEmpty()) {
                    for (Transicion i : colaN.get(0).getTransiciones()) {
                        System.out.println("Estoy en: "+colaN.get(0).getEstado());
                        if (i.getTransiciones().contains('_')) {
                            if (!colaA.get(0).getEstado().equals(i.getEstadoLlegada().getEstado())) {
                                colaA.add(colaN.get(0));
                                colaN.add(i.getEstadoLlegada());
                            }
                        }
                    }
                    if (colaN.get(0).getEsFinal() || colaA.get(0).getEsFinal()) {
                        return true;
                    }
                    colaN.remove(0);
                    colaA.remove(0);
                    cont++;
                    if(cont > 10000){
                        return false;
                    }
                }
            }
        }else{
            palabra = palabra.replace("_","");
            if(estadoInicial!=null) {
                for (Transicion i : estadoInicial.getTransiciones()) {
                    if (i.getTransiciones().contains('_')) {
                        colaA.add(estadoInicial);
                        colaN.add(i.getEstadoLlegada());
                        colaP.add(palabra);
                        colaC.add(0);
                    }

                    if (i.getTransiciones().contains(palabra.charAt(0))) {
                        colaA.add(estadoInicial);
                        colaN.add(i.getEstadoLlegada());
                        colaP.add(palabra.substring(1));
                        colaC.add(0);
                        recorrido.add(new Recorrido(estadoInicial,i.getEstadoLlegada(),palabra.charAt(0)));
                    }
                }
            }

            while(!colaN.isEmpty()){
                if(!colaN.isEmpty()){

                        for(Transicion i : colaN.get(0).getTransiciones()) {
                            System.out.println("Estoy en: "+colaN.get(0).getEstado()+" | con: "+colaP.get(0)+" | desde: "+colaA.get(0).getEstado()+" | cont: "+colaC.get(0));
                            if(i.getTransiciones().contains('_')) {
                                if(colaA.get(0).getEstado().equals(i.getEstadoLlegada().getEstado())) {
                                    if(colaC.get(0) < 5) {
                                        colaC.set(0, colaC.get(0)+1);
                                        colaA.add(colaN.get(0));
                                        colaN.add(i.getEstadoLlegada());
                                        colaP.add(colaP.get(0));
                                        colaC.add(colaC.get(0));
                                    }
                                }else{
                                    colaA.add(colaN.get(0));
                                    colaN.add(i.getEstadoLlegada());
                                    colaP.add(colaP.get(0));
                                    colaC.add(colaC.get(0));
                                }
                            }

                            if(!colaP.get(0).trim().isEmpty()) {
                                if(i.getTransiciones().contains(colaP.get(0).charAt(0))) {
                                    colaA.add(colaN.get(0));
                                    colaN.add(i.getEstadoLlegada());
                                    colaP.add(colaP.get(0).substring(1));
                                    colaC.add(colaC.get(0));
                                    recorrido.add(new Recorrido(colaN.get(0),i.getEstadoLlegada(),colaP.get(0).charAt(0)));
                                }
                            }
                        }

                        if(colaP.get(0).isEmpty()){
                            if(colaN.get(0).getEsFinal()){
                                return true;
                            }
                        }
                        colaP.remove(0);
                        colaN.remove(0);
                        colaA.remove(0);
                        colaC.remove(0);
                        cont++;
                        if(cont > 10000){
                            return false;
                        }
                    System.out.println(cont);
                }
            }

        }
        return false;
    }

    /**
     * verifica si la palabra esta compuesta solamente por '_'
     * @param palabra
     * @return
     */
    public boolean comprobarPalabraVacia(String palabra){
        if(palabra.replace("_","").length() > 0){
            return true;
        }
        return false;
    }

    public void printTransiciones(){
        System.out.println(estados.size());
    }

    public String palabraMasCorta (){
        ArrayList<String> colaP = new ArrayList<>();
        ArrayList<String> palabras = new ArrayList<>();
        ArrayList<Nodo> colaN = new ArrayList<>();
        ArrayList<String> colaA = new ArrayList<>();
        ArrayList<Integer> colaC = new ArrayList<>();
        ArrayList<String> visitados = new ArrayList<>();
        int min = 0;

        if(estadoInicial != null && estadoInicial.getEsFinal()){
            return " Es la palabra vacia (_).";
        }

        if(estadoInicial != null && existeFinal()){
                visitados.add(estadoInicial.getEstado());

                for(Transicion i : estadoInicial.getTransiciones()){
                    if(!i.getEstadoLlegada().equals(estadoInicial.getEstado())) {
                        if (i.getTransiciones().contains('_')) {
                            colaN.add(i.getEstadoLlegada());
                            colaP.add("");
                            colaA.add(estadoInicial.getEstado());
                            colaC.add(0);
                            visitados.add(i.getEstadoLlegada().getEstado());
                        } else {
                            for (Character j : i.getTransiciones()) {
                                colaN.add(i.getEstadoLlegada());
                                colaP.add("" + j);
                                colaA.add(estadoInicial.getEstado());
                                colaC.add(0);
                                visitados.add(i.getEstadoLlegada().getEstado());
                            }
                        }
                    }
                }

                while(!colaN.isEmpty()){
                    if(!colaN.isEmpty()){

                        if(colaN.get(0).getEsFinal()) {
                            palabras.add(colaP.get(0));
                            System.out.println(palabras);
                            colaN.remove(0);
                            colaP.remove(0);
                            colaA.remove(0);
                            colaC.remove(0);
                        }else{
                            for(Transicion i : colaN.get(0).getTransiciones()){
                                if(!colaA.get(0).equals(i.getEstadoLlegada().getEstado())){
                                    if(visitados.contains(i.getEstadoLlegada().getEstado())) {
                                        if(colaC.get(0) < 5) {
                                            if (i.getTransiciones().contains('_')) {
                                                colaC.set(0, colaC.get(0)+1);
                                                colaN.add(i.getEstadoLlegada());
                                                colaP.add(colaP.get(0));
                                                colaA.add(colaN.get(0).getEstado());
                                                colaC.add(colaC.get(0));
                                            } else {
                                                for (Character j : i.getTransiciones()) {
                                                    colaC.set(0, colaC.get(0)+1);
                                                    colaN.add(i.getEstadoLlegada());
                                                    colaP.add(colaP.get(0) + j);
                                                    colaA.add(colaN.get(0).getEstado());
                                                    colaC.add(colaC.get(0));
                                                }
                                            }
                                        }
                                    }else{
                                        if (i.getTransiciones().contains('_')) {
                                            colaN.add(i.getEstadoLlegada());
                                            colaP.add(colaP.get(0));
                                            colaA.add(colaN.get(0).getEstado());
                                            colaC.add(colaC.get(0));
                                        } else {
                                            for (Character j : i.getTransiciones()) {
                                                colaN.add(i.getEstadoLlegada());
                                                colaP.add(colaP.get(0) + j);
                                                colaA.add(colaN.get(0).getEstado());
                                                colaC.add(colaC.get(0));
                                            }
                                        }
                                    }
                                }
                            }
                            colaN.remove(0);
                            colaP.remove(0);
                            colaA.remove(0);
                            colaC.remove(0);
                        }
                    }
                }
                min = palabras.get(0).length();
                for(String i : palabras){
                    if(i.length() < min){
                        min = i.length();
                    }
                }

                if(min == 0){
                    return " Es la palabra vacia (_).";
                }

                for(String i : palabras){
                    if(i.length() == min){
                        return i;
                    }
                }

        }else{
            return " El autómata no es válido";
        }

        return " No existe un camino a un estado final.";
    }


    /**
     * Muerstra los nodos y sus transiciones por consola.
     */
    public void matrizTransiciones(){
        ArrayList<Transicion> lista = new ArrayList<>();
        lista = estadoInicial.getTransiciones();
        System.out.print(estadoInicial.getEstado()+": ");
        for(Transicion i : lista){
            System.out.print(i.getEstadoLlegada().getEstado()+"; ");
        }
        System.out.println();
        for(Nodo j : estados){
            lista = j.getTransiciones();
            System.out.print(j.getEstado()+": ");
            for(Transicion k : lista){
                System.out.print(k.getEstadoLlegada().getEstado()+"; ");
            }
            System.out.println();
        }
    }

    /**
     * Funcion usada para saber si existe algun nodo final o no
     * @return true si es que existe
     */
    public boolean existeFinal(){
        for(Nodo i : estados){
            if(i.getEsFinal()){
                return true;
            }
        }
        return false;
    }

    /**
     * Funcion que comprueba la integridad del AFND
     * @return retorna true si es que esta correcto
     */
    public boolean comprobarAutomata(){
        ArrayList<String> visitados = new ArrayList<>();
        ArrayList<Nodo> cola = new ArrayList<>();
        Nodo revisando = new Nodo();

        if(estadoInicial !=null&&estadoInicial.getEsFinal()){
            return true;
        }

        if(estadoInicial != null && existeFinal()){
            cola.add(estadoInicial);
            visitados.add(estadoInicial.getEstado());

            while(!cola.isEmpty()){
                revisando = cola.get(0);
                cola.remove(0);
                if(revisando.getEsFinal()){
                    return true;
                }

                for(Transicion i : revisando.getTransiciones()){
                    if(!visitados.contains(i.getEstadoLlegada().getEstado())){
                        visitados.add(i.getEstadoLlegada().getEstado());
                        cola.add(i.getEstadoLlegada());
                    }
                }
            }
        }

        return false;
    }

    /**
     * Asigna una un alfabeto de caracteres (de uno en uno) desde una lista de String[]
     * con solo un caracter por posición en el arreglo.
     * @param alphabetValid un arreglo de String de solo una letra cada uno.
     */
    public void setAlfabeto(String[] alphabetValid) {

        String alphabet = new String("");
        for (int i = 0; i < alphabetValid.length; i++) {
            String s = alphabetValid[i];
            alphabet = alphabet + s;
        }
        this.alfabeto = alphabet;
    }

    /**
     * Retorna una lista con los estados
     * @return ArrayList<Nodo>
     */


    public ArrayList<Nodo> getEstados2(){
        ArrayList<Nodo> aux = this.estados;
        return aux;
    }

    public ArrayList<Nodo> getEstados(){
        return this.estados;
    }

    /*public ArrayList<String> getArrayEstados() {

        ArrayList<String> transitions = new ArrayList<>();

        if (estadoInicial != null) {
            transitions.add(estadoInicial.getEstado());
        }

        for (Nodo nodo : this.estados){

            String id = new String(nodo.getEstado());

            for (Transicion t : nodo.getTransiciones()) {
                id = id + " " + t.getTransiciones();
            }

            transitions.add(id);

        };

        return transitions;
    }*/
    /*
    public ArrayList<String> getArrayEstados(){
        ArrayList<String> lista = new ArrayList<>();
        String aux;
        String aux2;

        if(this.estadoInicial != null) {
            aux = this.estadoInicial.getEstado() + "=";
            for (Transicion i : this.estadoInicial.getTransiciones()) {
                aux2 = "(";
                for(Character j : i.getTransiciones()){
                    aux2 += j+",";
                }
                aux2 += ")";
                aux += " " + aux2 + i.getEstadoLlegada().getEstado() + " ;";
                aux2 = "";
            }
            lista.add(aux);
            aux = "";
        }

        if(this.estados != null) {
            for (Nodo i : this.estados) {
                aux = i.getEstado() + "= ";
                for (Transicion j : i.getTransiciones()) {
                    aux += " " + j.getTransiciones() + "(" + j.getEstadoLlegada().getEstado() + ") ;";
                }
                lista.add(aux);
                aux = "";
            }
        }
        return lista;
    }*/

    /**
     *      *
     * Creates Strings with the nodes and it's transitions 
     * @return
     */
    public ArrayList<String> getArrayEstados() {
        ArrayList<String> lista = new ArrayList<>();
        StringBuilder aux = new StringBuilder();

        if (this.estadoInicial != null) {
            for (Transicion i : this.estadoInicial.getTransiciones()) {
                for(Character j : i.getTransiciones()) {
                    aux.append("f"+" ("+this.estadoInicial.getEstado());
                    aux.append("," + j);
                    aux.append(") = " + i.getEstadoLlegada().getEstado());
                    lista.add(aux.toString());
                    aux.setLength(0);
                    aux.trimToSize();
                }
            }
        }

        // todo Cuando se agrega un nodo, la matriz de transiciones debe incluirlo con llegada a Vacio. ??????
        if (this.estados != null) {
            for (Nodo i : this.estados) {
                for(Transicion j : i.getTransiciones()){

                    for(Character k : j.getTransiciones()){
                        aux.append("f"+" ("+i.getEstado());
                        aux.append("," + k);
                        aux.append(") =  "+j.getEstadoLlegada().getEstado());
                        lista.add(aux.toString());
                        aux.setLength(0);
                        aux.trimToSize();
                    }
                }
            }
        }
        return lista;
    }

    public boolean removeNode(Nodo nodo){

        if (nodo.isEsInitial()){
            estadoInicial = null;
            return true;
        }

        if (nodo.equals(estadoInicial)){
            estadoInicial = null;
            return true;
        }

        return estados.remove(nodo);
    }

    public boolean removeAll(ArrayList<Transicion> borrarList) {
        for (Transicion transicion : borrarList) {
            estados.remove(transicion);
        }
        return true;
    }

    public boolean existeNodo(String nodo){
        if(nodo==null){
            return false;
        }else {
            nodo= nodo.replace(" ","");
            nodo= nodo.replace(",","");
        }
        if(estadoInicial!=null){
            if(estadoInicial.getEstado().equalsIgnoreCase(nodo))
                return true;
        }
            for (Nodo temp_nodo: this.getEstados()){
                if(temp_nodo.getEstado().equalsIgnoreCase(nodo))
                    return true;
            }

        return false;
    }

    public void printRecorrido(){
        if(recorrido!=null){
            for (Recorrido i : recorrido){
                System.out.println(i.getInicio().getEstado()+" | "+i.getLlegada().getEstado()+"  |  con: "+i.getTransicion());
            }
        }
    }

    public ArrayList<Recorrido> getRecorrido() {
        return recorrido;
    }
}
