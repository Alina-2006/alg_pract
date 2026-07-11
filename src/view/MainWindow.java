package view;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {

    private  JMenuBar menuBar;
    private GraphCanvas graphCanvas;
    private  ToolbarPanel toolbarPanel;
    private  BottomPanel bottomPanel;

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
        fileMenu.add(new JMenuItem("Открыть"));
        fileMenu.add(new JMenuItem("Сохранить"));
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
}
