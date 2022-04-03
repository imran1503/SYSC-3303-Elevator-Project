package Elevator_Subsystem;

import Floor_Subsystem.Floor;

public class ElevatorButton {
    private Boolean pressed;
    private int floorNumber;

    /**
    * Constructor for an elevator button. Initialized the floor number and 
    * if it was pressed
    * 
    * @param floorNumber
    */
    public ElevatorButton(int floorNumber){
        this.floorNumber=floorNumber;
        pressed=false;
    }
    /**
    * Shows the User if the button was pressed by using a 
    * a boolean value (true if pressed).
    *
    */
    public Boolean getPressed() {
        return pressed;
    }
    /**
    * Returns the floor number of the button
    * @return floorNumber
    */
    public int getFloorNumber() {
        return floorNumber;
    }
    /**
    * Sets the floor number of the button
    * @param floorNumber
    */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    /**
    * Sets the button to pressed based on parameter in method
    * @param press
    */
   
    public void setPressed(Boolean press){ pressed = press; }

    /** 
    * Presses the button (sets pressed to true)
    */
    public void pressButton(){
        pressed = true;
    }

}
