package view;

import javax.swing.*;
import java.awt.*;

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

    private void initButtons() {
        btnStart = createButton("Play", Color.GREEN);
        btnPrevStep = createButton("◀", Color.GREEN);
        btnNextStep = createButton("▶", Color.GREEN);
        btnReset = createButton("■", Color.RED);

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
}
