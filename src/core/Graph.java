package core;

import java.util.ArrayList;
import java.util.HashMap;

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
        for(Edge edge : this.edges){
            if((edge.getVertices()[0] == vertices[0] && edge.getVertices()[1] == vertices[1]) ||
                    (edge.getVertices()[1] == vertices[0] && edge.getVertices()[0] == vertices[1])){
                edge.changeWeight(weight);
                return;
            }
        }
        this.edges.add(new Edge(weight, vertices));
    }
    public void removeEdge(Edge edge){
        edge.remove();
        this.edges.remove(edge);
    }
    public void removeVertex(Vertex vertex){
        ArrayList<Edge> edgesToRemove = new ArrayList<>(vertex.getEdges());
        this.vertices.remove(vertex.getNumber());
        for (Edge edge : edgesToRemove) {
            edge.remove();
            this.edges.remove(edge);
        }
    }
    public void changeWeight(Edge edge, int weight){
        edge.changeWeight(weight);
    }
    public ArrayList<Edge> getEdges(){
        return this.edges;
    }
    public HashMap<Integer, Vertex> getVertices(){
        return this.vertices;
    }
    public int getSize(){
        return vertices.size();
    }
    public void save(String fileName) {
        if(!fileName.toLowerCase().endsWith(".txt")){
            //Выбрасываем исключение из-за того, что файл не .txt
            return;
        }
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName)) {
            for (Edge edge : this.edges) {
                Vertex[] v = edge.getVertices();
                writer.write(v[0].getNumber() + " " + v[1].getNumber() + " " + edge.getWeight() + "\n");
            }
        } catch (java.io.IOException e) {
            //Выдаём ошибку, что сохранить не удалось
        }
    }

    public void addEdge(int from, int to, int weight) {
        Vertex v1 = vertices.get(from);
        Vertex v2 = vertices.get(to);
        if (v1 != null && v2 != null) {
            Vertex[] toAdd = {v1, v2};
            addEdge(weight, toAdd);
        }
    }
}
