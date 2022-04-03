package Elevator_Subsystem;

import Floor_Subsystem.Floor;

public class ElevatorLamp {
    private int floorNumber;
    private Boolean visited;


    /**
    * Initializes the elevator lamps based on the floor selected 
    * floor number
    * @param floornum
    */
    public ElevatorLamp(int floornum){
        this.floorNumber = floornum;
        if (floorNumber == 1) {
            visited=true;
        }
        visited=false;
    }
    
    
    /**
    * Returns the floor number of the lamp
    * @return floorNumber
    */
    public int getFloorNumber() {
        return floorNumber;
    }
    /** 
    * Sets the elevator lamp to the choesn floor
    * @param floorNumber
    */
    
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    
    /**
    * Returns the boolean based on if the floor was visited 
    * @return visited
    */
    public Boolean getVisited() {
        return visited;
    }
    /**
    * Sets the boolean value of if a floor was visited or not
    * @param visited
    */
    public void setVisited(Boolean visited) {
        this.visited = visited;
    }
}
