package controller;

import core.*;
import view.*;

import javax.swing.*;
import java.io.File;
import java.util.*;

public class Controller {
    private MainWindow mainWindow;
    private Runner runner;
    private Graph graph;
    private PrimAlgorithm algorithm;
    private List<VertexData> vertexDataList = new ArrayList<>();
    private List<EdgeData> edgeDataList = new ArrayList<>();

    public Controller() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        setupListeners();
    }

    private void setupListeners() {
        BottomPanel bottom = mainWindow.getBottomPanel();

        bottom.addStartListener(e -> onStart());
        bottom.addNextStepListener(e -> onNextStep());
        bottom.addPrevStepListener(e -> onPrevStep());
        bottom.addResetListener(e -> onReset());
        bottom.addAuthorsListener(e -> showAuthors());

        // Меню "Открыть"
        mainWindow.getMainMenuBar().getMenu(0).getItem(0).addActionListener(e -> loadGraphFromFile());
        // Меню "Сохранить"
        mainWindow.getMainMenuBar().getMenu(0).getItem(1).addActionListener(e -> saveResultToFile());
        
        mainWindow.getToolbarPanel().addAddVertexListener(e -> onAddVertex());
        mainWindow.getToolbarPanel().addAddEdgeListener(e -> onAddEdge());
    }

    private void onAddEdge() {
        if (graph == null) {
            createEmptyGraph();
        }

        int[] data = mainWindow.getToolbarPanel().showAddEdgeDialog();
        if (data != null) {
            try {
                int from = data[0];
                int to = data[1];
                int weight = data[2];
                graph.addEdge(from, to, weight);
                convertGraphToData();
                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().repaint();
                ToastNotification.showSuccess(mainWindow,"Ребро " + from + "-" + to + " (вес: " + weight + ") добавлено");
            } catch (Exception ex) {
                ToastNotification.showError(mainWindow, "Ошибка добавления ребра: " + ex.getMessage());
            }
        }
    }

    private void onAddVertex() {
        if (graph == null) {
            createEmptyGraph();
        }

        int[] data = mainWindow.getToolbarPanel().showAddVertexDialog();
        if (data != null) {
            try {
                int id = data[0];
                graph.addVertex(id);
                convertGraphToData();
                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().repaint();
                ToastNotification.showSuccess(mainWindow,"Вершина " + id + " добавлена");
            } catch (Exception ex) {
                ToastNotification.showError(mainWindow,"Ошибка добавления вершины: " + ex.getMessage());
            }
        }
    }

    public void loadGraphFromFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = chooser.getSelectedFile();
                runner = new Runner(file.getAbsolutePath());
                graph = runner.getGraph();

                convertGraphToData();

                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().reset();

                ToastNotification.showInformation(mainWindow, "Граф загружен: " + file.getName());
                mainWindow.getBottomPanel().setButtonsState(true, false, false, true);

            } catch (Exception ex) {
                ToastNotification.showError(mainWindow, "Ошибка загрузки: " + ex.getMessage());
            }
        }
    }

    private void createEmptyGraph() {
        if (graph == null) {
            graph = new Graph(new ArrayList<>());
            vertexDataList.clear();
            edgeDataList.clear();
            mainWindow.getGraphCanvas().setVertices(vertexDataList);
            mainWindow.getGraphCanvas().setEdges(edgeDataList);
            mainWindow.getGraphCanvas().repaint();
            ToastNotification.showInformation(mainWindow, "Создан пустой граф");
        }
    }

    private void convertGraphToData() {
        vertexDataList.clear();
        edgeDataList.clear();

        HashMap<Integer, Vertex> vertices = graph.getVertices();
        int size = vertices.size();
        int radius = 200;
        int centerX = 400, centerY = 300;
        double angleStep = 2 * Math.PI / size;
        int i = 0;
        for (Integer id : vertices.keySet()) {
            double angle = i * angleStep - Math.PI / 2;
            double x = centerX + radius * Math.cos(angle);
            double y = centerY + radius * Math.sin(angle);
            vertexDataList.add(new VertexData(id, x, y));
            i++;
        }

        for (Edge edge : graph.getEdges()) {
            Vertex[] v = edge.getVertices();
            edgeDataList.add(new EdgeData(v[0].getNumber(), v[1].getNumber(), edge.getWeight()));
        }
    }

    public void onStart() {
        if (graph == null) {
            ToastNotification.showError(mainWindow,"Сначала создайте или загрузите граф!");
            return;
        }

        try {
            if (runner == null) {
                runner = new Runner(graph);  // ← нужно добавить этот конструктор
            }
            runner.run();
            algorithm = runner.getAlgorithm();

            ArrayList<Edge> mstEdges = algorithm.getMSTEdges();
            int totalWeight = algorithm.getMstLen();

            Set<String> mstEdgeKeys = new HashSet<>();
            for (Edge edge : mstEdges) {
                Vertex[] v = edge.getVertices();
                String key = v[0].getNumber() + "-" + v[1].getNumber();
                mstEdgeKeys.add(key);
            }

             mainWindow.getGraphCanvas().updateState(
                new HashSet<>(),           // visitedVertices (можно пока пустое)
                mstEdgeKeys,               // mstEdges — выделить зелёным
                null,                      // currentVertex
                null                       // currentEdge
            );

            ToastNotification.showInformation(mainWindow, "Алгоритм завершён! Вес МОД: " + algorithm.getMstLen());
            mainWindow.getBottomPanel().setButtonsState(false, false, false, true);

        } catch (Exception ex) {
            ToastNotification.showError(mainWindow, "Ошибка выполнения: " + ex.getMessage());
        }
    }

    public void onNextStep() {
        ToastNotification.showInformation(mainWindow, "Бета-версия: пошаговый режим будет позже");
    }

    public void onPrevStep() {
        ToastNotification.showInformation(mainWindow,"Финальная версия: шаг назад будет позже");
    }

    public void onReset() {
        mainWindow.getGraphCanvas().reset();
        mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
        ToastNotification.showInformation(mainWindow,"Сброшено");
    }

    public void saveResultToFile() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            try {
                graph.save(chooser.getSelectedFile().getAbsolutePath());
                ToastNotification.showInformation(mainWindow,"Результат сохранён");
            } catch (Exception ex) {
                ToastNotification.showError(mainWindow,"Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    private void showAuthors() {
        String info = "Разработчики:\n\n" +
                      "Наумов Матвей — модуль ядра и алгоритма\n" +
                      "Попова Елизавета — пользовательский интерфейс\n" +
                      "Резяпова Алина — модуль управления и интеграции\n\n" +
                      "Бригада: 8";
        JOptionPane.showMessageDialog(mainWindow, info, "О разработчиках", JOptionPane.INFORMATION_MESSAGE);
    }
}