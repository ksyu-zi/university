package ru.university.gui;

import ru.university.model.Gender;
import ru.university.model.Student;
import ru.university.repository.StudentRepository;

import javax.swing.*;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class StudentsTableWindow extends JInternalFrame {
    private final StudentRepository studentRepository;
    private final JTable table;
    private final DefaultTableModel model;
    private int lastSelectedRow = -1;

    public StudentsTableWindow() {
        setTitle("Студенты");
        setSize(500, 300);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        studentRepository = new StudentRepository();
        String[] columnNames = {"ID", "Фамилия", "Имя", "Пол", "ID Группы"};

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
                        StudentsTableWindow.this,
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
        model.addRow(new Object[]{getNextId(), "", "", Gender.MALE, ""});
    }

    private boolean rowValid(int row) {
        String surname = model.getValueAt(row, 1).toString().trim();
        String name = model.getValueAt(row, 2).toString().trim();
        String genderStr = model.getValueAt(row, 3).toString().trim();
        String groupIdStr = model.getValueAt(row, 4).toString().trim();

        try {
            Gender.valueOf(genderStr.toUpperCase());
            return surname.isEmpty() || name.isEmpty() || Integer.parseInt(groupIdStr) <= 0;
        } catch (Exception e) {
            return true;
        }
    }

    private void saveRowChanges(int row) {
        if (row < 0 || row >= model.getRowCount()) return;

        String surname = model.getValueAt(row, 1).toString().trim();
        String name = model.getValueAt(row, 2).toString().trim();
        String genderStr = model.getValueAt(row, 3).toString().trim();
        String groupIdStr = model.getValueAt(row, 4).toString().trim();

        if (surname.isEmpty() || name.isEmpty() || genderStr.isEmpty() || groupIdStr.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Заполните все поля!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Gender gender = Gender.valueOf(genderStr.toUpperCase());
            int groupId = Integer.parseInt(groupIdStr);
            if (groupId <= 0) throw new NumberFormatException();

            Student student = (row < studentRepository.list().size())
                    ? studentRepository.list().get(row)
                    : new Student();

            student.setId((int) model.getValueAt(row, 0));
            student.setSurname(surname);
            student.setName(name);
            student.setGender(String.valueOf(gender));
            student.setGroupId(groupId);

            if (row < studentRepository.list().size()) {
                studentRepository.edit(student);
            } else {
                studentRepository.add(student);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "ID группы должен быть положительным числом!",
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this,
                    "Пол должен быть 'MALE' или 'FEMALE'!",
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
                model.getValueAt(row, 2).toString().trim().isEmpty() &&
                model.getValueAt(row, 3).toString().trim().isEmpty() &&
                model.getValueAt(row, 4).toString().trim().isEmpty();

        if (isEmpty) {
            model.removeRow(row);
            return;
        }

        if (JOptionPane.showConfirmDialog(this, "Удалить студента?",
                "Подтверждение", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            studentRepository.delete((int) model.getValueAt(row, 0));
            updateTable();
        }
    }

    private void updateTable() {
        model.setRowCount(0);
        for (Student student : studentRepository.list()) {
            model.addRow(new Object[]{student.getId(), student.getSurname(), student.getName(), student.getGender(), student.getGroupId()});
        }
    }

    private int getNextId() {
        return studentRepository.list().stream().mapToInt(Student::getId).max().orElse(0) + 1;
    }
}
