import java.awt.*;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    static Graph[] graphs = new Graph[22];
    static int maxNode;
    public static void main(String[] args) {
        for (int i = 0; i < graphs.length; i++) {
            graphs[i] = new Graph();
            graphs[i].graphNum = i;
        }


        //here we are reading all the data points from day 2 of the data collection period
        //although there are 22 distinct graphs, the final data will be updated for each graph
        //because we update the nodes and edges for the same graph as we read the txt files
        ArrayList<String> lines2_1 = lineReader("out2_1.txt");
        constructGraph(lines2_1, graphs);
        ArrayList<String> lines2_2 = lineReader("out2_2.txt");
        constructGraph(lines2_2, graphs);
        ArrayList<String> lines2_3 = lineReader("out2_3.txt");
        constructGraph(lines2_3, graphs);
        ArrayList<String> lines2_4 = lineReader("out2_4.txt");
        constructGraph(lines2_4, graphs);
        ArrayList<String> lines2_5 = lineReader("out2_5.txt");
        constructGraph(lines2_5, graphs);
        ArrayList<String> lines2_6 = lineReader("out2_6.txt");
        constructGraph(lines2_6, graphs);
        ArrayList<String> lines2_7 = lineReader("out2_7.txt");
        constructGraph(lines2_7, graphs);

        //DO NOT CHANGE ANYTHING ABOVE THIS LINE
        //
        //change graph number to find the MST between source node and target node
        Graph g = graphs[1]; //can change graph number from 1 to 22, there is no graph 0
        int i = g.vertices.size();
        List<Integer> l = importantPath.getImportantPath(g, 1, 64, maxNode);
        System.out.println(l);

        //change number to find the first [num] heaviest edges in the graph
        importantPath.findImportantEdges(20);
    }


    /**
     * helper to put lines into array list by scanning through each text file
     */
    public static ArrayList<String> lineReader(String s) {
        Scanner sc = null;
        try {
            sc = new Scanner(new FileReader(s));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ArrayList<String> node = new ArrayList<>();
        while (sc.hasNextLine()) {
            String line = sc.nextLine();
            node.add(line);
        }
        return node;
    }

    /**
     * helper to split lines based on their information, and construct graph accordingly
     * the nodes are in the sequence of:
     *      graph, client node id, server node id, csv of port, protocol, and number of packets.
     */
    public static void constructGraph(ArrayList<String> s, Graph[] g) {
        for (int i = 0; i < s.size(); i++) {
            String line = s.get(i);
            int gNum = Integer.parseInt(line.split("g")[1].split("\t")[0]);
            int clientNode = Integer.parseInt(line.split("\t")[1]);
            int serverNode = Integer.parseInt(line.split("\t")[2]);
            String weight = line.split("\t")[3];
            String[] individualWeight = weight.split(",");
            long weightSum = 0;
            for (String st : individualWeight) {
                Long num= Long.valueOf(st.split("-")[1]);
                weightSum += num;
            }

            //because the number of nodes does not correspond to the largest node in the graph
            //we use this to keep track of the value of the largest node amongst all the graphs
            //useful later when running bfs
            maxNode = -1;
            if (clientNode > maxNode) {
                maxNode = clientNode;
            }
            if (serverNode > maxNode) {
                maxNode = serverNode;
            }

            //add the edges to each of the graphs
            Node currNode;
            if (graphs[gNum].hasNode(serverNode)) {
                currNode = graphs[gNum].getNode(serverNode);
            } else {
                currNode = new Node(gNum);
            }
            currNode.addEdge(clientNode, weightSum);
            graphs[gNum].vertices.put(serverNode, currNode);
            if (!graphs[gNum].hasNode(clientNode)) {
                graphs[gNum].vertices.put(clientNode, new Node(gNum));
            }
        }
    }

}
