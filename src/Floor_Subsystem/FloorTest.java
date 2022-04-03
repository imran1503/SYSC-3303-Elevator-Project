/**
 * 
 */
package Floor_Subsystem;

import Elevator_Subsystem.*;
import Scheduler.Scheduler;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class FloorTest {

	@org.junit.Test
	public void getFloorNumber() throws FileNotFoundException {
		ArrayList<FloorLamp> floorLamps = new ArrayList<>();
        ArrayList<FloorButton> floorButtons = new ArrayList<>();
        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();
        ArrayList<ElevatorLamp> elevatorLamps = new ArrayList<>();
        ArrayList<ElevatorButton> elevatorButtons = new ArrayList<>();
        ElevatorDoor elevatorDoor = new ElevatorDoor();
        ElevatorMotor elevatorMotor = new ElevatorMotor(0.29947, 0.0032,0.50);
       
        for (int i = 0; i < 22; i++) {
            ElevatorLamp temp = new ElevatorLamp(i);
            ElevatorLamp temp2 = new ElevatorLamp(i);    // Showing dir(elevator) up/down
            elevatorLamps.add(temp);
            elevatorLamps.add(temp2);



            ElevatorButton tempButton = new ElevatorButton(i);
            elevatorButtons.add(tempButton);


        }

        Scheduler scheduler = new Scheduler();

        ArrayList<Elevator> elevators = new ArrayList<>();
        ElevatorSubsystem elevatorSS = new ElevatorSubsystem(elevators);
        Elevator elevator =new Elevator(0,3);
        elevators.add(elevator);

        for (int i = 0; i < 3; i++) {
            FloorLamp templamp1 = new FloorLamp(i);
            floorLamps.add(templamp1);
            if (i==0){
                FloorButton tempButtonUp = new FloorButton(i,1);
                floorButtons.add(tempButtonUp);
            }
            else if (i == 3-1){
                FloorButton tempButtonDown = new FloorButton(i,0);
                floorButtons.add(tempButtonDown);

            }
            else {
                FloorButton tempButtonUp = new FloorButton(i, 1);
                FloorButton tempButtonDown = new FloorButton(i, 0);
                floorButtons.add(tempButtonUp);
                floorButtons.add(tempButtonDown);
            }
            for (int j = 0; j < 1.; j++) {

            }
        }

        File file1 = new File("src/events.txt");
        BufferedReader file = new BufferedReader( new FileReader(file1));




        Floor floor = new Floor(1, floorLamps, 22 ,arrivalSensors );
        
        assertEquals(1, floor.getFloorNumber());
	}
	
	
	

}
