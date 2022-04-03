package Elevator_Subsystem;

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

        byte[] tempByteArray = new byte[4];
        tempByteArray[0] = 0;
        tempByteArray[1] = 0;
        tempByteArray[2] = 1;
        tempByteArray[3] = 0;

        DatagramPacket test = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);


        assertEquals(1, elevatorSS.elevatorAction(test));
        assertNotEquals(0, elevatorSS.elevatorAction(test));
        assertNotEquals(-1, elevatorSS.elevatorAction(test));


    }
}