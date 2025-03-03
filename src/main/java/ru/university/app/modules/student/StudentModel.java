package ru.university.app.modules.student;

import ru.university.app.modules.student.executors.DeleteExecutor;
import ru.university.app.modules.student.executors.EditExecutor;
import ru.university.app.modules.student.executors.AddExecutor;
import ru.university.app.modules.student.executors.ListExecutor;
import ru.university.exception.UnexpectedParameterException;
import ru.university.repository.StudentRepository;

import java.util.Arrays;

public class StudentModel {
    private final StudentRepository studentRepository;
    private final AddExecutor addExecutor;
    private final ListExecutor listExecutor;
    private final DeleteExecutor deleteExecutor;
    private final EditExecutor editExecutor;


    public StudentModel() {
        studentRepository = new StudentRepository();
        addExecutor = new AddExecutor();
        listExecutor = new ListExecutor();
        deleteExecutor = new DeleteExecutor();
        editExecutor = new EditExecutor();
    }

    public void executeCommand(String[] commandArgs) {
        switch (commandArgs[0]) {
            case "help" -> showHelp();
            case "add" -> addExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    studentRepository);
            case "list" -> listExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    studentRepository);
            case "delete" -> deleteExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    studentRepository);
            case "edit" -> editExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    studentRepository);
            default -> throw new UnexpectedParameterException(commandArgs[0]);
        }
    }

    void showHelp() {
        System.out.println("""
                Student module commands:
                help
                list - show students
                add - add new student
                edit - edit existing student
                delete - delete student or all students
                """);
    }

    public void close() {
        studentRepository.saveAll();
    }
}
