package Elevator_Subsystem;

public class ElevatorMotor {
    private Boolean isMoving;
    private Double speed;
    private Double acceleration;
    private Double maxSpeed;

    public ElevatorMotor (double speed, double a, double MSpd){
        this.speed=speed;
        this.maxSpeed=MSpd;
        this.acceleration=a;
        isMoving=false;
    }
    /** 
    * Returns the boolean based on if the elevator is moving 
    * @return isMoving
    */
    public Boolean getMoving() {
    	System.out.println("moter activated");
        return isMoving;
    }
    /**
    * Set the boolean to move the elevator or not
    * @param moving
    */
    public void setMoving(Boolean moving) {
    	System.out.println("moter stopped");
        isMoving = moving;
    }
}
