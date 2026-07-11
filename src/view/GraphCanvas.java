package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

public class GraphCanvas extends JPanel{

    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;

    public GraphCanvas() {
        setBackground(Color.WHITE);

        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.isControlDown()) {
                    if (e.getWheelRotation() < 0) {
                        scale *= 1.1;
                    } else {
                        scale /= 1.1;
                    }
                } else {
                    offsetY -= e.getWheelRotation() * 20;
                }
                repaint();
            }
        });
    }

    @Override
    protected void  paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        drawTestGraph(g2);
    }

    private  void drawTestGraph(Graphics2D g2) {
        int x1 = 200, y1 = 150;
        int x2 = 350, y2 = 300;
        int radius = 25;

        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(x1, y1, x2, y2);

        g2.setFont(new Font("Arial", Font.PLAIN, 16));
        g2.drawString("10", (x1 + x2) / 2 - 10, (y1 + y2) / 2);

        // Рисуем вершину 1 (круг)
        g2.setColor(Color.WHITE);
        g2.fillOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
        g2.setColor(Color.BLACK);
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x1 - radius, y1 - radius, radius * 2, radius * 2);
        g2.drawString("1", x1 - 5, y1 + 5);

        // Рисуем вершину 2 (круг)
        g2.setColor(Color.WHITE);
        g2.fillOval(x2 - radius, y2 - radius, radius * 2, radius * 2);
        g2.setColor(Color.BLACK);
        g2.drawOval(x2 - radius, y2 - radius, radius * 2, radius * 2);
        g2.drawString("2", x2 - 5, y2 + 5);
    }
}
