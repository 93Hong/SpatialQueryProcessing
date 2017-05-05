package SpatialQueryProcessing;

import java.awt.*;
import java.util.Arrays;

/*
* QuadraticAlgorithm class.  O(n^2) algorithm.
*/
class QuadraticAlgorithm extends TriangulationAlgorithm {
	int s, t, u, bP;
	Circle bC = new Circle();
	final static String algName = "O(n^2)";
	int nFaces;
	int cnt = 0;
	int path[] = null;

	// source to destination
	int Source = 1, Destination = 4;

	private static final float INF = 10000000;

	// pass through keywords
	// {"Restaurant", "Laundry", "Bookstore", "Pharmacy", "Bakery"};
	String waypoint[] = { "Restaurant", "Bookstore"};

	public QuadraticAlgorithm(Triangulation t, RealWindow w, int nPoints) {
		super(t, w, algName, nPoints);
	}
	
	// Find the two closest points.
	public void findClosestNeighbours(RealPoint p[], int nPoints, Int u, Int v) {
		System.out.println("find");
		int i, j;
		float d, min;
		int s, t;
		

		s = t = 0;
		min = Float.MAX_VALUE;
		for (i = 0; i < nPoints - 1; i++)
			for (j = i + 1; j < nPoints; j++) {
				d = p[i].distanceSq(p[j]);
				if (d < min) {
					s = i;
					t = j;
					min = d;
				}
			}
		s = 11;
		t = 74;
		u.setValue(s);
		v.setValue(t);
		System.out.println("asd " + s + " " + t);
		
		
	}

	public void reset() {
		nFaces = 0;
		triCanvas.needToClear = true;
		super.reset();
	}

	public void draw(RealWindowGraphics rWG, Triangulation tri) {
		if (state[pathState]) {
			int i;
			for (i = 0; i < path.length-1; i++) {
				rWG.drawPoint(tri.point[path[i]], Color.red);
				rWG.drawLine(tri.point[path[i]], tri.point[path[i+1]], Color.red);
			}
			rWG.drawPoint(tri.point[path[i]], Color.red);
		} else if (state[triangleState]) {
			if (aniControl.animate(AnimateControl.triangles)) {
				rWG.drawTriangle(tri.point[s], tri.point[t], tri.point[bP], Color.green);
				rWG.drawLine(tri.point[s], tri.point[t], Color.blue);
			}
			if (aniControl.animate(AnimateControl.circles))
				rWG.drawCircle(bC, Color.green);

		} else if (state[pointState]) {
			if (aniControl.animate(AnimateControl.points))
				rWG.drawPoint(tri.point[u], Color.orange);

		} else if (state[insideState]) {
			if (aniControl.animate(AnimateControl.triangles)) {
				rWG.drawTriangle(tri.point[s], tri.point[t], tri.point[bP], Color.red);
				rWG.drawLine(tri.point[s], tri.point[t], Color.blue);
			}

			if (aniControl.animate(AnimateControl.circles))
				rWG.drawCircle(bC, Color.red);
			if (aniControl.animate(AnimateControl.points))
				rWG.drawPoint(tri.point[s], Color.red);

		} else if (state[triangulationState]) {
			tri.draw(rWG, Color.black, Color.black);
		} else {
			tri.draw(rWG, Color.black, Color.black);
		}
	}

