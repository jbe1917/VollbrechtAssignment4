import com.cyberbotics.webots.controller.DifferentialWheels;
import com.cyberbotics.webots.controller.DistanceSensor;
import com.cyberbotics.webots.controller.Camera;

public class AssignmentCBangBang extends DifferentialWheels {
	private static final int TIME_STEP = 15;

	private static final int COLOR_TOLERANCE = 10;
	private static final int DISTANCE_TOLERANCE = 1000;

	private static final int MIN_SPEED = 0; // min. motor speed
	private static final int MAX_SPEED = 1000; // max. motor speed

	private static final int FRONT_LEFT_SENSOR = 0;
	private static final int FRONT_RIGHT_SENSOR = 1;
	private final DistanceSensor[] distanceSensors;
	private final Camera camera;

	/**
	 * Constructor
	 */
	public AssignmentCBangBang() {
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
			//analyse image and save rgb values at right, left and center of image
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

			if (!(gCenter < COLOR_TOLERANCE && bCenter < COLOR_TOLERANCE) &&
					(gRight < COLOR_TOLERANCE && bRight < COLOR_TOLERANCE) &&
					(gLeft > COLOR_TOLERANCE && bLeft > COLOR_TOLERANCE)) {
				// drive Right - ball is at the right focus of the camera
				driveRight();
			} else if (!(gCenter < COLOR_TOLERANCE && bCenter < COLOR_TOLERANCE) &&
					(gLeft < COLOR_TOLERANCE && bLeft < COLOR_TOLERANCE) &&
					(gRight > COLOR_TOLERANCE && bRight > COLOR_TOLERANCE)) {
				// drive Left - ball is at the left focus of the camera
				driveLeft();
			} else if (!(gCenter < COLOR_TOLERANCE && bCenter < COLOR_TOLERANCE)) {
				//ball is not in focus -> search for it
				searchBall(image, width, height);
			} else {
				//ball is just ahead. balance it using the distance sensors
				double difference = distanceSensors[FRONT_LEFT_SENSOR].getValue()
						- distanceSensors[FRONT_RIGHT_SENSOR].getValue();

				if (difference > DISTANCE_TOLERANCE) {
					driveLeft();
				} else if (difference < (DISTANCE_TOLERANCE * -1)) {
					driveRight();
				} else { //drive forward because ball is straight ahead
					driveForward();
				}
			}
		}

	}

	private void searchBall(int[] image, int width, int height) {
		//searches for the ball in the camera image
		//saves the start and end index which is the most left and most right pixel belonging to the ball
		int startAtIndex = 0;
		int endAtIndex = 0;
		for (int index = 0; (index < width) && (startAtIndex == 0); index++) {
			int green = Camera.imageGetGreen(image, camera.getWidth(), index, (height / 2));
			int blue = Camera.imageGetBlue(image, camera.getWidth(), index, (height / 2));
			if (green < 10 && blue < 10) {
				startAtIndex = index;
			}
		}
		//ball not found. turn left to search there
		if (startAtIndex == 0) {
			driveLeft();
			return;
		}
		for (int index = startAtIndex; (index < width) && (endAtIndex == 0); index++) {
			int green = Camera.imageGetGreen(image, camera.getWidth(), index, (height / 2));
			int blue = Camera.imageGetBlue(image, camera.getWidth(), index, (height / 2));
			if (green > COLOR_TOLERANCE || blue > COLOR_TOLERANCE) {
				endAtIndex = index;
			}
		}
		//ball reaches until end of image at the right
		if (endAtIndex == 0) {
			endAtIndex = width - 1;
		}
		//center of the ball
		int median = ((startAtIndex + endAtIndex) / 2);
		if (median < (width / 2)) {
			driveLeft();
		} else {
			driveRight();
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
	 * Main method - in this method an instance of the controller is created and
	 * the method to launch the robot is called.
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		AssignmentCBangBang controller = new AssignmentCBangBang();
		controller.run();
	}
}