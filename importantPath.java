import java.util.*;

/**
 * Returns a widest path between two vertices in an undirected graph. A widest path between two
 * vertices maximizes the weight of the minimum-weight edge in the path.
 * <p/>
 * There are multiple ways to solve this problem. The following algorithm may be helpful:
 * - Kruskal's algorithm using Union Find
 * You are NOT allowed to use Prim's
 * Feel free to use any previous algorithms that you have already implemented.
 */
public final class importantPath {
    private importantPath() {
    }

    /**
     * Class: edge
     * startNode, endNode, edgeWeight
     */
    private static class Edge implements Comparable<Edge> {
        int startNode;
        int endNode;
        long edgeWeight;

        Edge(int u, int v, long w) {
            startNode = u;
            endNode = v;
            edgeWeight = w;
        }

        @Override
        public int compareTo(Edge o) {
            Long obj1 = new Long(this.edgeWeight);
            Long obj2 = new Long(o.edgeWeight);
            return obj1.compareTo(obj2);
        }
    }


    /**
     * Computes a widest path from {@param src} to {@param tgt} for an undirected graph.
     * If there are multiple widest paths, this method may return any one of them.
     * Input {@param g} guaranteed to be undirected.
     * Input {@param src} and {@param tgt} are guaranteed to be valid and in-bounds.
     * <p/>
     * Do NOT modify this method header.
     *
     * @param g   the graph
     * @param src the vertex from which to start the search
     * @param tgt the vertex to find via {@code src}
     * @return an ordered list of vertices on a widest path from {@code src} to {@code tgt}, or an
     * empty list if there is no such path. The first element is {@code src} and the last
     * element is {@code tgt}. If {@code src == tgt}, a list containing just that element is
     * returned.
     * @implSpec This method should run in worst-case O((n + m) log n) time.
     */
    //global variables
    //first Int = node, second Int = parent
    private static HashMap<Integer, Integer> parents = new HashMap<>();
    //first Int = node, second Int = rank(node)
    private static HashMap<Integer, Integer> rank = new HashMap<>();
    private static ArrayList<Edge> edges = new ArrayList<>();

    public static List<Integer> getImportantPath(Graph g, int src, int tgt, int maxnum) {
        HashMap<Integer, Integer> mstPar = new HashMap<>();
        LinkedList<Integer> path = new LinkedList<>();
        Graph mstT = new Graph();
        //get all the edges of g
        for (int u : g.vertices.keySet()) {
            //for each v ∈ V: make them their own connect component
            makeSet(u);
            if (g.hasNode(u) && g.vertices.get(u).serverWeight != null) {
                for (int v : g.outNeighbors(u)) {
                    //add edges to the edge class created in this file
                    edges.add(new Edge(u, v, g.getWeight(u, v)));
                }
            }
        }

        //sort edges in E in decreasing order of weight
        Collections.sort(edges);
        Collections.reverse(edges);

        //for each edge in edges list
        for (Edge e : edges) {
            //if the endpoints of the edge does not belong to the same connected component
            if (find(e.startNode) != find(e.endNode)) {
                //add that edge to the MST because we know that it's important
                Node currNode;
                //construct MST
                if (mstT.hasNode(e.startNode)) {
                    currNode = mstT.getNode(e.startNode);
                } else {
                    currNode = new Node(0);
                }
                currNode.addEdge(e.endNode, e.edgeWeight);
                mstT.vertices.put(e.startNode, currNode);

                if (mstT.hasNode(e.endNode)) {
                    currNode = mstT.getNode(e.endNode);
                } else {
                    currNode = new Node(0);
                }
                currNode.addEdge(e.startNode, e.edgeWeight);
                mstT.vertices.put(e.endNode, currNode);

                //put the end points into the same connect component
                union(e.startNode, e.endNode);
            }
        }

        //run BFS to check if MST is connected, find the path in MST by tracking parent pointers
        if (src == tgt) {
            path.addFirst(src);
        } else if (bfs(mstT, src, tgt, mstPar, maxnum)) {
            path.addLast(tgt);
            int par = mstPar.get(tgt);
            while (par != src) {
                path.addFirst(par);
                par = mstPar.get(par);
            }
            path.addFirst(src);
        }
        return path;
    }

    /**
     * Find most important edges by weight
     * @param num number of edges user want with heaviest weight
     */
    public static void findImportantEdges(int num) {
        for (int i = 0; i < num; i++) {
            System.out.println("startnode is: " + edges.get(i).startNode);
            System.out.print("; endnode is: " + edges.get(i).endNode);
            System.out.print("; Edge weight is: " + edges.get(i).edgeWeight);
        }
    }

    /**
     * BFS function to track path between src and tgt
     */

    private static boolean bfs(Graph g, int src, int tgt, HashMap<Integer, Integer> parents, int maxnode) {
        Deque<Integer> queue = new LinkedList<>();
        boolean[] discovered = new boolean[maxnode]; //visited list of nodes
        //initialization
        discovered[src] = true;
        queue.addFirst(src);
        parents.put(src, null);

        while (!queue.isEmpty()) {
            if (queue.peekFirst() != null) {
                int u = queue.pollFirst();
                if (g.vertices.get(u).serverWeight != null) {
                    for (int v : g.outNeighbors(u)) {
                        if (!discovered[v]) {
                            discovered[v] = true;
                            queue.addLast(v);
                            parents.put(v, u);
                        }
                        if (v == tgt) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Implementation of Union-Find Data Structure
     * Make-Set(x) creates a new set whose only member (and thus representative) is x.
     */
    private static void makeSet(int x) {
        parents.put(x, x);
        rank.put(x, 0);
    }

    /**
     * Find(x)-PathCompression returns a pointer to
     * the representative element of the set containing x.
     * If x and y belong to the same set (i.e., they are connected), Find(x) == Find(y).
     */
    private static int find(int x) {
        if (x != parents.get(x)) {
            parents.put(x, find(parents.get(x)));
        }
        return parents.get(x);
    }

    /**
     * Union(x,y) unions the two sets that contain x and y, say Sx and Sy,
     * into a new set that is the union of these two sets.
     */
    private static void union(int x, int y) {
        int rx = find(x);
        int ry = find(y);
        if (rx == ry) {
            return; //already in the same set
        }
        if (rank.get(rx) > rank.get(ry)) {
            parents.replace(ry, rx);
        } else {
            parents.replace(rx, ry);
            if (rank.get(rx) == rank.get(ry)) {
                rank.replace(ry, rank.get(ry) + 1);
            }
        }
    }
}
