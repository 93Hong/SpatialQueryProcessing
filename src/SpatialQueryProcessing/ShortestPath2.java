package SpatialQueryProcessing;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Vector;

public class ShortestPath2 {
	// A utility function to find the vertex with minimum distance value,
	// from the set of vertices not yet included in shortest path tree
	private static final float INF = 10000000;
	private final String separator = ",";

	RealPoint points[];
	Edge edges[];
	Triangulation tri;

	int nV, nE;
	int source;
	int[] prev;

	public ShortestPath2(Triangulation tri) {
		this.tri = tri;
		this.nV = tri.nPoints;
		this.nE = tri.nEdges;
	}

	public float[] dijkstra(int source) {
		this.source = source;

		@SuppressWarnings("unchecked")
		Vector<Element>[] adj = new Vector[nV];
		for (int i = 0; i < adj.length; i++) {
			adj[i] = new Vector<>();
		}

		for (int i = 0; i < nE; i++) {
			adj[tri.edge[i].s()].add(new Element(tri.edge[i].dis(), tri.edge[i].t()));
			adj[tri.edge[i].t()].add(new Element(tri.edge[i].dis(), tri.edge[i].s()));
		}

		float[] dist = new float[nV];
		prev = new int[nV];
		boolean[] visited = new boolean[nV];

		for (int i = 0; i < nV; i++) {
			dist[i] = INF;
		}

		PriorityQueue<Element> pq = new PriorityQueue<>();

		// 다익스트라 알고리즘
		dist[source] = 0; // 시작점에서 시작점까지 거리는 0이므로 초기화
		pq.offer(new Element(0, source));

		while (!pq.isEmpty()) {
			int curVertex;
			do {
				curVertex = pq.peek().vertex;
				pq.poll();
			} while (!pq.isEmpty() && visited[curVertex]);

			if (visited[curVertex])
				break;

			for (Element e : adj[curVertex]) {
				int next = e.vertex;
				float oldDis = dist[next];
				float newDis = dist[curVertex] + e.d;

				if (newDis < oldDis) {
					dist[next] = newDis;
					prev[next] = curVertex;
					pq.offer(new Element(newDis, next));
				}
			}
		}
		// System.out.println("Distance " + source + " to " + dest + " : " + dist[dest]);
		return dist;
	}

	// source to destination path But not include destination
	public int[] getPath(int destination) {
		int tmp = destination;
		String path = "";
		// if need destination, delete '//'
		//path += destination + separator;

		while (source != prev[tmp]) {
			path += prev[tmp] + separator;
			tmp = prev[tmp];
		}
		path += prev[tmp] + separator;
		
		int[] arr = Arrays.stream(path.split(",")).map(String::trim).mapToInt(Integer::parseInt).toArray();
		
		for(int i = 0; i < arr.length / 2; i++) {
		    int temp = arr[i];
		    arr[i] = arr[arr.length - i - 1];
		    arr[arr.length - i - 1] = temp;
		}
		System.out.println(Arrays.toString(arr) + " " + destination + " " + tri.point[destination].keyword[0]);
		
		return arr;
	}
}

/*
 * void start(int source) { mkGraph(); dijkstra(mkGraph(), 1, source); }
 * 
 * float[][] mkGraph() { float graph[][] = new float[nPoint][nPoint];
 * 
 * for (int i = 0; i < nEdge; i++) { graph[edges[i].s][edges[i].t] =
 * edges[i].dis; graph[edges[i].t][edges[i].s] = edges[i].dis; }
 * 
 * // for (int i = 0; i < nPoint; i++) { // for (int j = 0; j < nPoint; j++) {
 * // System.out.print(graph[i][j] + " "); // } // System.out.println(); // }
 * 
 * return graph; }
 * 
 * int minDistance(float dist[], Boolean sptSet[]) { // Initialize min value
 * float min = Integer.MAX_VALUE; int min_index = -1;
 * 
 * for (int v = 0; v < nPoint; v++) if (sptSet[v] == false && dist[v] <= min) {
 * min = dist[v]; min_index = v; }
 * 
 * return min_index; }
 * 
 * // A utility function to print the constructed distance array void
 * printSolution(float dist[], int n) { System.out.println(
 * "Vertex   Distance from Source"); for (int i = 0; i < nPoint; i++)
 * System.out.println(i + " \t\t " + dist[i]); }
 * 
 * // Function that implements Dijkstra's single source shortest path //
 * algorithm for a graph represented using adjacency matrix // representation
 * void dijkstra(float graph[][], int dest, int src) { float dist[] = new
 * float[nPoint]; // The output array. dist[i] will hold // the shortest
 * distance from src to i
 * 
 * // sptSet[i] will true if vertex i is included in shortest // path tree or
 * shortest distance from src to i is finalized Boolean sptSet[] = new
 * Boolean[nPoint];
 * 
 * // Initialize all distances as INFINITE and stpSet[] as false for (int i = 0;
 * i < nPoint; i++) { dist[i] = Integer.MAX_VALUE; sptSet[i] = false; }
 * 
 * // Distance of source vertex from itself is always 0 dist[src] = 0;
 * 
 * // Find shortest path for all vertices for (int count = 0; count < nPoint -
 * 1; count++) { // Pick the minimum distance vertex from the set of vertices //
 * not yet processed. u is always equal to src in first // iteration. int u =
 * minDistance(dist, sptSet);
 * 
 * // Mark the picked vertex as processed sptSet[u] = true;
 * 
 * // Update dist value of the adjacent vertices of the // picked vertex. for
 * (int v = 0; v < nPoint; v++)
 * 
 * // Update dist[v] only if is not in sptSet, there is an // edge from u to v,
 * and total weight of path from src to // v through u is smaller than current
 * value of dist[v] if (!sptSet[v] && graph[u][v] != 0 && dist[u] !=
 * Integer.MAX_VALUE && dist[u] + graph[u][v] < dist[v]) { dist[v] = dist[u] +
 * graph[u][v]; System.out.println(u); } }
 * 
 * // print the constructed distance array printSolution(dist, nPoint); } }
 */