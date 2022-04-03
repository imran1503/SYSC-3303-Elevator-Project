package Elevator_Subsystem;

import Floor_Subsystem.ArrivalSensor;
import Floor_Subsystem.FloorButton;
import Floor_Subsystem.FloorLamp;
import Scheduler.Scheduler;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static java.lang.Math.pow;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorStateTest {



    @Test
    void goTo() {
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

        Elevator elevator = new Elevator(0,3);
        elevators.add(elevator);

        for (int i = 0; i < 3; i++) {
            FloorLamp templamp1 = new FloorLamp(i);
            floorLamps.add(templamp1);
            for (int j = 0; j < 1.; j++) {

            }
        }

        System.out.println("Elevator load f(x)");
        while(elevator.getIsMoving()){
            try {
                wait();
            }
            catch (InterruptedException e1){}
        }
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        elevator.getDoor().setDoorsOpen(true);
        System.out.println("Elevator doors open. Please board.");
        try {
            long start = System.nanoTime();
            Thread.sleep(6500);
            long end = System.nanoTime();

            // Seeing if it stopped for that long
            assertNotEquals((end - start), 6.5*pow(10,9));

        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }
        assertNotEquals(false,elevator.getDoor().getDoorsOpen());
        elevator.getDoor().setDoorsOpen(false);
        assertNotEquals(true,elevator.getDoor().getDoorsOpen());

        if(elevator.getDoor().getDoorsOpen()){

            System.out.println("fault detected: Elevator doors are stuck open");

        }
        System.out.println("Elevator doors are now closed.");
        if(elevator.getDestinations().isEmpty()) {

    }





    }
}