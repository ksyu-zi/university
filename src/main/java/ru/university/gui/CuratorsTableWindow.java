package ru.university.gui;

import ru.university.model.Curator;
import ru.university.repository.CuratorRepository;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class CuratorsTableWindow extends JInternalFrame {
    private final CuratorRepository curatorRepository;
    private final JTable table;
    private final DefaultTableModel model;
    private int lastSelectedRow = -1;

    public CuratorsTableWindow() {
        setTitle("Кураторы");
        setSize(400, 300);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        curatorRepository = new CuratorRepository();
        String[] columnNames = {"ID", "Фамилия", "Имя"};

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
                        CuratorsTableWindow.this,
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
        String surname = model.getValueAt(row, 1).toString().trim();
        String name = model.getValueAt(row, 2).toString().trim();
        return surname.isEmpty() || name.isEmpty();
    }

    private void saveRowChanges(int row) {
        if (row < 0 || row >= model.getRowCount()) return;

        String surname = model.getValueAt(row, 1).toString().trim();
        String name = model.getValueAt(row, 2).toString().trim();

        if (surname.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Заполните все поля!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Curator curator = (row < curatorRepository.list().size())
                ? curatorRepository.list().get(row)
                : new Curator();

        curator.setId((int) model.getValueAt(row, 0));
        curator.setSurname(surname);
        curator.setName(name);

        if (row < curatorRepository.list().size()) {
            curatorRepository.edit(curator);
        } else {
            curatorRepository.add(curator);
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

        if (JOptionPane.showConfirmDialog(this, "Удалить куратора?",
                "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            curatorRepository.delete((int) model.getValueAt(row, 0));
            updateTable();
        }
    }

    private void updateTable() {
        model.setRowCount(0);
        for (Curator curator : curatorRepository.list()) {
            model.addRow(new Object[]{curator.getId(), curator.getSurname(), curator.getName()});
        }
    }

    private int getNextId() {
        return curatorRepository.list().stream().mapToInt(Curator::getId).max().orElse(0) + 1;
    }
}