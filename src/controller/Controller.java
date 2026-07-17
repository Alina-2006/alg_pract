package controller;

import core.*;
import view.*;

import javax.swing.*;

import java.io.File;
import java.nio.file.Files;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.awt.RenderingHints;

public class Controller {
    private MainWindow mainWindow;
    private Runner runner;
    private Graph graph;
    private PrimAlgorithm algorithm;
    private List<VertexData> vertexDataList = new ArrayList<>();
    private List<EdgeData> edgeDataList = new ArrayList<>();
    private Integer firstVertexForEdge = null;

    public Controller() {
        mainWindow = new MainWindow();
        mainWindow.setVisible(true);
        setupListeners();
    }

    private void setupListeners() {
        BottomPanel bottom = mainWindow.getBottomPanel();
        ToolbarPanel toolbar = mainWindow.getToolbarPanel();

        bottom.addStartListener(e -> onStart());
        bottom.addNextStepListener(e -> onNextStep());
        bottom.addPrevStepListener(e -> onPrevStep());
        bottom.addResetListener(e -> onReset());
        bottom.addAuthorsListener(e -> showAuthors());
        bottom.addHistoryListener(e -> toggleHistoryPanel());

        mainWindow.getMainMenuBar().getMenu(0).getItem(0).addActionListener(e -> loadGraphFromFile());
        mainWindow.getMainMenuBar().getMenu(0).getItem(1).addActionListener(e -> saveResultToFile());

        // клики - кнопка B
        JButton btnAddVertex = mainWindow.getToolbarPanel().getBtnAddVertex();
        btnAddVertex.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onAddVertex();
                } else if (e.getClickCount() == 1) {
                    toolbar.toggleAddVertexMode();
                    if (toolbar.isAddVertexMode()) {
                        toolbar.setAddEdgeMode(false);
                        toolbar.setEraserMode(false);
                        ToastNotification.showInformation(mainWindow, 
                            "Режим добавления вершин. Кликните на поле.");
                    } else {
                        ToastNotification.showInformation(mainWindow, 
                            "Режим добавления вершин отключён");
                    }
                }
            }
        });

        // клики - кнопка ─
        JButton btnAddEdge = toolbar.getBtnAddEdge();
        btnAddEdge.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    onAddEdge();
                } else {
                    toolbar.toggleAddEdgeMode();
                    if (toolbar.isAddEdgeMode()) {
                        toolbar.setAddVertexMode(false);
                        toolbar.setEraserMode(false);
                        ToastNotification.showInformation(mainWindow, 
                            "Режим добавления рёбер. Кликните на первую вершину, затем на вторую.");
                    } else {
                        toolbar.setSelectedVertexForEdge(null);
                        ToastNotification.showInformation(mainWindow, 
                            "Режим добавления рёбер отключён");
                    }
                }
            }
        });

        // Кнопка "E" — ластик
        toolbar.addEraserListener(e -> {
            toolbar.setEraserMode(!toolbar.isEraserMode());
            mainWindow.getGraphCanvas().setEraserMode(toolbar.isEraserMode());
            firstVertexForEdge = null;
            if (toolbar.isEraserMode()) {
                toolbar.setAddVertexMode(false);
                toolbar.setAddEdgeMode(false);
                ToastNotification.showInformation(mainWindow, 
                    "Режим удаления. Кликните на вершину или ребро.");
            } else {
                ToastNotification.showInformation(mainWindow, 
                    "Режим удаления отключён");
            }
        });

        // Кнопка "C" — очистить всё
        toolbar.addClearAllListener(e -> {
            if (toolbar.showClearAllConfirmation()) {
                clearAll();
            }
        });

        setupCanvasListeners();
    }

    private void clearAll() {
        graph = null;
        runner = null;
        algorithm = null;
        vertexDataList.clear();
        edgeDataList.clear();
        stepHistory.clear();
        currentStepIndex = -1;
        firstVertexForEdge = null;
        mainWindow.getGraphCanvas().reset();
        mainWindow.getGraphCanvas().setVertices(vertexDataList);
        mainWindow.getGraphCanvas().setEdges(edgeDataList);
        mainWindow.getBottomPanel().setButtonsState(true, false, false, false);
        mainWindow.getBottomPanel().clearResult(); 
        ToastNotification.showInformation(mainWindow, "Граф очищен");
    }

    private void addStepToHistory(String description) {
        HistoryPanel historyPanel = mainWindow.getHistoryPanel();
        if (historyPanel != null) {
            historyPanel.addStep(description);
        }
    }

    private void toggleHistoryPanel() {
        HistoryPanel historyPanel = mainWindow.getHistoryPanel();
        if (historyPanel == null) {
            historyPanel = new HistoryPanel();
            mainWindow.setHistoryPanel(historyPanel);
            mainWindow.add(historyPanel, BorderLayout.WEST);
        }
        historyPanel.setVisible(!historyPanel.isVisible());
        mainWindow.revalidate();
        mainWindow.repaint();
    }

    private void setupCanvasListeners() {
        GraphCanvas canvas = mainWindow.getGraphCanvas();
        ToolbarPanel toolbar = mainWindow.getToolbarPanel();

        canvas.addVertexCreateListener((x, y) -> {
            if (graph == null) {
                createEmptyGraph();
            }
            if (toolbar.isAddVertexMode()) {
                int newId = getNextVertexId();
                graph.addVertex(newId);
                
                VertexData newVertex = new VertexData(newId, x, y);
                vertexDataList.add(newVertex);
                
                updateEdgeDataFromGraph();
                
                canvas.setVertices(vertexDataList);
                canvas.setEdges(edgeDataList);
                canvas.repaint();
                
                ToastNotification.showSuccess(mainWindow, "Вершина " + newId + " добавлена");
            }
        });

        canvas.addEdgeCreateListener(new GraphCanvas.EdgeCreateListener() {

            @Override
            public void onVertexSelectedForEdge(int vertexId) {
                if (!toolbar.isAddEdgeMode()) return;
                
                if (firstVertexForEdge == null) {
                    firstVertexForEdge = vertexId;
                    ToastNotification.showInformation(mainWindow, 
                        "Выбрана вершина " + firstVertexForEdge + ". Теперь выберите вторую вершину.");
                } else {
                    if (firstVertexForEdge != vertexId) {
                        Integer weight = toolbar.showWeightDialog(mainWindow, firstVertexForEdge, vertexId);
                        if (weight != null && weight > 0) {
                            if (graph == null) createEmptyGraph();
                            graph.addEdge(firstVertexForEdge, vertexId, weight);
                            updateEdgeDataFromGraph();
                            canvas.setEdges(edgeDataList);
                            canvas.repaint();
                            ToastNotification.showSuccess(mainWindow, 
                                "Ребро " + firstVertexForEdge + "-" + vertexId + " (вес: " + weight + ") добавлено");
                        }
                    } else {
                        ToastNotification.showError(mainWindow, "Нельзя соединить вершину с самой собой!");
                    }
                    firstVertexForEdge = null;  // Сбрасываем
                }
            }

            @Override
            public void onEdgeCreateRequested(int from, int to, int weight) {
                // Не используется
            }
        });

        canvas.addCanvasDeleteListener(new GraphCanvas.CanvasDeleteListener() {
            @Override
            public void onVertexDeleteRequested(int vertexId) {
                if (graph == null) return;
                if (!toolbar.isEraserMode()) return;
                
                Vertex vertex = graph.getVertices().get(vertexId);
                if (vertex != null) {
                    graph.removeVertex(vertex);
                    resetAlgorithmState();
                    convertGraphToData();
                    canvas.setVertices(vertexDataList);
                    canvas.setEdges(edgeDataList);
                    canvas.repaint();
                    ToastNotification.showSuccess(mainWindow, "Вершина " + vertexId + " удалена");
                }
            }

            @Override
            public void onEdgeDeleteRequested(int from, int to) {
                if (graph == null) return;
                if (!toolbar.isEraserMode()) return;
                
                for (Edge edge : graph.getEdges()) {
                    Vertex[] v = edge.getVertices();
                    if ((v[0].getNumber() == from && v[1].getNumber() == to) ||
                        (v[0].getNumber() == to && v[1].getNumber() == from)) {
                        graph.removeEdge(edge);
                        resetAlgorithmState();
                        convertGraphToData();
                        canvas.setVertices(vertexDataList);
                        canvas.setEdges(edgeDataList);
                        canvas.repaint();
                        ToastNotification.showSuccess(mainWindow, "Ребро " + from + "-" + to + " удалено");
                        return;
                    }
                }
            }
        });
    }

    private void resetAlgorithmState() {
        runner = null;
        algorithm = null;
        stepHistory.clear();
        currentStepIndex = -1;
        firstVertexForEdge = null;
        mainWindow.getGraphCanvas().reset();
        mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
    }

    private int getNextVertexId() {
        int maxId = 0;
        for (VertexData v : vertexDataList) {
            if (v.getId() > maxId) maxId = v.getId();
        }
        return maxId + 1;
    }

    private void updateEdgeDataFromGraph() {
        edgeDataList.clear();
        if (graph != null) {
            for (Edge edge : graph.getEdges()) {
                Vertex[] v = edge.getVertices();
                edgeDataList.add(new EdgeData(v[0].getNumber(), v[1].getNumber(), edge.getWeight()));
            }
        }
    }

    private void onAddEdge() {
        if (graph == null) {
            createEmptyGraph();
        }

        if (graph.getVertices().isEmpty()) {
            ToastNotification.showError(mainWindow, "Сначала добавьте вершины!");
            return;
        }

        int[] data = mainWindow.getToolbarPanel().showAddEdgeDialog(mainWindow);
        if (data != null) {
            try {
                int from = data[0];
                int to = data[1];
                int weight = data[2];

                if (!graph.getVertices().containsKey(from) || !graph.getVertices().containsKey(to)) {
                    ToastNotification.showError(mainWindow, "Одна из вершин не найдена!");
                    return;
                }

                graph.addEdge(from, to, weight);
                convertGraphToData();
                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().repaint();
                mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
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

        int[] data = mainWindow.getToolbarPanel().showAddVertexDialog(mainWindow);
        if (data != null) {
            try {
                int id = data[0];
                graph.addVertex(id);
                convertGraphToData();
                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().repaint();
                mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
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

                // Читаем файл и проверяем, есть ли вес МОД в конце
                List<String> lines = Files.readAllLines(file.toPath());
                boolean hasResult = false;
                int totalWeight = 0;

                if (!lines.isEmpty()) {
                    String lastLine = lines.get(lines.size() - 1);
                    try {
                        totalWeight = Integer.parseInt(lastLine.trim());
                        hasResult = true; // если последняя строка — число, значит это результат
                    } catch (NumberFormatException e) {
                        // последняя строка не число — обычный граф
                    }
                }

                runner = new Runner(file.getAbsolutePath());
                graph = runner.getGraph();

                convertGraphToData();

                mainWindow.getGraphCanvas().setVertices(vertexDataList);
                mainWindow.getGraphCanvas().setEdges(edgeDataList);
                mainWindow.getGraphCanvas().reset();

                if (hasResult) {
                    // Устанавливаем текст в BottomPanel (он будет висеть постоянно)
                    mainWindow.getBottomPanel().setResultText("Вес МОД: " + totalWeight);
                    
                    // Дополнительно показываем Toast, но он исчезнет через 2.5 секунды
                    ToastNotification.showInformation(
                        mainWindow, 
                        "Результат МОД загружен!\nВес минимального остовного дерева:: " + totalWeight
                    );
                } else {
                    // Если результата нет - очищаем текст
                    mainWindow.getBottomPanel().clearResult();
                    ToastNotification.showInformation(mainWindow, "Граф загружен: " + file.getName());
                }

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
            mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
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
            ToastNotification.showError(mainWindow, "Сначала создайте или загрузите граф!");
            return;
        }

        try {
            runner = new Runner(graph);
            algorithm = runner.getAlgorithm();
            algorithm.start();
            stepHistory.clear();
            saveCurrentStepSnapshot("Алгоритм запущен");
            currentStepIndex = 0;
            updateViewFromCurrentStep();
            
            mainWindow.getBottomPanel().setButtonsState(false, true, false, true);
            ToastNotification.showInformation(mainWindow, "Алгоритм запущен. Нажимайте 'N' для следующего шага.");
            
        } catch (Exception ex) {
            ToastNotification.showError(mainWindow, "Ошибка запуска: " + ex.getMessage());
        }
    }

    public void onPrevStep() {
        if (graph == null) {
            ToastNotification.showError(mainWindow, "Сначала загрузите или создайте граф!");
            return;
        }

        if (algorithm == null) {
            ToastNotification.showInformation(mainWindow, "Алгоритм ещё не запущен!");
            return;
        }

        try {
            if (!algorithm.canGoBack()) {
                ToastNotification.showInformation(mainWindow, "Нельзя откатиться дальше!");
                return;
            }

            algorithm.prevStep();
            updateViewAfterPrevStep();
            addStepToHistory("Откат на шаг назад");

            boolean canPrev = algorithm.canGoBack();
            mainWindow.getBottomPanel().setButtonsState(false, true, canPrev, true);

            ToastNotification.showInformation(mainWindow, "Шаг назад выполнен");

        } catch (Exception ex) {
            ToastNotification.showError(mainWindow, "Ошибка отката: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    private void updateViewAfterPrevStep() {
        Set<Integer> visitedVertices = new HashSet<>(algorithm.getMSTVerticesSet());
        Set<String> mstEdgeKeys = algorithm.getMSTEdgesKeys();
        Integer currentVertex = algorithm.getCurrentVertex();
        String currentEdgeKey = algorithm.getCurrentEdgeKey();
        
        mainWindow.getGraphCanvas().updateState(
            visitedVertices,
            mstEdgeKeys,
            currentVertex,
            currentEdgeKey
        );
    }

    public void onReset() {
        mainWindow.getGraphCanvas().reset();
        mainWindow.getBottomPanel().setButtonsState(true, false, false, true);
        mainWindow.getBottomPanel().clearResult();
        ToastNotification.showInformation(mainWindow,"Сброшено");
    }

    public void saveResultToFile() {
        if (algorithm == null) {
            ToastNotification.showError(mainWindow, "Сначала запустите алгоритм!");
            return;
        }

        JFileChooser chooser = new JFileChooser();
        if (chooser.showSaveDialog(mainWindow) == JFileChooser.APPROVE_OPTION) {
            try {
                algorithm.saveResult(chooser.getSelectedFile().getAbsolutePath());
                ToastNotification.showInformation(mainWindow,"Результат сохранён");
            } catch (Exception ex) {
                ToastNotification.showError(mainWindow,"Ошибка сохранения: " + ex.getMessage());
            }
        }
    }

    /*private void showAuthors() {
        String info = "Разработчики:\n\n" +
                      "Наумов Матвей — модуль ядра и алгоритма\n" +
                      "Попова Елизавета — пользовательский интерфейс\n" +
                      "Резяпова Алина — модуль управления и интеграции\n\n" +
                      "Бригада: 8";
        JOptionPane.showMessageDialog(mainWindow, info, "О разработчиках", JOptionPane.INFORMATION_MESSAGE);
    }*/

        private void showAuthors() {
        JDialog dialog = new JDialog(mainWindow, "О разработчиках", true);
        dialog.setLayout(new BorderLayout());
        dialog.setSize(600, 600);
        dialog.setLocationRelativeTo(mainWindow);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Заголовок
        JLabel title = new JLabel("Разработчики", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        panel.add(title);
        panel.add(Box.createVerticalStrut(15));
        
        // Загружаем картинки
        panel.add(createAuthorRow("memes/mem1.png", "Наумов Матвей", "модуль ядра и алгоритма"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createAuthorRow("memes/mem2.jpg", "Попова Елизавета", "пользовательский интерфейс"));
        panel.add(Box.createVerticalStrut(10));
        panel.add(createAuthorRow("memes/mem3.jpg", "Резяпова Алина", "модуль управления и интеграции"));
        panel.add(Box.createVerticalStrut(15));
        
        
        JLabel brigade = new JLabel("Бригада: 8", SwingConstants.CENTER);
        brigade.setFont(new Font("Arial", Font.BOLD, 14));
        brigade.setForeground(new Color(41, 128, 185));
        panel.add(brigade);
        
        // Кнопка закрыть
        JButton closeBtn = new JButton("Закрыть");
        closeBtn.addActionListener(e -> dialog.dispose());
        JPanel btnPanel = new JPanel();
        btnPanel.add(closeBtn);
        
        dialog.add(panel, BorderLayout.CENTER);
        dialog.add(btnPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    private JPanel createAuthorRow(String imagePath, String name, String role) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));

        // Загружаем картинку
        ImageIcon icon = null;
        try {
            java.net.URL imgUrl = getClass().getResource(imagePath);
            if (imgUrl != null) {
                Image img = new ImageIcon(imgUrl).getImage();
                Image scaledImg = img.getScaledInstance(80, 80, Image.SCALE_SMOOTH);
                icon = new ImageIcon(scaledImg);
            }
        } catch (Exception e) {
            // Если картинки нет - создаём заглушку
            icon = createDefaultAvatar(name);
        }
        
        JLabel avatar = new JLabel(icon != null ? icon : new ImageIcon());
        avatar.setPreferredSize(new Dimension(85, 85));
        row.add(avatar);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 16));
        JLabel roleLabel = new JLabel(role);
        roleLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        roleLabel.setForeground(Color.GRAY);
        textPanel.add(nameLabel);
        textPanel.add(roleLabel);
        row.add(textPanel);
        
        return row;
    }

    private ImageIcon createDefaultAvatar(String name) {
        // Создаём цветной круг с инициалом
        int size = 50;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = image.createGraphics();
        
        // Рисуем круг
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(new Color(100, 149, 237));
        g2.fillOval(0, 0, size, size);
        
        // Рисуем инициал
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        String initial = name.substring(0, 1);
        FontMetrics fm = g2.getFontMetrics();
        int x = (size - fm.stringWidth(initial)) / 2;
        int y = (size + fm.getAscent()) / 2 - 2;
        g2.drawString(initial, x, y);
        
        g2.dispose();
        return new ImageIcon(image);
    }

    //////////
    private List<StepSnapshot> stepHistory = new ArrayList<>();
    private int currentStepIndex = -1;

    private static class StepSnapshot {
        Set<Integer> visitedVertices;
        Set<String> mstEdges;
        Integer currentVertex;
        String currentEdge;
        String description;
        int stepNumber;
    }

    public void onNextStep() {
        if (graph == null) {
            ToastNotification.showError(mainWindow, "Сначала загрузите граф!");
            return;
        }

        try {
            if (algorithm == null) {
                runner = new Runner(graph);
                algorithm = runner.getAlgorithm();
                algorithm.start();
                stepHistory.clear();
                saveCurrentStepSnapshot("Алгоритм запущен");
                currentStepIndex = 0;
                updateViewFromCurrentStep();
                mainWindow.getBottomPanel().setButtonsState(false, true, false, true);
                return;
            }

            if (currentStepIndex >= stepHistory.size() - 1 && stepHistory.size() > 0) {
                String lastEntry = algorithm.getLastHistoryEntry();
                if (lastEntry != null && lastEntry.contains("завершён")) {
                    ToastNotification.showInformation(mainWindow, "Алгоритм уже завершён! Нажмите 'Старт' для перезапуска.");
                    return;
                }
            }

            boolean hasNext = algorithm.nextStep();
            saveCurrentStepSnapshot(algorithm.getHistory().get(algorithm.getHistory().size() - 1));
            currentStepIndex = stepHistory.size() - 1;
            updateViewFromCurrentStep();

            if (!hasNext) {
                ToastNotification.showInformation(mainWindow, 
                    "Алгоритм завершён! Вес МОД: " + algorithm.getMstLen());
                mainWindow.getBottomPanel().setButtonsState(false, false, true, true);
            } else {
                boolean canPrev = algorithm.canGoBack();
                mainWindow.getBottomPanel().setButtonsState(false, true, true, true);
            }

        } catch (Exception ex) {
            ToastNotification.showError(mainWindow, "Ошибка шага: " + ex.getMessage());
        }
    }

    private void saveCurrentStepSnapshot(String description) {
        StepSnapshot snapshot = new StepSnapshot();
        snapshot.visitedVertices = new HashSet<>(algorithm.getVisitedVertices());
        snapshot.mstEdges = new HashSet<>();
        for (Edge edge : algorithm.getMSTEdges()) {
            Vertex[] v = edge.getVertices();
            snapshot.mstEdges.add(v[0].getNumber() + "-" + v[1].getNumber());
        }
        snapshot.currentVertex = algorithm.getCurrentVertex();
        snapshot.currentEdge = algorithm.getCurrentEdgeKey();
        snapshot.description = description;
        snapshot.stepNumber = stepHistory.size() + 1;
        stepHistory.add(snapshot);
        
        addStepToHistory("Шаг " + snapshot.stepNumber + ": " + description);
    }

    private void updateViewFromCurrentStep() {
        if (currentStepIndex < 0 || currentStepIndex >= stepHistory.size()) return;
        
        StepSnapshot snapshot = stepHistory.get(currentStepIndex);
        mainWindow.getGraphCanvas().updateState(
            snapshot.visitedVertices,
            snapshot.mstEdges,
            snapshot.currentVertex,
            snapshot.currentEdge
        );
    }
}