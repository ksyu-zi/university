package ru.university;

import ru.university.app.UniversityApplication;
import ru.university.gui.MainWindow;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
//        UniversityApplication app = new UniversityApplication();
//        app.run(args);
        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.setVisible(true);
        });
    }
}