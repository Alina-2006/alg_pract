package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class HistoryPanel extends JPanel {

    private JList<String> stepList;
    private DefaultListModel<String> listModel;
    private JScrollPane scrollPane;

    public HistoryPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(300, 400));

        initList();
    }

    private void initList() {
        listModel = new DefaultListModel<>();
        stepList = new JList<>(listModel);
        stepList.setFont(new Font("Arial", Font.PLAIN, 14));

        scrollPane = new JScrollPane(stepList);
        add(scrollPane, BorderLayout.CENTER);
    }

    // Метод для добавления шага
    public void addStep(String stepDescription) {
        listModel.addElement(stepDescription);
        stepList.ensureIndexIsVisible(listModel.getSize() - 1);
    }

    // Метод для очистки
    public void clear() {
        listModel.clear();
    }

    // Метод для показа/скрытия панели
    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    // Геттер для списка (если понадобится)
    public JList<String> getStepList() {
        return stepList;
    }
}