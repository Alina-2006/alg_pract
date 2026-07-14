package core;

import java.util.ArrayList;
import java.util.HashMap;

public class PrimAlgorithm {
    HashMap<Integer, Vertex> mstVertices;
    ArrayList<Integer> verticesHistory;
    ArrayList<MinHeap> heapHistory;
    ArrayList<Edge> mstEdges;
    ArrayList<String> history;
    Graph graph;
    MinHeap heap;
    Edge currentEdge;
    Vertex currentVertex;
    int mstLen;

    public PrimAlgorithm(Graph graph) {
        this.heap = new MinHeap();
        this.graph = graph;
        this.mstVertices = new HashMap<>();
        this.mstEdges = new ArrayList<>();
        this.mstLen = 0;
        this.history = new ArrayList<>();
        this.heapHistory = new ArrayList<>();
        this.verticesHistory = new ArrayList<>();
    }

    public void setNewVertex() {
        this.currentVertex.setColor('r');
        this.verticesHistory.add(this.currentVertex.getNumber());
        this.mstVertices.put(this.currentVertex.getNumber(), this.currentVertex);
        this.currentVertex.uploadEdges(this.heap);
    }

    public void start() {
        this.currentEdge = null;
        this.currentVertex = this.graph.getVertices().values().iterator().next();
        setNewVertex();
    }

    //Вернёт true, если есть смысл продолжать шаги и
    // false, если продолжать их бессмысленно (дерево построено или возникла ошибка)
    public boolean nextStep() {
        this.currentVertex.setColor('g');
        if (this.currentEdge != null) {
            this.currentEdge.setColor('g');
        }
        if (this.mstVertices.size() == graph.getSize()) {
            this.history.add("Минимальное остовное дерево построено, алгоритм завершён");
            return false;
        }

        this.heapHistory.add(this.heap.copy());
        while (true) {
            this.currentEdge = this.heap.pop();
            if (this.currentEdge == null) {
                //Тут необходимо выдать ошибку, потому что получается, что граф несвязный
                return false;
            }
            Vertex[] maybe_new_vertices = this.currentEdge.getVertices();
            if (this.mstVertices.containsKey(maybe_new_vertices[0].getNumber())) {
                if (this.mstVertices.containsKey(maybe_new_vertices[1].getNumber())) {
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() + ",  но " +
                            "не стали добавлять это ребро, так как обе вершины уже есть в дереве");
                } else {
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                            "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                            maybe_new_vertices[1].getNumber());
                    this.currentEdge.setColor('r');
                    this.mstEdges.add(currentEdge);
                    this.mstLen += this.currentEdge.getWeight();
                    this.currentVertex = maybe_new_vertices[1];
                    this.setNewVertex();
                    return true;
                }
            } else {
                this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                        maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                        "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                        maybe_new_vertices[0].getNumber());
                this.currentEdge.setColor('r');
                this.mstEdges.add(currentEdge);
                this.mstLen += this.currentEdge.getWeight();
                this.currentVertex = maybe_new_vertices[0];
                this.setNewVertex();
                return true;
            }
        }
    }

    public void prevStep(){
        if(heapHistory.isEmpty()){
            //Выдаём, что нельзя откатить, так как мы ещё не делали шагов
            return;
        }
        this.heap = this.heapHistory.getLast();
        System.out.printf("\n%s\n", this.heap.heap.toString());
        this.heapHistory.removeLast();
        this.mstEdges.getLast().setColor('b');
        this.mstLen -= this.mstEdges.getLast().getWeight();
        this.history.add("Откатили алгоритм на шаг назад, теперь вершина " + this.currentVertex.getNumber() + " и " +
                "соединяющее её ребро больше не принадлежат дереву");
        this.mstEdges.removeLast();
        this.mstVertices.get(this.verticesHistory.getLast()).setColor('b');
        this.mstVertices.remove(this.verticesHistory.getLast());
        this.verticesHistory.removeLast();
        this.mstVertices.get(this.verticesHistory.getLast()).setColor('r');
        this.currentVertex = this.mstVertices.get(this.verticesHistory.getLast());
        if(!this.mstEdges.isEmpty()){
            this.mstEdges.getLast().setColor('r');
            this.currentEdge = this.mstEdges.getLast();
        }
    }

    public int getMstLen() {
        return mstLen;
    }

    public ArrayList<Edge> getMSTEdges() {
        return this.mstEdges;
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
