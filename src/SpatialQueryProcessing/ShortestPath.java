package SpatialQueryProcessing;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Vector;

public class ShortestPath {
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
	
	HashSet<String> count = new HashSet<>();
	boolean bTable[][];

	public ShortestPath(Triangulation tri) {
		this.tri = tri;
		this.nV = tri.nPoints;
		this.nE = tri.nEdges;
		mkBoolTable();
	}

	public void mkBoolTable() {
		for (int i = 0; i < tri.point.length; i++)
			for (int j = 0; j < tri.point[i].keyword.length; j++)
				count.add(tri.point[i].keyword[j]);
		
		bTable = new boolean[count.size()][nV];
		
		for (int i = 0; i < tri.point.length; i++)
			for (int j = 0; j < tri.point[i].keyword.length; j++) {
				int k = 0;
				for (Iterator<String> it = count.iterator(); it.hasNext(); ) {
			        String f = it.next();
			        if (f.equals(tri.point[i].keyword[j]))
			        	bTable[k][i] = true;
			        k++;
			    }
			}
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
		// Arrays.equals(a, b); <- int array 비교가능
		dist[source] = 0; // 시작점에서 시작점까지 거리는 0이므로 초기화
		pq.offer(new Element(0, source));
		
		@SuppressWarnings("unused")
		int done = 0;
		int pCount = 1;
		LinkedList<String> path = new LinkedList<>();
		
		String pre = source+"";
		while (!pq.isEmpty()) {
			int curVertex;
			do {
				curVertex = pq.peek().vertex;
				pq.poll();
			} while (!pq.isEmpty() && visited[curVertex]);

			if (visited[curVertex])
				break;
			
			done++;
			pCount++;

			int j = 0;
			boolean isFirst = true;
			
			for (int i = 0; i < path.size(); i++) {
				if (path.get(i).length() < pCount) {
					pre = path.get(i);
					j = i;
					break;
				}
			}
			
			for (Element e : adj[curVertex]) {
				path.add(pre);
				if (isFirst) {
					path.set(j, pre+curVertex);
					isFirst = false;
				} else if (pre.equals(path.get(j))) {
					path.set(j, pre+curVertex);
				} else {
					path.add(j, pre+curVertex);
				}
				j++;
				
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