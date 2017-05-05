package SpatialQueryProcessing;

/*
* Point class.  RealPoint to avoid clash with java.awt.Point.
*/
class RealPoint {
	float x, y;
	int numOfKey, count = 0;
	String[] keyword;

	RealPoint() {
		x = y = 0.0f;
	}

	RealPoint(float x, float y) {
		this.x = x;
		this.y = y;
	}

	RealPoint(RealPoint p) {
		x = p.x;
		y = p.y;
	}
	
	public void setNumOfKey(int numOfKey) {
		this.numOfKey = numOfKey;
		keyword = new String[numOfKey];
	}
	
	public void setKeyword(String k) {
		this.keyword[count++] = k;
	}
	
	public String[] getKeyword() {
		return keyword;
	}

	public float x() {
		return this.x;
	}

	public float y() {
		return this.y;
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public float distance(RealPoint p) {
		float dx, dy;

		dx = p.x - x;
		dy = p.y - y;
		return (float) Math.sqrt((double) (dx * dx + dy * dy));
	}

	public float distanceSq(RealPoint p) {
		float dx, dy;

		dx = p.x - x;
		dy = p.y - y;
		return (float) (dx * dx + dy * dy);
	}
}