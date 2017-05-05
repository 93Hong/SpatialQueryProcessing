package SpatialQueryProcessing;

/*
* A window is essentially a rectangle.
*/
class RealWindow extends RealRectangle {
	RealWindow() {
	}

	RealWindow(float xMin, float yMin, float xMax, float yMax) {
		super(xMin, yMin, xMax, yMax);
	}

	RealWindow(RealWindow w) {
		super(w.ll(), w.ur());
	}
}