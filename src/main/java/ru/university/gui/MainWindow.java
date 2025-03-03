package ru.university.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {

    public MainWindow() {
        setTitle("Главное окно");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(2, 2)); // Сетка 2x2 для кнопок

        JButton curatorsButton = new JButton("Таблица кураторов");
        curatorsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CuratorsTableWindow().setVisible(true);
            }
        });

        JButton groupsButton = new JButton("Таблица групп");
        groupsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new GroupsTableWindow().setVisible(true);
            }
        });

        JButton studentsButton = new JButton("Таблица студентов");
        studentsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new StudentsTableWindow().setVisible(true);
            }
        });

        JButton genderChartButton = new JButton("Диаграмма");
        genderChartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new DiagramWindow().setVisible(true);
            }
        });

        add(curatorsButton);
        add(groupsButton);
        add(studentsButton);
        add(genderChartButton);
    }
}