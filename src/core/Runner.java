package core;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Runner {
    Graph graph;
    PrimAlgorithm algorithm;
    public Runner(String fileName){
        ArrayList<Integer[]> edges = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.trim().split("\\s+");

                if (tokens.length == 3) {
                    Integer[] row = new Integer[3];
                    row[0] = Integer.parseInt(tokens[0]);
                    row[1] = Integer.parseInt(tokens[1]);
                    row[2] = Integer.parseInt(tokens[2]);

                    edges.add(row);
                }
            }
        } catch (IOException e) {
            //Выдаём ошибку из-за того, что не удалось прочитать файл
        } catch (NumberFormatException e) {
            //Выдаём ошибку из-за того, что в файле не строки по 3 числа
        }
        this.graph = new Graph(edges);
        this.algorithm = new PrimAlgorithm(this.graph);
    }
    public Graph getGraph(){
        return this.graph;
    }
    public void run(){
        this.algorithm.start();
        while(this.algorithm.nextStep()){}
    }

    public PrimAlgorithm getAlgorithm() {
        return algorithm;
    }
}
