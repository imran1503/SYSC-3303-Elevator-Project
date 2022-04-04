package javafxGUI;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import static java.lang.Thread.sleep;

public class GUITest {
    public static void main(String[] args){
        DatagramSocket socket = null;
        DatagramPacket floorPacket;
        DatagramPacket elevatorPacket;
        byte[] floorMSG = new byte[45];
        byte[] elevatorMSG = new byte[100];
        floorMSG[0] = 1;
        elevatorMSG[0] = 0;

        //some floor lamps
        floorMSG[22] = 1;
        floorMSG[40] = 1;
        floorMSG[21] = 1;
        //elevator 1
        elevatorMSG[1] = 16;
        elevatorMSG[2] = 1;
        //elevator 2, 3, 4
        elevatorMSG[25] = 6;
        elevatorMSG[49] = 22;
        elevatorMSG[73] = 13;
        //some elevator buttons
        elevatorMSG[74] = 1;
        elevatorMSG[20] = 1;
        elevatorMSG[3] = 1;

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        try {
            socket = new DatagramSocket();
            floorPacket = new DatagramPacket(floorMSG, floorMSG.length, InetAddress.getLocalHost(), 4000);
            elevatorPacket = new DatagramPacket(elevatorMSG, elevatorMSG.length, InetAddress.getLocalHost(), 4000);
            socket.send(floorPacket);
            socket.send(elevatorPacket);
        }catch(IOException e){
            System.err.println(e);
        }

        try {
            sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        floorMSG[22] = 0;
        floorMSG[40] = 0;
        floorMSG[21] = 0;
        floorMSG[16] = 1;
        floorMSG[3] = 0;
        elevatorMSG[1] = 16;
        elevatorMSG[2] = 1;
        elevatorMSG[20] = 0;
        elevatorMSG[3] = 0;
        elevatorMSG[1] = 18;
        elevatorMSG[25] = 2;
        elevatorMSG[60] = 1;

        try {
            floorPacket = new DatagramPacket(floorMSG, floorMSG.length, InetAddress.getLocalHost(), 4000);
            elevatorPacket = new DatagramPacket(elevatorMSG, elevatorMSG.length, InetAddress.getLocalHost(), 4000);
            socket.send(floorPacket);
            socket.send(elevatorPacket);
        }catch(IOException e){
            System.err.println(e);
        }
    }
}
