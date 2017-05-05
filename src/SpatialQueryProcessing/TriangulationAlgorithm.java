package SpatialQueryProcessing;

/*
* TriangulationAlgorithm class.  Abstract.  Superclass for
* actual algorithms.  Has several abstract function members -
* including the triangulation member which actually computes
* the triangulation.
*/
abstract class TriangulationAlgorithm {
	String algName;
	TriangulationCanvas triCanvas;
	AnimateControl aniControl;
	// AlgorithmUI algorithmUI;
	RealWindow w;
	RealWindowGraphics rWG;

	// Variables and constants for animation state.
	final int nStates = 5;
	boolean state[] = new boolean[nStates];
	static final int triangulationState = 0;
	static final int pointState = 1;
	static final int triangleState = 2;
	static final int insideState = 4;
	static final int edgeState = 5;
	static final int pathState = 3;

	public TriangulationAlgorithm(Triangulation t, RealWindow w, String name, int nPoints) {
		algName = name;
		aniControl = new AnimateControl(this, nPoints);
		// algorithmUI = new AlgorithmUI(this, name, nPoints,
		// aniControl.getPause());
		triCanvas = new TriangulationCanvas(t, w, this);

		for (int s = 0; s < nStates; s++)
			state[s] = false;
		triCanvas.needToClear = true;
	}

	public void setCanvas(TriangulationCanvas tc) {
		triCanvas = tc;
	}

	public AnimateControl control() {
		return aniControl;
	}

	// public AlgorithmUI algorithmUI() {
	// return algorithmUI;
	// }

	public TriangulationCanvas canvas() {
		return triCanvas;
	}

	public void setAlgorithmState(int stateVar, boolean value) {
		state[stateVar] = value;
	}

	public void pause() {
		if (aniControl.mode() == AnimateControl.automatic)
			try {
				wait(aniControl.getPause());
			} catch (InterruptedException e) {
			}
		else
			try {
				wait();
			} catch (InterruptedException e) {
			}
	}

	public void animate(int state) {
		if ((aniControl.animate(AnimateControl.triangles) || aniControl.animate(AnimateControl.circles))
				&& state == triangulationState)
			triCanvas.needToClear = true;

		setAlgorithmState(state, true);

		triCanvas.repaint();

		pause();

		setAlgorithmState(state, false);
	}

	public void reset() {
		for (int s = 0; s < nStates; s++)
			state[s] = false;
		triCanvas.needToClear = true;
	}

	public synchronized void nextStep() {
		notify();
	}

	abstract public void // synchronized
			triangulate(Triangulation t);

	abstract public void // synchronized
			draw(RealWindowGraphics rWG, Triangulation t);
}