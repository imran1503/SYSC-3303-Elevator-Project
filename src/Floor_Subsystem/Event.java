/**
 * 
 */
package Floor_Subsystem;

import java.sql.Timestamp;


public class Event {
	
	private Timestamp timestamp;
	private int floorNumber;
	private FloorButton floorButton;
	private int carButton;
	private boolean validEvent;
	
	/**
	* Initializes an event based on the time stamp, the floorNumber, floorButton, elevatorId
	* @param timestamp, floorNumber, floorButton, elevatorId
	*/
	public Event(Timestamp timestamp, int floorNumber, FloorButton floorButton, int carButton) {
		this.timestamp = timestamp;
		this.floorNumber = floorNumber;
		this.floorButton = floorButton;
		this.carButton = carButton;
		this.validEvent = false;
	}
	
	/**
	* Returns the time stamp of the event
	* @return time stamp
	*/
	
	public Timestamp getTimestamp() {
		return timestamp;
	}
	/**
	* Returns the floor number of the event
	* @return floorNumber
	*/
	public int getFloorNumber() {
		return floorNumber;
	}
	/**
	* Returns the floor button of the event
	* @return floorButton
	*/
	public FloorButton getFloorButton() {
		return floorButton;
	}
	/** 
	* Returns the elevator id of the event
	* @returns evelatorId
	*/
	public int getCarButton() {
		return carButton;
	}
	
	/** 
	* Returns if the event is valid
	* @returns validEvent
	*/
	public boolean isValid() {
		return validEvent;
	}
	
	/** 
	* Sets the validity of the event
	* @returns 
	*/
	public void setValidity(boolean validity) {
		this.validEvent = validity;
	}

}
