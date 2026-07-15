package core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class PrimAlgorithm {
<<<<<<< HEAD
    HashMap<Integer, Vertex> vertices;
=======
    HashMap<Integer, Vertex> mstVertices;
    ArrayList<Integer> verticesHistory;
    ArrayList<MinHeap> heapHistory;
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
    ArrayList<Edge> mstEdges;
    ArrayList<String> history;
    Graph graph;
    MinHeap heap;
    Edge currentEdge;
    Vertex currentVertex;
    int mstLen;

    public ArrayList<MinHeap> heapHistory; 

    public PrimAlgorithm(Graph graph) {
        this.heap = new MinHeap();
        this.graph = graph;
<<<<<<< HEAD
        this.vertices = new HashMap<>();
=======
        this.mstVertices = new HashMap<>();
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
        this.mstEdges = new ArrayList<>();
        this.mstLen = 0;
        this.history = new ArrayList<>();
        this.heapHistory = new ArrayList<>();
<<<<<<< HEAD
=======
        this.verticesHistory = new ArrayList<>();
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
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
<<<<<<< HEAD
        if (this.vertices.size() == graph.getSize()) {
            this.history.add("Минимальное остовное дерево построено, алгоритм завршён");
=======
        if (this.mstVertices.size() == graph.getSize()) {
            this.history.add("Минимальное остовное дерево построено, алгоритм завершён");
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
            return false;
        }
        this.heapHistory.add(this.heap.copy());

        this.heapHistory.add(this.heap.copy());
        while (true) {
            this.currentEdge = this.heap.pop();
            if (this.currentEdge == null) {
                //Тут необходимо выдать ошибку, потому что получается, что граф несвязный
                return false;
            }
<<<<<<< HEAD
            Vertex[] maybe_new_vertices = this.current_edge.getVertices();
            if (this.vertices.containsKey(maybe_new_vertices[0].getNumber())) {
                if (this.vertices.containsKey(maybe_new_vertices[1].getNumber())) {
=======
            Vertex[] maybe_new_vertices = this.currentEdge.getVertices();
            if (this.mstVertices.containsKey(maybe_new_vertices[0].getNumber())) {
                if (this.mstVertices.containsKey(maybe_new_vertices[1].getNumber())) {
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() + ",  но " +
                            "не стали добавлять это ребро, так как обе вершины уже есть в дереве");
                } else {
                    this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                            maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                            "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                            maybe_new_vertices[1].getNumber());
<<<<<<< HEAD
                    this.current_edge.setColor('r');
                    this.mstEdges.add(current_edge);
                    this.mstLen += this.current_edge.getWeight();
                    this.current_vertex = maybe_new_vertices[1];
=======
                    this.currentEdge.setColor('r');
                    this.mstEdges.add(currentEdge);
                    this.mstLen += this.currentEdge.getWeight();
                    this.currentVertex = maybe_new_vertices[1];
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
                    this.setNewVertex();
                    return true;
                }
            } else {
                this.history.add("Мы начали рассматривать ребро, соединяющее вершины " +
                        maybe_new_vertices[0].getNumber() + " и " + maybe_new_vertices[1].getNumber() +
                        "и добавили это ребро, теперь к минимальному остовному дереву добавилась вершина " +
                        maybe_new_vertices[0].getNumber());
<<<<<<< HEAD
                this.current_edge.setColor('r');
                this.mstEdges.add(current_edge);
                this.mstLen += this.current_edge.getWeight();
                this.current_vertex = maybe_new_vertices[0];
=======
                this.currentEdge.setColor('r');
                this.mstEdges.add(currentEdge);
                this.mstLen += this.currentEdge.getWeight();
                this.currentVertex = maybe_new_vertices[0];
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
                this.setNewVertex();
                return true;
            }
        }
    }

<<<<<<< HEAD
=======
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

>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
    public int getMstLen() {
        return mstLen;
    }

    public ArrayList<Edge> getMSTEdges() {
<<<<<<< HEAD
        return this.mstEdges;  // это рёбра, вошедшие в МОД
=======
        return this.mstEdges;
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
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
<<<<<<< HEAD
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
=======
>>>>>>> 23ae418532b103110a1c50d53982041b6d15b39a
    }
}