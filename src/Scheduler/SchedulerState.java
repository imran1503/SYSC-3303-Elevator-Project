package Scheduler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public enum SchedulerState {

    ACTIVE_STATE {
        @Override
        public SchedulerState change(int elevatorId, Scheduler scheduler) {
            try {
                
                //get elevator dests 


                byte[] dataGetDest = new byte[3];
                dataGetDest[0] = (byte) elevatorId;
                dataGetDest[1] = 0;
                dataGetDest[2] = 5;


                try {
                    scheduler.sendElevator(elevatorId, dataGetDest);
                }
                catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }


                //retireve dest from packet

                byte[] tempByteDest = new byte[23];
                scheduler.recieveElevatorPacket = new DatagramPacket(tempByteDest, tempByteDest.length);
                try {
                    scheduler.recieveElevatorSocket.receive(scheduler.recieveElevatorPacket);
                } catch (IOException e) {
                    e.printStackTrace();

                }

                byte[] dests = scheduler.recieveElevatorPacket.getData();
                
                //Get elevator position

                byte[] tempData = new byte[3];
                tempData[0] = 0;
                tempData[1] = 0;
                tempData[2] = 7;

                byte[] recieveTempData = new byte[16];
                try {
                    scheduler.sendElevatorPacket = new DatagramPacket(tempData, tempData.length, InetAddress.getByName("localhost"), 5002);
                    scheduler.sendElevatorSocket.send(scheduler.sendElevatorPacket);

                    scheduler.recieveElevatorPacket = new DatagramPacket(recieveTempData, recieveTempData.length);
                    scheduler.recieveElevatorSocket.receive(scheduler.recieveElevatorPacket);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                int[] locations = new int[4];
                recieveTempData = scheduler.recieveElevatorPacket.getData();
                locations[0] = recieveTempData[0];
                locations[1] = recieveTempData[2];
                locations[2] = recieveTempData[4];
                locations[3] = recieveTempData[6];
                
                if (locations[elevatorId] == dests[0]) {
                    System.out.println("Scheduler stop elevator if statement");

                    scheduler.stopElevatorAtFloor(elevatorId, dests[0]);

                    // elevator.getDestinations().remove(elevator.getDestinations().get(0));
                    // elevator.getDestinations().remove(elevator.getCurrentFloor());
                    byte[] dataremove = new byte[3];
                    dataremove[0] = (byte) elevatorId;
                    dataremove[1] = 0;
                    dataremove[2] = 8;
                    dataremove[1] = 0;
                    dataremove[2] = dests[0];

                    try {
                        scheduler.sendElevator(elevatorId, dataremove);
                    }
                    catch (UnknownHostException e) {
                        e.printStackTrace();
                    }
                    catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                } else {

                    byte[] dataStartElevator = new byte[7];
                    dataStartElevator[0] = (byte) elevatorId;
                    dataStartElevator[1] = 0;
                    dataStartElevator[2] = 6;
                    dataStartElevator[3] = 0;
                    dataStartElevator[4] = dests[0];
                    dataStartElevator[5] = 0;
                    dataStartElevator[6] = 1;

                    try {
                        scheduler.sendElevator(elevatorId, dataStartElevator);
                    } catch (UnknownHostException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                System.out.println("Scheduler elevator pos: " + locations[elevatorId]);
            }
            catch(IndexOutOfBoundsException e){
                scheduler.setSchedulerState(SchedulerState.IDLE_STATE);
            }

            return IDLE_STATE;
        }
    },

    IDLE_STATE {
        @Override
        public SchedulerState change(int elevator, Scheduler scheduler) {
            // elevator will receive a signal(event) to move to ACTIVE_STATE
            System.out.print("ELEVATOR AWAITING INSTRUCTION");
            byte[] data = new byte[3];
            byte[] recieveData = new byte[4];
            int elevatorMoving = -1;
            DatagramPacket recievePacket = new DatagramPacket(recieveData,recieveData.length);

            data[0] = (byte) elevator;
            data[1] = (byte) 0;
            data[2] = (byte) 10;
            try {
                scheduler.sendElevator(elevator, data);

                scheduler.recieveElevatorSocket.receive(scheduler.recieveElevatorPacket);
                recievePacket.setData(scheduler.recieveElevatorPacket.getData());
                elevatorMoving = recievePacket.getData()[2];
            }
            catch (UnknownHostException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (NullPointerException e) {
                e.printStackTrace();
            }
            if (elevatorMoving == -1){
                System.out.println("Error in changing scheduler state");
            }

            else if (elevatorMoving == 0){
                return ACTIVE_STATE;
            }

            return IDLE_STATE;

        }

    };

    public abstract SchedulerState change(int elevatorId, Scheduler scheduler);

}

