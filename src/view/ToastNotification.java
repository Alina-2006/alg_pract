package view;

import javax.swing.*;
import java.awt.*;

public class ToastNotification {

    /**
     * Показывает уведомление с иконкой, которое исчезает само через 2.5 секунды
     * @param owner Родительское окно (для позиционирования)
     * @param message Текст уведомления
     * @param messageType Тип сообщения (JOptionPane.ERROR_MESSAGE, QUESTION_MESSAGE и т.д.)
     */
    public static void show(Window owner, String message, int messageType) {
        JWindow toast = new JWindow(owner);
        toast.setLayout(new BorderLayout());

        // 1. Получаем иконку на основе типа сообщения
        Icon icon = getIconForType(messageType);
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5)); // Отступ справа от иконки

        // 2. Создаем текстовую метку
        JLabel textLabel = new JLabel(message, SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.BOLD, 14));

        // 3. Определяем цвета фона и текста в зависимости от типа
        Color bgColor;
        Color textColor = Color.BLACK;

        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE:
                bgColor = new Color(255, 200, 200); // Красный фон для ошибок
                break;
            case JOptionPane.WARNING_MESSAGE:
                bgColor = new Color(255, 255, 200); // Желтый фон для предупреждений
                break;
            case JOptionPane.QUESTION_MESSAGE:
                bgColor = new Color(200, 220, 255); // Голубой фон для вопросов
                break;
            case JOptionPane.INFORMATION_MESSAGE:
                bgColor = new Color(200, 255, 200); // Зеленый фон для успеха
                break;
            default:
                bgColor = Color.WHITE;
        }

        // 4. Собираем панель: Иконка + Текст
        JPanel contentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        contentPanel.setBackground(bgColor);
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // Тонкая рамка

        if (icon != null) {
            contentPanel.add(iconLabel);
        }
        contentPanel.add(textLabel);

        toast.add(contentPanel, BorderLayout.CENTER);
        toast.pack(); // Расчет размера окна

        // 5. Позиционируем в правом нижнем углу главного окна
        if (owner != null) {
            Point location = owner.getLocation();
            Dimension size = owner.getSize();
            // 20px отступ справа, 70px отступ снизу (чтобы не перекрывать BottomPanel)
            int x = location.x + size.width - toast.getWidth() - 20;
            int y = location.y + size.height - toast.getHeight() - 70;
            toast.setLocation(x, y);
        } else {
            toast.setLocationRelativeTo(null);
        }

        toast.setAlwaysOnTop(true);
        toast.setVisible(true);

        // 6. Таймер на автоматическое закрытие (2.5 секунды)
        Timer timer = new Timer(2500, e -> toast.dispose());
        timer.setRepeats(false);
        timer.start();
    }

    // Вспомогательный метод для получения иконки из системы
    private static Icon getIconForType(int messageType) {
        String key;
        switch (messageType) {
            case JOptionPane.ERROR_MESSAGE: key = "OptionPane.errorIcon"; break;
            case JOptionPane.WARNING_MESSAGE: key = "OptionPane.warningIcon"; break;
            case JOptionPane.QUESTION_MESSAGE: key = "OptionPane.questionIcon"; break;
            case JOptionPane.INFORMATION_MESSAGE: key = "OptionPane.informationIcon"; break;
            default: return null;
        }
        return UIManager.getIcon(key);
    }

    // === Удобные методы-обертки ===

    public static void showError(Window owner, String message) {
        show(owner, message, JOptionPane.ERROR_MESSAGE);
    }

    public static void showSuccess(Window owner, String message) {
        show(owner, message, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void showQuestion(Window owner, String message) {
        show(owner, message, JOptionPane.QUESTION_MESSAGE);
    }

    public static void showInformation(Window owner, String message) {
        show(owner, message, JOptionPane.INFORMATION_MESSAGE);
    }
}