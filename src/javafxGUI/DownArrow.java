package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class DownArrow {
    Line line1, line2,line3;
    public DownArrow(double x, double y, Pane layout){
        line1 = new Line(x,y+15,x-5,y+10);
        line2 = new Line(x,y+15,x+5,y+10);
        line3 = new Line(x,y+15,x,y+5);
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
