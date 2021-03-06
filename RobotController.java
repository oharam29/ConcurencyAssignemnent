package Ca4006;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.*;  
import Ca4006.*;

public class RobotController implements Runnable{
	private Queue<Robot> waitingRobots;
	private InProductionList InProduction;
	private ExecutorService ThreadPool;


	public RobotController(InProductionList inProductionList){
		this.waitingRobots = new LinkedList<Robot>();
		this.InProduction = inProductionList;
	}

	public void GenerateRobots(){
		List<Part> partsToBeInstalled = new ArrayList<Part>();  

		for(int i = 1; i < 6; i++){
			Robot newRobot = new Robot(i,partsToBeInstalled, InProduction);
			waitingRobots.add(newRobot);
		}

	}

	public void assignAndStartRobotJob(Robot robot, Aircraft aircraft){
		
		robot.SetRobotCurrentWorkPlan(aircraft.getAricraftWorkPlan());
		robot.SetAircraftID(aircraft.getAircraftId());

		System.out.println("Robot: " + robot.GetRobotID()+ " has a workplan of size: " + aircraft.getAricraftWorkPlan().size());
		System.out.println();

		System.out.println("Ready To start robot: " + robot.GetRobotID()+ " working on Aircraft: " + aircraft.getAircraftId());
		System.out.println();

		ThreadPool.execute(robot); 

		waitingRobots.add(robot);
	}



	@Override
	public void run(){
		GenerateRobots();
		System.out.println("waitingRobots: " + waitingRobots.size());
		System.out.println();

		System.out.println("Thread Pool Started");
		System.out.println();

		ThreadPool = Executors.newFixedThreadPool(3); // starts thread pool
		System.out.println();

		while(true){
			Aircraft openAircraft = InProduction.WaitForProductionLine();
			if (openAircraft != null){
				Robot currentRobot = waitingRobots.poll();
				assignAndStartRobotJob(currentRobot, openAircraft);
			}

		}

	}

}