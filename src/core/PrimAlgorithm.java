package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrimAlgorithm {
    HashMap<Integer, Vertex> vertices;
    ArrayList<Edge> mstEdges;
    ArrayList<String> history;
    Graph graph;
    MinHeap heap;
    Edge current_edge;
    Vertex current_vertex;
    int mstLen;

    public ArrayList<MinHeap> heapHistory; 

    public PrimAlgorithm(Graph graph) {
        this.heap = new MinHeap();
        this.graph = graph;
        this.vertices = new HashMap<>();
        this.mstEdges = new ArrayList<>();
        this.mstLen = 0;
        this.history = new ArrayList<>();
        this.heapHistory = new ArrayList<>();
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
        this.heapHistory.add(this.heap.copy());

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

    public Set<Integer> getVisitedVertices() {
        return vertices.keySet();
    }

    public Integer getCurrentVertex() {
        return current_vertex != null ? current_vertex.getNumber() : null;
    }

    public String getCurrentEdgeKey() {
        if (current_edge == null) return null;
        Vertex[] v = current_edge.getVertices();
        return v[0].getNumber() + "-" + v[1].getNumber();
    }

    public String getLastHistoryEntry() {
        if (history == null || history.isEmpty()) return "";
        return history.get(history.size() - 1);
    }


    public HashMap<Integer, Vertex> getMSTVerticesMap() {
        return vertices;
    }

    public Edge getCurrentEdge() {
        return current_edge;
    }

    public ArrayList<MinHeap> getHeapHistory() {
        return heapHistory;
    }

    public boolean canGoBack() {
        return heapHistory != null && !heapHistory.isEmpty();
    }

    public Set<Integer> getMSTVerticesSet() {
        return vertices != null ? vertices.keySet() : new HashSet<>();
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

    public void prevStep() {
        if (heapHistory == null || heapHistory.isEmpty()) {
            return;
        }
        
        // Восстанавливаем кучу из истории
        this.heap = this.heapHistory.get(this.heapHistory.size() - 1);
        
        // Удаляем последнее добавленное ребро из МОД
        if (!mstEdges.isEmpty()) {
            Edge lastEdge = mstEdges.remove(mstEdges.size() - 1);
            lastEdge.setColor('b');
            this.mstLen -= lastEdge.getWeight();
            
            // Удаляем последнюю добавленную вершину
            if (!vertices.isEmpty()) {
                // Находим последнюю добавленную вершину (ту, что не входит в МОД)
                // Здесь нужна более сложная логика, но пока просто откатываем
                System.out.println("Откат на шаг назад");
            }
        }
    }
}