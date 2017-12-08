import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Camera;

public class AssignmentCProportional extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private final static int COLOR_TOLERANCE = 10;
	private final static int DISTANCE_TOLERANCE = 75;

	private final static int FRONT_LEFT_SENSOR = 0;
	private final static int FRONT_RIGHT_SENSOR = 1;

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

			int gCenter = Camera.imageGetGreen(image, camera.getWidth(), (width / 2), (height / 2));
			int bCenter = Camera.imageGetBlue(image, camera.getWidth(), (width / 2), (height / 2));

			//if the ball is ahead the ballConstant is not zero
			int ballConstant = (2 * COLOR_TOLERANCE / (1 + gCenter + bCenter));

			//says if the difference between front distance sensors is within tolerance
			int toleranceConstant = buildAbs((int) ((distanceSensors[FRONT_LEFT_SENSOR].getValue() - distanceSensors[FRONT_RIGHT_SENSOR].getValue()) / DISTANCE_TOLERANCE));

			//gives the speed calculated with the sensor values. if the ball is more at the right the right wheel turns slower
			int leftSensorValues = (1000 - buildAbs((int) ((distanceSensors[FRONT_LEFT_SENSOR].getValue() / distanceSensors[FRONT_RIGHT_SENSOR].getValue()) * toleranceConstant)));
			int rightSensorValues = (1000 - buildAbs((int) ((distanceSensors[FRONT_RIGHT_SENSOR].getValue() / distanceSensors[FRONT_LEFT_SENSOR].getValue()) * toleranceConstant)));

			//right wheel is multiplied with ball constant -> if ball is not found the robot turns searching for it.
			int speedLeft = buildAbs(Math.round(leftSensorValues * ballConstant));
			int speedRight = buildAbs(Math.round(rightSensorValues));

			setSpeed(Math.min(1000, speedLeft), Math.min(1000, speedRight));
		}
	}

	private int buildAbs(int number){
		return (int) Math.sqrt(Math.pow(number, 2));
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