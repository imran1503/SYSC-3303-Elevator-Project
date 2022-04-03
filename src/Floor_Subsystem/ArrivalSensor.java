package Floor_Subsystem;

import Elevator_Subsystem.*;
import Scheduler.Scheduler;

import java.io.IOException;
import java.net.*;

public class ArrivalSensor {
	private int elevatorid;
	private int floorNumber;
	private int id;
	
	/**
	* Initializes the Arrival sensor using the elevator and the floor number
	* @param elevatorid, floorNumber
	*/
	public ArrivalSensor(int elevatorid, int floorNumber, int id) {
		this.elevatorid = elevatorid;
		this.floorNumber = floorNumber;
		this.id=id;

	}
	/**
	* Checks if there is an elevator at the floor. if there is, it 
	* will return true.
	* @return boolean
	*/
	public boolean hasElevator(DatagramSocket floorToElevator) {

		byte[] temp = new byte[5];
		temp[0] = (byte) elevatorid;
		temp[0] = 0;
		temp[0] = 1;
		temp[0] = 0;
		byte[] recieveTemp = new byte[4];
		DatagramPacket recievePacket =  new DatagramPacket(recieveTemp, recieveTemp.length);
		try {
			DatagramPacket packet = new DatagramPacket(temp, temp.length, InetAddress.getLocalHost(), 5000);
			floorToElevator.send(packet);
			floorToElevator.receive(recievePacket);
			recieveTemp = recievePacket.getData();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}


		return floorNumber == recieveTemp[2];
	}

	public int getElevatorId() {
		return elevatorid;
	}

	public int getFloorNumber() {
		return floorNumber;
	}

	/**
	 *
	 * @return
	 */
	// Send Scheduler info to the floor instead of using scheduler class
	public Boolean getArrivalSensorData(DatagramSocket socket){
		byte[] destinations = new byte[23];

		//ask for destinations
		byte[] tempDest1 = new byte[3];
		tempDest1[0] = (byte)elevatorid;
		tempDest1[1] = 0;
		tempDest1[2] = 5;

		byte[] recieveTemp = new byte[23];
		DatagramPacket recievePacket =  new DatagramPacket(recieveTemp, recieveTemp.length);
		try {
			DatagramPacket packet = new DatagramPacket(destinations, destinations.length, InetAddress.getLocalHost(), 5000);
			socket.send(packet);
			socket.receive(recievePacket);
			recieveTemp = recievePacket.getData();

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if(hasElevator(socket)==true && getFloorNumber() == recieveTemp[0] ){

				// scheduler.stopElevatorAtFloor(elevator,floorNumber);

			return true;
		}
		return false;
	}
	
	
	
}
