package Floor_Subsystem;

import Elevator_Subsystem.Elevator;

import java.io.*;
import java.net.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Scanner;


public class FloorSubsystem extends Thread {
    private final BufferedReader file;
    private ArrayList<Floor> floors;
    private DatagramPacket ackPacket;

    DatagramPacket receiveServerPacket;

    DatagramSocket sendElevatorSocket;
    DatagramSocket receiveElevatorSocket;
    DatagramSocket sendServerSocket;
    DatagramSocket receiveServerSocket;

    private final ArrayList<Event> eventArrayList;
    int fault;
    boolean debug;

    /**
     * Creates an array list of floors and Initializes send and receive
     * Sockets for the scheduler and elevator
     *
     * @param floorArrayList
     * @param file
     */
    public FloorSubsystem(ArrayList<Floor> floorArrayList, BufferedReader file) throws SocketException {
        floors = new ArrayList<>();
        floors = floorArrayList;
        this.file = file;
        int fault = 0;
        this.eventArrayList = new ArrayList<>();
        try {
        	readFile();
            sendElevatorSocket = new DatagramSocket();
            receiveElevatorSocket = new DatagramSocket(5005);

            sendServerSocket = new DatagramSocket();
            receiveServerSocket = new DatagramSocket(5004);
        } catch (SocketException se) {   // Can't create the socket.
            se.printStackTrace();
            System.exit(1);
        } catch (IOException e) {
			e.printStackTrace();
			System.out.println("Failed to read file");
			System.exit(1);
		}
        debug = true;
    }

    public BufferedReader getFile() {
        return file;
    }

    
    /**
     * read time in "hh:mm:ss.mmm" format and convert it to milliseconds
     * @param str
     * @return
     */
    public long timeToMilliseconds(String str) {
        long milliseconds = 0;
        String[] st = str.split(":");

        milliseconds += Long.valueOf(st[0]) * 60 * 60 * 1000;
        milliseconds += Long.valueOf(st[1]) * 60 * 1000;
        milliseconds += Long.valueOf(st[2].substring(0, st[2].indexOf("."))) * 1000;
        milliseconds += Long.valueOf(st[2].substring(st[2].indexOf(".") + 1));

        return milliseconds;
    }


    /**
     * Change string elevator directions into an Event.
     *
     * @param line
     * @return event
     */
    public Event readEvent(String line) {
        Event event;
        Timestamp timestamp;
        int floorNumber, carButton, direction;
        FloorButton floorButton = null;
        boolean valid = true;

        String[] words = line.split(" ");
        fault = Integer.parseInt(words[4]);

        System.out.println("Fault is: " + fault);
        timestamp = new Timestamp(timeToMilliseconds(words[0]));
        floorNumber = Integer.parseInt(words[1]);
        direction = words[2].toLowerCase().equals("up") ? 1 : 0;

        if (floors.get(floorNumber).getButtons().size() == 2) {
            if (words[2].toLowerCase().equals("up")) {
                floorButton = floors.get(floorNumber).getButtons().get(0);

            } else {
                floorButton = floors.get(floorNumber).getButtons().get(1);
            }
        } else if (floors.get(floorNumber).getButtons().size() == 1) {
            floorButton = floors.get(floorNumber).getButtons().get(0);
        }

        carButton = Integer.parseInt(words[3]);

        event = new Event(timestamp, floorNumber, floorButton, carButton);

        if (floorNumber < 0 || floorNumber > 22 || carButton < 0 || carButton > 22) {
            valid = false;
        }

        if (direction == 0) {
            if (carButton > floorNumber) {
                valid = false;
            }
        } else if (direction == 1) {
            if (carButton < floorNumber) {
                valid = false;
            }
        }

        event.setValidity(valid);
        if (debug) {System.out.println("is Event valid?   " + event.isValid());}
        return event;
    }


    public void setDebug(Boolean bool){debug = bool;}

    public static void main(String[] args) throws FileNotFoundException, SocketException {
        Scanner myObj = new Scanner(System.in);  // Create a Scanner object
        System.out.println("Enable Debug? 1/0?");

        String debug = myObj.nextLine();  // Read user input
        int amountOfFloorsinBuilding = 22;

        ArrayList<Elevator> elevators = new ArrayList<>();

        //create floor parts
        ArrayList<FloorLamp> floorLamps = new ArrayList<>();
        ArrayList<ArrivalSensor> arrivalSensors = new ArrayList<>();


        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
            FloorLamp templamp1 = new FloorLamp(i);
            floorLamps.add(templamp1);

            for (int j = 0; j < elevators.size(); j++) {
                ArrivalSensor sensor = new ArrivalSensor(elevators.get(i).getId(), i, j);
                arrivalSensors.add(sensor);
            }
        }

        //make packet for events
        File file1 = new File("src/events.txt");
        BufferedReader file = new BufferedReader(new FileReader(file1));

        ArrayList<Floor> floorsArrayList = new ArrayList<>();

        for (int i = 0; i < amountOfFloorsinBuilding; i++) {
            Floor floor = new Floor(i, floorLamps, amountOfFloorsinBuilding, arrivalSensors);
            floor.setElevators(elevators);
            floorsArrayList.add(floor);
        }

