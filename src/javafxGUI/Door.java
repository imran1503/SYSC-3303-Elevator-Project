package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Door {
    Rectangle leftDoor;
    Rectangle rightDoor;
    double x;
    double y;
    public Door(int x, int y, Pane layout){
        leftDoor = new Rectangle(x,y,10,26);
        leftDoor.setFill(Color.color(0.9,0.9,0.9));
        leftDoor.setStroke(Color.BLACK);
        rightDoor = new Rectangle(x+10,y,10,26);
        rightDoor.setFill(Color.color(0.9,0.9,0.9));
        rightDoor.setStroke(Color.BLACK);
        this.x = x;
        this.y = y;
        layout.getChildren().add(leftDoor);
        layout.getChildren().add(rightDoor);

    }

    public void open(){
        leftDoor.setX(x-10);
        rightDoor.setX(x+20);

    }

    public void close(){
        leftDoor.setX(x);
        rightDoor.setX(x+10);

    }

    public void setY(double y){
        this. y = y+2;
        leftDoor.setY(this.y);
        rightDoor.setY(this.y);
    }

}
