package SpatialQueryProcessing;

/*
* Wrapper class for basic int type for pass by reference.
*/
class Int {
	int i;

	public Int() {
		i = 0;
	}

	public Int(int i) {
		this.i = i;
	}
	
	/*
	public void Int() {
		i = 0;
	}

	public void Int(int i) {
		this.i = i;
	}
	 */

	public void setValue(int i) {
		this.i = i;
	}

	public int getValue() {
		return i;
	}
}