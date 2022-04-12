package Elevator_Subsystem;

import Floor_Subsystem.Floor;

import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Thread.sleep;

//import Elevator_Subsystem.*;

public class Elevator  {
    private ArrayList<ElevatorButton> buttons;
    private ArrayList<ElevatorLamp> lamps;
    private ElevatorMotor motor;
    private ElevatorDoor door;
    private int id;
    private Boolean isMoving;
    private int currentFloor;
    private ArrayList<Integer> destinations;
    private int direction;   // 1 = ascending 0 = descending
    //Change name later
    private Boolean isEvent;
    private int numFloorTotal;
    private int lastButtonPressed;
    private int destination;
    private ElevatorState elevatorState;
    private int fault;

    /**
     * Constructor method that initializes an elevator for the scheduler and
     * floor threads.
     *
     * @param id, elevatorButtonArrayList, elevatorLampArrayList, elevatorMotor, elevatorDoor, amountOfFloorsInBuilding, scheduler
     */
    public Elevator(int id, int amountOfFloorsInBuilding) {
        buttons = new ArrayList<>();
        lamps = new ArrayList<>();
        for(int i =0; i< amountOfFloorsInBuilding; i++){
            this.buttons.add(new ElevatorButton(i));
            this.lamps.add(new ElevatorLamp(i));
        }
        destinations = new ArrayList<>();
        direction = 0;
        isEvent = false;
        this.door = new ElevatorDoor();
        this.id = id;
        this.motor = new ElevatorMotor(0.29947, 0.0032,0.50);
        isMoving = false;
        this.currentFloor = 2;
        this.numFloorTotal = amountOfFloorsInBuilding;
        lastButtonPressed=-1;
        this.destination = 0;
        this.elevatorState = ElevatorState.IDLE;
    }

    public Elevator(int id, ArrayList<ElevatorButton> elevatorButtonArrayList, ArrayList<ElevatorLamp> elevatorLampArrayList,
                    ElevatorMotor elevatorMotor, ElevatorDoor elevatorDoor, int amountOfFloorsInBuilding, ElevatorState state, int fault) {
        buttons = new ArrayList<ElevatorButton>();
        lamps = new ArrayList<ElevatorLamp>();

        for (int i = 0; i < amountOfFloorsInBuilding; i++) {
            ElevatorLamp temp = new ElevatorLamp(i);
            lamps.add(temp);

            ElevatorButton tempButton = new ElevatorButton(i);
            buttons.add(tempButton);
        }
        destinations = new ArrayList<Integer>();
        direction = 0;
        isEvent = false;
        this.buttons = elevatorButtonArrayList;
        this.door = elevatorDoor;
        this.id = id;
        this.motor = elevatorMotor;
        this.lamps = elevatorLampArrayList;
        isMoving = false;
        this.currentFloor = 1;
        this.numFloorTotal = amountOfFloorsInBuilding;
        lastButtonPressed=-1;
        this.destination = 0;
        this.elevatorState = state;
        this.fault = fault;
    }


    public void setLastButtonPressed(int lastButtonPressed) {
        this.lastButtonPressed = lastButtonPressed;
    }

    public int getLastButtonPressed() {
        return lastButtonPressed;
    }

    public ArrayList<Integer> getDestinations() {
        return destinations;
    }

    public Boolean getMoving() {
        return isMoving;
    }

    public void setMoving(Boolean moving) {
        isMoving = moving;
    }

    public ElevatorMotor getMotor() {
        return motor;
    }
    
    public int getDirection() {
        return direction;
    }

    public Boolean getEvent() {
        return isEvent;
    }

    public void setEvent(Boolean event) {
        isEvent = event;
    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }

    public int getNumFloorTotal() {
        return numFloorTotal;
    }

    public ArrayList<ElevatorButton> getButtons() {
        return buttons;
    }

    public ArrayList<ElevatorLamp> getLamps() {
        return lamps;
    }

    public void setNumFloorTotal(int numFloorTotal) {
        this.numFloorTotal = numFloorTotal;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
    
    public void setDestination(int floor) {
    	this.destination = floor;
    }
    
    public int getDestination() {
    	return destination;
    };
    
    public void execute(int floor) {
    	elevatorState.goTo(this, floor);
    }
    
    public void addDestinations(int floor) {
    	System.out.println("addDestination is called");
        execute(floor);
    }

    public void addNewDestination(int floor){
        destinations.add(floor);
    }
    
    public boolean getIsMoving() {
    	return isMoving;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ElevatorState getElevatorState() {
        return elevatorState;
    }

    public void setElevatorState(ElevatorState elevatorState) {
        this.elevatorState = elevatorState;
    }

    /**
     * Make button show it has been pressed and registered as a destination
     *
     * @param floor
     */
    public void pressFloorNumberButton(int floor) {
    	//button only reacts when its is in the direction as the elevator's moving
    	if((currentFloor > floor && direction == -1) || (currentFloor < floor && direction == 1)) {
    		buttons.get(floor).setPressed(true);
        	lamps.get(floor).setVisited(true);
        	lastButtonPressed=floor;
        	this.execute(floor);
        	isEvent = true;
    	}
    }


    public synchronized void load() throws InterruptedException {
        this.elevatorState = ElevatorState.LOADING;
        execute(currentFloor);
    }



    /**
     * this method changes the elevator lamp based on the floor
     * that is inputted.
     *
     * @param floor
     */

    public void changeElevatorLamp(Floor floor) {
        for (int i = 0; i < lamps.size(); i++) {
            if (lamps.get(i).getFloorNumber() != floor.getFloorNumber()) {
                lamps.get(i).setVisited(false);
            } else {
                lamps.get(i).setVisited(true);
            }

        }
    }

    /**
     * Moves to the specified direction
     * @param direction
     * @throws InterruptedException
     */
    public synchronized void move(int direction) throws InterruptedException {

        /*
        setDirection(direction);
        this.direction=direction;
        System.out.println("Move: move?" + isMoving);
        System.out.println("dir: " + this.direction);
        while (isMoving) {
            try {
                wait(50);
                System.out.println("DEBUG >> In Move Wait Loop~~~");
            } catch (InterruptedException e) {
                System.err.print(e);
                e.printStackTrace();
            }
        }
        System.out.println("Move try");

        try {
            if (direction == 1) {
                System.out.println("CurFloor ++");
                currentFloor++;
            } else {
                System.out.println("CurFloor --");
                currentFloor--;
            }
            wait(500);
            //setMoving(true);
            getMotor().setMoving(true);
            System.out.println("After move: move?" + isMoving);
            System.out.println("dir: " + this.direction);
        }
        catch (InterruptedException e){}
*/
    }



    /**
     * Tells the elevator to stop at the selected floor. it adds the floor to the
     * destinations list if it is not already on there
     *
     * @param floor
     */
    public synchronized void stopAt(int floor) {
        System.out.println("DEBUG >> at stopAt method");
        while (isMoving) {
            try {
                wait();

            } catch (InterruptedException e) {
                System.err.print(e);
                e.printStackTrace();
            }
        }

    }


    public int getCurrentFloor() {
        return currentFloor;
    }


    public void setDoor(ElevatorDoor door) {
        this.door = door;
    }

    public ElevatorDoor getDoor() {
        return door;
    }

    public void moveUp(){
        currentFloor++;
    }

    public void moveDown(){
        currentFloor--;
    }


}


