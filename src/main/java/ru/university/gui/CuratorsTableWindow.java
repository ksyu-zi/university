package ru.university.gui;

import ru.university.model.Curator;
import ru.university.repository.CuratorRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class CuratorsTableWindow extends JFrame {
    public CuratorsTableWindow() {
        setTitle("Кураторы");
        setSize(500, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Имя", "Фамилия"};
        CuratorRepository curatorRepository = new CuratorRepository();
        List<Curator> curators = curatorRepository.list();
        Object[][] data = new Object[curators.size()][3];
        for (int i = 0; i < curators.size(); i++) {
            Curator curator = curators.get(i);
            data[i][0] = curator.getId();
            data[i][1] = curator.getSurname();
            data[i][2] = curator.getName();
        }

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}