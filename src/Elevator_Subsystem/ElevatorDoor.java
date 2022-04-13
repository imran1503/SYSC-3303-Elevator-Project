package Elevator_Subsystem;

public class ElevatorDoor {
    private Boolean doorsOpen;
    
    /**
    * Closes the doors of the elevator
    */
    public ElevatorDoor(){ doorsOpen = true; }

    /** 
    * Returns the boolean of the elevator doors
    * @return doorsOpen
    */
    public Boolean getDoorsOpen() {
        return doorsOpen;
    }
    
    /** 
    * Sets the doors open or close based on the parameter
    * @param doorsOpen
    */
    public void setDoorsOpen(Boolean doorsOpen) {
        this.doorsOpen = doorsOpen;
    }
}
