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
			//if wall is too far the value is below 500 to turn left, if the wall distance is ok the value is over 500
			int leftSpeedValue = (int) (500 * Math.sqrt(Math.pow((sensors[S_LEFT].getValue() / WALL_TO_FAR_VALUE), 2)));
			//if wall is too near the value is below 500 to turn right, if the wall distance is ok the value is over 500
			int rightSpeedValue = (int) (500 * Math.sqrt(Math.pow((WALL_TO_NEAR_VALUE / sensors[S_LEFT].getValue()), 2)) * Math.sqrt(Math.pow((WALL_TO_NEAR_VALUE / sensors[S_MEDIUM_LEFT].getValue()), 2)));

			//if a wall is ahead the value is zero
			int frontConstant = (int) Math.sqrt(Math.pow((int) (MAX_SENSOR_VALUE / ((sensors[S_FRONT_LEFT].getValue() + sensors[S_FRONT_RIGHT].getValue()) / 2)), 2));

			//right speed is zero if wall is ahead to turn right
			int leftSpeed = Math.min(1000, leftSpeedValue);
			int rightSpeed = Math.min(1000, rightSpeedValue * frontConstant);

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