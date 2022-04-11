package Elevator_Subsystem;

import static java.lang.Thread.sleep;

public enum ElevatorState {
	IDLE {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			System.out.println("===Elevator "+ e.getId()+" is in IDLE state===\n");
			//determine a direction
			if(e.getCurrentFloor() > floor) {
				e.setDirection(0);
			}else if(e.getCurrentFloor() < floor) {
				e.setDirection(1);
			}
			e.setDestination(floor);
	        e.addNewDestination(floor);
	        //close doors before moving
	        e.getDoor().setDoorsOpen(false);
	        e.getMotor().setMoving(true);
	        Thread move = new Thread(new Runnable() {
				@Override
				public void run() {
					System.out.println("A new move thread generated");
					while (e.getCurrentFloor() != e.getDestination()){
						try {
							sleep(3000);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
						if(e.getDirection() == 1){
							e.moveUp();
							System.out.println("Elevator moved up one floor");
						}else if(e.getDirection() == 0){
							e.moveDown();
							System.out.println("Elevator moved down one floor");

						}
					}
					//it reached one destination load
					e.getDestinations().remove(new Integer(e.getDestination()));
					e.setElevatorState(LOADING);
					goTo(e, 0);
					//at end of moving thread, check if there are more destination to go, and transit state depending on current state
					if(e.getDestinations().isEmpty()){
						System.out.println("Destination list is empty");
						if(e.getElevatorState() == ElevatorState.MOVING){
							e.setElevatorState(LOADING);
							//goTo(e, 0);
						}
					}else{
						System.out.println("There are more destinations");
						//e.setDestination(e.getDestinations().get(0));
						//e.execute(e.getDestination());
					}
					System.out.println("End of a move thread");
				}

			});
	        move.start();
			return MOVING;
		}
	},


	
	MOVING {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			//if the newly assigned destination is closer, go to new destination first
			System.out.print("===Elevator is in ACTIVE state===\n");
			if((e.getDirection() == 0 && e.getDestination() < floor) ||
							(e.getDirection() == 1 && e.getDestination() > floor)){
				e.setDestination(floor);
			}
	        e.addNewDestination(floor);
			return this;
		}
	},
	
	LOADING {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			System.out.print("===Elevator is in Loading state===\n");
	        while(e.getIsMoving()){
	            try {
					System.out.println("Fault detected: Door is stuck open");
	                wait();
	            }
	            catch (InterruptedException e1){}
	        }
	        try {
				sleep(2000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	        e.getDoor().setDoorsOpen(true);
	        System.out.println("Elevator doors open. Please board.");
	        try {
				sleep(5000);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
	        //if there are more destinations to go, close doors transit to MOVING state, else keep doors open and transit to IDLE state
	        if(!e.getDestinations().isEmpty()) {
				e.getDoor().setDoorsOpen(false);
				System.out.println("Elevator doors are now closed.");
				return IDLE;
			}else{

	        	return MOVING;
			}
		}
	};
	
	abstract ElevatorState goTo(Elevator e, int floor);
}