package Elevator_Subsystem;

import static java.lang.Thread.sleep;

public enum ElevatorState {
	IDLE {
		@Override
		ElevatorState goTo(Elevator e, int floor) {
			if(floor == 0){
				return IDLE;
			}
			System.out.println("===Elevator "+ e.getId()+" is in IDLE state===\n");
			e.setDestination(floor);
	        e.addNewDestination(floor);
	        //close doors before moving
	        e.getDoor().setDoorsOpen(false);
	        e.getMotor().setMoving(true);
	        e.setElevatorState(MOVING);
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
						//determine a direction
						if(e.getCurrentFloor() > floor) {
							e.setDirection(-1);
						}else if(e.getCurrentFloor() < floor) {
							e.setDirection(1);
						}
						System.out.println("current destination is: " + e.getDestination());
						if(e.getDirection() == 1){
							e.moveUp();
							System.out.println("Elevator moved up one floor");
						}else if(e.getDirection() == -1){
							e.moveDown();
							System.out.println("Elevator moved down one floor");

						}
					}
					//it reached one destination, load
					e.getDestinations().remove(new Integer(e.getDestination()));
					e.setElevatorState(LOADING);
					e.execute(0);
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
			if((e.getDirection() == -1 && e.getDestination() < floor && floor < e.getCurrentFloor()) ||
							(e.getDirection() == 1 && e.getDestination() > floor && floor > e.getCurrentFloor())){
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
			if(floor == 0) {
				e.getMotor().setMoving(false);
				e.setMoving(false);
				int direction = e.getDirection();
				e.setDirection(0);
				Thread closeDoor = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							sleep(2000);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
						e.getDoor().setDoorsOpen(true);
						try {
							sleep(5000);
						} catch (InterruptedException interruptedException) {
							interruptedException.printStackTrace();
						}
						e.setElevatorState(IDLE);
						if (!e.getDestinations().isEmpty()) {

							ArrayList<Integer> destinations = new ArrayList<>(e.getDestinations());
							Collections.sort(destinations);
							if(direction == 1){
								e.setDestination(e.getDestinations().get(e.getDestinations().size()-1));
							}else{
								e.setDestination(e.getDestinations().get(0));
							}
							e.execute(e.getDestination());
						}
					}

				});
				closeDoor.start();
				return IDLE;
			}else{
				e.addNewDestination(floor);
				return LOADING;
			}
			/*
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


			 */
		}


	};
	
	abstract ElevatorState goTo(Elevator e, int floor);
}
