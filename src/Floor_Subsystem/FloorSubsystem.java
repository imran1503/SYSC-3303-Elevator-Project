package Floor_Subsystem;

import Elevator_Subsystem.Elevator;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.ArrayList;


public class FloorSubsystem extends Thread {
    private final BufferedReader file;
    private ArrayList<Floor> floors;
    static DatagramPacket ackPacket;

    DatagramPacket receiveServerPacket;

    DatagramSocket sendElevatorSocket;
    DatagramSocket receiveElevatorSocket;
    DatagramSocket sendServerSocket;
    DatagramSocket receiveServerSocket;

    private final ArrayList<Event> eventArrayList;
    int fault;

    /**
     * Creates an arraylist of floors and Initializes send and receive
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
            sendElevatorSocket = new DatagramSocket();
            receiveElevatorSocket = new DatagramSocket(5005);

            sendServerSocket = new DatagramSocket();
            receiveServerSocket = new DatagramSocket(5004);
        } catch (SocketException se) {   // Can't create the socket.
            se.printStackTrace();
            System.exit(1);
        }
    }

    public BufferedReader getFile() {
        return file;
    }

    // read time in "hh:mm:ss.mmm" format and convert it to milliseconds
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
                floorButton = floors.get(floorNumber).getButtons().get(1);

            } else {
                floorButton = floors.get(floorNumber).getButtons().get(0);
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
        System.out.println("is Event valid?   " + event.isValid());
        return event;
    }

    public static void main(String[] args) throws FileNotFoundException, SocketException {

        int amountOfFloorsinBuilding = 22;

        ArrayList<Elevator> elevators = new ArrayList<>();
        //get elevators from elevator ss via packets
//        elevators.add(elevator);

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

        floorSS.run();
    }


    /**
     * This method creates a packet of the floor events
     *
     * @param packetID
     * @return tempPacket
     * @throws IOException
     */
    public DatagramPacket createPacket(int packetID) throws IOException {
        System.out.println("DEBUG >> In create Packet for making a event");
        DatagramPacket tempPacket;
        byte[] tempByteArray = new byte[9];
        byte[] ack = "acknowledgment".getBytes();
        long startCP = System.nanoTime();

        if (packetID == 0) {
            //This will create a packet for each event.

            while (file != null && file.ready()) {
                Event event = readEvent(file.readLine());
                eventArrayList.add(event);
                System.out.println("In floor while loop for reading in packets");
            }

            tempByteArray[0] = (byte) 0;
            tempByteArray[1] = (byte) (eventArrayList.get(0).getFloorNumber());
            tempByteArray[2] = (byte) (0);
            tempByteArray[3] = (byte) (eventArrayList.get(0).getFloorButton().getDirection());
            tempByteArray[4] = (byte) (0);
            tempByteArray[5] = (byte) (eventArrayList.get(0).getCarButton());
            tempByteArray[6] = (byte) (0);

            if (!eventArrayList.get(0).isValid()) {
                tempByteArray[7] = (byte) (1);
            } else {
                tempByteArray[7] = (byte) (2);
            }
            tempByteArray[8] = (byte) fault;


            System.out.println("DEBUG >> CreatePacketTest");
            for (int i = 0; i < tempByteArray.length; i++) {
                System.out.print(tempByteArray[i]);

            }
            System.out.println();
            tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5003);
            long endCP = System.nanoTime();
            System.out.println("Timing of RecieveFloor: " + (endCP - startCP) + ", start = " + startCP + ", end = " + endCP);
            return tempPacket;
        } else {
            return null;
        }
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
        System.out.println("DEBUG >> In create Packet for stopping a elevator");
        DatagramPacket tempPacket;
        byte[] tempByteArray = new byte[4];
        byte[] ack = "acknowledgment".getBytes();


        //This will create a packet for each event.

//            while (file != null && file.ready()) {
//                //scheduler.setAvailable(true);
//                Event event = readEvent(file.readLine());
//                
//                if(event != null) {
//                	hasFaultInEvent = true;
//                }
//                
//                eventArrayList.add(event);
//                System.out.println("In floor while loop");
//                if ("".equals(file.readLine())) {
//                    break;
//                }
//            }

        tempByteArray[0] = (byte) elevatorIndex;
        tempByteArray[1] = (byte) (0);

        tempByteArray[2] = (byte) floorNum;
        tempByteArray[3] = (byte) (0);
        tempByteArray[4] = (byte) fault;


        tempPacket = new DatagramPacket(tempByteArray, tempByteArray.length, InetAddress.getLocalHost(), 5002);
        return tempPacket;

    }


    /**
     * Sends created event packets to the scheduler.
     */
    public void run() {
        System.out.println("DEBUG >> Run method");
        try {

            while (true) {
                // Sends Create packet to scheduler
                byte[] temp = createPacket(0).getData();
                System.out.println("DEBUG >> run temp length: " + temp.length);
                System.out.println("DEBUG >> CreatePacketTest");
                for (int i = 0; i < temp.length; i++) {
                    System.out.print(temp[i]);
                }
                System.out.println();

                sendServerSocket.send(createPacket(0));
                long sendCp = System.nanoTime();
                System.out.println("Timing of send cp " + sendCp);
                //ASK ELEVATORS WHERE THEY ARE


                //Receives ack packet from floor
                byte[] ack = "Acknowledgement".getBytes();
                receiveServerPacket = new DatagramPacket(ack, ack.length);//empty packet created


                receiveServerSocket.receive(receiveServerPacket);


                System.out.println("out of floor loop 1");


            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("out of floor loop 2");

//
//        for (int i = 0; i < eventArrayList.size() ; i++) {
//            try {
//                System.out.println(floors.get(eventArrayList.get(i).getFloorNumber()).getElevators().get(0).getDestinations());
//
//                scheduler.callElevator(eventArrayList.get(i),floors.get(eventArrayList.get(i).getFloorNumber()).getElevators().get(0), floors.get(eventArrayList.get(i).getFloorNumber()));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }


         


        System.out.println("Floor subsystem pre Loop check ");
        for (int i = 0; i < floors.size(); i++) {
            if (floors.get(i).getArrivalSensors().get(i).getArrivalSensorData(sendElevatorSocket)) {

                try {
                    createPacket(floors.get(i).getElevators().get(0).getId(), floors.get(i).getFloorNumber());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // scheduler.stopElevatorAtFloor(floors.get(i).getElevators().get(0),floors.get(i).getFloorNumber() );

                System.out.println("Stop elevator from sensor");
            } 
              else{}


        }


    }
}
