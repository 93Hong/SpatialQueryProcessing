package SpatialQueryProcessing;

/*
* Circle class. Circles are fundamental to computation of Delaunay
* triangulations.  In particular, an operation which computes a
* circle defined by three points is required.
*/
class Circle {
	RealPoint c;
	float r;

	Circle() { c = new RealPoint(); r = 0.0f; }
	Circle(RealPoint c, float r) { this.c = c; this.r = r; }
	public RealPoint center() { return c; }
	public float radius() { return r; }
	public void set(RealPoint c, float r) { this.c = c; this.r = r; }

	/*
	* Tests if a point lies inside the circle instance.
	*/
	public boolean inside(RealPoint p) {
		if (c.distanceSq(p) < r * r)
			return true;
		else
			return false;
	}

	/*
	* Compute the circle defined by three points (circumcircle).
	*/
	public void circumCircle(RealPoint p1, RealPoint p2, RealPoint p3) {
		float cp;

		cp = Vector.crossProduct(p1, p2, p3);
		if (cp != 0.0)
		{
			float p1Sq, p2Sq, p3Sq;
			@SuppressWarnings("unused")
			float num, den;
			float cx, cy;

			p1Sq = p1.x() * p1.x() + p1.y() * p1.y();
			p2Sq = p2.x() * p2.x() + p2.y() * p2.y();
			p3Sq = p3.x() * p3.x() + p3.y() * p3.y();
			num = p1Sq*(p2.y() - p3.y()) + p2Sq*(p3.y() - p1.y()) + p3Sq*(p1.y() - p2.y());
			cx = num / (2.0f * cp);
			num = p1Sq*(p3.x() - p2.x()) + p2Sq*(p1.x() - p3.x()) + p3Sq*(p2.x() - p1.x());
			cy = num / (2.0f * cp);

			c.set(cx, cy);
		}

		// Radius 
		r = c.distance(p1);
	}
}