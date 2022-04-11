package javafxGUI;

import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.ArrayList;

public class ElevatorPanel {
    Text title;
    ArrayList<ElevatorButton> buttons;
    public ElevatorPanel(int id, double x, double y, Pane layout){
        buttons = new ArrayList<>();
        for(int i =0;i<22;i++){
            ElevatorButton button = new ElevatorButton(i+1, id, x, y +30*i, layout);
            buttons.add(button);
        }
        title = new Text("E. No." + (id+1));
        title.setFont(Font.font(13));
        title.setX(x-20);
        title.setY(y-20);
        layout.getChildren().add(title);
    }


    public void pressButton(int floor){
        buttons.get(floor).lampOn();
    }

    public void clearButton(int floor){
        buttons.get(floor).lampOff();
    }
}
