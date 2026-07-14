package core;

import java.util.ArrayList;
import java.util.HashMap;

public class PrimAlgorithm {
    HashMap<Integer, Vertex> vertices;
    ArrayList<Edge> mstEdges;
    ArrayList<String> history;
    Graph graph;
    MinHeap heap;
    Edge current_edge;
    Vertex current_vertex;
    int mstLen;

    public PrimAlgorithm(Graph graph) {
        this.heap = new MinHeap();
        this.graph = graph;
        this.vertices = new HashMap<>();
        this.mstEdges = new ArrayList<>();
        this.mstLen = 0;
        this.history = new ArrayList<>();
    }

    public void setNewVertex() {
        this.current_vertex.setColor('r');
        this.vertices.put(this.current_vertex.getNumber(), this.current_vertex);
        this.current_vertex.uploadEdges(this.heap);
    }

    public void start() {
        this.current_edge = null;
        this.current_vertex = this.graph.getVertices().values().iterator().next();
        setNewVertex();
    }

    //Вернёт true, если есть смысл продолжать шаги и
    // false, если продолжать их бессмысленно (дерево построено или возникла ошибка)
    public boolean nextStep() {
        this.current_vertex.setColor('g');
        if (this.current_edge != null) {
            this.current_edge.setColor('g');
        }
        if (this.vertices.size() == graph.getSize()) {
            this.history.add("Минимальное остовное дерево построено, алгоритм завршён");
            return false;
        }

        while (true) {
            this.current_edge = this.heap.pop();
            if (this.current_edge == null) {
                //Тут необходимо выдать ошибку, потому что получается, что граф несвязный
                return false;
            }
            Vertex[] maybe_new_vertices = this.current_edge.getVertices();
            if (this.vertices.containsKey(maybe_new_vertices[0].getNumber())) {
                if (this.vertices.containsKey(maybe_new_vertices[1].getNumber())) {
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() + ",  но " +
                            "не стали добавлять это ребро, так как обе вершины уже есть в дереве");
                } else {
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                            "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                            maybe_new_vertices[1].getNumber());
                    this.current_edge.setColor('r');
                    this.mstEdges.add(current_edge);
                    this.mstLen += this.current_edge.getWeight();
                    this.current_vertex = maybe_new_vertices[1];
                    this.setNewVertex();
                    return true;
                }
            } else {
                this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                        maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                        "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                        maybe_new_vertices[0].getNumber());
                this.current_edge.setColor('r');
                this.mstEdges.add(current_edge);
                this.mstLen += this.current_edge.getWeight();
                this.current_vertex = maybe_new_vertices[0];
                this.setNewVertex();
                return true;
            }
        }
    }

    public int getMstLen() {
        return mstLen;
    }

    public ArrayList<Edge> getMSTEdges() {
        return this.mstEdges;  // это рёбра, вошедшие в МОД
    }

    public ArrayList<String> getHistory() {
        return this.history;
    }

    public void saveResult(String file_name) {
        try (java.io.FileWriter writer = new java.io.FileWriter(file_name)) {
            for (Edge edge : this.mstEdges) {
                Vertex[] v = edge.getVertices();
                writer.write(v[0].getNumber() + " " + v[1].getNumber() + " " + edge.getWeight() + "\n");
            }
            writer.write(this.mstLen + "\n");
        } catch (java.io.IOException e) {
            //Выдаём ошибку, что сохранить не удалось
        }
    }
}
