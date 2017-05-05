package SpatialQueryProcessing;

import java.awt.*;

/*
* RealWindowGraphics class. Has a window, a viewport and a
* graphics context into which to draw.  The graphics context
* is only set after calls to repaint result in calls to update.
* Contains drawing operations, drawTriangle for example, needed
* elsewhere.
*/
class RealWindowGraphics {
	RealWindow w = null;	// window
	Dimension v = null;	// viewport
	Graphics g = null;
	float scale = 1.0f;

	static final float realPointRadius = 0.04f;
	static final int pixelPointRadius = 4;
	static final int halfPixelPointRadius = 2;

	RealWindowGraphics(RealWindow w) {
		this.w = new RealWindow(w);
	}

	RealWindowGraphics(RealWindow w, Dimension d, Graphics g) {
		this.w = new RealWindow(w);
		this.v = new Dimension(d.width, d.height);
		this.g = g;
		calculateScale();
	}

	public void setWindow(RealWindow w) {
		this.w = new RealWindow(w);
		calculateScale();
	}

	public void setViewport(Dimension d) {
		this.v = new Dimension(d.width, d.height);
		calculateScale();
	}

	public void setGraphics(Graphics g) {
		this.g = g;
	}

	public Graphics getGraphics(Graphics g) {
		return g;
	}

	public void calculateScale() {
		float sx, sy;

		sx = v.width / w.width();
		sy = v.height / w.height();

		if (sx < sy)
			scale = sx;
		else
			scale = sy;
	}

	public void drawTriangle(RealPoint p1,
		RealPoint p2,
		RealPoint p3,
		Color c) {
		drawLine(p1, p2, c);
		drawLine(p2, p3, c);
		drawLine(p3, p1, c);
	}

	public void drawLine(RealPoint p1, RealPoint p2, Color c) {
		int x1, y1, x2, y2;

		g.setColor(c);
		x1 = (int)(p1.x() * scale);
		y1 = (int)(p1.y() * scale);
		x2 = (int)(p2.x() * scale);
		y2 = (int)(p2.y() * scale);
		g.drawLine(x1, y1, x2, y2);
	}

	public void drawPoint(RealPoint p, Color c) {
		g.setColor(c);

		g.fillOval((int)(scale * p.x()) - halfPixelPointRadius,
			(int)(scale * p.y()) - halfPixelPointRadius,
			pixelPointRadius, pixelPointRadius);
	}

	public void drawCircle(Circle circle, Color c) {
		drawCircle(circle.center().x(), circle.center().y(), circle.radius(), c);
	}

	public void drawCircle(RealPoint p, float r, Color c) {
		drawCircle(p.x(), p.y(), r, c);
	}

	public void drawCircle(float x, float y, float r, Color c) {
		g.setColor(c);

		g.drawOval((int)(scale * (x - r)), (int)(scale * (y - r)),
			(int)(2.0f * r * scale), (int)(2.0f * r * scale));
	}

	public void fillCircle(float x, float y, float r, Color c) {
		g.setColor(c);

		g.fillOval((int)(scale * (x - r)), (int)(scale * (y - r)),
			(int)(2.0f * r * scale), (int)(2.0f * r * scale));
	}
}