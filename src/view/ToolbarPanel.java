package view;

import javax.swing.*;
import java.awt.*;

public class ToolbarPanel extends JPanel{
    private JButton btnAddVertex;
    private JButton btnAddEdge;
    private  JButton btnEraser;
    private  JButton btnClearAll;

    public ToolbarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(60, 200));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        initButtons();
    }

    private void initButtons() {
        btnAddVertex = createToolButton("B");
        btnAddEdge = createToolButton("-");
        btnEraser = createToolButton("Er");
        btnClearAll = createToolButton("\uD83D\uDDD1");

        add(btnAddVertex);
        add(Box.createVerticalStrut(5));
        add(btnAddEdge);
        add(Box.createVerticalStrut(5));
        add(btnEraser);
        add(Box.createVerticalStrut(5));
        add(btnClearAll);
    }

    private  JButton createToolButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(50, 50));
        button.setMaximumSize(new Dimension(50, 50));
        button.setFont(new Font("Arial", Font.BOLD, 20));
        return button;
    }
    
}
