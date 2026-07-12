package view;

import controller.Controller;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Controller();  // ← здесь создаётся Controller
        });
    }
}