package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private  JMenuBar menuBar;
    private GraphCanvas graphCanvas;
    private  ToolbarPanel toolbarPanel;
    private  BottomPanel bottomPanel;
    private HistoryPanel historyPanel;

    public MainWindow(){
        setTitle("Алгоритм Прима");
        setSize(1024, 768);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        initMenuBar();
        initLayout();
    }

    private void initMenuBar() {
        menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("Файл");
        JMenuItem openItem = new JMenuItem("Открыть");
        JMenuItem saveItem = new JMenuItem("Сохранить");

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);
    }

    private void initLayout() {
        setLayout(new BorderLayout());

        //Холст для графа
        graphCanvas = new GraphCanvas();
        add(graphCanvas, BorderLayout.CENTER);

        //Панель инструментов справа
        toolbarPanel = new ToolbarPanel();
        add(toolbarPanel, BorderLayout.EAST);

        //Панель управления анимацией снизу
        bottomPanel = new BottomPanel();
        add(bottomPanel, BorderLayout.SOUTH);
    }
    // Метод для показа ошибок
    public void showError(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Ошибка",
                JOptionPane.ERROR_MESSAGE);
    }

    // Метод для показа предупреждений
    public void showWarning(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Предупреждение",
                JOptionPane.WARNING_MESSAGE);
    }

    // Метод для показа информационных сообщений
    public void showInfo(String message) {
        JOptionPane.showMessageDialog(this,
                message,
                "Информация",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // геттеры для панелей

    public GraphCanvas getGraphCanvas() {
        return graphCanvas;
    }

    public ToolbarPanel getToolbarPanel() {
        return toolbarPanel;
    }

    public BottomPanel getBottomPanel() {
        return bottomPanel;
    }

    public HistoryPanel getHistoryPanel() {
        return historyPanel;
    }

    // Геттеры для меню (если понадобятся)
    public JMenuBar getMainMenuBar() {
        return menuBar;
    }

    // Метод для установки истории
    public void setHistoryPanel(HistoryPanel historyPanel) {
        this.historyPanel = historyPanel;
    }
}
