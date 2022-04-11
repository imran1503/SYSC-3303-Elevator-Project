package javafxGUI;

import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Elevator extends Group {
    private Door door;
    private Rectangle elevator;

    public Elevator(int x, Pane layout){
        elevator = new Rectangle();
        elevator.setX(x);
        elevator.setY(650);
        elevator.setHeight(30);
        elevator.setWidth(20);
        elevator.setFill(Color.color(0.8,0.8,0.8));
        elevator.setStroke(Color.BLACK);
        layout.getChildren().add(elevator);
        door = new Door(x,652, layout);
    }

    public void setFloor(int floor){
        elevator.setY(650 - (floor-1)*30);
        door.setY(650 - (floor-1)*30);
    }

    public void updateDoor(int open){
        if(open == 1){
            door.open();
        }else{
            door.close();
        }
    }
}
