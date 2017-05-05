package SpatialQueryProcessing;

/*
* AnimateControl class.  Each algorithm animation has various entities
* which can be displayed.  This class provides the state which controls
* what is being displayed.  It is manipulated by AlgorithmUI and
* accessed by the animation routines in each algorithm.
*/
class AnimateControl {
	TriangulationAlgorithm triAlg;
	static final int automatic = 0;
	static final int manual = 1;
	int animateMode = automatic;
	int pause = 10;
	static final int algorithm = 0;
	static final int triangles = 1;
	static final int points = 2;
	static final int circles = 3;
	static final int nEntities = 4;
	boolean run;
	boolean animate[];
	int nPoints;

	AnimateControl(TriangulationAlgorithm algorithm) {
		triAlg = algorithm;
		animate = new boolean[nEntities];
		for (int i = 0; i < nEntities; i++)
			animate[i] = true;
		run = true;
	}

	AnimateControl(TriangulationAlgorithm algorithm, int nPoints) {
		this(algorithm);
		this.nPoints = nPoints;
	}

	public void setAnimate(int entity, boolean v) {
		animate[entity] = v;
		if (!v)
			triAlg.canvas().needToClear = true;
	}

	public boolean animate(int entity) {
		return animate[entity];
	}

	public int mode() {
		return animateMode;
	}

	public void setManualAnimateMode() {
		animateMode = manual;
	}

	public void setAutomaticAnimateMode() {
		animateMode = automatic;
	}

	public int getPause() {
		return pause;
	}

	public void setPause(int p) {
		pause = p;
	}

	public int getNPoints() {
		return nPoints;
	}

	public void setNPoints(int n) {
		nPoints = n;
	}

	public void setRun(boolean v) {
		run = v;
	}

	public boolean getRun() {
		return run;
	}
}