	public synchronized void triangulate(Triangulation tri) {
		@SuppressWarnings("unused")
		int seedEdge, currentEdge;
		int nFaces;
		Int s, t;

		// Initialise.
		nFaces = 0;
		s = new Int();
		t = new Int();

		// Find closest neighbours and add edge to triangulation.
		findClosestNeighbours(tri.point, tri.nPoints, s, t);
		// System.out.println(s.getValue() + " " + t.getValue());

		// Create seed edge and add it to the triangulation.
		seedEdge = tri.addEdge(s.getValue(), t.getValue(), Triangulation.Undefined, Triangulation.Undefined);

		currentEdge = 0;
		while (currentEdge < tri.nEdges) {
			//long s1 = System.currentTimeMillis();
			if (tri.edge[currentEdge].l == Triangulation.Undefined) {
				completeFacet(currentEdge, tri, nFaces);
				animate(triangulationState);
			}

			if (tri.edge[currentEdge].r == Triangulation.Undefined) {
				completeFacet(currentEdge, tri, nFaces);
				animate(triangulationState);
			}
			currentEdge++;
			//long s2 = System.currentTimeMillis();
			//System.out.println("카운트으" + n++ + " : " + (s2-s1)/1000.0);
			/*try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
		long end = System.currentTimeMillis();

		// end algorithm
		System.out.println("실행 시간 : " + (end - TriangulationApplet.Timer) / 1000.0);

		// calculate weight (distance)
		for (int i = 0; i < tri.edge.length; i++) {
			tri.edge[i].dis = (float) Math.sqrt(Math.pow((tri.point[tri.edge[i].s].x - tri.point[tri.edge[i].t].x), 2)
					+ Math.pow((tri.point[tri.edge[i].s].y - tri.point[tri.edge[i].t].y), 2));
		}

		System.out.println(Source + " to " + Destination);

		//path = tripPlanning(tri);
		
		path = tripPlanning2(tri);

		// source to destination using Dijkstra
		// ShortestPath sPath = new ShortestPath(tri);
		// sPath.dijkstra(Source);
		// path = sPath.getPath(Destination);

		///////////////////////////////////////////////////////////////////////////////////
		// path keyword print
		System.out.println();
		for (int i = 0; i < path.length; i++) {
			System.out.print(path[i] + "'s keywords : ");
			for (int j = 0; j < tri.point[path[i]].numOfKey; j++)
				System.out.print(tri.point[path[i]].getKeyword()[j] + " ");
			System.out.println();
		}
		///////////////////////////////////////////////////////////////////////////////////

		// paint path point (red)
		if (path != null)
			animate(pathState);
	}

	// just one
	public int[] tripPlanning(Triangulation tri) {
		ShortestPath sPath = new ShortestPath(tri);
		float[] dist = sPath.dijkstra(Source);
		float min = Float.MAX_VALUE;
		int via = 0;
		
		boolean[] visited = new boolean[tri.point.length];
		visited[Source] = true;	visited[Destination] = true;

		for (int i = 0; i < dist.length; i++)
			if (Arrays.asList(tri.point[i].keyword).contains(waypoint[0])) {
				// doesn't care if source or destination is pass though
				if (visited[i])
					continue;
				if (min > dist[i]) {
					min = dist[i];
					via = i;
				}
			}
		// exception
		if (dist[via] == INF) {
			System.out.println("There is no path");
			return null;
		}
		int sourceToPoint[] = sPath.getPath(via);

		dist = sPath.dijkstra(via);
		// exception
		if (dist[Destination] == INF) {
			System.out.println("There is no path");
			return null;
		}
		int pointToDest[] = sPath.getPath(Destination);

		int[] path = new int[sourceToPoint.length + pointToDest.length + 1];
		System.arraycopy(sourceToPoint, 0, path, 0, sourceToPoint.length);
		System.arraycopy(pointToDest, 0, path, sourceToPoint.length, pointToDest.length);
		path[path.length - 1] = Destination;

		System.out.println(Arrays.toString(path));

		return path;
	}

	// upgrade
	public int[] tripPlanning2(Triangulation tri) {
		int s = Source;
		float min;
		int path[] = null;
		
		ShortestPath sPath = new ShortestPath(tri);
		
		boolean[] visited = new boolean[tri.point.length];
		visited[Source] = true;	visited[Destination] = true;

		for (int i = 0; i < waypoint.length; i++) {
			float[] dist = sPath.dijkstra(s);
			min = Float.MAX_VALUE;
			for (int j = 0; j < dist.length; j++)
				if (Arrays.asList(tri.point[j].keyword).contains(waypoint[i])) {
					// if source or destination is pass though, ignore point
					if (visited[j])
						continue;
					if (min > dist[j]) {
						min = dist[j];
						s = j;
					}
				}
			visited[s] = true;
			int pathToSource[] = sPath.getPath(s);

			if (path != null) {
				int[] tmp = new int[path.length + pathToSource.length];
				System.arraycopy(path, 0, tmp, 0, path.length);
				System.arraycopy(pathToSource, 0, tmp, path.length, pathToSource.length);
				path = tmp;
			} else
				path = pathToSource;
		}
		

		sPath.dijkstra(s);
		int pointToDest[] = sPath.getPath(Destination);		

		int[] tmp = new int[path.length + pointToDest.length + 1];
		System.arraycopy(path, 0, tmp, 0, path.length);
		System.arraycopy(pointToDest, 0, tmp, path.length, pointToDest.length);
		
		path = tmp;
		
		path[path.length - 1] = Destination;
		
		System.out.println(Arrays.toString(path));

		return path;
	}
	
	// 들릴곳과 연결된 엣지 중 가장 작은곳으로
	// 만약 들릴곳과 직접적으로 연결된게 없으면 도착지에 가까운 들릴곳으로
	// 그담에 도착지로 가좌아
	public void tripPlanning3() {
		/*
		 * 
		 */
	}



	/*
	 * Complete a facet by looking for the circle free point to the left of the
	 * edge "e_i". Add the facet to the triangulation.
	 *
	 * This function is a bit long and may be better split.
	 */
	public void completeFacet(int eI, Triangulation tri, int nFaces) {
		float cP;
		Edge e[] = tri.edge;
		RealPoint p[] = tri.point;
		// Cache s and t.
		if (e[eI].l == Triangulation.Undefined) {
			s = e[eI].s;
			t = e[eI].t;
		} else if (e[eI].r == Triangulation.Undefined) {
			s = e[eI].t;
			t = e[eI].s;
		} else
			// Edge already completed.
			return;

		// Find a point on left of edge.
		for (u = 0; u < tri.nPoints; u++) {
			if (u == s || u == t)
				continue;
			if (Vector.crossProduct(p[s], p[t], p[u]) > 0.0)
				break;
		}

		// Find best point on left of edge.
		bP = u;
		if (bP < tri.nPoints) {
			bC.circumCircle(p[s], p[t], p[bP]);

			animate(triangleState);

			for (u = bP + 1; u < tri.nPoints; u++) {
				if (u == s || u == t)
					continue;

				animate(pointState);

				cP = Vector.crossProduct(p[s], p[t], p[u]);

				if (cP > 0.0)
					if (bC.inside(p[u])) {
						animate(insideState);
						bP = u;
						bC.circumCircle(p[s], p[t], p[u]);
						animate(triangleState);
					}
			}
		}

		// Add new triangle or update edge info if s-t is on hull.
		if (bP < tri.nPoints) {
			// Update face information of edge being completed.
			tri.updateLeftFace(eI, s, t, nFaces);
			nFaces++;

			// Add new edge or update face info of old edge.
			eI = tri.findEdge(bP, s);
			if (eI == Triangulation.Undefined)
				// New edge.
				eI = tri.addEdge(bP, s, nFaces, Triangulation.Undefined);
			else
				// Old edge.
				tri.updateLeftFace(eI, bP, s, nFaces);

			// Add new edge or update face info of old edge.
			eI = tri.findEdge(t, bP);
			if (eI == Triangulation.Undefined)
				// New edge.
				eI = tri.addEdge(t, bP, nFaces, Triangulation.Undefined);
			else
				// Old edge.
				tri.updateLeftFace(eI, t, bP, nFaces);
		} else
			tri.updateLeftFace(eI, s, t, Triangulation.Universe);

	}
}