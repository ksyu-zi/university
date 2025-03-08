package ru.university.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Главное окно");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 10));

        JDesktopPane desktopPane = new JDesktopPane();

        Dimension buttonSize = new Dimension(200, 30);

        JButton curatorsButton = new JButton("Таблица кураторов");
        curatorsButton.setMaximumSize(buttonSize);
        curatorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame frame = new CuratorsTableWindow();
                desktopPane.add(frame);
                frame.setVisible(true);
            }
        });

        JButton groupsButton = new JButton("Таблица групп");
        groupsButton.setMaximumSize(buttonSize);
        groupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame frame = new GroupsTableWindow();
                desktopPane.add(frame);
                frame.setVisible(true);
            }
        });

        JButton studentsButton = new JButton("Таблица студентов");
        studentsButton.setMaximumSize(buttonSize);
        studentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame frame = new StudentsTableWindow();
                desktopPane.add(frame);
                frame.setVisible(true);
            }
        });

        JButton genderChartButton = new JButton("Диаграмма");
        genderChartButton.setMaximumSize(buttonSize);
        genderChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JInternalFrame frame = new DiagramWindow();
                desktopPane.add(frame);
                frame.setVisible(true);
            }
        });

        buttonPanel.add(curatorsButton);
        buttonPanel.add(groupsButton);
        buttonPanel.add(studentsButton);
        buttonPanel.add(genderChartButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(desktopPane, BorderLayout.CENTER);
    }
}
