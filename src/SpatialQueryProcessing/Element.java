package SpatialQueryProcessing;

public class Element implements Comparable<Element> {
	float d;
	int vertex;

	public Element(float f, int vertex) {
		this.d = f;
		this.vertex = vertex;
	}

	@Override
	public int compareTo(Element o) {
		return d <= o.d ? -1 : 1;
	}
}