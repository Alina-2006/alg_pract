package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

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

    
    public void addAddVertexListener(ActionListener listener) {
        btnAddVertex.addActionListener(listener);
    }

    public void addAddEdgeListener(ActionListener listener) {
        btnAddEdge.addActionListener(listener);
    }

    public void addEraserListener(ActionListener listener) {
        btnEraser.addActionListener(listener);
    }

    public void addClearAllListener(ActionListener listener) {
        btnClearAll.addActionListener(listener);
    }

    // Диалог для добавления вершины
    public int[] showAddVertexDialog() {
        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("ID вершины:"));
        panel.add(idField);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Добавить вершину",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                return new int[]{id};
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введите корректное число!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }

    // Диалог для добавления ребра
    public int[] showAddEdgeDialog() {
        JTextField fromField = new JTextField(10);
        JTextField toField = new JTextField(10);
        JTextField weightField = new JTextField(10);

        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.add(new JLabel("FROM (вершина):"));
        panel.add(fromField);
        panel.add(new JLabel("TO (вершина):"));
        panel.add(toField);
        panel.add(new JLabel("Вес ребра:"));
        panel.add(weightField);

        int result = JOptionPane.showConfirmDialog(
            this,
            panel,
            "Добавить ребро",
            JOptionPane.OK_CANCEL_OPTION,
            JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            try {
                int from = Integer.parseInt(fromField.getText().trim());
                int to = Integer.parseInt(toField.getText().trim());
                int weight = Integer.parseInt(weightField.getText().trim());
                return new int[]{from, to, weight};
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Введите корректные числа!", "Ошибка", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        return null;
    }
}
