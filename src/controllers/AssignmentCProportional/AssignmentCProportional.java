package controllers.AssignmentCProportional;

import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Camera;

public class AssignmentCProportional extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static int COLOR_TOLERANCE = 10;
	private static int DISTANCE_TOLERANCE = 75;

	private static int MIN_SPEED = 0; // min. motor speed
	private static int MAX_SPEED = 1000; // max. motor speed

	private static int FRONT_LEFT_SENSOR = 0;
	private static int FRONT_RIGHT_SENSOR = 1;
	private final DistanceSensor[] distanceSensors;
	private final Camera camera;

	/**
	 * Constructor
	 */
	public AssignmentCProportional() {
		super();

		camera = getCamera("camera");
		camera.enable(10);

		distanceSensors = new DistanceSensor[] { getDistanceSensor("ps7"), getDistanceSensor("ps0") };

		for (int i = 0; i < 2; i++) {
			distanceSensors[i].enable(10);
		}
	}

	/**
	 * User defined function for initializing and running the
	 * BangBangFollowTheWall class
	 */
	public void run() {
		int height = camera.getHeight();
		int width = camera.getWidth();
		while (step(TIME_STEP) != -1) {

			int[] image = camera.getImage();

			int rLeft = Camera.imageGetRed(image, camera.getWidth(), 0, (height / 2));
			int gLeft = Camera.imageGetGreen(image, camera.getWidth(), 0, (height / 2));
			int bLeft = Camera.imageGetBlue(image, camera.getWidth(), 0, (height / 2));

			int rCenter = Camera.imageGetRed(image, camera.getWidth(), (width / 2), (height / 2));
			int gCenter = Camera.imageGetGreen(image, camera.getWidth(), (width / 2), (height / 2));
			int bCenter = Camera.imageGetBlue(image, camera.getWidth(), (width / 2), (height / 2));

			int rRight = Camera.imageGetRed(image, camera.getWidth(), width - 1, (height / 2));
			int gRight = Camera.imageGetGreen(image, camera.getWidth(), width - 1, (height / 2));
			int bRight = Camera.imageGetBlue(image, camera.getWidth(), width - 1, (height / 2));

			int ballConstant = (1 / (1 + gCenter + bCenter));
			int toleranceConstant = (int) Math.sqrt(Math.pow(((distanceSensors[FRONT_LEFT_SENSOR].getValue() - distanceSensors[FRONT_RIGHT_SENSOR].getValue()) / DISTANCE_TOLERANCE), 2));

			int leftSensorValues = (int)(1000 - ((Math.sqrt(Math.pow((distanceSensors[FRONT_LEFT_SENSOR].getValue() / distanceSensors[FRONT_RIGHT_SENSOR].getValue()), 2)) * toleranceConstant)));
			int rightSensorValues = (int)(1000 - ((Math.sqrt(Math.pow((distanceSensors[FRONT_RIGHT_SENSOR].getValue() / distanceSensors[FRONT_LEFT_SENSOR].getValue()), 2)) * toleranceConstant)));
			
			int speedLeft = (int) Math.sqrt(Math.pow(Math.round(leftSensorValues * ballConstant), 2));
			int speedRight = (int) Math.sqrt(Math.pow(Math.round(rightSensorValues), 2));

			setSpeed(Math.min(1000, speedLeft), Math.min(1000, speedRight));
		}
	}

	/**
	 * Main method - in this method an instance of the controller is created and
	 * the method to launch the robot is called.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AssignmentCProportional controller = new AssignmentCProportional();
		controller.run();
	}
}