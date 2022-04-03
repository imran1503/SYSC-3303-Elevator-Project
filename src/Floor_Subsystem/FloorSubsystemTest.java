package Floor_Subsystem;

import Elevator_Subsystem.*;
import Scheduler.Scheduler;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.SocketException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class FloorSubsystemTest {
// NOTE YOU MUST RUN ALL TESTS INDIVIDUALLY OTHERWISE THE PORTS WILL ALREADY BE IN USE ---------------------
    @Test
    void createPacket() throws IOException {

        int amountOfFloorsinBuilding = 22;

        ArrayList<Elevator> elevators = new ArrayList<>();
        //get elevators from elevator ss via packets
//        elevators.add(elevator);

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
        assertEquals(0, packet.getData()[0]);
        assertEquals(4, packet.getData()[5]);
    }

    @Test
    void testCreatePacket() throws IOException {

        int amountOfFloorsinBuilding = 22;

        ArrayList<Elevator> elevators = new ArrayList<>();
        //get elevators from elevator ss via packets
//        elevators.add(elevator);

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








        DatagramPacket packet =  floorSS.createPacket(0, 1);
        assertEquals(0, packet.getData()[0]);
        assertEquals(1, packet.getData()[2]);
    }
}