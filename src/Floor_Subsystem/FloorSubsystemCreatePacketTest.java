/**
 * 
 */
package Floor_Subsystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import Elevator_Subsystem.Elevator;


public class FloorSubsystemCreatePacketTest {
	
	 @Test
	 public void createPacket() throws IOException {

	        int amountOfFloorsinBuilding = 22;

	        ArrayList<Elevator> elevators = new ArrayList<>();

	        //create floor parts
	        ArrayList<FloorLamp> floorLamps = new ArrayList<>();
	        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();


	        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
	            FloorLamp templamp1 = new FloorLamp(i);
	            floorLamps.add(templamp1);

	            for (int j = 0; j < elevators.size(); j++) {
	                ArrivalSensor sensor = new ArrivalSensor(elevators.get(i).getId(), i,j);
	                arrivalSensors.add(sensor);
	            }
	        }

	        //make packet for events
	        File file1 = new File("src/events.txt");
	        BufferedReader file = new BufferedReader(new FileReader(file1));

	        ArrayList<Floor> floorsArrayList = new ArrayList<>();
	        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
	            Floor floor = new Floor(i, floorLamps, amountOfFloorsinBuilding, arrivalSensors);
	            floor.setElevators(elevators);
	            floorsArrayList.add(floor);
	        }

	        FloorSubsystem floorSS = new FloorSubsystem(floorsArrayList, file);






	        DatagramPacket packet =  floorSS.createPacket(0);
	        assertEquals(20, packet.getData()[1]);
	        assertEquals(0, packet.getData()[3]);
	        assertEquals(10, packet.getData()[5]);
	    }

}
