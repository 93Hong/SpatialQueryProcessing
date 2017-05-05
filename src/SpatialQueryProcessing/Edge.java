package SpatialQueryProcessing;

/*
* Edge class. Edges have two vertices, s and t, and two faces,
* l (left) and r (right). The triangulation representation and
* the Delaunay triangulation algorithms require edges.
*/
class Edge {
	int s, t;
	int l, r;
	float dis;

	Edge() {
		s = t = 0;
	}

	Edge(int s, int t) {
		this.s = s;
		this.t = t;
	}

	int s() {
		return this.s;
	}

	int t() {
		return this.t;
	}

	int l() {
		return this.l;
	}

	int r() {
		return this.r;
	}
	
	float dis() {
		return this.dis;
	}
}