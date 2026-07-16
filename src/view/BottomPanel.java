package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * Панель внизу, с кнопками для анимациии
 **/

public class BottomPanel extends JPanel {

    private JButton btnStart;
    private JButton btnPrevStep;
    private JButton btnNextStep;
    private JButton btnReset;
    private JButton btnHistory;
    private JButton btnAuthors;
    private JLabel resultText;

    public BottomPanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(1024, 50));

        initButtons();
    }
    // Инициализация кнопок
    private void initButtons() {
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        btnStart = createButton("S", Color.GREEN);
        btnPrevStep = createButton("P", Color.GREEN);
        btnNextStep = createButton("N", Color.GREEN);
        btnReset = createButton("R", Color.RED);

        leftPanel.add(btnStart);
        leftPanel.add(btnPrevStep);
        leftPanel.add(btnNextStep);
        leftPanel.add(btnReset);
        leftPanel.add(Box.createHorizontalStrut(20));

        btnHistory = new JButton("История");
        btnHistory.setFont(new Font("Arial",  Font.PLAIN, 14));
        leftPanel.add(btnHistory);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnAuthors = new JButton("Авторы");
        btnAuthors.setFont(new Font("Arial", Font.PLAIN, 14));
        rightPanel.add(btnAuthors);

        resultText = new JLabel("");
        resultText.setFont(new Font("Arial", Font.BOLD, 16));
        resultText.setForeground(new Color(34, 139, 34));
        resultText.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        add(resultText);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    public void setResultText(String text) {
        resultText.setText(text);
    }

    public void clearResult() {
        resultText.setText("");
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(color);
        button.setPreferredSize(new Dimension(60, 35));
        return button;
    }

    public void setButtonsState(boolean canStart, boolean canNext, boolean canPrev, boolean canReset) {
        btnStart.setEnabled(canStart);
        btnNextStep.setEnabled(canNext);
        btnPrevStep.setEnabled(canPrev);
        btnReset.setEnabled(canReset);
    }

    // Методы для добавления обработчиков событий
    public void addStartListener(ActionListener listener) {
        btnStart.addActionListener(listener);
    }

    public void addPrevStepListener(ActionListener listener) {
        btnPrevStep.addActionListener(listener);
    }

    public void addNextStepListener(ActionListener listener) {
        btnNextStep.addActionListener(listener);
    }

    public void addResetListener(ActionListener listener) {
        btnReset.addActionListener(listener);
    }

    public void addHistoryListener(ActionListener listener) {
        btnHistory.addActionListener(listener);
    }

    public void addAuthorsListener(ActionListener listener) {
        btnAuthors.addActionListener(listener);
    }

    // Геттеры для кнопок (если понадобятся прямые ссылки)
    public JButton getBtnStart() { return btnStart; }
    public JButton getBtnPrevStep() { return btnPrevStep; }
    public JButton getBtnNextStep() { return btnNextStep; }
    public JButton getBtnReset() { return btnReset; }
    public JButton getBtnHistory() { return btnHistory; }
    public JButton getBtnAuthors() { return btnAuthors; }
}