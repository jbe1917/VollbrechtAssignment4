package controllers.AssignmentAProportional;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.LightSensor;

public class AssignmentAProportional extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static final int L_BACK_LEFT = 0; // Light-Sensor left
	private static final int L_LEFT = 1; // Light-Sensor front left
	private static final int L_MEDIUM_LEFT = 2; // Light-Sensor front right
	private static final int L_FRONT_LEFT = 3; // Light-Sensor left
	private static final int L_FRONT_RIGHT = 4; // Light-Sensor left
	private static final int L_MEDIUM_RIGHT = 5; // Light-Sensor front left
	private static final int L_RIGHT = 6; // Light-Sensor front right
	private static final int L_BACK_RIGHT = 7; // Light-Sensor left

	private final LightSensor[] lightSensors; // Array with all light sensors

	/**
	 * Constructor
	 */
	public AssignmentAProportional() {
		super();
               // get light sensors and save them in array
		lightSensors = new LightSensor[] { getLightSensor("ls4"),
				getLightSensor("ls5"), getLightSensor("ls6"),
				getLightSensor("ls7"), getLightSensor("ls0"),
				getLightSensor("ls1"), getLightSensor("ls2"),
				getLightSensor("ls3") };
		for (int i=0; i<8; i++){
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
			double leftValue = lightSensors[L_FRONT_LEFT].getValue() + lightSensors[L_LEFT].getValue()
                        			+ lightSensors[L_MEDIUM_LEFT].getValue() + lightSensors[L_BACK_LEFT].getValue();
                      leftValue += 1;
                      leftValue /= 4;
                      double rightValue = lightSensors[L_FRONT_RIGHT].getValue() + lightSensors[L_RIGHT].getValue()
                        			+ lightSensors[L_MEDIUM_RIGHT].getValue() + lightSensors[L_BACK_RIGHT].getValue();
                      rightValue += 1;
                      rightValue /= 4;
                      
                      double leftSpeed = ((1000 / rightValue) * 1000);
                      double rightSpeed = ((1000 / leftValue) * 1000);
                           
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
		AssignmentAProportional controller = new AssignmentAProportional();
		controller.run();
	}
}