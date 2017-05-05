package SpatialQueryProcessing;

/*
* Vector class.  A few elementary vector operations.
*/
class Vector {
	float u, v;

	Vector() { u = v = 0.0f; }
	Vector(RealPoint p1, RealPoint p2) {
		u = p2.x() - p1.x();
		v = p2.y() - p1.y();
	}
	Vector(float u, float v) { this.u = u; this.v = v; }

	float dotProduct(Vector v) { return u * v.u + this.v * v.v; }

	static float dotProduct(RealPoint p1, RealPoint p2, RealPoint p3) {
		float u1, v1, u2, v2;

		u1 = p2.x() - p1.x();
		v1 = p2.y() - p1.y();
		u2 = p3.x() - p1.x();
		v2 = p3.y() - p1.y();

		return u1 * u2 + v1 * v2;
	}

	float crossProduct(Vector v) { return u * v.v - this.v * v.u; }

	static float crossProduct(RealPoint p1, RealPoint p2, RealPoint p3) {
		float u1, v1, u2, v2;

		u1 = p2.x() - p1.x();
		v1 = p2.y() - p1.y();
		u2 = p3.x() - p1.x();
		v2 = p3.y() - p1.y();

		return u1 * v2 - v1 * u2;
	}

	void setRealPoints(RealPoint p1, RealPoint p2) {
		u = p2.x() - p1.x();
		v = p2.y() - p1.y();
	}
}