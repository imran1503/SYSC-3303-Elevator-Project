package Scheduler;

import Elevator_Subsystem.Elevator;
import Floor_Subsystem.Floor;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Scheduler extends Thread {
    DatagramPacket sendFloorPacket;
    DatagramPacket sendElevatorPacket;
    DatagramPacket recieveElevatorPacket;
    DatagramPacket recieveFloorPacket;
    DatagramPacket ackPacket;

    DatagramSocket ackSocket;
    DatagramSocket sendFloorSocket;
    DatagramSocket recieveFloorSocket;
    DatagramSocket sendElevatorSocket;
    DatagramSocket recieveElevatorSocket;
    private boolean available = false;
    private SchedulerState schedulerState = SchedulerState.IDLE_STATE;
    ArrayList<Floor> floorArrayList;
    ArrayList<Integer> elevatorList;
    int elevatorIndex;
    int fault;
    private static final long expectedTime = 950000000; //expected time of elevator reaching its destination
    Boolean debug;
    long start;


    /**
     * This creates a new scheduler that initializes all the ports required for it to
     * contact the other subsystems: Elevator and Floor
     */
    public Scheduler(){
        elevatorList = new ArrayList<>();
        try {
            sendElevatorSocket = new DatagramSocket();
            recieveElevatorSocket = new DatagramSocket(5002);

            sendFloorSocket = new DatagramSocket();
            recieveFloorSocket = new DatagramSocket(5003);
        } catch (java.net.BindException e){

        }
        catch (Exception e) {
            e.printStackTrace();
        }

        try {

        } catch (Exception e) {
            e.printStackTrace();
        }


        debug = true;
        elevatorIndex =0;
    }

    /**
     * Door opens to either receive or dismiss users for a certain time frame, and then closes the door
     *Communicates its instructions to the elevator, allowing it to perform them.
     *
     * It sends the data as byte arrays inside DatagramPackets sent by DatagramSockets.
     *
     * @param elevatorId is the elevator being interacted with.
     * @param dest is the destination being added.
     */
    public synchronized void openDoor(int elevatorId, int dest) {
        byte[] dataLoad = new byte[3];
        dataLoad[0] = (byte) elevatorId;
        dataLoad[1] = 0;
        dataLoad[2] = 4;


        try {
            sendElevator(elevatorId, dataLoad);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        //press button
        byte[] dataAddDest = new byte[6];
        dataAddDest[0] = (byte) elevatorId;
        dataAddDest[1] = 0;
        dataAddDest[2] = 2;
        dataAddDest[3] = 0;
        dataAddDest[4] = (byte)dest;
        dataAddDest[5] = 0;


        try {
            sendElevator(elevatorId, dataAddDest);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public void setDebug(Boolean bool){debug = bool;}

    /**
     * Stop an elevator at a specified floor.
     * This function tells the elevator to stop via datagramPackets sent over DatagramSockets.
     *
     * @param floorNum
     * @param elevatorId
     */
    public void loadElevator(int elevatorId, int floorNum) {
        byte[] data = new byte[3];
        data[0] = (byte) elevatorId;
        data[1] = (byte) 0;
        data[2] = (byte) 4;

        try {
            sendElevator(elevatorId, data);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public void setSchedulerState(SchedulerState schedulerState) {
        this.schedulerState = schedulerState;
    }

    public SchedulerState getSchedulerState() {
        return this.schedulerState;
    }

    /**
     * Stop an elevator at a specified floor.
     * This function tells the elevator to stop via datagramPackets sent over DatagramSockets.
     * @param floorNum
     * @param elevatorId
     */
    public synchronized void stopElevatorAtFloor(int elevatorId, int floorNum) { // From arrival sensor / floor Subsystem run()
        byte[] data = new byte[5];
        data[0] = (byte) elevatorId;
        data[1] = 0;
        data[2] = 1;
        data[3] = 0;
        data[4] = (byte)floorNum;

        try {
            sendElevator(elevatorId, data);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        byte[] tempByte = new byte[4];
        recieveElevatorPacket = new DatagramPacket(tempByte, tempByte.length);
        try {
            recieveElevatorSocket.receive(recieveElevatorPacket);
        } catch (IOException e) {
            e.printStackTrace();

        }


        byte[] ack = "Acknowledgement".getBytes();
        try{
            DatagramPacket recieveFloorPacket  =new DatagramPacket(ack, ack.length, InetAddress.getByName("localhost"), 5002);
            recieveFloorSocket.send(recieveFloorPacket);
        } catch(IOException e) {
            e.printStackTrace();
            System.exit(1);
        }



        byte[] dataFromElevatorPacket = recieveElevatorPacket.getData();
        Boolean bool = dataFromElevatorPacket[2] == (floorNum);


        if (debug) {System.out.println("stopElevatorAtFloor while loop cond " + bool);}
        while (!(dataFromElevatorPacket[2] == floorNum)) {
            try {
                wait();

            } catch (InterruptedException e) {
                System.err.print(e);
                e.printStackTrace();
            }
        }


        if (debug) {System.out.println("Send a packet to make elevator call its own version of stopElevatorAtFloor method");}
        byte[] dataStopElevator = new byte[7];
        dataStopElevator[0] = (byte) elevatorId;
        dataStopElevator[1] = 0;
        dataStopElevator[2] = 6;
        dataStopElevator[3] = 0;
        dataStopElevator[4] = (byte)floorNum;
        dataStopElevator[5] = 0;
        dataStopElevator[6] = 0;

        try {
            sendElevator(elevatorId, dataStopElevator);
        } catch (UnknownHostException | InterruptedException e) {
            e.printStackTrace();
        }
        try {
            sendElevator(elevatorId, data);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        //Need to get the destinations again

        byte[] dataGetDest = new byte[3];
        dataGetDest[0] = (byte) elevatorId;
        dataGetDest[1] = 0;
        dataGetDest[2] = 5;


        try {
            sendElevator(elevatorId, dataGetDest);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }


        //Retrieve dest from packet

        byte[] tempByteDest = new byte[23];
        recieveElevatorPacket = new DatagramPacket(tempByteDest, tempByteDest.length);
        try {
            recieveElevatorSocket.receive(recieveElevatorPacket);
        } catch (IOException e) {
            e.printStackTrace();

        }

        byte[] dests = recieveElevatorPacket.getData();

        openDoor(elevatorId, dests[0]); 


        byte[] dataremove = new byte[3];
        dataremove[0] = (byte) elevatorId;
        dataremove[1] = 0;
        dataremove[2] = 8;
        dataremove[1] = 0;
        dataremove[2] = dests[0];

        try {
            sendElevator(elevatorId, dataremove);
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    public synchronized void deactivateElevator(Elevator elevator) {
        elevator.setEvent(false);
    }


    /**
     * Allows the elevator to move until it has no more destinations to go
     *
     * @param
     */
    public void activeState(ArrayList<Integer> elevatorList, int elevatorIndex) {
        Boolean goToIdleBoolean = true;
        if (debug) {System.out.println("In scheduler active state function");}
        for(int i = 0; i < elevatorList.size(); i++) {

            //CREATE PACKET TO ASK FOR ALL DESTINATIONS OF A ELEVATOR

            byte[] data = new byte[3];
            data[0] = (byte) i;
            data[1] = 0;
            data[2] = (byte) 10;
            data[3] = 0;


            try {
                if (debug) {
                    System.out.print("DEBUG >>> active state is moving? data: ");
                    for (int j = 0; j < data.length; j++) {
                        System.out.print(data[j]);

                    }
                    System.out.println();
                }
                sendElevator(i, data);
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            byte[] tempByteDest = new byte[4];
            recieveElevatorPacket = new DatagramPacket(tempByteDest, tempByteDest.length);
            try {
                recieveElevatorSocket.receive(recieveElevatorPacket);
            } catch (IOException e) {
                e.printStackTrace();

            }

            byte[] dataIsMoving = recieveElevatorPacket.getData();

            if(dataIsMoving[2] == 1) {
                goToIdleBoolean = false;
            }

        }

        if(goToIdleBoolean) {
            setSchedulerState(SchedulerState.IDLE_STATE);
        } else {
            setSchedulerState(SchedulerState.ACTIVE_STATE);
        }
    }


    /**
     * This function takes in a event datagramPacket that is sent over a datagramSocket,
     * decodes that event into another array which it can interact with it, and sends the information required
     * to elevator that is required, such as sending one of the elevator its next destination.
     *
     * It also goes through the possible elevators, and gets their data over the sockets in order to determine which elevator would
     * have the most efficient method of accepting the floor's call.
     * @return
     */
    public int receiveFloor() {
        long startRF = System.nanoTime();
        int floorPort;
        if (debug) {
            System.out.println("DEBUG >> In schedulers recieve Floor packet function");
        }
        if (debug) {
            System.out.println("DEBUG >> Scheduler State: " + schedulerState);
        }

        byte[] data = new byte[8]; //issue


        try {
            //
            recieveFloorPacket = new DatagramPacket(data, data.length);
            recieveFloorSocket.receive(recieveFloorPacket);
            floorPort = recieveFloorPacket.getLength();
            if (debug) {
                System.out.println("DEBUG >> Port: " + floorPort);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        byte[] floordata;
        floordata = recieveFloorPacket.getData();
        fault = floordata[7];


        if (fault == 1) {
            long t1 = System.nanoTime();
            double time = (t1 - start) / Math.pow(10, 9);
            System.out.println("Fault detected: Door is stuck open (" + time + "s) ");
            faultToGUI(fault, time);
        }


        if (fault == 2) {
            long t2 = System.nanoTime();
            double time = (t2 - start) / Math.pow(10, 9);
            System.out.println("Fault Detected: Took elevator too long to reach destination (" + time + "s) ");
            faultToGUI(fault, time);

        }

        if (fault == 3) {
            long t3 = System.nanoTime();
            double time = (t3 - start) / Math.pow(10, 9);
            System.out.println("Fault detected: Floor timer exceeded expected time. Elevator will now shut off (" + time + "s) ");
            faultToGUI(fault, time);
            try {
                wait();
            } catch (InterruptedException e1) {

            }

        }
            if (debug) {
                System.out.print("DEBUG >> Data Recieved from F.SS:");

                for (int i = 0; i < floordata.length; i++) {
                    System.out.print(floordata[i]);

                }
                System.out.println("");
                System.out.println("Fault is: " + fault);
            }


            // IF its 8 bytes long, then its a event packet
            if (floordata.length == 8) {

                if (floordata[7] == 1) {
                    System.out.println("\nFault: There is fault in event data provided");

                }


                String floorNumberS = null;
                String elevDirectionS = null;
                String elevDestS = null;
                try {
                    floorNumberS = String.valueOf(floordata[1]); 
                    elevDirectionS = String.valueOf(floordata[3]);
                    elevDestS = String.valueOf(floordata[5]);
                    if (debug) {
                        System.out.println(" Recieve Floor Data from packet: ");
                    }

                } catch (ArrayIndexOutOfBoundsException e) {
                    System.out.println("Error: No packet sent");
                }
                byte[] tempData = new byte[3];
                tempData[0] = 0;
                tempData[1] = 0;
                tempData[2] = 7;

                byte[] recieveTempData = new byte[16];
                try {
                    sendElevator(elevatorIndex, tempData);
                    recieveElevatorPacket = new DatagramPacket(recieveTempData, recieveTempData.length);
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                    if (debug) {
                        System.out.print("DEBUG >> 007 Req All pos: recieveElevatorPacket.getData(): ");

                        for (int i = 0; i < recieveElevatorPacket.getData().length; i++) {
                            System.out.print(recieveElevatorPacket.getData()[i]);

                        }
                        System.out.println();
                    }
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long endRF = System.nanoTime();
                System.out.println("Timing of RecieveFloor: " + (endRF - startRF) + ", start = " + startRF + ", end = " + endRF);

                long startBE = System.nanoTime();


                int[] locations = new int[4];

                locations[0] = recieveTempData[0];
                locations[1] = recieveTempData[2];
                locations[2] = recieveTempData[4];
                locations[3] = recieveTempData[6];


                int[] directions = new int[4];
                directions[0] = recieveTempData[8];
                directions[1] = recieveTempData[10];
                directions[2] = recieveTempData[12];
                directions[3] = recieveTempData[14];


                byte[] destinations = new byte[23];
                byte[] destinations2 = new byte[23];
                byte[] destinations3 = new byte[23];
                byte[] destinations4 = new byte[23];
                //ask for destinations
                byte[] tempDest1 = new byte[3];
                tempDest1[0] = 0;
                tempDest1[1] = 0;
                tempDest1[2] = 5;
                byte[] tempDest2 = new byte[3];
                tempDest2[0] = 1;
                tempDest2[1] = 0;
                tempDest2[2] = 5;
                byte[] tempDest3 = new byte[3];
                tempDest3[0] = 2;
                tempDest3[1] = 0;
                tempDest3[2] = 5;
                byte[] tempDest4 = new byte[3];
                tempDest4[0] = 3;
                tempDest4[1] = 0;
                tempDest4[2] = 5;

                byte[] recievedesttemp = new byte[23];
                try {
                    recieveElevatorPacket = new DatagramPacket(recievedesttemp, recievedesttemp.length);
                    System.out.println("--------------------------");

                    sendElevator(0, tempDest1); 
                    System.out.println("--------------------------");
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                    System.out.println("--------------------------");
                    destinations = recieveElevatorPacket.getData();

                    if (debug) {
                        System.out.print("DEBUG >> Setting move packets of elevators in receive floor: destinations: ");

                        for (int i = 0; i < destinations.length; i++) {
                            System.out.print(destinations[i] + " ");

                        }
                    }

                    sendElevator(1, tempDest2);
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                    destinations2 = recieveElevatorPacket.getData();

                    sendElevator(2, tempDest3);
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                    destinations3 = recieveElevatorPacket.getData();

                    sendElevator(3, tempDest4);
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                    destinations4 = recieveElevatorPacket.getData();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                //Algorithm to decide what elevator is the best for a event call.
                int sum = 0;
                int bestElevator = -1;
                int shortestDistance = 22;

                ArrayList<byte[]> destList = new ArrayList();
                destList.add(destinations);
                destList.add(destinations2);
                destList.add(destinations3);
                destList.add(destinations4);

                int floorloc = Integer.parseInt(floorNumberS);
                int floorDir = Integer.parseInt(elevDirectionS);
                for (int i = 0; i < locations.length; i++) {
                    //if no elevator is assigned, sign it to elevator, then try to find better elevator to replace it.
                    if (bestElevator == -1){
                        bestElevator = i;
                        shortestDistance = Math.abs(locations[i] - floorloc);
                    }else{
                        int floorDiff = floorloc-locations[i];
                        if(directions[i] == 0 || directions[i]*floorDiff>0){
                            if(shortestDistance > Math.abs(floorDiff)){
                                shortestDistance = Math.abs(floorDiff);
                                bestElevator = i;
                            }
                        }
                    }
                }
                if (debug) {
                    System.out.println("DEBUG >>> Request from floor, best elevator: " + bestElevator);
                }
                byte[] information = new byte[9];
                information[0] = (byte) bestElevator;
                information[1] = (byte) 0;
                information[2] = (byte) 3;
                information[3] = (byte) 0;
                information[4] = (byte) Integer.parseInt(elevDirectionS);
                information[5] = (byte) 0;
                information[6] = (byte) floorloc;
                information[7] = (byte) 0;
                information[8] = (byte) fault;
                long endBE = System.nanoTime();
                System.out.println("Timing of Best Elevator: " + (endBE - startBE) + ", start = " + startBE + ", end = " + endBE);
                long startSM = System.nanoTime();

                if (debug) {
                    System.out.print("DEBUG >> Setting move packets of elevators in receive floor: information: ");

                    for (int i = 0; i < information.length; i++) {
                        System.out.print(information[i] + " ");

                    }
                }
                try {
                    if (debug) {
                        System.out.println("Line 635 Send Elevator");
                    }
                    sendElevator(information[0], information);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                long endSM = System.nanoTime();
                System.out.println("Timing of move elevator: " + (endSM - startSM) + ", start = " + startSM + ", end = " + endSM);
            }


            //-----------------


            // IF its 4 bytes long, then its a stopElevatorAt packet
            else if (floordata.length == 4) {
                if (debug) {
                    System.out.println("DEBUG >> recieveFloor Else if Len = 4");
                }
                String floorString = new String(recieveFloorPacket.getData());
                String[] tempS = floorString.split("0");

                long start = System.nanoTime();
                stopElevatorAtFloor(Integer.parseInt(tempS[0]), Integer.parseInt(tempS[1]));
                long end = System.nanoTime();


                if (fault == 3) {
                    System.out.println("Fault detected: Elevetor took more time than expected to reach its destination");
                    byte[] msg = new byte[3];
                    msg[0] = 2;
                    msg[1] = 3;
                    try {
                        DatagramSocket GUISocket = new DatagramSocket();
                        DatagramPacket packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(), 4000);
                        GUISocket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }

            }


        return -1;

        }

        public void faultToGUI(int fault, double time){
            byte[] msg = new byte[20];
            msg[0] = 2;
            msg[1] = (byte)fault;
            byte[] num = new byte[8];
            num = Double.toString(time).getBytes();
            for(int i =0; i< num.length; i++){
                msg[i+2] = num[i];
            }


            try {
                DatagramSocket socket = new DatagramSocket();
                DatagramPacket packet = new DatagramPacket(msg, msg.length,InetAddress.getLocalHost(), 4000);
                socket.send(packet);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    /**
     * This is the helper method created to simplify sending data to the elevator subsystem since we are doing it everywhere on multiple
     * occasions.
     * @param selectedElevator is the elevator id (and index in the Elevator subsystems list) for easy referencing.
     * @param info is the byte array data that you would like to insert into a datagramPacket and by extension, the datagramSocket.
     * @throws UnknownHostException Can be caused if the localhost is not configured correctly.
     * @throws InterruptedException can be caused if threads are able to cause a deadlock.
     */
    public int sendElevator(int selectedElevator, byte[] info) throws UnknownHostException, InterruptedException {
        if (selectedElevator > 3){ // FAIL COND
            System.out.println("------------------ sel Elev: " + selectedElevator);
            return -1;
        }
        else{
            try {
                if (debug) {
                    System.out.print("DEBUG >> Send Elevator Data:");

                    for (int i = 0; i < info.length; i++) {
                        System.out.print(info[i]);

                    }
                    System.out.println();
                }
                if (debug) {System.out.println("DEBUG >>> Elevator index: " + selectedElevator);}

                sendElevatorPacket = new DatagramPacket(info, info.length, InetAddress.getByName("localhost"), 5001);
                sendElevatorSocket.send(sendElevatorPacket);
                if (debug) {System.out.println("DEBUG >> Packet sent from SendElevator f(x).");}
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }


            catch (NullPointerException e) {
            	System.err.print(e);
                System.out.println("Nothing yet.");
            }

            byte[] ack = "Acknowledgement".getBytes();
            try {
                DatagramPacket ackPacket = new DatagramPacket(ack, ack.length, InetAddress.getByName("localhost"), 5001);
                recieveElevatorSocket.send(ackPacket);
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
            return 0;
        }
    }


    @Override
    public void run() {
        // Get interrupted when : Floor calls elevator through scheduler;     elevatorButton pressed to add destination;
        // after  planElevatorTrip() scheduling, set elevator.isEvent = true
    	
        start = System.nanoTime();

        Boolean run = true;
        while (run) {
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            receiveFloor();
            if (debug) {System.out.println("DEBUG >>Scheduler State " + schedulerState);}

            if (schedulerState == SchedulerState.ACTIVE_STATE) {

                activeState(elevatorList, elevatorIndex);
            }

            for (int i = 0; i < 3; i++) {
                byte[] dataGetDest = new byte[3];
                dataGetDest[0] = (byte) i;
                dataGetDest[1] = 0;
                dataGetDest[2] = 5;
                if (debug) {System.out.println("DEBUG >> Scheduler RUN DataGetDest[0] = byte(ElevId): " + dataGetDest[0] + " And elevId as int: " + elevatorIndex);}

                try {
                    sendElevator(elevatorIndex, dataGetDest);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //Retrieve dest from packet

                byte[] tempByteDest = new byte[23];
                recieveElevatorPacket = new DatagramPacket(tempByteDest, tempByteDest.length);
                try {
                    recieveElevatorSocket.receive(recieveElevatorPacket);
                } catch (IOException e) {
                    e.printStackTrace();

                }

                byte[] dests = recieveElevatorPacket.getData();


                if (debug) {System.out.println("Scheduler RUN Dest length: " + dests.length);}
                if (dests.length == 23) {
                    schedulerState = SchedulerState.IDLE_STATE;
                    activeState(elevatorList, elevatorIndex);
                    byte[] tempData = new byte[3];
                    tempData[0] = 0;
                    tempData[1] = 0;
                    tempData[2] = 7;

                    byte[] recieveTempData = new byte[16];
                    try {
                        
                        sendElevator(elevatorIndex, tempData);

                        recieveElevatorPacket = new DatagramPacket(recieveTempData, recieveTempData.length);
                        recieveElevatorSocket.receive(recieveElevatorPacket);
                        if (debug) {
                            System.out.print("DEBUG >> 007 Req All pos: recieveElevatorPacket.getData(): ");
                            for(int j = 0; j < recieveElevatorPacket.getData().length; j++){
                                System.out.print(recieveElevatorPacket.getData()[j]);

                            }
                            System.out.println();
                        }
                    } catch (UnknownHostException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    int[] locations = new int[4];

                    locations[0] = recieveTempData[0];
                    locations[1] = recieveTempData[2];
                    locations[2] = recieveTempData[4];
                    locations[3] = recieveTempData[6];

                    if (dests[0] == locations[i]){
                        loadElevator(i, locations[i]);
                    }

                }
                else{
                    long end = System.nanoTime();
                    long total = end - start;

                    System.out.println("Total time of the scheduler: " + total / Math.pow(10,9) + " s");
                    run = false;
                }
            }

        }



    }

    public int getFault() {
        return fault;
    }

    public void setFault(int fault) {
        this.fault = fault;
    }



    public static void main(String[] args) throws FileNotFoundException {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enable Debug? 1/0?");

        String debug = myObj.nextLine();  // Read user input


        int[] elevators= new int[4];
        for (int i = 0; i < elevators.length; i++){elevators[i]=i;}
        Scheduler scheduler = new Scheduler();
        if (Integer.parseInt(debug) == 0){scheduler.setDebug(false);}
        System.out.println("Scheduler Activated.");

        if (scheduler.debug) { System.out.println("DEBUG >>  Scheduler State: " + scheduler.schedulerState); }
        long start = System.nanoTime();

        scheduler.start();

        long end = System.nanoTime();

        System.out.println("Scheduler took " + (end - start) + " nanoseconds");

    }
}




