package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

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

    public void prevStep() {
        if (heapHistory.isEmpty()) {
            return;
        }
        this.heap = this.heapHistory.getLast();
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
        if (!this.mstEdges.isEmpty()) {
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

    public void saveResult(String fileName) {
        if(!fileName.toLowerCase().endsWith(".txt")){
            //Выбрасываем исключение из-за того, что файл не .txt
        }
        try (java.io.FileWriter writer = new java.io.FileWriter(fileName)) {
            for (Edge edge : this.mstEdges) {
                Vertex[] v = edge.getVertices();
                writer.write(v[0].getNumber() + " " + v[1].getNumber() + " " + edge.getWeight() + "\n");
            }
            writer.write(this.mstLen + "\n");
        } catch (java.io.IOException e) {
            // Выдаём ошибку, что сохранить не удалось
        }
    }

    public Set<Integer> getVisitedVertices() {
        return mstVertices.keySet();
    }

    public Integer getCurrentVertex() {
        return currentVertex != null ? currentVertex.getNumber() : null;
    }

    public String getCurrentEdgeKey() {
        if (currentEdge == null) return null;
        Vertex[] v = currentEdge.getVertices();
        return v[0].getNumber() + "-" + v[1].getNumber();
    }

    public String getLastHistoryEntry() {
        if (history == null || history.isEmpty()) return "";
        return history.get(history.size() - 1);
    }

    public HashMap<Integer, Vertex> getMSTVerticesMap() {
        return mstVertices;
    }

    public Edge getCurrentEdge() {
        return currentEdge;
    }

    public ArrayList<MinHeap> getHeapHistory() {
        return heapHistory;
    }

    public boolean canGoBack() {
        return heapHistory != null && !heapHistory.isEmpty();
    }

    public Set<Integer> getMSTVerticesSet() {
        return mstVertices != null ? mstVertices.keySet() : new HashSet<>();
    }

    public Set<String> getMSTEdgesKeys() {
        Set<String> keys = new HashSet<>();
        if (mstEdges != null) {
            for (Edge edge : mstEdges) {
                Vertex[] v = edge.getVertices();
                keys.add(v[0].getNumber() + "-" + v[1].getNumber());
            }
        }
        return keys;
    }

    public Set<Integer> getVisitedVertices() {
        return mstVertices.keySet();
    }

    public Integer getCurrentVertex() {
        return currentVertex != null ? currentVertex.getNumber() : null;
    }

    public String getCurrentEdgeKey() {
        if (currentEdge == null) return null;
        Vertex[] v = currentEdge.getVertices();
        return v[0].getNumber() + "-" + v[1].getNumber();
    }

    public String getLastHistoryEntry() {
        if (history == null || history.isEmpty()) return "";
        return history.get(history.size() - 1);
    }

    public HashMap<Integer, Vertex> getMSTVerticesMap() {
        return mstVertices;
    }

    public Edge getCurrentEdge() {
        return currentEdge;
    }

    public ArrayList<MinHeap> getHeapHistory() {
        return heapHistory;
    }

    public boolean canGoBack() {
        return heapHistory != null && !heapHistory.isEmpty();
    }

    public Set<Integer> getMSTVerticesSet() {
        return mstVertices != null ? mstVertices.keySet() : new HashSet<>();
    }

    public Set<String> getMSTEdgesKeys() {
        Set<String> keys = new HashSet<>();
        if (mstEdges != null) {
            for (Edge edge : mstEdges) {
                Vertex[] v = edge.getVertices();
                keys.add(v[0].getNumber() + "-" + v[1].getNumber());
            }
        }
        return keys;
    }
}