package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Floor {
    Rectangle floor;
    Text text;
    UpArrow up;
    DownArrow down;

    public Floor(double x,double y, double height, double width, int floorNub, Pane layout){
        floor = new Rectangle();
        text = new Text("F"+floorNub);
        text.setFont(Font.font(20));
        text.setX(x + width/2 - 10);
        text.setY(y+24);
        text.setFill(Color.GRAY);
        floor.setX(x);
        floor.setY(y);
        floor.setHeight(height);
        floor.setWidth(width);
        floor.setFill(Color.WHITE);
        floor.setStroke(Color.BLACK);

        layout.getChildren().add(floor);
        layout.getChildren().add(text);
        up = new UpArrow(x+85,y+10,layout);
        down = new DownArrow(x+265,y+5,layout);
    }
}
