package Floor_Subsystem;
import java.util.ArrayList;
import java.sql.Timestamp;
import java.io.*;
import java.lang.Long;

import Elevator_Subsystem.*;
import Scheduler.*;

public class Floor {


    //TODO private Time time;
    private int floorNumber;
    private ArrayList<Elevator> elevators;
    private ArrayList<FloorButton> buttons;
    private ArrayList<FloorLamp> lamps;

    private ArrayList<ArrivalSensor> arrivalSensors;
	
    /**
     * Constructor method that initializes an floor
     * 
     *
     * @param floorNumber, floorLamps, amountOfFloorsInBuilding, arrivalSensors
     */	
    public Floor(int floorNumber, ArrayList<FloorLamp> floorLamps, int amountOfFloorsInBuilding, ArrayList<ArrivalSensor> arrivalSensors ) {
        this.buttons = new ArrayList<FloorButton>();
        this.lamps = new ArrayList<FloorLamp>(); 
        if(floorNumber == 1){
            this.buttons.add(new FloorButton(1,1));
        }else if(floorNumber == amountOfFloorsInBuilding-1){
            this.buttons.add(new FloorButton(floorNumber, -1));
        }else{
            this.buttons.add(new FloorButton(floorNumber,1));
            this.buttons.add(new FloorButton(floorNumber, -1));
        }
        this.floorNumber=floorNumber;
        this.lamps=floorLamps;
        this.elevators = new ArrayList<Elevator>();
        this.arrivalSensors = arrivalSensors;
    }

	
	public ArrayList<ArrivalSensor> getArrivalSensors() {
		return arrivalSensors;
	}
	
	public void setElevators(ArrayList<Elevator> elevators) {
		this.elevators = elevators;
	}

	public int getFloorNumber() {
        return floorNumber;
        }

	public ArrayList<Elevator> getElevators() {
		return elevators;
	}

	public ArrayList<FloorButton> getButtons() {
		return buttons;
	}

	public ArrayList<FloorLamp> getLamps() {
		return lamps;
	}


}
