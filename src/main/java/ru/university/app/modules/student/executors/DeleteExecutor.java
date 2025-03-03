package ru.university.app.modules.student.executors;

import ru.university.model.Student;
import ru.university.repository.Repository;

public class DeleteExecutor implements Executor<Student>{
    @Override
    public void execute(String[] args, Repository<Student> repository) {
        int i = 0;
        while (i < args.length) {
            int finalI = i;
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> repository.delete(Long.parseLong(args[i + 1]));
                case "-a" -> repository.deleteAll();
                case "-g" -> repository.list().stream()
                        .filter(s -> s.getGroupId() == Long.parseLong(args[finalI + 1]))
                        .forEach(s -> repository.delete(s.getId()));
            }
            i += 2;
        }
    }

    public void showHelp() {
        System.out.println("""
                Student delete command parameters:
                -i - delete by ID
                -a - delete all
                -g - delete by group ID
                """);
    }
}
