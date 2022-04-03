package Elevator_Subsystem;

import Floor_Subsystem.*;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collections;

public class ElevatorSubsystem extends Thread{
    ArrayList<Elevator> elevators;
    DatagramPacket sendFloorPacket;
    DatagramPacket sendServerPacket;
    DatagramPacket recieveServerPacket;
    DatagramPacket recieveFloorPacket;

    DatagramPacket ackPacket;
    DatagramSocket sendFloorSocket;
    DatagramSocket recieveFloorSocket;
    DatagramSocket sendServerSocket;
    DatagramSocket recieveServerSocket;

    DatagramSocket elevatorSocket;
    int elevatorIndex; // what elevator are you trying to interact with
    int fault;

    // Time Variables
    long start;
    long end;
    long total;
    long timeBetweenFloors = 14500; // In ms

    /**
     * Creates an arraylist of elevators and Initializes receive
     * Sockets for the Floor and scheduler
     *
     * @param elevatorlist
     */
    public ElevatorSubsystem(ArrayList<Elevator> elevatorlist){
        elevators = elevatorlist;
        try {
            recieveFloorSocket = new DatagramSocket(5000);
            recieveServerSocket = new DatagramSocket(5001);
            sendServerSocket = new DatagramSocket();
            sendFloorSocket = new DatagramSocket();
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


    }




    public ArrayList<Elevator> getElevators() {
        return elevators;
    }

    public void setElevators(ArrayList<Elevator> elevators) {
        this.elevators = elevators;
    }

    public int getElevatorIndex() {
        return elevatorIndex;
    }

    public void setElevatorIndex(int elevatorIndex) {
        this.elevatorIndex = elevatorIndex;
    }

    /**
     *
     * This method creates a packet of the elevator's
     * information to send to the floor
     *
     * @param packetID
     * @return tempPacket
     * @throws IOException
     */

    public DatagramPacket createElevatorPacket(int packetID) throws IOException {
        DatagramPacket tempPacket;
        byte[] tempByteArray = new byte[30];
        byte[] ack = "acknowledgment".getBytes();

        //Create a elevator packet for elevator (0); TODO Copy for other 3 elevators when confident in design.
        if(packetID == 0){
            //This will create a packet for each event.

            elevatorIndex = 0;
            tempByteArray[0] = (byte) (elevators.get(elevatorIndex).getId());
            tempByteArray[1] = (byte) (-1);
            int j=2;
            for (int i=0;  elevators.get(elevatorIndex).getDestinations().get(i+1)!= null; i++) {
                tempByteArray[j] = (byte)( (int) (elevators.get(elevatorIndex).getDestinations().get(i)));
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getDirection());
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getEvent() ? 1 : 0 );
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            byte[] textArray = ("" + (elevators.get(elevatorIndex).getElevatorState())).getBytes(); // LOADING MOVING IDLE
            for (int i=0; i < textArray.length; i++) {
                tempByteArray[j] = textArray[i];
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);
            return tempPacket;


        }
        if(packetID == 1){
            //This will create a packet for each event.

            elevatorIndex = 1;
            tempByteArray[0] = (byte) (elevators.get(elevatorIndex).getId());
            tempByteArray[1] = (byte) (-1);
            int j=2;
            for (int i=0;  elevators.get(elevatorIndex).getDestinations().get(i+1)!= null; i++) {
                tempByteArray[j] = (byte)( (int) (elevators.get(elevatorIndex).getDestinations().get(i)));
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getDirection());
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getEvent() ? 1 : 0 );
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            byte[] textArray = ("" + (elevators.get(elevatorIndex).getElevatorState())).getBytes(); // LOADING MOVING IDLE
            for (int i=0; i < textArray.length; i++) {
                tempByteArray[j] = textArray[i];
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);
            return tempPacket;


        }
        if(packetID == 2){
            //This will create a packet for each event.

            elevatorIndex = 2;
            tempByteArray[0] = (byte) (elevators.get(elevatorIndex).getId());
            tempByteArray[1] = (byte) (-1);
            int j=2;
            for (int i=0;  elevators.get(elevatorIndex).getDestinations().get(i+1)!= null; i++) {
                tempByteArray[j] = (byte)( (int) (elevators.get(elevatorIndex).getDestinations().get(i)));
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getDirection());
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getEvent() ? 1 : 0 );
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            byte[] textArray1 = ("" + (elevators.get(elevatorIndex).getElevatorState())).getBytes(); // LOADING MOVING IDLE
            for (int i=0; i < textArray1.length; i++) {
                tempByteArray[j] = textArray1[i];
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);
            return tempPacket;


        }
        if(packetID == 3){
            //This will create a packet for each event.

            elevatorIndex = 3;
            tempByteArray[0] = (byte) (elevators.get(elevatorIndex).getId());
            tempByteArray[1] = (byte) (-1);
            int j=2;
            for (int i=0;  elevators.get(elevatorIndex).getDestinations().get(i+1)!= null; i++) {
                tempByteArray[j] = (byte)( (int) (elevators.get(elevatorIndex).getDestinations().get(i)));
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getDirection());
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            tempByteArray[j] = (byte) (elevators.get(elevatorIndex).getEvent() ? 1 : 0 );
            j++;
            tempByteArray[j] = (byte) (-1);
            j++;
            byte[] textArray2 = ("" + (elevators.get(elevatorIndex).getElevatorState())).getBytes(); // LOADING MOVING IDLE
            for (int i=0; i < textArray2.length; i++) {
                tempByteArray[j] = textArray2[i];
                j++;
            }
            tempByteArray[j] = (byte) (-1);
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5000);
            return tempPacket;
        }
        else {
            return null;
        }


    }

    /**
     * This method receives a packet that contains information for the elevator.
     * Based on the data in the packet, the elevator will perform different tasks and/or
     * send information to the scheduler or floor class
     *
     * @param actionPacket
     */
    public int elevatorAction(DatagramPacket actionPacket){
        System.out.println("DEBUG >> In Elevator Action method");
        byte[] data = new byte[100]; //issue
//        recieveServerPacket = new DatagramPacket(data,data.length);
//        try {
//            recieveServerSocket.receive(recieveServerPacket);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        data =  actionPacket.getData();
        elevatorIndex = data[0];
        System.out.println("DEBUG >> In Elevator Action method    Looking at data length: " + data.length);
        System.out.print("DEBUG >> In Elevator Action method    Looking at data: ");

        for(int i = 0; i < data.length; i++){
            System.out.print(data[i] + " ");

        }
        System.out.println();
        System.out.println("DEBUG >> Looking at data[2]: " + data[2]);

        if(elevatorIndex <= 3 && elevatorIndex >=0) {
            elevators.get(elevatorIndex).setEvent(true);
            if (data[2] == 1) { //getCurrent floor, return as packet
                //send back currentFloor as packet,
                //describe packet structure: 1. eleIndex = data[0], data[1] = 0, action = 1 at data[2]=1, data[3] = 0
                byte[] tempByteArray = new byte[4];

                tempByteArray[0] = (byte) elevatorIndex;
                tempByteArray[1] = (byte) 0;
                tempByteArray[2] = (byte) elevators.get(elevatorIndex).getCurrentFloor();
                tempByteArray[3] = (byte) fault;

                try {
                    DatagramPacket tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
                    sendServerSocket.send(tempPacket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;


            } else if (data[2] == 2) { //add destination and planTrip / sort
                //packet structure: data[0]=elevId, data[1]=0, data[2]=2, data[3]=0, data[4]=destinationToAdd, data[5]=0
                elevators.get(elevatorIndex).getDestinations().add((int) data[4]);
                planElevatorTrip(elevators.get(elevatorIndex).getId());
                return 1;
            } else if (data[2] == 3) { //Move  elevator
                //packet structure: data[0]=elevIndex, data[1]=0, data[2]=3 data[3]=0 data[4]=direction(1 or 0), data[5]=0, data[6]=destination floorNum, data[7] = 0

                try {
                    elevators.get(elevatorIndex).move(data[4]);
                    elevators.get(elevatorIndex).addDestinations(data[6]);
                    planElevatorTrip(elevators.get(elevatorIndex).getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;
            } else if (data[2] == 4) {// Open doors, close doors/ load
                //packet structure: data[0]=elevindex, data[1]=0, data[2]=4
                try {
                    elevators.get(elevatorIndex).load();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return 1;

            } else if (data[2] == 5) {//getDestinations, return as a packet to scheduler
                //packet structure: data[0]=elevIndex, data[1]=0, data[2]=5
                ArrayList<Integer> destinations = elevators.get(elevatorIndex).getDestinations();

                byte[] tempByteArray = new byte[23];
                tempByteArray[0] = (byte) elevatorIndex;
                tempByteArray[1] = (byte) 0;

                int index = 2;
                System.out.print("DEBUG >>> 005 Req All pos: : ");
                for (int i = 0; i < destinations.size(); i++) {
                    tempByteArray[index++] = destinations.get(i).byteValue();
                    tempByteArray[index++] = (byte) 0;
                    System.out.print(tempByteArray[index]);
                }
                System.out.print("");


                try {
                    DatagramPacket tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
                    sendServerSocket.send(tempPacket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;

            } else if (data[2] == 6) { // set moving to ? T/F depends on next piece of data
                //packet structure: data[0] = elevId, data[1] = 0, data[2]=6, data[3] = 0, data[4] = floorNum, data[5] = 0, data[6] = setmoving
                if (data[6] == 0) {
                    elevators.get(elevatorIndex).setMoving(false);
                    elevators.get(elevatorIndex).stopAt(data[4]);
                }
                return 1;
            } else if (data[2] == 7) {// requesting all elevator positions
                //packet: data[0]=0, data[1]=0, data[2]=7

                byte[] tempByteArray = new byte[16];

                tempByteArray[0] = (byte) elevators.get(0).getCurrentFloor();
                tempByteArray[1] = (byte) 0;
                tempByteArray[2] = (byte) elevators.get(1).getCurrentFloor();
                tempByteArray[3] = (byte) 0;
                tempByteArray[4] = (byte) elevators.get(2).getCurrentFloor();
                tempByteArray[5] = (byte) 0;
                tempByteArray[6] = (byte) elevators.get(3).getCurrentFloor();
                tempByteArray[7] = (byte) 0;

                //directions
                tempByteArray[8] = (byte) elevators.get(0).getDirection();
                tempByteArray[9] = (byte) 0;
                tempByteArray[10] = (byte) elevators.get(1).getDirection();
                tempByteArray[11] = (byte) 0;
                tempByteArray[12] = (byte) elevators.get(2).getDirection();
                tempByteArray[13] = (byte) 0;
                tempByteArray[14] = (byte) elevators.get(3).getDirection();
                tempByteArray[15] = (byte) 0;

                System.out.print("DEBUG >> 007 Req All pos: ");
                for (int i = 0; i < tempByteArray.length; i++) {
                    System.out.print(tempByteArray[i] + " ");

                }
                System.out.println();
                try {
                    DatagramPacket tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
                    sendServerSocket.send(tempPacket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;
            } else if (data[2] == 8) { //remove destination and planTrip / sort
                //packet structure: data[0]=elevId, data[1]=0, data[2]=8, data[3]=0, data[4]=destinationToRemove, data[5]=0
                elevators.get(elevatorIndex).getDestinations().remove(data[4]);
                planElevatorTrip(elevators.get(elevatorIndex).getId());
                return 1;

            } else if (data[2] == 9) { //add stop destination and planTrip / sort
                //packet structure: data[0]=elevId, data[1]=0, data[2]=2, data[3]=0, data[4]=destinationToAdd, data[5]=0
                elevators.get(elevatorIndex).getDestinations().add((int) data[4]);
                planElevatorTrip(elevators.get(elevatorIndex).getId());
                return 1;
            } else if (data[2] == 10) { //is moving? , return as packet
                //send back currentFloor as packet,
                //describe packet structure: 1. eleIndex = data[0], data[1] = 0, data[2]=10, data[3] = 0
                byte[] tempByteArray = new byte[4];

                tempByteArray[0] = (byte) elevatorIndex;
                tempByteArray[1] = (byte) 0;
                tempByteArray[2] = (byte) (elevators.get(elevatorIndex).getIsMoving() ? 1 : 0);
                tempByteArray[3] = (byte) 0;

                try {
                    DatagramPacket tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
                    sendServerSocket.send(tempPacket);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                return 1;

            }
        }
        else{

            System.out.println("Failed to action elevator: ElevID =" + elevatorIndex );
            return -1;
        }



        return 0;
    }

    /**
     * Updates the elevators information based on the packet parameter.
     * Can change multiple values for the elevator including: Destinations, state, etc
     *
     * @param elevatorPacket
     */


    public void updateElevator(DatagramPacket elevatorPacket){
        byte[] data =  elevatorPacket.getData();
        elevatorIndex = data[0];


        ElevatorDoor tempDoor;
        ElevatorMotor tempMotor;
        int numfloor;



        tempMotor = elevators.get(elevatorIndex).getMotor();
        tempDoor  = elevators.get(elevatorIndex).getDoor();
        numfloor = elevators.get(elevatorIndex).getNumFloorTotal();

        //From packet: Destinations, State, Moving, event, id, buttonList
        ArrayList<ElevatorButton> tempButtons= elevators.get(elevatorIndex).getButtons();
        ArrayList<ElevatorLamp> tempLamps = elevators.get(elevatorIndex).getLamps();
        int j = 0;
        ElevatorState temp = null;
        for (int i = 0; i < data.length; i++) {
            if( data[i] == ("" + ElevatorState.LOADING).getBytes()[0]){
                temp = ElevatorState.LOADING;
            }
            if( data[i] == ("" + ElevatorState.IDLE).getBytes()[0]){
                temp = ElevatorState.IDLE;
            }
            else{//data[i] == ("" + ElevatorState.MOVING).getBytes()[0]
                temp = ElevatorState.MOVING;
            }
        }





        Elevator tempElevator = new Elevator(data[0],  tempButtons, tempLamps, tempMotor, tempDoor, numfloor, temp, fault);
        elevators.add(elevatorIndex, tempElevator);

    }




    /**
     * Before the elevator can move in either direction, it must first ensure it hits all the floors required in sequential order.
     * Sort destinations by floor number EX: {4,2,8} asec -> {2,4,8}, desc {9,5,3,4} ->{9,5,4,3}
     *
     * @return a sorted array of what floors it must go to.
     */
    public java.util.ArrayList<java.lang.Integer> planElevatorTrip(int elevatorid) {
        Collections.sort(elevators.get(elevatorid).getDestinations()); //Sort by inc fl #
        try {
            if (elevators.get(elevatorid).getMoving() == false && elevators.get(elevatorid).getCurrentFloor() < elevators.get(elevatorid).getDestinations().get(0)) { // if at floor 3 and the lowest button pressed is 2 AND not moving / at a floor waiting
                elevators.get(elevatorid).setDirection(1); //aces
                return elevators.get(elevatorid).getDestinations();

            } else if (elevators.get(elevatorid).getMoving() == false && elevators.get(elevatorid).getCurrentFloor() < elevators.get(elevatorid).getDestinations().get(elevators.get(elevatorid).getDestinations().size() / 4)) {
                elevators.get(elevatorid).setDirection(1);
                return elevators.get(elevatorid).getDestinations();
            } else if (elevators.get(elevatorid).getMoving() == false && elevators.get(elevatorid).getCurrentFloor() > elevators.get(elevatorid).getDestinations().get(elevators.get(elevatorid).getDestinations().size() - 2)) {
                Collections.reverse(elevators.get(elevatorid).getDestinations());
                elevators.get(elevatorid).setDirection(0);  // desc
                return elevators.get(elevatorid).getDestinations();
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println(elevators.get(elevatorid).getDestinations().size());
        }

        return elevators.get(elevatorid).getDestinations();

    }

    public static void main(String[] args) {
        System.out.println("Elevator SS Starting ...");
        //initialize elevators
        Elevator e1 = new Elevator(1,3);
        Elevator e2 = new Elevator(2,3);
        Elevator e3 = new Elevator(3,3);
        Elevator e4 = new Elevator(4,3);

        //create elevator list
        ArrayList<Elevator> elevators = new ArrayList<>();
        elevators.add(e1);
        elevators.add(e2);
        elevators.add(e3);
        elevators.add(e4);


        ElevatorSubsystem elevatorSS = new ElevatorSubsystem(elevators);
        //create elevator parts
        ArrayList<Floor> floors = new ArrayList<Floor>();
        elevatorSS.run();


    }


    /**
     *
     * Receives packets from the floor subsystem and the scheduler
     * Based on data from floor's Packet, the elevator will perform
     * its main functionalities (Can be seen on elevator subsystem state diagram
     *
     */
    public void run() {
        System.out.println("DEBUG >> In run method ");
        byte[] msgFromFloor = new byte[8];
        recieveFloorPacket = new DatagramPacket(msgFromFloor, msgFromFloor.length);
        byte[] msgFromServer = new byte[8];
        recieveServerPacket = new DatagramPacket(msgFromServer, msgFromServer.length);

        //Start the timer
        start = System.nanoTime();


        // Get interrupted when : Floor calls elevator through scheduler;   it reaches/leaves destination; button is pressed ; arrives at a floor;
        while (true) {
            try {
                for (Elevator e:elevators) {
                    System.out.println("Elevator " + e.getId() + " is at floor " + e.getCurrentFloor());
                }

                System.out.println("DEBUG >> In try catch to receive packets");
                System.out.println("DEBUG >> S P: " + recieveServerSocket.getLocalPort());

                // recieveFloorSocket.receive(recieveFloorPacket);
                recieveServerSocket.receive(recieveServerPacket);


                byte[] serverData = recieveServerPacket.getData();

                System.out.print("DEBUG >> Data Received from S.SS:");

                for (int i = 0; i < serverData.length; i++) {
                    System.out.print(serverData[i]);
                }
                System.out.println();
                DatagramPacket tempPacket = new DatagramPacket(serverData, serverData.length);
                elevatorAction(tempPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("DEBUG >> elevator index: " + elevatorIndex);
            if (elevatorIndex <= 3 && elevatorIndex >=0) {
                System.out.println("DEBUG >> elevator is event? " + elevators.get(elevatorIndex).getEvent());
                if (elevators.get(elevatorIndex).getEvent()) {

                    // Setting value for amount of floors to calculate
                    if (elevators.get(elevatorIndex).getDestinations().isEmpty()) {
                        this.elevators.get(elevatorIndex).setElevatorState(ElevatorState.IDLE);
                        continue;
                    }
                    long floors = elevators.get(elevatorIndex).getDestinations().get(0) - (elevators.get(elevatorIndex).getCurrentFloor());

                    //state 1 init
                    System.out.println("elevators.get( elevatorIndex ).getDestinations().contains(elevators.get( elevatorIndex ).getLastButtonPressed()): " + elevators.get(elevatorIndex).getDestinations().contains(elevators.get(elevatorIndex).getLastButtonPressed()) + " " + elevators.get(elevatorIndex).getLastButtonPressed() + " " + elevators.get(elevatorIndex).getDestinations());
                    if (elevators.get(elevatorIndex).getDestinations().contains(elevators.get(elevatorIndex).getLastButtonPressed())) {
                        System.out.println("Elevator Position: " + elevators.get(elevatorIndex).getCurrentFloor());
                        System.out.println("Elevator Position 2: " + elevators.get(elevatorIndex).getCurrentFloor());
                        try {
                            elevators.get(elevatorIndex).stopAt(elevators.get(elevatorIndex).getDestinations().get(0));

                            // Stop the timer and compare to see if it took longer than usual
                            end = System.nanoTime();
                            total = end - start;


                            if (fault == 2) {
                                System.out.println("Fault Detected: Took elevator too long to reach destination");

                            }
                        } catch (IndexOutOfBoundsException e) {
                            System.out.println("Elevator " + this.elevators.get(elevatorIndex).getId() + " has no destinations to go to. ");
                            this.elevators.get(elevatorIndex).setEvent(false);
                        }
                        System.out.println("des : " + elevators.get(elevatorIndex).getDestinations());
                    }
                }

                if (elevators.get(elevatorIndex).getDestinations().isEmpty()) {
                    this.elevators.get(elevatorIndex).setElevatorState(ElevatorState.IDLE);
                }
            }

        }

    }
}

