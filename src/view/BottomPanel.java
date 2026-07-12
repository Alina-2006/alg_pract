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

    public BottomPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        setPreferredSize(new Dimension(1024, 50));

        initButtons();
    }
// Инициализация кнопок
    private void initButtons() {
        btnStart = createButton("Запуск", Color.GREEN);
        btnPrevStep = createButton("Назад", Color.GREEN);
        btnNextStep = createButton("Вперёд", Color.GREEN);
        btnReset = createButton("Сброс", Color.RED);

        add(btnStart);
        add(btnPrevStep);
        add(btnNextStep);
        add(btnReset);

        add(Box.createHorizontalStrut(20));

        btnHistory = new JButton("История");
        btnHistory.setFont(new Font("Arial",  Font.PLAIN, 14));

        add(Box.createHorizontalStrut(20));

        add(Box.createHorizontalGlue());
        btnAuthors = new JButton("Авторы");
        btnAuthors.setFont(new Font("Arial", Font.PLAIN, 14));
        add(btnAuthors);
    }

    private JButton createButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setForeground(color);
        button.setPreferredSize(new Dimension(40, 35));
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

