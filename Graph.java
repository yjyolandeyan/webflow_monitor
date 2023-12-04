import java.util.HashMap;
import java.util.Set;
public class Graph {
    int graphNum;
    int maxNode;
    HashMap<Integer, Node> vertices = new HashMap<>(); //int = server node
    Graph() {
        vertices = new HashMap<>();
    }

    public void addNode(int n) {
        vertices.put(n, new Node(graphNum));
    }

    public boolean hasNode(int nodeId) {
        return vertices.containsKey(nodeId);
    }

    public Node getNode(int nodeId) {
        return vertices.get(nodeId);
    }
    public int getSize() {
        return vertices.size();
    }

    public long getWeight(int nodeId, int client) {
        return vertices.get(nodeId).serverWeight.get(client);
    }

    public Set<Integer> outNeighbors(int nodeId) {
        return vertices.get(nodeId).serverWeight.keySet();
    }

    public void addEdge(int nodeId, int clientId, long edgeWeight) {
        vertices.get(nodeId).addEdge(clientId, edgeWeight);
    }
}

class Node {
    int graphNum; //from which graph
    HashMap<Integer, Long> serverWeight; //first int = client id; second int = weight

    Node(int g) {
        graphNum = g;
        serverWeight = new HashMap<>();
    }

    void addEdge(int client, long weight) {
        serverWeight.put(client, weight);
    }

}


