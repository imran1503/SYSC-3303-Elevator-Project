package Floor_Subsystem;

public class FloorLamp {
    private int floorNumber;
    private Boolean active;

    /**
    * Initializes the floor lamp of the created floor by obtaining the floorNum
    * @param floornum
    */
    public FloorLamp(int floorNumber){
        this.floorNumber=floorNumber;
        if (this.floorNumber == 1) {
            active=true;
        }
        active=false;
    }
    
    /** 
    * Return the floor number of the floor lamp
    * @return floorNumber
    */
    
    public int getFloorNumber() {
        return floorNumber;
    }
    /**
    * Sets the floor number of the floor lamp
    * @param floorNumber
    */
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }
    /**
    * Returns the boolean that represents if the the lamp
    * is active or not (True = is active)
    * @return active
    */
    public Boolean getActive() {
        return active;
    }

    /**
    * Sets the lamp to active or not based on the sset parameter
    * @param active
    */
    public void setActive(Boolean active) {
        this.active = active;
    }
}
