package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ElevatorButton {
    private Circle circle;
    private int floor;
    private int id;
    private Text text;
    public ElevatorButton(int floor, int id,double x, double y, Pane layout){
        this.floor = floor;
        this.id = id;
        circle = new Circle(x,y,10, Color.GRAY);
        circle.setStroke(Color.BLACK);
        text = new Text(Integer.toString(floor));
        if(floor >= 10){
            text.setX(x-4);
        }else{
            text.setX(x-2);
        }
        text.setY(y+3);

        text.setStroke(Color.WHITE);
        text.setFont(Font.font(8));
        layout.getChildren().add(circle);
        layout.getChildren().add(text);
    }

    public void lampOn(){
        circle.setFill(Color.WHITE);
        text.setStroke(Color.BLACK);
    }

    public void lampOff(){
        circle.setFill(Color.GRAY);
        text.setStroke(Color.WHITE);
    }
}
