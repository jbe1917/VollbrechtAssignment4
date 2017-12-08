package controllers.AssignmentEProportional;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;

public class AssignmentEProportional extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static final int WALL_TO_NEAR_VALUE = 400;
	private static final int WALL_TO_FAR_VALUE = 250;
	private static final int MAX_SENSOR_VALUE = 100;

	private static final int S_LEFT = 0; // Sensor left
	private static final int S_MEDIUM_LEFT = 1;
	private static final int S_FRONT_LEFT = 2; // Sensor front left
	private static final int S_FRONT_RIGHT = 3; // Sensor front left

	private final DistanceSensor[] sensors; // Array with all distance sensors

	/**
	 * Constructor
	 */
	public AssignmentEProportional() {
		super();
		// get distance sensors and save them in array
		sensors = new DistanceSensor[] { getDistanceSensor("ps5"), getDistanceSensor("ps6"), getDistanceSensor("ps7"),
				getDistanceSensor("ps0") };
		for (int i = 0; i < 4; i++) {
			sensors[i].enable(10);
		}
	}

	/**
	 * User defined function for initializing and running the
	 * BangBangFollowTheWall class
	 */
	public void run() {
		while (step(TIME_STEP) != -1) {
        		int leftConstant = (int)(500 * Math.sqrt(Math.pow((sensors[S_LEFT].getValue() / WALL_TO_FAR_VALUE), 2)));
        		System.out.println(leftConstant);
        		int rightConstant = (int)(500 * Math.sqrt(Math.pow((WALL_TO_NEAR_VALUE / sensors[S_LEFT].getValue()), 2)) * Math.sqrt(Math.pow((WALL_TO_NEAR_VALUE / sensors[S_MEDIUM_LEFT].getValue()), 2)));
			System.out.println(rightConstant);
			int leftSpeed = Math.min(1000, leftConstant);
			int frontConstant = (int)Math.sqrt(Math.pow((int)(MAX_SENSOR_VALUE / ((sensors[S_FRONT_LEFT].getValue() + sensors[S_FRONT_RIGHT].getValue()) / 2)), 2));
			int rightSpeed = Math.min(1000, rightConstant * frontConstant);

			setSpeed(leftSpeed, rightSpeed);
		}

	}

	/**
	 * Main method - in this method an instance of the controller is created and
	 * the method to launch the robot is called.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AssignmentEProportional controller = new AssignmentEProportional();
		controller.run();
	}
}