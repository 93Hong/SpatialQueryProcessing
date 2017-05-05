package SpatialQueryProcessing;

import java.applet.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/*
* TriangulationApplet class.  "Main Class"
*/
@SuppressWarnings("serial")
public class TriangulationApplet extends Applet implements Runnable {
	Thread triangulateThread[];
	int nPoints; // number of points

	Triangulation triangulation[];
	TriangulationAlgorithm algorithm[];

	RealWindow w;
	RealWindowGraphics rWG;

	AppletUI appUI;
	public static final int On2 = 0;
	Panel canvases;

	static final int nAlgorithms = 1;
	// location of data set
	static final String FileSource = "C:/Users/hong/workspace3/dataset3.txt";
	static final int Cipher = 1;

	static long Timer;

	public void init() {

		setSize(1000, 900);

		// Create a rectangle in the real plane for points.
		w = new RealWindow(0.0f, 0.0f, 1000.0f, 900.0f);

		// Create array of triangulations, including random points.
		triangulation = new Triangulation[nAlgorithms];

		nPoints = countPoint();

		triangulation[0] = new Triangulation(nPoints);

		nPoints = triangulation[0].randomPoints(w);

		for (int i = 1; i < nAlgorithms; i++) {
			triangulation[i] = new Triangulation(nPoints);
			triangulation[i].copyPoints(triangulation[0]);
		}

		// Create an array of triangulation algorithms.
		algorithm = new TriangulationAlgorithm[nAlgorithms];
		algorithm[0] = new QuadraticAlgorithm(triangulation[0], w, nPoints);
		// algorithm[1] = new CubicAlgorithm(triangulation[1], w, nPoints);
		// algorithm[2] = new QuarticAlgorithm(triangulation[2], w, nPoints);

		// Array of thread references (one for each algorithm).
		triangulateThread = new Thread[nAlgorithms];

		// Create user interface.
		Panel heading = new Panel();
		heading.setLayout(new BorderLayout());
		// heading.add("Center", new Label("The Triangulator", Label.CENTER));
		// Panel algHeadings = new Panel();
		// algHeadings.setLayout(new GridLayout(0, nAlgorithms));

		// for (int i = 0; i < nAlgorithms; i++)
		// algHeadings.add(new Label(algorithm[i].algName, Label.CENTER));

		// heading.add("South", algHeadings);
		canvases = new Panel();
		canvases.setLayout(new GridLayout(0, nAlgorithms));
		for (int i = 0; i < nAlgorithms; i++)
			canvases.add(algorithm[i].canvas());

		setLayout(new BorderLayout());
		// add("North", heading);
		add("Center", canvases);
		appUI = new AppletUI(algorithm);
		add("South", appUI);
	}

	public int countPoint() {
		int count = 0, numOfLines = 0;

		try (BufferedReader br = new BufferedReader(new FileReader(FileSource))) {
			String line = br.readLine();

			while (line != null) {
				if (numOfLines % Cipher == 0)
					count++;
				numOfLines++;
				line = br.readLine();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return count;
	}

	/*
	 * Called for each algorithm thread when started.
	 */
	public void run() {
		int algNo;
		String threadName;

		// Work out which algorithm to run from the thread name.
		threadName = Thread.currentThread().getName();
		algNo = Integer.parseInt(threadName.substring(threadName.length() - 1));
		algorithm[algNo].triangulate(triangulation[algNo]);
	}

	public Insets insets() {
		// Right offset is more than left, due to Choice bug.
		return new Insets(5, 10, 5, 15);
	}

	/*
	 * Actually start the triangulation algorithms running.
	 */
	private synchronized void startTriangulate() {
		for (int i = 0; i < triangulateThread.length; i++)
			if (triangulateThread[i] != null && triangulateThread[i].isAlive()) {
				stop();
			}
		for (int i = 0; i < triangulateThread.length; i++) {
			if (algorithm[i].control().getRun()) {
				triangulateThread[i] = new Thread(this, "Triangulation-" + String.valueOf(i));
				triangulateThread[i].setPriority(Thread.MIN_PRIORITY);
				triangulateThread[i].start();
			}
		}
	}

	/*
	 * Generate new points for the algorithms.
	 */
	private synchronized void newPoints() {
		int max, alg;

		stop();

		// Find algorithm with max points.
		max = 0;
		alg = -1;
		for (int i = 0; i < nAlgorithms; i++)
			if (algorithm[i].control().getRun() && algorithm[i].control().getNPoints() > max) {
				max = algorithm[i].control().getNPoints();
				alg = i;
			}

		// Generate maximum number of points.
		if (alg != -1) {
			triangulation[alg].setNPoints(algorithm[alg].control().getNPoints());
			triangulation[alg].randomPoints(w);
		}

		/*
		 * Now copy points into other algorithms. This has the effect that
		 * algorithms with the same number of points wind up with the same
		 * points.
		 */
		for (int i = 0; i < nAlgorithms; i++)
			if (algorithm[i].control().getRun() && i != alg) {
				triangulation[i].setNPoints(algorithm[i].control().getNPoints());
				triangulation[i].copyPoints(triangulation[alg]);
			}

		for (int i = 0; i < nAlgorithms; i++)
			if (algorithm[i].control().getRun()) {
				algorithm[i].reset();
				algorithm[i].canvas().repaint();
			}
	}

	/*
	 * Stop the applet. Kill the triangulation algorithm if still triangulating.
	 */

	@SuppressWarnings("deprecation")
	public synchronized void stop() {
		for (int i = 0; i < triangulateThread.length; i++) {
			if (triangulateThread[i] != null) {
				try {
					triangulateThread[i].stop();
				} catch (IllegalThreadStateException e) {
				}

				triangulateThread[i] = null;
			}
		}
	}

	/*
	 * Gets the current value in a text field.
	 */
	int getValue(TextField tF) {
		int i;
		try {
			i = Integer.valueOf(tF.getText()).intValue();
		} catch (java.lang.NumberFormatException e) {
			i = 0;
		}
		return i;
	}

	/*
	 * Handle main level events.
	 */
	public boolean handleEvent(Event evt) {
		if (evt.id == Event.ACTION_EVENT) {
			if ("Start".equals(evt.arg)) {
				// 시간 시작
				Timer = System.currentTimeMillis();
				startTriangulate();
				return true;
			} else if ("Stop".equals(evt.arg)) {
				stop();
				return true;
			} else if ("New".equals(evt.arg)) {
				newPoints();
			} else if ("Manual".equals(evt.arg)) {
				for (int i = 0; i < nAlgorithms; i++)
					algorithm[i].control().setManualAnimateMode();
				return true;
			} else if ("Auto".equals(evt.arg)) {
				for (int i = 0; i < nAlgorithms; i++)
					algorithm[i].control().setAutomaticAnimateMode();
				return true;
			}
		} else if (evt.id == Event.MOUSE_DOWN) {
			// These events only occur in the canvases.
			for (int i = 0; i < nAlgorithms; i++)
				if (algorithm[i].control().mode() == AnimateControl.manual)
					algorithm[i].nextStep();
			return true;
		} else if (evt.id == Event.MOUSE_MOVE) {
			return true;
		}

		return false;
	}
}