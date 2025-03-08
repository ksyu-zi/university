package ru.university.gui;

import ru.university.model.Group;
import ru.university.repository.GroupRepository;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class GroupsTableWindow extends JInternalFrame {
    private final GroupRepository groupRepository;
    private final JTable table;
    private final DefaultTableModel model;
    private int lastSelectedRow = -1;

    public GroupsTableWindow() {
        setTitle("Группы");
        setSize(400, 300);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        groupRepository = new GroupRepository();
        String[] columnNames = {"ID", "Название группы", "ID Куратора"};

        model = new DefaultTableModel(columnNames, 0);
        table = new JTable(model);
        updateTable();

        table.getColumnModel().getColumn(0)
                .setCellEditor(new DefaultCellEditor(new JTextField()) {
                    @Override
                    public boolean isCellEditable(java.util.EventObject anEvent) {
                        return false;
                    }
                });

        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (lastSelectedRow != -1 && lastSelectedRow != selectedRow) {
                    saveRowChanges(lastSelectedRow);
                }
                lastSelectedRow = selectedRow;
            }
        });

        table.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (lastSelectedRow != -1) {
                    saveRowChanges(lastSelectedRow);
                }
            }
        });

        addInternalFrameListener(new InternalFrameAdapter() {
            @Override
            public void internalFrameClosing(InternalFrameEvent e) {
                int option = JOptionPane.showConfirmDialog(
                        GroupsTableWindow.this,
                        "Не корректные данные могут не сохраниться. Закрыть окно?",
                        "Подтверждение закрытия",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

                if (option == JOptionPane.YES_OPTION) {
                    dispose();
                } else {
                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton addButton = new JButton("Добавить");
        addButton.addActionListener(e -> addNewRow());

        JButton deleteButton = new JButton("Удалить");
        deleteButton.addActionListener(e -> deleteSelectedRow());

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);

        add(new JScrollPane(table));
        add(buttonPanel);
    }

    private void addNewRow() {
        for (int row = 0; row < model.getRowCount(); row++) {
            if (rowValid(row)) {
                JOptionPane.showMessageDialog(this,
                        "Исправьте ошибки перед добавлением!",
                        "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
        model.addRow(new Object[]{getNextId(), "", ""});
    }

    private boolean rowValid(int row) {
        String groupName = model.getValueAt(row, 1).toString().trim();
        String curatorIdStr = model.getValueAt(row, 2).toString().trim();

        try {
            return groupName.isEmpty() || Integer.parseInt(curatorIdStr) <= 0;
        } catch (Exception e) {
            return true;
        }
    }

    private void saveRowChanges(int row) {
        if (row < 0 || row >= model.getRowCount()) return;

        String groupName = model.getValueAt(row, 1).toString().trim();
        String curatorIdStr = model.getValueAt(row, 2).toString().trim();

        if (groupName.isEmpty() || curatorIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Заполните все поля!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int curatorId = Integer.parseInt(curatorIdStr);
            if (curatorId <= 0) throw new NumberFormatException();

            Group group = (row < groupRepository.list().size())
                    ? groupRepository.list().get(row)
                    : new Group();

            group.setId((int) model.getValueAt(row, 0));
            group.setGroupName(groupName);
            group.setCuratorId(curatorId);

            if (row < groupRepository.list().size()) {
                groupRepository.edit(group);
            } else {
                groupRepository.add(group);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "ID куратора должен быть положительным числом!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ошибка при сохранении данных!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedRow() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "Выберите строку для удаления!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean isEmpty = model.getValueAt(row, 1).toString().trim().isEmpty() &&
                model.getValueAt(row, 2).toString().trim().isEmpty();

        if (isEmpty) {
            model.removeRow(row);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Удалить группу?",
                "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            groupRepository.delete((int) model.getValueAt(row, 0));
            updateTable();
        }
    }

    private void updateTable() {
        model.setRowCount(0);
        for (Group group : groupRepository.list()) {
            model.addRow(new Object[]{group.getId(), group.getGroupName(), group.getCuratorId()});
        }
    }

    private int getNextId() {
        return groupRepository.list().stream().mapToInt(Group::getId).max().orElse(0) + 1;
    }
}
