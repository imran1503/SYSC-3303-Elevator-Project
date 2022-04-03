package Floor_Subsystem;

public class FloorButton {
    private Boolean pressed ;
    private int floorNumber;
    private int direction; // 1 = up, 0 = down

    /** 
    * Initializes the the floor buttons using the floor number and the direction of the 
    * elevator passing it
    * @param floorNumber, direction
    */
    
    public FloorButton(int floorNumber, int direction){
        this.floorNumber=floorNumber;
        pressed=false;
        this.direction=direction;

    }
    /** 
    * Returns the direction of the elevator
    * @return direction
    */
    public int getDirection() {
    	return direction;
    }
    /**
    * Changes the direction based on the set parameter direction
    * @param direction
    */
    public void setDirection(int direction) {
        this.direction = direction;
    }
    /** 
    * Returns the boolean tat represents if the button was pressed 
    * or not
    * @return pressed
    */
    
    public Boolean getPressed() {
        return pressed;
    }
    /** 
    * Sets the boolean that represents if the button is pressed
    * or not
    * @param pressed
    */
    public void setPressed(Boolean pressed) {
        this.pressed = pressed;
    }
    /**
    * returns the floor number of the floor
    * @return floorNumber
    */
    public int getFloorNumber() {
        return floorNumber;
    }
    /** 
    * Sets the floor number of the floor 
    * @param floorNumber
    */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }

    //TODO
    public void pressButton(){

    }
}
