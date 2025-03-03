package ru.university.gui;

import ru.university.model.Student;
import ru.university.repository.StudentRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class StudentsTableWindow extends JFrame {
    public StudentsTableWindow() {
        setTitle("Студенты");
        setSize(500, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Имя", "Фамилия", "Пол", "ID Группы"};
        StudentRepository studentRepository = new StudentRepository();
        List<Student> students = studentRepository.list();
        Object[][] data = new Object[students.size()][5];
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            data[i][0] = student.getId();
            data[i][1] = student.getSurname();
            data[i][2] = student.getName();
            data[i][3] = student.getGender();
            data[i][4] = student.getGroupId();
        }

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}
