package ru.university.gui;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import ru.university.model.Gender;
import ru.university.model.Student;
import ru.university.repository.StudentRepository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class DiagramWindow extends JInternalFrame {
    public DiagramWindow() {
        setTitle("Диаграмма");
        setSize(300, 250);
        setClosable(true);
        setIconifiable(true);
        setResizable(true);
        setDefaultCloseOperation(HIDE_ON_CLOSE);

        DefaultPieDataset<String> dataset = getPieDataset();

        JFreeChart chart = ChartFactory.createPieChart(
                "Соотношение полов",
                dataset,
                true,
                true,
                false
        );

        PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint("Девушки", Color.PINK);
        plot.setSectionPaint("Парни", Color.BLUE);

        plot.setLabelGenerator(new org.jfree.chart.labels.StandardPieSectionLabelGenerator("{0}: {2}"));

        ChartPanel chartPanel = new ChartPanel(chart);
        add(chartPanel, BorderLayout.CENTER);
    }

    private static DefaultPieDataset<String> getPieDataset() {
        StudentRepository studentRepository = new StudentRepository();
        List<Student> students = studentRepository.list();
        int femaleCount = 0;
        int maleCount = 0;

        for (Student student : students) {
            if (Gender.FEMALE.equals(student.getGender())) {
                femaleCount++;
            } else if (Gender.MALE.equals(student.getGender())) {
                maleCount++;
            }
        }

        DefaultPieDataset<String> dataset = new DefaultPieDataset<>();
        dataset.setValue("Девушки", femaleCount);
        dataset.setValue("Парни", maleCount);
        return dataset;
    }
}
