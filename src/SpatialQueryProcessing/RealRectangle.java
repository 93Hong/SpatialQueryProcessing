package SpatialQueryProcessing;

/*
* Rectangle class.  Need rectangles for window to viewport mapping.
*/
class RealRectangle {
	RealPoint ll;
	RealPoint ur;

	RealRectangle() {
	}

	RealRectangle(RealRectangle r) {
		this.ll = new RealPoint(r.ll);
		this.ur = new RealPoint(r.ur);
	}

	RealRectangle(RealPoint ll, RealPoint ur) {
		this.ll = new RealPoint(ll);
		this.ur = new RealPoint(ur);
	}

	RealRectangle(float xMin, float yMin, float xMax, float yMax) {
		this.ll = new RealPoint(xMin, yMin);
		this.ur = new RealPoint(xMax, yMax);
	}

	public float width() {
		return ur.x() - ll.x();
	}

	public float height() {
		return ur.y() - ll.y();
	}

	public RealPoint ll() {
		return ll;
	}

	public RealPoint ur() {
		return ur;
	}

	public float xMin() {
		return ll.x;
	}

	public float yMin() {
		return ll.y;
	}

	public float xMax() {
		return ur.x;
	}

	public float yMax() {
		return ur.y;
	}
}