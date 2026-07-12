package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.*;
import java.util.List;

/**
 * Отрисовка графов
 **/
public class GraphCanvas extends JPanel {

    // масштаб и смещение
    private Point lastMousePosition = null;
    private boolean isPanning = false;
    private double scale = 1.0;
    private int offsetX = 0;
    private int offsetY = 0;

    // данные для отрисовки
    private List<VertexData> vertices = new ArrayList<>();
    private List<EdgeData> edges = new ArrayList<>();

    // состояние алгоритма (выделение цветом)
    private Set<Integer> visitedVertices = new HashSet<>();
    private Set<String> mstEdges = new HashSet<>();
    private String currentEdge = null;
    private Integer currentVertex = null;

    // размер вершин
    private int vertexRadius = 25;

    public GraphCanvas() {
        setBackground(Color.WHITE);
        initMouseHandlers();
    }

    // функция для мыши
    private void initMouseHandlers() {
        // Проверка на нажатие
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    lastMousePosition = e.getPoint();
                    isPanning = true;
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isMiddleMouseButton(e)) {
                    isPanning = false;
                    setCursor(Cursor.getDefaultCursor());
                }
            }
        });

        // перетаскивание при зажатом колёсике
        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (isPanning) {
                    int dx = e.getX() - lastMousePosition.x;
                    int dy = e.getY() - lastMousePosition.y;
                    offsetX += dx;
                    offsetY += dy;
                    lastMousePosition = e.getPoint();
                    repaint();
                }
            }
        });

        // зум прокрутом колёсиком
        addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                if (e.getWheelRotation() < 0) {
                    scale *= 1.1;
                } else {
                    scale /= 1.1;
                }
                repaint();
            }
        });
    }

    public void setVertices(List<VertexData> vertices) {
        this.vertices = vertices;
        repaint();
    }

    public void setEdges(List<EdgeData> edges) {
        this.edges = edges;
        repaint();
    }

    public void updateState(Set<Integer> visited, Set<String> mstEdges,
                            Integer currentVertex, String currentEdge) {
        this.visitedVertices = visited;
        this.mstEdges = mstEdges;
        this.currentVertex = currentVertex;
        this.currentEdge = currentEdge;
        repaint();
    }

    public void reset() {
        visitedVertices.clear();
        mstEdges.clear();
        currentVertex = null;
        currentEdge = null;
        repaint();
    }

    // отрисовка графа
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // сглаживание
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        // применяем масштаб и смещение
        g2.translate(offsetX, offsetY);
        g2.scale(scale, scale);

        // Сначала рисуем рёбра (чтобы были под вершинами)
        drawEdges(g2);

        // Потом вершины
        drawVertices(g2);
    }

    private void drawEdges(Graphics2D g2) {
        for (EdgeData edge : edges) {
            VertexData v1 = findVertex(edge.getFrom());
            VertexData v2 = findVertex(edge.getTo());

            if (v1 == null || v2 == null) continue;

            // Определяем цвет ребра
            String edgeKey = edge.getFrom() + "-" + edge.getTo();
            if (mstEdges.contains(edgeKey)) {
                g2.setColor(new Color(34, 139, 34)); // зелёный — в МОД
                g2.setStroke(new BasicStroke(3));
            } else if (edgeKey.equals(currentEdge)) {
                g2.setColor(new Color(220, 80, 50)); // красный — текущее
                g2.setStroke(new BasicStroke(2));
            } else {
                g2.setColor(Color.BLACK); // обычное
                g2.setStroke(new BasicStroke(1.5F));
            }

            // Рисуем линию
            g2.drawLine((int) v1.getX(), (int) v1.getY(),
                    (int) v2.getX(), (int) v2.getY());

            // Рисуем вес посередине
            int midX = ((int) v1.getX() + (int) v2.getX()) / 2;
            int midY = ((int) v1.getY() + (int) v2.getY()) / 2;
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 14));
            g2.drawString(String.valueOf(edge.getWeight()), midX - 5, midY - 5);
        }
    }

    private void drawVertices(Graphics2D g2) {
        for (VertexData vertex : vertices) {
            int x = (int) vertex.getX();
            int y = (int) vertex.getY();

            // Определяем цвет вершины
            if (vertex.getId() == (currentVertex != null ? currentVertex : -1)) {
                g2.setColor(new Color(255, 200, 0)); // жёлтый — текущая
            } else if (visitedVertices.contains(vertex.getId())) {
                g2.setColor(new Color(60, 179, 113)); // зелёный — посещённая
            } else {
                g2.setColor(new Color(100, 149, 237)); // голубой — обычная
            }

            // Рисуем круг
            g2.fillOval(x - vertexRadius, y - vertexRadius,
                    vertexRadius * 2, vertexRadius * 2);
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawOval(x - vertexRadius, y - vertexRadius,
                    vertexRadius * 2, vertexRadius * 2);

            // Рисуем номер вершины
            g2.setFont(new Font("Arial", Font.BOLD, 16));
            String text = String.valueOf(vertex.getId());
            FontMetrics fm = g2.getFontMetrics();
            int textWidth = fm.stringWidth(text);
            g2.drawString(text, x - textWidth / 2, y + 5);
        }
    }

    private VertexData findVertex(int id) {
        for (VertexData v : vertices) {
            if (v.getId() == id) return v;
        }
        return null;
    }

    // === МЕТОДЫ ИЗ ВАШЕЙ ВЕРСИИ ===

    // Метод для передачи графа из ядра (если используется класс Graph)
    
    public void setGraph(Graph graph) {
        // Конвертируем Graph в VertexData и EdgeData
        if (graph != null) {
            this.vertices = graph.getVerticesAsData(); // предполагаемый метод
            this.edges = graph.getEdgesAsData();       // предполагаемый метод
            repaint();
        }
    }
    */

    // Альтернативный метод updateState, который принимает объект Step
    // TODO: Раскомментировать, когда стыковщик создаст класс Step
    /*
    public void updateState(Step step) {
        if (step != null) {
            this.visitedVertices = step.getVisitedVertices();
            this.mstEdges = step.getMstEdges();
            this.currentVertex = step.getCurrentVertex();
            this.currentEdge = step.getCurrentEdge();
            repaint();
        }
    }
    

    // Метод для получения информации о вершине по клику
    public VertexData getVertexAtPoint(int x, int y) {
        for (VertexData vertex : vertices) {
            double dx = vertex.getX() - x;
            double dy = vertex.getY() - y;
            double distance = Math.sqrt(dx * dx + dy * dy);
            if (distance <= vertexRadius) {
                return vertex;
            }
        }
        return null;
    }

    // Метод для получения информации о ребре по клику
    public EdgeData getEdgeAtPoint(int x, int y) {
        // Упрощенная проверка - можно улучшить
        for (EdgeData edge : edges) {
            VertexData v1 = findVertex(edge.getFrom());
            VertexData v2 = findVertex(edge.getTo());
            if (v1 != null && v2 != null) {
                // Проверяем, находится ли точка близко к линии
                if (isPointNearLine(x, y, v1.getX(), v1.getY(), v2.getX(), v2.getY(), 10)) {
                    return edge;
                }
            }
        }
        return null;
    }

    // Вспомогательный метод для проверки расстояния до линии
    private boolean isPointNearLine(int px, int py, double x1, double y1, double x2, double y2, double tolerance) {
        double A = px - x1;
        double B = py - y1;
        double C = x2 - x1;
        double D = y2 - y1;

        double dot = A * C + B * D;
        double len_sq = C * C + D * D;
        double param = -1;
        if (len_sq != 0) // в случае, если начальная и конечная точки не совпадают
            param = dot / len_sq;

        double xx, yy;

        if (param < 0) {
            xx = x1;
            yy = y1;
        } else if (param > 1) {
            xx = x2;
            yy = y2;
        } else {
            xx = x1 + param * C;
            yy = y1 + param * D;
        }

        double dx = px - xx;
        double dy = py - yy;
        return Math.sqrt(dx * dx + dy * dy) < tolerance;
    }
}
