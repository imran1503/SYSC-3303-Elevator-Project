package javafxGUI;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

public class GUI extends Application {
    private ArrayList<Floor> floors;
    private ArrayList<Elevator> elevators;
    private ArrayList<ElevatorPanel> panels;
    public static void main(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Elevator System");
        Pane layout = new Pane();
        Scene scene = new Scene(layout, 600, 800);
        primaryStage.setScene(scene);

        //initialize floors
        floors = new ArrayList<>();
        for(int i =0;i<22;i++){
            Floor floor = new Floor(20,650-i*30,30,350, i+1, layout);
            floors.add(floor);
        }

        //initialize elevators and elevator panels
        elevators = new ArrayList<>();
        panels = new ArrayList<>();
        for(int i =0;i<4;i++){
            Elevator elevator = new Elevator(50+i*90, layout);
            elevators.add(elevator);
            ElevatorPanel panel = new ElevatorPanel(i,420+i*50, 40,layout);
            panels.add(panel);
        }

        //initialize fault display
        TextArea fault = new TextArea();
        fault.setMaxWidth(350);
        fault.setMaxHeight(80);
        fault.setLayoutX(20);
        fault.setLayoutY(700);
        fault.setEditable(false);
        layout.getChildren().add(fault);

        Thread t = new Thread(new Runnable(){
            @Override
            public void run(){
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket(4000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                byte[] msg = new byte[100];
                DatagramPacket packet = new DatagramPacket(msg, msg.length);
                while (true) {
                    try {
                        socket.receive(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    updateStatus(msg, fault);
                }
            }
        });
        t.start();
        primaryStage.show();
    }


    public void updateStatus(byte[] msg, TextArea fault){
        //message is from elevator
        if(msg[0] == 0){
            updateElevator(msg);
        //message is from floor
        }else if (msg[0] == 1){
            updateFloor(msg);
        //message is from scheduler
        }else if(msg[0] == 2){
            displayFault(msg, fault);
        }

    }
    public void updateElevator(byte[] msg){
        for(int i =0;i<elevators.size();i++){
            elevators.get(i).setFloor(msg[i*24+1]);
            elevators.get(i).updateDoor(msg[i*24+1+1]);
            for(int j=0;j<22;j++){
                if(msg[i*24+3+j] == 1){
                    panels.get(i).pressButton(j);
                }else if(msg[i*24+3+j] == 0){
                    panels.get(i).clearButton(j);
                }
            }
        }
    }


    public void updateFloor(byte[] msg){
        for(int i =0;i<22;i++){
            if(msg[i*2+1]==1) {
                floors.get(i).up.turnOn();
            }else if(msg[i*2+1]==0){
                floors.get(i).up.turnOff();
            }
            if(msg[i*2+2] == 1){
                floors.get(i).down.turnOn();
            }else if(msg[i*2+2] == 0){
                floors.get(i).down.turnOff();
            }
        }
    }

    public void displayFault(byte[] msg, TextArea text){
        System.out.println("fault received");
        String fault = "";
        double time = msg[2];
        if(msg[1] == 1){
            fault = "Fault detected: Door is stuck open (" + time + "s) ";
        }else if(msg[1] == 2){
            fault = "Fault Detected: Took elevator too long to reach destination (" + time + "s) ";
        }else if(msg[1] == 3){
            fault = "Fault detected: Floor timer exceeded expected time. Elevator will now shut off (" + time + "s) ";
        }

        text.appendText(fault);
        text.appendText("\n");
    }
}

