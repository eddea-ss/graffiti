package sample;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.event.Event;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.GregorianCalendar;


/**
 * Created by Freyja on 20-04-2017.
 * clase para dibujar, aqui se mostraran los automatas
 */
    public class Lienzo extends GridPane implements  EventHandler<Event>{


    public Lienzo(){
        for (int i=0;i<100;i++) {
            for (int j=0;j<100;j++) {
                this.add(createPane(),i,j);
            }
        }
        StackPane pane= (StackPane) this.getNode(0,0);

        ;

    }

    @Override
    public void handle(Event event) {
    }

    public StackPane createPane(){
        StackPane pane= new StackPane();
        pane.setMaxSize(30,30);
        pane.setMinSize(30,30);
        pane.getChildren().addAll(new Circle(5, Color.BLACK));
        pane.setAlignment(Pos.CENTER);
        return pane;
    }

    public Node getNode (final int row, final int column) {
        Node result = null;
        ObservableList<Node> childrens = this.getChildren();

        for (Node node : childrens) {
            if(this.getRowIndex(node) == row && this.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
