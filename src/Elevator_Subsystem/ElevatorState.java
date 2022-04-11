package Elevator_Subsystem;

public enum ElevatorState {
	IDLE {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			//close doors
			System.out.println("===Elevator is in IDLE state when button is pressed===");
			e.getDoor().setDoorsOpen(false);
			//determine a direction
			if(e.getCurrentFloor() > floor) {
				e.setDirection(0);
			}else if(e.getCurrentFloor() < floor) {
				e.setDirection(1);
			}
			e.setDestination(floor);
	        e.addDestinations(floor);
	        //close doors before moving
	        e.getDoor().setDoorsOpen(false);
			return MOVING;
		}
	},


	
	MOVING {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			//if the newly assigned destination is closer, go to new destination first
			System.out.print("===Elevator is in ACTIVE state when button is pressed===");
			if((e.getDirection() == 0 && e.getDestination() < floor) ||
							(e.getDirection() == 1 && e.getDestination() > floor)){
				e.setDestination(floor);
			}
	        e.addDestinations(floor);        
			return this;
		}
	},
	
	LOADING {
		@Override
		synchronized ElevatorState goTo(Elevator e, int floor) {
			System.out.println("Elevator load f(x)");
	        while(e.getIsMoving()){
	            try {
					System.out.println("Fault detected: Floor timer exceeded expected time. Elevator will now shut off.");
	                wait();
	            }
	            catch (InterruptedException e1){}
	        }
	        try {
				Thread.sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	        e.getDoor().setDoorsOpen(true);
	        System.out.println("Elevator doors open. Please board.");
	        try {
				Thread.sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.getDoor().setDoorsOpen(false);
			System.out.println("Elevator doors are now closed.");
	        if(e.getDestinations().isEmpty()) {
	        	return IDLE;
	        }else {
	        	return MOVING;
	        }
		}
	};
	
	abstract ElevatorState goTo(Elevator e, int floor);
}