package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class UpArrow {
    Line line1, line2,line3;
    public UpArrow(double x, double y, Pane layout){
        line1 = new Line(x,y,x-5,y+5);
        line2 = new Line(x,y,x+5,y+5);
        line3 = new Line(x,y,x,y+10);
        line1.setStroke(Color.GRAY);
        line2.setStroke(Color.GRAY);
        line3.setStroke(Color.GRAY);
        layout.getChildren().addAll(line1,line2,line3);
    }

    public void turnOn(){
        line1.setStroke(Color.RED);
        line2.setStroke(Color.RED);
        line3.setStroke(Color.RED);
    }

    public void turnOff(){
        line1.setStroke(Color.GRAY);
        line2.setStroke(Color.GRAY);
        line3.setStroke(Color.GRAY);
    }
}
