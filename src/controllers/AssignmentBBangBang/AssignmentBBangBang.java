package controllers.AssignmentBBangBang;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.LightSensor;

public class AssignmentBBangBang extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static final int MAX_SENSOR_VALUE = 550;
	private static final int MAX_SENSOR_DIFFERENCE = 200;

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
	private static final int MIN_SPEED = 0; // min. motor speed
	private static final int MAX_SPEED = 1000; // max. motor speed

	private final DistanceSensor[] distanceSensors; // Array with all distance sensors
	private final LightSensor[] lightSensors; // Array with all light sensors

	/**
	 * Constructor
	 */
	public AssignmentBBangBang() {
		super();
		// get distance sensors and save them in array
		distanceSensors = new DistanceSensor[] { getDistanceSensor("ps7"), getDistanceSensor("ps0") };
		// get light sensors and save them in array
		lightSensors = new LightSensor[] { getLightSensor("ls4"), getLightSensor("ls5"), getLightSensor("ls6"),
				getLightSensor("ls7"), getLightSensor("ls0"), getLightSensor("ls1"), getLightSensor("ls2"),
				getLightSensor("ls3") };
		for (int i = 0; i < 2; i++) {
			distanceSensors[i].enable(10);
		}
		for (int i = 0; i < 8; i++) {
			lightSensors[i].enable(10);
		}
	}

	/**
	 * User defined function for initializing and running the
	 * BangBangFollowTheWall class
	 */
	public void run() {
		while (step(TIME_STEP) != -1) {
			// difference between the measurement of the front-left and front-right sensor.
			// if negative -> light is at the left
			// if positive -> light is at the right
			double frontDifference = lightSensors[L_FRONT_LEFT].getValue() - lightSensors[L_FRONT_RIGHT].getValue();
			if ((lightSensors[L_LEFT].getValue() < lightSensors[L_FRONT_LEFT].getValue())
					|| (lightSensors[L_MEDIUM_LEFT].getValue() < lightSensors[L_FRONT_LEFT].getValue())
					|| (lightSensors[L_BACK_LEFT].getValue() < lightSensors[L_FRONT_LEFT].getValue())
					|| (frontDifference < (-1 * MAX_SENSOR_DIFFERENCE))) {
				// drive left - light is at the left
				driveLeft();
			} else if ((lightSensors[L_RIGHT].getValue() < lightSensors[L_FRONT_RIGHT].getValue())
					|| (lightSensors[L_MEDIUM_RIGHT].getValue() < lightSensors[L_FRONT_RIGHT].getValue())
					|| (lightSensors[L_BACK_RIGHT].getValue() < lightSensors[L_FRONT_RIGHT].getValue())
					|| (frontDifference > MAX_SENSOR_DIFFERENCE)) {
				// drive left - light is at the left
				driveRight();
			} else if ((distanceSensors[D_FRONT_LEFT].getValue() > MAX_SENSOR_VALUE)
					|| (distanceSensors[D_FRONT_RIGHT].getValue() > MAX_SENSOR_VALUE)) {
				// stop - lightsource reached
				stop();
			} else {
				//drive forward because light is ahead
				driveForward();
			}
		}

	}

	/**
	 * Robot drives to the right
	 */
	private void driveRight() {
		setSpeed(MAX_SPEED, MIN_SPEED);
	}

	/**
	 * Robot drives to the left
	 */
	private void driveLeft() {
		setSpeed(MIN_SPEED, MAX_SPEED);
	}

	/**
	 * Robot drives forward
	 */
	private void driveForward() {
		setSpeed(MAX_SPEED, MAX_SPEED);
	}

	/**
	 * Robot stops
	 */
	private void stop() {
		setSpeed(MIN_SPEED, MIN_SPEED);
	}

	/**
	 * Main method - in this method an instance of the controller is created and
	 * the method to launch the robot is called.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AssignmentBBangBang controller = new AssignmentBBangBang();
		controller.run();
	}
}