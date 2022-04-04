package Elevator_Subsystem;

import Floor_Subsystem.ArrivalSensor;
import Floor_Subsystem.FloorButton;
import Floor_Subsystem.FloorLamp;
import Scheduler.Scheduler;
import org.junit.Assert;
import org.junit.jupiter.api.Test;

import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ElevatorSubsystemTest {

    private ArrayList<Elevator> elevatorlist;
    private ObjectInputStream.GetField elevators;

    @Test
    void elevatorActionTest() throws SocketException, UnknownHostException {

        System.out.println("Elevator SS Starting ...");
        //initialize elevators
        Elevator e1 = new Elevator(1, 3);
        Elevator e2 = new Elevator(2, 3);
        Elevator e3 = new Elevator(3, 3);
        Elevator e4 = new Elevator(4, 3);

        //create elevator list
        ArrayList<Elevator> elevators = new ArrayList<>();
        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);
        elevators.add(e4);


        ElevatorSubsystem elevatorSS = new ElevatorSubsystem(elevators);

        byte[] tempByteArray = new byte[5];
        tempByteArray[0] = 0;
        tempByteArray[1] = 0;
        tempByteArray[2] = 1;
        tempByteArray[3] = 0;
        tempByteArray[4] = 1; // Fault



        DatagramPacket test = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);


        assertEquals(1, elevatorSS.elevatorAction(test));
        assertNotEquals(0, elevatorSS.elevatorAction(test));
        assertNotEquals(-1, elevatorSS.elevatorAction(test));


    }

    public void setFault(int fault) {

        ArrayList<FloorLamp> floorLamps = new ArrayList<>();
        ArrayList<FloorButton> floorButtons = new ArrayList<>();
        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();
        ArrayList<ElevatorLamp> elevatorLamps = new ArrayList<>();
        ArrayList<ElevatorButton> elevatorButtons = new ArrayList<>();
        ElevatorDoor elevatorDoor = new ElevatorDoor();
        ElevatorMotor elevatorMotor = new ElevatorMotor(0.29947, 0.0032, 0.50);
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

        Elevator elevator = new Elevator(0, elevatorButtons, elevatorLamps, elevatorMotor, elevatorDoor, 3, ElevatorState.IDLE, fault);
        elevators.add(elevator);

        // Testing Faults
        elevatorSS.setFault(2);
        Assert.assertEquals(elevatorSS.getFault(), 0);

    }
}
