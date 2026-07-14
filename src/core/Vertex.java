package core;

import java.util.ArrayList;

public class Vertex {
    int number;
    char color;
    ArrayList<Edge> edges;
    public Vertex(int number) {
        this.number = number;
        this.edges = new ArrayList<>();
        this.color = 'b';
    }
    public int getNumber(){
        return this.number;
    }
    public void uploadEdges(MinHeap heap){
        for(Edge edge : this.edges){
            heap.add(edge);
        }
    }
    public void addEdge(Edge edge){
        edges.add(edge);
    }
    public void removeEdge(Edge edge){
        edges.remove(edge);
    }
    public void setColor(char color){
        this.color = color;
    }
    public char getColor(){
        return this.color;
    }
    public ArrayList<Edge> getEdges(){
        return this.edges;
    }
}
