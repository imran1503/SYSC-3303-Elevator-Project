package Floor_Subsystem;

public class DirectionLamp {
	private boolean hasElevatorArrivved;
	private int direction; //0 -> down, 1 -> up
	/** 
	* Initializes the direction lamp using the elevators arrival 
	* and the direction of the elevator.
	* @param hasElevatorArrived, direction
	*/
	public DirectionLamp(boolean hasElevatorArrived, int direction) {
		this.hasElevatorArrivved = hasElevatorArrivved;
		this.direction = direction;
	}
	
	/**
	* Returns whether the elevator has arrived or not
	* @return hasElevatorArrived
	*/
	public boolean getHasElevatorArrivved() {
		return hasElevatorArrivved;
	}
	/**
	* returns which direction the elevator is moving
	* @return direction
	*/
	public int getDirection() {
		return direction;
	}
	/**
	* Sets the elevator's arrival to a certain floor
	* @param hasElevatorArrived
	*/
	public void setHasElevatorArrivved(boolean hasElevatorArrived) {
		this.hasElevatorArrivved = hasElevatorArrivved;
	}
	/**
	* Sets the direction of the elevator
	* @param direction
	*/
	public void setDirection(int direction) {
		this.direction = direction;
	}
}
