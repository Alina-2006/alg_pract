import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Edge {
    Vertex[] vertices;
    int weight;
    char color;
    public Edge(int weight, Vertex[] vertexes) {
        this.weight = weight;
        this.vertices = vertexes;
        this.vertices[0].addEdge(this);
        this.vertices[1].addEdge(this);
        this.color = 'b';
    }
    public Vertex[] getVertices(){
        return this.vertices;
    }
    public void changeWeight(int weight){
        this.weight = weight;
    }
    public int getWeight(){
        return this.weight;
    }
    public void setColor(char color){
        this.color = color;
    }
    public char getColor(){
        return this.color;
    }
    public void remove(){
        this.vertices[0].removeEdge(this);
        this.vertices[1].removeEdge(this);
    }
    public Vertex getOppositeVertex(Vertex vertex) {
        if (vertex == this.vertices[0]) {
            return this.vertices[1];
        }
        return  this.vertices[0];
    }
    public void save(String file_name) {
        try (FileWriter writer = new FileWriter(file_name)) {
            writer.write("" + this.vertices[0] + " " + this.vertices[1] + " " + this.weight);
        } catch (IOException e) {
            //нужно добавить текст ошибки
            return;
        }
    }
}
