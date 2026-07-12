import java.util.ArrayList;
import java.util.HashMap;

public class PrimAlgorithm {
    HashMap<Integer, Vertex> vertices;
    ArrayList<Edge> edges;
    Graph graph;
    MinHeap heap;
    Edge current_edge;
    Vertex current_vertex;
    int mstLen;

    public PrimAlgorithm(Graph graph) {
        this.heap = new MinHeap();
        this.graph = graph;
        this.vertices = new HashMap<>();
        this.edges = new ArrayList<>();
        this.mstLen = 0;
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
        if (this.vertices.size() == graph.getSize()){
            //Пишем о том, что дерево построено и алгоритм завершён
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
                    //Тут в историю пойдёт запись о том, что мы начали рассматривать ребро, но оно
                    // не подошло, потому что не расширяло дерево, а создавало цикл
                }
                else {
                    //В историю пойдёт запись о том, что мы добавили новое ребро и вершину
                    this.current_edge.setColor('r');
                    this.edges.add(current_edge);
                    this.mstLen += this.current_edge.getWeight();
                    this.current_vertex = maybe_new_vertices[1];
                    this.setNewVertex();
                    return true;
                }
            } else {
                //В историю пойдёт запись о том, что мы добавили новое ребро и вершину
                this.current_edge.setColor('r');
                this.edges.add(current_edge);
                this.mstLen += this.current_edge.getWeight();
                this.current_vertex = maybe_new_vertices[0];
                this.setNewVertex();
                return true;
            }
        }
    }
    public int getMstLen(){
        return mstLen;
    }
}
