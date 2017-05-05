package SpatialQueryProcessing;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Random;

/*
* Triangulation class.  A triangulation is represented as a set of
* points and the edges which form the triangulation.
*/
class Triangulation {
	static final int Undefined = -1;
	static final int Universe = 0;
	int nPoints;
	RealPoint point[];
	int nEdges;
	int maxEdges;
	Edge edge[];
	int edgeCnt = 0;
	Keyword keyword;
	
	private static final Random RANDOM = new Random();
	private static final int MAXKEYWORD = 3;
	int KeywordSize;

	Triangulation(int nPoints) {

		// Allocate points.
		this.nPoints = nPoints;
		this.point = new RealPoint[nPoints];
		for (int i = 0; i < nPoints; i++) {
			point[i] = new RealPoint();
			point[i].setNumOfKey(RANDOM.nextInt(MAXKEYWORD) + 1);
		}

		// Allocate edges.
		maxEdges = 3 * nPoints - 6; // Max number of edges.
		edge = new Edge[maxEdges];
		for (int i = 0; i < maxEdges; i++)
			edge[i] = new Edge();
		nEdges = 0;
		
		// Allocate keywords.
		keyword = new Keyword();
		KeywordSize = keyword.getKeywordSize();
	}

	/*
	 * Sets the number of points in the triangulation. Reuses already allocated
	 * points and edges.
	 */
	public void setNPoints(int nPoints) {
		// Fix edge array.
		Edge tmpEdge[] = edge;
		int tmpMaxEdges = maxEdges;
		maxEdges = 3 * nPoints - 6; // Max number of edges.
		edge = new Edge[maxEdges];

		// Which is smaller?
		int minMaxEdges;
		if (tmpMaxEdges < maxEdges)
			minMaxEdges = tmpMaxEdges;
		else
			minMaxEdges = maxEdges;

		// Reuse allocated edges.
		for (int i = 0; i < minMaxEdges; i++)
			this.edge[i] = tmpEdge[i];

		// Get new edges.
		for (int i = minMaxEdges; i < maxEdges; i++)
			this.edge[i] = new Edge();

		// Fix point array.
		RealPoint tmpPoint[] = point;
		point = new RealPoint[nPoints];

		// Which is smaller?
		int minPoints;
		if (nPoints < this.nPoints)
			minPoints = nPoints;
		else
			minPoints = this.nPoints;

		// Reuse allocated points.
		for (int i = 0; i < minPoints; i++)
			this.point[i] = tmpPoint[i];

		// Get new points.
		for (int i = minPoints; i < nPoints; i++)
			this.point[i] = new RealPoint();

		this.nPoints = nPoints;
	}


	// 여기다가 point 값 넣으면 될듯
	public int randomPoints(RealWindow w) {
		int numOfLines = 0, i = 0;
		try (BufferedReader br = new BufferedReader(new FileReader(TriangulationApplet.FileSource))) {

			String line = br.readLine();

			while (line != null) {
				if (numOfLines % TriangulationApplet.Cipher == 0) {
					point[i].x = Float.parseFloat((line.split(" ")[1])) / 10;
					point[i].y = Float.parseFloat((line.split(" ")[2])) / 10;
					for (int j = 0; j < point[i].numOfKey; j++) {
						point[i].setKeyword(keyword.getKeyword(RANDOM.nextInt(KeywordSize)));
					}
					i++;
				}
				numOfLines++;
				line = br.readLine();
			}
			nEdges = 0;
			System.out.println(numOfLines);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return i;

		// for (int i = 0; i < nPoints; i++)
		// {
		// point[i].x = (float)Math.random() * w.xMax(); // 0~윈도우 x까지
		// point[i].y = (float)Math.random() * w.yMax();
		// System.out.println("x : " + point[i].x + " y : " + point[i].y);
		// }
		// nEdges = 0;
		// System.out.println(w.xMax() + " " + w.yMax());
	}

	/*
	 * Copies a set of points.
	 */
	public void copyPoints(Triangulation t) {
		int n;

		if (t.nPoints < nPoints)
			n = t.nPoints;
		else
			n = nPoints;

		for (int i = 0; i < n; i++) {
			point[i].x = t.point[i].x;
			point[i].y = t.point[i].y;
		}

		nEdges = 0;
	}

	void addTriangle(int s, int t, int u) {
		addEdge(s, t);
		addEdge(t, u);
		addEdge(u, s);
	}

	public int addEdge(int s, int t) {
		return addEdge(s, t, Undefined, Undefined);
	}

	/*
	 * Adds an edge to the triangulation. Store edges with lowest vertex first
	 * (easier to debug and makes no other difference).
	 */
	public int addEdge(int s, int t, int l, int r) {
		int e;

		// Add edge if not already in the triangulation.
		e = findEdge(s, t);
		if (e == Undefined) { // 두 점에대한 엣지가 없으면
			if (s < t) // lowest vertex first 이니까
			{
				edge[nEdges].s = s;
				edge[nEdges].t = t;
				edge[nEdges].l = l;
				edge[nEdges].r = r;
				return nEdges++;
			} else {
				edge[nEdges].s = t;
				edge[nEdges].t = s;
				edge[nEdges].l = r;
				edge[nEdges].r = l;
				return nEdges++;
			}
		}
		//
		else
			return Undefined;
	}

	// 점 s t에 대해서 두개가 연결되어있는지 확인하는 것
	public int findEdge(int s, int t) {
		boolean edgeExists = false;
		int i;

		for (i = 0; i < nEdges; i++)
			if (edge[i].s == s && edge[i].t == t || edge[i].s == t && edge[i].t == s) {
				edgeExists = true;
				break;
			}

		if (edgeExists)
			return i;
		else
			return Undefined;
	}

	/*
	 * Update the left face of an edge.
	 */
	public void updateLeftFace(int eI, int s, int t, int f) {
		if (!((edge[eI].s == s && edge[eI].t == t) || (edge[eI].s == t && edge[eI].t == s)))
			Panic.panic("updateLeftFace: adj. matrix and edge table mismatch");
		if (edge[eI].s == s && edge[eI].l == Triangulation.Undefined)
			edge[eI].l = f;
		else if (edge[eI].t == s && edge[eI].r == Triangulation.Undefined)
			edge[eI].r = f;
		else
			Panic.panic("updateLeftFace: attempt to overwrite edge info");
	}

	public void draw(RealWindowGraphics rWG, Color pC, Color eC) {
		drawPoints(rWG, pC);
		drawEdges(rWG, eC);
	}

	public void drawPoints(RealWindowGraphics rWG, Color c) {
		for (int i = 0; i < nPoints; i++)
			rWG.drawPoint(point[i], c);
	}

	public void drawEdges(RealWindowGraphics rWG, Color c) {
		for (int i = 0; i < nEdges; i++)
			drawEdge(rWG, edge[i], c);
	}

	public void drawEdge(RealWindowGraphics rWG, Edge e, Color c) {
		rWG.drawLine(point[e.s], point[e.t], c);
	}

	public void print(PrintStream p) {
		printPoints(p);
		printEdges(p);
	}

	public void printPoints(PrintStream p) {
		for (int i = 0; i < nPoints; i++)
			p.println(String.valueOf(point[i].x) + " " + String.valueOf(point[i].y));
	}

	public void printEdges(PrintStream p) {
		for (int i = 0; i < nEdges; i++)
			p.println(String.valueOf(edge[i].s) + " " + String.valueOf(edge[i].t));
	}
}