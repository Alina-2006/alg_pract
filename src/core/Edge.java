import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Edge {
    Vertex[] vertexes;
    int weight;
    char color;
    public Edge(int weight, Vertex[] vertexes) {
        this.weight = weight;
        this.vertexes = vertexes;
        this.vertexes[0].addEdge(this);
        this.vertexes[1].addEdge(this);
        this.color = 'b';
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
        this.vertexes[0].removeEdge(this);
        this.vertexes[1].removeEdge(this);
    }
    public Vertex getOppositeVertex(Vertex vertex) {
        if (vertex == this.vertexes[0]) {
            return this.vertexes[1];
        }
        return  this.vertexes[0];
    }
    public void save(String file_name) {
        try (FileWriter writer = new FileWriter(file_name)) {
            writer.write("" + this.vertexes[0] + " " + this.vertexes[1] + " " + this.weight);
        } catch (IOException e) {
            //нужно добавить текст ошибки
            return;
        }
    }
}
