import roombacomm.*;
import roombacomm.net.*;
import java.lang.*;
import procontroll.*;
import processing.core.*;
import java.util.Random;

public class robotController {

	static RoombaCommSerial roombacomm = new RoombaCommSerial();
	static String roombacommPort = "/dev/ttyS0";
	static Random random = new Random();

	static int robotMood = 0;

	static String deviceName = "Logitech Logitech RumblePad 2 USB";
	static ControllIO controllIO;
	static ControllDevice device;
	static ControllButton[] buttons;

	// positive mood thresholds
	static int h1 = 4;
	static int h2 = 8;
	static int h3 = 12;
	static int h4 = 16;
	static int h5 = 20;

	// negative mood threshold
	static int s1 = -4;
	static int s2 = -8;
	static int s3 = -12;
	static int s4 = -16;
	static int s5 = -20;

	public void gamePadInit() {
		// need to do this in a processing context?
		//controllIO = ControllIO.getInstance(this);
		controllIO.printDevices();
		device = controllIO.getDevice(0);
		device.setTolerance((float) 0.05);
		int numButtons = device.getNumberOfButtons();
		buttons = new ControllButton[numButtons];
		for (int i = 0; i < buttons.length; i++) {
			buttons[i] = device.getButton(i);
		}
	}

	// TO DO
	// 1. rather than use the Tribble class, we can build songs using the
	// createSong() function
	// 2. the parameters of drive(int, int) are speed and radius, respectively.
	// Thus, we can build
	// realistic behaviors (turing, swerving and so on) but modifying the radius
	// dynamically
	// either by using the random.nextFloat() method or by some heuristic of our
	// devise
	// 3. Build the roomba screen display using Processing.

	public static void h1MoodAction() {
		// Happy

		roombacomm.drive(3, 0); // fast-ish

		if (random.nextFloat() > .5) {
			roombacomm.playNote(84, 6);
		}
	}

	public static void h2MoodAction() {
		// Elated

		roombacomm.drive(4, 0);

		if (random.nextFloat() > .5) {
			roombacomm.playNote(91, 50);
			roombacomm.playNote(96, 150);
		}

	}

	public static void h3MoodAction() {
		roombacomm.drive(4, 100);
		roombacomm.playNote(100, 64);

	}

	public static void h4MoodAction() {
		if (random.nextFloat() > .5) {
			roombacomm.drive(5, 180);
			roombacomm.playNote(100, 64);
		} else {
			roombacomm.drive(5, 0);
			roombacomm.playNote(110, 32);
			roombacomm.playNote(100, 32);
		}

	}

	public static void h5MoodAction() {
		if (random.nextFloat() > .2) {
			roombacomm.drive(6, 360);
			roombacomm.playNote(125, 64);
		} else {
			roombacomm.drive(6, 30);
		}

	}

	public static void s1MoodAction() {
		roombacomm.drive(3, 0); // fast-ish

		if (random.nextFloat() > .5) {
			roombacomm.playNote(40, 32);
		}

	}

	public static void s2MoodAction() {
		roombacomm.drive(3, 0);
		if (random.nextFloat() < .3) {
			roombacomm.drive(1, 60);
			roombacomm.playNote(35, 32);
		}

	}

	public static void s3MoodAction() {
		// Depressed

		if (random.nextFloat() > .4) {
			roombacomm.drive(2, 180); // slowly
		}

		if (random.nextFloat() > .3) {
			roombacomm.playNote(45, 100);
			roombacomm.playNote(43, 100);
			roombacomm.playNote(42, 100);
			roombacomm.playNote(41, 100);
		}

	}

	public static void s4MoodAction() {
		// The robot hates its life...

		if (random.nextFloat() > .1) {
			roombacomm.playNote(38, 200);
			roombacomm.pause(100);
			roombacomm.playNote(36, 200);
		}

		if (random.nextFloat() > .1) {
			roombacomm.drive(1, 360); // very small increments, slowly
		}
	}

	public static void s5MoodAction() {
		roombacomm.drive(1, 360);
		roombacomm.playNote(31, 128);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		roombacomm.connect(roombacommPort);
		performRoombaActions();
	}

	public static void performRoombaActions() {
		while (true) {
			
			// shock robot
			if (buttons[0].pressed()){
				robotMood -= 4;
			}
			// insult robot
			if (buttons[1].pressed()){
				robotMood -= 2;
			}
			// talk robot
			if (buttons[2].pressed()){
				if (robotMood > 8){
					robotMood += 1;
				}
				else {
					robotMood -= 1;
				}
			}
			// comfort robot
			if (buttons[3].pressed()){
				robotMood += 4;
			}

			if (random.nextFloat() > .2) {
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// positive mood actions
			if (robotMood >= 0 && robotMood <= h1) {
				h1MoodAction();
			}
			if (robotMood > h1 && robotMood <= h2) {
				h2MoodAction();
			}
			if (robotMood > h2 && robotMood <= h3) {
				h3MoodAction();
			}
			if (robotMood > h3 && robotMood <= h4) {
				h4MoodAction();
			}
			if (robotMood > h4) {
				h5MoodAction();
			}

			// negative mood actions
			if (robotMood < 0 && robotMood >= s1) {
				s1MoodAction();
			}
			if (robotMood < s1 && robotMood >= s2) {
				s2MoodAction();
			}
			if (robotMood < s2 && robotMood >= s3) {
				s3MoodAction();
			}
			if (robotMood < s3 && robotMood >= s4) {
				s4MoodAction();
			}
			if (robotMood < s4) {
				s5MoodAction();
			}
		}
	}
}
