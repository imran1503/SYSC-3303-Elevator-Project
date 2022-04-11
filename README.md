# SYSC-3303 Elevator-Simulator
### Version: 5.0
### Authors:
- Imran Latif: @imran1503
- Duabo Dagogo Longjohn: @duabolongjohn
- Sukhrobjon Eshmirzaev: @Socrates2000-cu
- Mfumu, Ismael: @Deluxe395
- Qihang Peng: Qihangpeng

### Iteration goals
Our goal for this iteration was to create a graphical user interface while also rushing to complete establishing connections among subsystems using RCP which is an application of UDP.


### Deliverables
The deliverables for this milestone is as follows: readme file, the JUnit test files,
source codes in the form of java files, UML diagrams, state machines, documentation and breakdown of responsibilities.				

For this iteration we have three packages; Elevator_Subsystem, Floor_Subsytem and Scheduler.
We also have 3 main classes in each subsystem which initialize their components. 

### Elevator_Subsystem
The various classes in this package are the: Elevator, ElevatorButton, ElevatorDoor, ElevatorLamp,
ElevatorMotor and ElevatorTest. These classes contain methods and functions that concern the various 
parts of the Elevator system. They also include the concrete state sub classes for the following states: 

State 1: The Elevator is idle. this means that the elevator is not completing a task at the moment due to no
destination requests.

State 2: the Elevator initializes itself by assigning the elevator the desired destinations and get the 
associated values before it starts moving.

State 3: The elevator is active. this means that the elevator is currently in motion and its going to its
next desired destination selected by the passenger.

State 4: The elevator is loading. This state represents when the elevator is not moving and the doors are
open. It allows the passengers to get on board or off in a timely matter. 

We created 2 sockets to communicate with the other subsytems, one for sending and one for receiving. 
We created elevatorAction algorithm to decode all packets and perform the required functionality.


### Floor_Subsytem
The various classes in this package are the: Floor, FloorButton, FloorLamp, FloorTest, DirectionLamp,
Event and ArrivalSensor. These classes contain various methods and functions that concern the various
parts of the Floor system.

We created 2 sockets to communicate with the other subsytems, one for sending and one for receiving.
Floor subsystem is responsible for sending data to scheduler to start the program.

### Scheduler
The Scheduler package has the Scheduler and SchedulerTest, these classes deal with the control of communication
between the Elevator_Subsystem and Floor_Subsystem. They also include the concrete state sub classes for the following states: 

State 1: The scheduler is idle. This state represents when there are no events that the scheduler has 
to send to the elevator. 

State 2: The scheduler is now active. This happens when the floor sends ad arrival sensor to the elevator 
and continues to stay active until the elevator stops and there are no events to compute.

We created 2 sockets to communicate with the other subsytems, one for sending and one for receiving.
It encodes the required actions and send them to the corresponding subsystem which then decodes and perform the reuired actions

### GUI
The GUI package contains several components used to display elevator system.
GUI has a thread that constantly receives message from elevator subsystem and floor subsystem to update display.
GUI needs to be run to display elevator system.
GUITest is a test that passes some message to GUI to test if the GUI works(it need to be run after GUI so GUI can receive the messages). 


### Issues
1. An issue that has been facing is the fact that when our scheduler completes all of its tasks,
   it prints out that there are no more events on a continuous loop. 
2. Facing IndexOutOfBoundsException at the end of the program
3. JavaFx is not included in Java 11. IDE need to be set up to use JavaFx. 

### Set-Up Instructions
In order to run the program, run each of the subsystem's Main class as a Java application in the order provided below. Make sure that 
the file has the desired parameters and that it can be accessed through this project. 
the project will run and in order to know when it has completed, it will reveal that there is 
no more events that can be run at the time. 

Run the subsystems in the following order:
1. ElevatorSubsystem.java 
2. GUI.java
3. Scheduler.java
4. FloorSubsystem.java

