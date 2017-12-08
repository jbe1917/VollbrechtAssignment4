import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;

public class AssignmentBProportional extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static final int MAX_SENSOR_VALUE = 300;

	private static final int L_BACK_LEFT = 0; // Light-Sensor left
	private static final int L_LEFT = 1; // Light-Sensor front left
	private static final int L_MEDIUM_LEFT = 2; // Light-Sensor front right
	private static final int L_FRONT_LEFT = 3; // Light-Sensor left
	private static final int L_FRONT_RIGHT = 4; // Light-Sensor left
	private static final int L_MEDIUM_RIGHT = 5; // Light-Sensor front left
	private static final int L_RIGHT = 6; // Light-Sensor front right
	private static final int L_BACK_RIGHT = 7; // Light-Sensor left
	private static final int D_FRONT_LEFT = 0; // Distance-Sensor front left
	private static final int D_FRONT_RIGHT = 1; // Distance-Sensor front right

	private final LightSensor[] lightSensors; // Array with all light sensors
       private final DistanceSensor[] distanceSensors;
	/**
	 * Constructor
	 */
	public AssignmentBProportional() {
		super();
               // get light sensors and save them in array
		lightSensors = new LightSensor[] { getLightSensor("ls4"),
				getLightSensor("ls5"), getLightSensor("ls6"),
				getLightSensor("ls7"), getLightSensor("ls0"),
				getLightSensor("ls1"), getLightSensor("ls2"),
				getLightSensor("ls3") };
		distanceSensors = new DistanceSensor[] { getDistanceSensor("ps7"), getDistanceSensor("ps0")};
		for (int i=0; i<8; i++){
		  lightSensors[i].enable(10);
		}
		for (int i=0; i<2; i++){
		  distanceSensors[i].enable(10);
		}
	}

	/**
	 * User defined function for initializing and running the
	 * BangBangFollowTheWall class
	 */
	public void run() {
		while (step(TIME_STEP) != -1) {
			// calculates the average value of the light sensors at the left and right side of the robot.
			double leftValue = lightSensors[L_FRONT_LEFT].getValue() + lightSensors[L_LEFT].getValue()
					+ lightSensors[L_MEDIUM_LEFT].getValue() + lightSensors[L_BACK_LEFT].getValue();
			leftValue += 1;
			leftValue /= 4;
			double rightValue = lightSensors[L_FRONT_RIGHT].getValue() + lightSensors[L_RIGHT].getValue()
					+ lightSensors[L_MEDIUM_RIGHT].getValue() + lightSensors[L_BACK_RIGHT].getValue();
			rightValue += 1;
			rightValue /= 4;

			//distance constant is zero if left or right distance constant is zero so the robot stops.
			double distanceConstantLeft = (int) Math.sqrt(Math.pow(Math.round((MAX_SENSOR_VALUE / (distanceSensors[D_FRONT_LEFT].getValue() + 1))), 2));
			double distanceConstantRight = (int) Math.sqrt(Math.pow(Math.round((MAX_SENSOR_VALUE / (distanceSensors[D_FRONT_RIGHT].getValue() + 1))), 2));

			int distanceConstant = (int) Math.round((distanceConstantLeft * distanceConstantRight) / ((distanceConstantLeft * distanceConstantRight) + 1));

			//if light is more at the right the right value is lower so the left speed is higher
			//-> robot turns to right
			//if distance constant is zero the robot stops
			double leftSpeed = Math.min(1000, (((1000 / rightValue) * 1500 * distanceConstant)));
			double rightSpeed = Math.min(1000, (((1000 / leftValue) * 1500 * distanceConstant)));

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
		AssignmentBProportional controller = new AssignmentBProportional();
		controller.run();
	}
}