        FloorSubsystem floorSS = new FloorSubsystem(floorsArrayList, file);
        if (Integer.parseInt(debug) == 0) {
            floorSS.setDebug(false);
        }
        floorSS.run();
    }
    
    private void readFile() throws IOException{
    	while (file != null && file.ready()) {
            Event event = readEvent(file.readLine());
            eventArrayList.add(event);
            if (debug) {System.out.println("In floor while loop for reading in packets");}
        }
	}


    /**
     * This method creates a packet of the floor events
     *
     * @param packetID
     * @return tempPacket
     * @throws IOException
     */
    public DatagramPacket createPacket(int packetID) throws IOException {
    	if(packetID >= eventArrayList.size()) return null;
        if (debug) {System.out.println("DEBUG >> In create Packet for making a event");}
        DatagramPacket tempPacket;
        byte[] tempByteArray = new byte[9];
        byte[] ack = "acknowledgment".getBytes();
        long startCP = System.nanoTime();

            tempByteArray[0] = (byte) 0;
            tempByteArray[1] = (byte) (eventArrayList.get(packetID).getFloorNumber());
            tempByteArray[2] = (byte) (0);
            tempByteArray[3] = (byte) (eventArrayList.get(packetID).getFloorButton().getDirection());
            tempByteArray[4] = (byte) (0);
            tempByteArray[5] = (byte) (eventArrayList.get(packetID).getCarButton());
            tempByteArray[6] = (byte) (0);

            if (!eventArrayList.get(0).isValid()) {
                tempByteArray[7] = (byte) (1);
            } else {
                tempByteArray[7] = (byte) (2);
            }
            tempByteArray[8] = (byte) fault;


            if (debug) {
                System.out.println("DEBUG >> CreatePacketTest");
                for (int i = 0; i < tempByteArray.length; i++) {
                    System.out.print(tempByteArray[i]);

                }

                System.out.println();
            }
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5003);
            long endCP = System.nanoTime();
            System.out.println("Timing of createPacket: " + (endCP - startCP) + ", start = " + startCP + ", end = " + endCP);
            return tempPacket;
    }


    /**
     * This method creates a packet for stopping an elevator
     *
     * @param elevatorIndex
     * @param floorNum
     * @return tempPacket
     * @throws IOException
     */
    public DatagramPacket createPacket(int elevatorIndex, int floorNum) throws IOException {
        if (debug) {System.out.println("DEBUG >> In create Packet for stopping a elevator");}
        DatagramPacket tempPacket;
        byte[] tempByteArray = new byte[5];
        byte[] ack = "acknowledgment".getBytes();


        tempByteArray[0] = (byte) elevatorIndex;
        tempByteArray[1] = (byte) (0);

        tempByteArray[2] = (byte) floorNum;
        tempByteArray[3] = (byte) (0);
        tempByteArray[4] = (byte) fault;


        tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
        return tempPacket;

    }

    public byte[] codeGUIMSG(){
        byte[] msg = new byte[45];
        msg[0] = 1;
        for(int i =0;i<floors.size();i++){
            if(floors.get(i).getButtons().get(0).getPressed()){
                msg[i*2+1] = 1;
            }else{
                msg[i*2+1] = 0;
            }
            if(floors.get(i).getButtons().get(1).getPressed()){
                msg[i*2+1+1] = 1;
            }else{
                msg[i*2+1+1] = 0;
            }
        }
        return msg;
    }


    public DatagramPacket checkArrivalSensors(){
        byte[] data = new byte[24];

        DatagramPacket packet =  new DatagramPacket(data, data.length);
        return packet;
    }


    /**
     * Sends created event packets to the scheduler.
     */
    public void run() {
        Thread GUI = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] msg;
                msg = codeGUIMSG();
                DatagramSocket socket = null;
                try {
                    socket = new DatagramSocket();
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                while(true){
                    DatagramPacket packet = null;
                    try {
                        packet = new DatagramPacket(msg, msg.length, InetAddress.getLocalHost(),4000);

                        socket.send(packet);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        if (debug) {System.out.println("DEBUG >> Run method");}
        System.out.println("Floor Subsystem Activated.");
        try {
        	int index = 0;
        	if(debug) {
        		System.out.println("*********events size = " + eventArrayList.size());
        	}
            while (true) {
            	
            	DatagramPacket packetToSend = createPacket(index);

                long startAS = System.nanoTime();
            	checkArrivalSensors();
            	if(packetToSend == null) continue;
                byte[] temp = packetToSend.getData();
                if (debug) {
                    System.out.println("DEBUG >> run temp length: " + temp.length);
                    System.out.println("DEBUG >> CreatePacketTest");
                    for (int i = 0; i < temp.length; i++) {
                        System.out.print(temp[i]);
                    }
                    System.out.println();
                }
                System.out.println("*********Sending created packet ");

                sendServerSocket.send(packetToSend);
                long sendCp = System.nanoTime();
                System.out.println("DEBUG >> Timing of send cp " + sendCp);

                long endAS = System.nanoTime();
                if (debug){System.out.println("DEBUG >>Timing of Best Elevator: " + (endAS - startAS) + ", start = " + startAS + ", end = " + endAS);}

                //Receives ack packet from floor
                byte[] ack = "Acknowledgement".getBytes();
                receiveServerPacket = new DatagramPacket(ack, ack.length);//empty packet created
                index++;
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }



    }
}
