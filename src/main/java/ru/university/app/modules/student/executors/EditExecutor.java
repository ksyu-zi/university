package ru.university.app.modules.student.executors;

import ru.university.model.Student;
import ru.university.repository.Repository;

public class EditExecutor implements Executor<Student> {
    @Override
    public void execute(String[] args, Repository<Student> repository) {
        long id = Long.parseLong(args[1]);
        Student student = repository.get(id);
        int i = 2;
        while (i < args.length) {
            switch (args[i]) {
                case "help" -> showHelp();
                case "-s" -> student.setSurname(args[i + 1]);
                case "-n" -> student.setName(args[i + 1]);
                case "-g" -> student.setGender(args[i + 1]);
                case "-gi" -> student.setGroupId(Integer.parseInt(args[i + 1]));
            }
            i += 2;
            if (!args[i - 2].equals("help")) {
                System.out.println("Данные исправлены");
            }
        }
    }

    public void showHelp() {
        System.out.println("""
                Student edit command parameters:
                -i - ID (required first parameter)
                -s - edit surname
                -n - edit name
                -g - edit gender
                -gi - edit group ID
                """);
    }
}
