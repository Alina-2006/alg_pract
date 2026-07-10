import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Graph {
    HashMap<Integer, Vertex> vertices;
    ArrayList<Edge> edges;
    public Graph(ArrayList<Integer[]> edges){
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        for(Integer[] edge : edges) {
            this.addVertex(edge[0]);
            this.addVertex(edge[1]);
            Vertex[] to_add = {vertices.get(edge[0]), vertices.get(edge[1])};
            this.addEdge(edge[2], to_add);
        }
    }
    public void addVertex(int number){
        if (!vertices.containsKey(number)) {
            vertices.put(number, new Vertex(number));
        }
    }
    public void addEdge(int weight, Vertex[] vertices){
        this.edges.add(new Edge(weight, vertices));
    }
}
