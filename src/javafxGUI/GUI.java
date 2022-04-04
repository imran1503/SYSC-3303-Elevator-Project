package javafxGUI;

import javafx.application.Application;
import javafx.scene.Scene;
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
                    updateStatus(msg);
                }
            }
        });
        t.start();
        primaryStage.show();
    }


    public void updateStatus(byte[] msg){
        if(msg[0] == 0){
            updateElevator(msg);
        }else{
            updateFloor(msg);
        }

    }
    public void updateElevator(byte[] msg){
        for(int i =0;i<elevators.size();i++){
            elevators.get(i).setFloor(msg[i*24+1]);
            elevators.get(i).updateDoor(msg[i*24+1+1]);
            for(int j=0;j<2;j++){
                if(msg[i*24+3+j] == 1){
                    panels.get(i).pressButton(j);
                }else if(msg[i*24+3+j] == 0){
                    panels.get(i).pressButton(j);
                }
            }
        }
    }
    public void updateFloor(byte[] msg){
        for(int i =0;i<22;i++){
            if(msg[i*2+1]==1) {
                floors.get(i).up.turnOn();
            }else{
                floors.get(i).up.turnOff();
            }
            if(msg[i*2+2] == 1){
                floors.get(i).down.turnOn();
            }else{
                floors.get(i).down.turnOff();
            }
        }
    }
}

