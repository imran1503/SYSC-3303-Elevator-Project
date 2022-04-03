package Scheduler;

import Elevator_Subsystem.*;
import Floor_Subsystem.ArrivalSensor;
import Floor_Subsystem.Floor;
import Floor_Subsystem.FloorButton;
import Floor_Subsystem.FloorLamp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class SchedulerStateTest {

    
    public void change() throws FileNotFoundException {
        ArrayList<FloorLamp> floorLamps = new ArrayList<>();
        ArrayList<FloorButton> floorButtons = new ArrayList<>();
        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();
        ArrayList<ElevatorLamp> elevatorLamps = new ArrayList<>();
        ArrayList<ElevatorButton> elevatorButtons = new ArrayList<>();


        for (int i = 0; i < 22; i++) {
            ElevatorLamp temp = new ElevatorLamp(i);
            ElevatorLamp temp2 = new ElevatorLamp(i);    // Showing dir(elevator) up/down
            elevatorLamps.add(temp);
            elevatorLamps.add(temp2);
            ElevatorButton tempButton = new ElevatorButton(i);
            elevatorButtons.add(tempButton);

        }

        Scheduler scheduler = new Scheduler();
        SchedulerState state = SchedulerState.IDLE_STATE;
        ArrayList<Elevator> elevators = new ArrayList<>();
        ElevatorSubsystem elevatorSS = new ElevatorSubsystem(elevators);
        Elevator elevator = new Elevator(0,3);
        elevators.add(elevator);

        for (int i = 0; i < 3; i++) {
            FloorLamp templamp1 = new FloorLamp(i);
            floorLamps.add(templamp1);
        }

        File file1 = new File("src/events.txt");
        BufferedReader file = new BufferedReader( new FileReader(file1));

        Floor floor = new Floor(1,  floorLamps,22,arrivalSensors );
        Floor floor2 = new Floor(2,  floorLamps,22 ,arrivalSensors );
        Floor floor3 = new Floor(3,  floorLamps,22 ,arrivalSensors );

        scheduler.loadElevator(elevator.getId(), floor.getFloorNumber());
        scheduler.loadElevator(elevator.getId(), floor2.getFloorNumber());
        scheduler.loadElevator(elevator.getId(), floor3.getFloorNumber());
        assertEquals(state, scheduler.getSchedulerState());
    }
}
