package ru.university.gui;

import ru.university.model.Group;
import ru.university.repository.GroupRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class GroupsTableWindow extends JFrame {
    public GroupsTableWindow() {
        setTitle("Группы");
        setSize(500, 400);
        setLocationRelativeTo(null);

        String[] columnNames = {"ID", "Название группы", "ID Куратора"};
        GroupRepository groupRepository = new GroupRepository();
        List<Group> groups = groupRepository.list();
        Object[][] data = new Object[groups.size()][3];
        for (int i = 0; i < groups.size(); i++) {
            Group group = groups.get(i);
            data[i][0] = group.getId();
            data[i][1] = group.getGroupName();
            data[i][2] = group.getCuratorId();
        }

        JTable table = new JTable(new DefaultTableModel(data, columnNames));
        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane);
    }
}
