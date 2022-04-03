import Elevator_Subsystem.*;
import Floor_Subsystem.*;
import Scheduler.*;

import java.io.*;
import java.net.SocketException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws FileNotFoundException, SocketException {
        int amountOfFloorsinBuilding = 5;

        ArrayList<Elevator> elevators = new ArrayList<>();
        ElevatorSubsystem elevatorSS = new ElevatorSubsystem(elevators);
        //create elevator parts
        ArrayList<ElevatorLamp> elevatorLamps = new ArrayList<>();
        ArrayList<ElevatorButton> elevatorButtons = new ArrayList<>();
        ElevatorDoor elevatorDoor = new ElevatorDoor();
        ElevatorMotor elevatorMotor = new ElevatorMotor(0.29947, 0.0032,0.50);
        ArrayList<Floor> floors = new ArrayList<Floor>();
        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
            ElevatorLamp temp = new ElevatorLamp(i);
            ElevatorLamp temp2 = new ElevatorLamp(i);    // Showing dir(elevator) up/down
            elevatorLamps.add(temp);
            elevatorLamps.add(temp2);



            ElevatorButton tempButton = new ElevatorButton(i);
            elevatorButtons.add(tempButton);


        }

        Scheduler scheduler = new Scheduler();
        Elevator elevator = new Elevator(0,3);
        elevators.add(elevator);


        //create floor parts
        ArrayList<FloorLamp> floorLamps = new ArrayList<>();
        ArrayList<FloorButton> floorButtons = new ArrayList<>();
        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();


        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
            FloorLamp templamp1 = new FloorLamp(i);
            floorLamps.add(templamp1);
            if (i==0){
               FloorButton tempButtonUp = new FloorButton(i,1);
               for(int j=0;j<elevators.size();j++) {
                   ArrivalSensor sensor = new ArrivalSensor(elevators.get(0).getId(), i,j);
                   arrivalSensors.add(sensor);
               }
               floorButtons.add(tempButtonUp);
            }
            else if (i == amountOfFloorsinBuilding-1){
                FloorButton tempButtonDown = new FloorButton(i,0);
                floorButtons.add(tempButtonDown);
                for(int j=0;j<elevators.size();j++) {
                   ArrivalSensor sensor = new ArrivalSensor(elevators.get(0).getId(), i,j);
                   arrivalSensors.add(sensor);
                }

            }
            else {
                FloorButton tempButtonUp = new FloorButton(i, 1);
                FloorButton tempButtonDown = new FloorButton(i, 0);
                for(int j=0;j<elevators.size();j++) {
                    ArrivalSensor sensor = new ArrivalSensor(elevators.get(0).getId(), i,j);
                    arrivalSensors.add(sensor);
                }
                floorButtons.add(tempButtonUp);
                floorButtons.add(tempButtonDown);
            }
            for (int j = 0; j < elevators.size(); j++) {

            }
        }

        File file1 = new File("src/events.txt");
        BufferedReader file = new BufferedReader( new FileReader(file1));

        ArrayList<Floor> floorsArrayList = new ArrayList<>();

        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
            Floor floor = new Floor(i, floorLamps, amountOfFloorsinBuilding, arrivalSensors );
            floorsArrayList.add(floor);
        }

        FloorSubsystem floorSS = null;
        floorSS = new FloorSubsystem(floorsArrayList, file);
        floorSS.start();
        scheduler.start();
        elevatorSS.start();

    }


}
