package ru.university.app;

import ru.university.app.modules.curator.CuratorModel;
import ru.university.app.modules.group.GroupModel;
import ru.university.app.modules.student.StudentModel;
import ru.university.exception.UnexpectedParameterException;

import java.util.Arrays;

public class UniversityApplication {

    private final CuratorModel curatorModel;
    private final GroupModel groupModel;
    private final StudentModel studentModel;

    public UniversityApplication() {

        // parse args

        curatorModel = new CuratorModel();
        groupModel = new GroupModel();
        studentModel = new StudentModel();


    }

    public void run(String[] args) {
        parseArgs(args);
        close();
    }

    private void close() {
        groupModel.close();
        curatorModel.close();
        studentModel.close();
    }

    private void parseArgs(String[] args) {
        switch (args[0]) {
            case "help" -> showHelp();
            case "group" -> groupModel.executeCommand(Arrays.copyOfRange(args, 1, args.length));
            case "curator" -> curatorModel.executeCommand(Arrays.copyOfRange(args, 1, args.length));
            case "student" -> studentModel.executeCommand(Arrays.copyOfRange(args, 1, args.length));
            default -> throw new UnexpectedParameterException(args[0]);
        }
    }

    private void showHelp() {
        System.out.println("""
                Сommands:
                help
                group
                curator
                student
                """);
    }
}
