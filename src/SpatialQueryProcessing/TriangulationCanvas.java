package SpatialQueryProcessing;

import java.awt.*;

/*
* TriangulationCanvas class. Each of the triangulation algorithms
* needs a canvas to draw into.
*/
@SuppressWarnings("serial")
class TriangulationCanvas extends Canvas {
	
	Triangulation t;
	RealWindowGraphics rWG;	// Does the actual drawing. 
	boolean needToClear = false;
	boolean newPoints = false;
	TriangulationAlgorithm alg;	// The algorithm which uses this canvas. 

	TriangulationCanvas(Triangulation t,
		RealWindow w,
		TriangulationAlgorithm alg) {
		this.t = t;
		rWG = new RealWindowGraphics(w);
		this.alg = alg;
	}

	public Insets insets() {
		return new Insets(2, 10, 2, 15);
	}

	@SuppressWarnings("deprecation")
	public void paint(Graphics g) {
		if (needToClear) {
			g.clearRect(0, 0, size().width, size().height);

			needToClear = false;
		}
		g.drawRect(0, 0, size().width - 1, size().height - 1);
		rWG.setGraphics(g);
		rWG.setViewport(size());
		alg.draw(rWG, t);
	}

	public void update(Graphics g) {
		paint(g);
	}
}