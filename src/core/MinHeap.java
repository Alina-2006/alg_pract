package core;

import java.util.ArrayList;

public class MinHeap {
    ArrayList<Edge> heap;

    public MinHeap() {
        this.heap = new ArrayList<>();
    }

    public MinHeap(ArrayList<Edge> edges){
        this.heap = new ArrayList<>(edges);
    }

    public void add(Edge new_edge){
        this.heap.addLast(new_edge);
        int i = this.heap.size() - 1;
        Edge temp;
        while(i != 0 && this.heap.get(i).getWeight() < this.heap.get((i - 1) / 2).getWeight()){
            temp = this.heap.get((i - 1) / 2);
            this.heap.set((i - 1) / 2, this.heap.get(i));
            this.heap.set(i, temp);
            i = (i - 1) / 2;
        }
    }

    public Edge pop(){
        if(this.heap.isEmpty()){
            return null;
        }
        Edge ret = this.heap.getFirst();
        Edge new_edge = heap.getLast();
        this.heap.removeLast();
        if(this.heap.isEmpty()){
            return ret;
        }
        int size = this.heap.size();
        this.heap.set(0, new_edge);
        int i = 0;
        int left_or_right;
        Edge temp;
        while (2 * i + 1 < size){
            if(2 * i + 2 == size || this.heap.get(2 * i + 2).getWeight() > this.heap.get(2 * i + 1).getWeight()){
                left_or_right = 1;
            }
            else {
                left_or_right = 2;
            }
            if(this.heap.get(i).getWeight() > this.heap.get(2 * i + left_or_right).getWeight()){
                temp = this.heap.get(2 * i + left_or_right);
                this.heap.set(2 * i + left_or_right, this.heap.get(i));
                this.heap.set(i, temp);
                i = 2 * i + left_or_right;
            }
            else{
                break;
            }
        }
        return ret;
    }

    public MinHeap copy(){
        return new MinHeap(this.heap);
    }
}