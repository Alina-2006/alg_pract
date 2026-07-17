package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Панель инструментов (справа экрана)
 **/

public class ToolbarPanel extends JPanel{
    private JButton btnAddVertex;
    private JButton btnAddEdge;
    private  JButton btnEraser;
    private  JButton btnClearAll;

    // флаги режимов
    private boolean isAddVertexMode = false;
    private boolean isAddEdgeMode = false;
    private boolean isEraserMode = false;

    //для режима добавления рёбер - запоминаем первую выбранную вершину
    private Integer selectedVertexForEdge = null;

    public ToolbarPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(60, 200));
        setBorder(BorderFactory.createEmptyBorder(10, 5, 10, 5));

        initButtons();
    }

    private void initButtons() {
        btnAddVertex = createToolButton("", "icons/Vertex.png");
        btnAddEdge = createToolButton("","icons/Edge.png" );
        btnEraser = createToolButton("", "icons/Eraser.png");
        btnClearAll = createToolButton("", "icons/Clear.png");

        add(btnAddVertex);
        add(Box.createVerticalStrut(5));
        add(btnAddEdge);
        add(Box.createVerticalStrut(5));
        add(btnEraser);
        add(Box.createVerticalStrut(5));
        add(btnClearAll);
    }

    //создание аккуратных одинаково оформленных кнопок
    private  JButton createToolButton(String text, String iconPath) {
        JButton button = new JButton(text);

        ImageIcon icon = new ImageIcon(getClass().getResource(iconPath));

        Image img = icon.getImage();
        button.setIcon(new ImageIcon(img));

        button.setPreferredSize(new Dimension(50, 50));
        button.setMaximumSize(new Dimension(50, 50));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setBorderPainted(true);
        button.setFocusPainted(false);

        return button;
    }

    public boolean isAddVertexMode() { return isAddVertexMode; }
    public void setAddVertexMode(boolean addVertexMode) {
        this.isAddVertexMode = addVertexMode;
        btnAddVertex.setBackground(addVertexMode ? new Color(80, 150, 170) : null);
    }

    public boolean isAddEdgeMode() { return isAddEdgeMode; }
    public void setAddEdgeMode(boolean addEdgeMode) {
        this.isAddEdgeMode = addEdgeMode;
        btnAddEdge.setBackground(addEdgeMode ? new Color(80, 150, 170) : null);
    }

    // методы для управления режимом ластика
    public boolean isEraserMode() {
        return isEraserMode;
    }

    //методы для работы с выбранными вершинами
    public Integer getSelectedVertexForEdge() {
        return selectedVertexForEdge;
    }

    public void setSelectedVertexForEdge(Integer vertexId) {
        this.selectedVertexForEdge = vertexId;
    }

    public void clearSelectedVertexForEdge() {
        this.selectedVertexForEdge = null;
    }

    public void setEraserMode(boolean eraserMode) {
        this.isEraserMode = eraserMode;
        btnEraser.setBackground(eraserMode ? new Color(80, 150, 170) : null);
    }

    // общение с остальной частью проекта (отправка статуса кнопки)
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

    // ===== ДИАЛОГИ С ЦЕНТРИРОВАНИЕМ (исправленная версия) =====

    public int[] showAddVertexDialog(Window parent) {
        JTextField idField = new JTextField(10);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("ID вершины:"));
        panel.add(idField);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "Добавить вершину");
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        int result = (Integer) optionPane.getValue();
        if (result == JOptionPane.OK_OPTION) {
            try {
                int id = Integer.parseInt(idField.getText().trim());
                ToastNotification.showSuccess(parent, "Вершина " + id + " добавлена");
                return new int[]{id};
            } catch (NumberFormatException e) {
                ToastNotification.showError(parent, "Введите корректное число!");
                return null;
            }
        }
        return null;
    }

    public int[] showAddEdgeDialog(Window parent) {
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

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "Добавить ребро");
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        int result = (Integer) optionPane.getValue();
        if (result == JOptionPane.OK_OPTION) {
            try {
                int from = Integer.parseInt(fromField.getText().trim());
                int to = Integer.parseInt(toField.getText().trim());
                int weight = Integer.parseInt(weightField.getText().trim());
                return new int[]{from, to, weight};
            } catch (NumberFormatException e) {
                ToastNotification.showError(parent, "Введите корректные числа!");
                return null;
            }
        }
        return null;
    }

    public Integer showWeightDialog(Window parent, int from, int to) {
        JTextField weightField = new JTextField(10);
        JPanel panel = new JPanel(new FlowLayout());
        panel.add(new JLabel("Вес ребра " + from + " → " + to + ":"));
        panel.add(weightField);

        JOptionPane optionPane = new JOptionPane(panel, JOptionPane.QUESTION_MESSAGE, JOptionPane.OK_CANCEL_OPTION);
        JDialog dialog = optionPane.createDialog(parent, "Добавить ребро");
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);

        int result = (Integer) optionPane.getValue();
        if (result == JOptionPane.OK_OPTION) {
            try {
                int weight = Integer.parseInt(weightField.getText().trim());
                return weight;
            } catch (NumberFormatException e) {
                ToastNotification.showError(parent, "Введите корректное число!");
                return null;
            }
        }
        return null;
    }

    // Диалог подтверждения полного сброса графа
    public boolean showClearAllConfirmation() {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);

        int result = JOptionPane.showConfirmDialog(
                parentWindow,
                "Вы уверены, что хотите удалить весь граф?\nЭто действие нельзя отменить.",
                "Подтверждение удаления",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        return result == JOptionPane.YES_OPTION;
    }

    //методы для перекоючения режимов

    public void toggleAddVertexMode() {
        setAddVertexMode(!isAddVertexMode);
        if (isAddVertexMode) {
            setAddEdgeMode(false);
            setEraserMode(false);
            selectedVertexForEdge = null;
            btnAddVertex.setBackground(new Color(80, 150, 170));
        } else {
            btnAddVertex.setBackground(null);
        }
    }

    public void toggleAddEdgeMode() {
        setAddEdgeMode(!isAddEdgeMode);
        if (isAddEdgeMode) {
            setAddVertexMode(false);
            setEraserMode(false);
            selectedVertexForEdge = null;
            btnAddEdge.setBackground(new Color(80, 150, 170));
        } else {
            btnAddEdge.setBackground(null);
            selectedVertexForEdge = null;
        }
    }

    public void toggleEraserMode() {
        setEraserMode(!isEraserMode);
        if (isEraserMode) {
            setAddVertexMode(false);
            setAddEdgeMode(false);
            selectedVertexForEdge = null;
            btnEraser.setBackground(new Color(80, 150, 170));
        } else {
            btnEraser.setBackground(null);
        }
    }

    //Гетеры

    public JButton getBtnAddVertex() {
        return btnAddVertex;
    }

    public JButton getBtnAddEdge() {
        return btnAddEdge;
    }

    public JButton getBtnEraser() {
        return btnEraser;
    }

    public JButton getBtnClearAll() {
        return btnClearAll;
    }
}