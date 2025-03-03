package ru.university.app.modules.group.executors;

import ru.university.model.Group;
import ru.university.repository.Repository;

public class EditExecutor implements Executor<Group> {

    @Override
    public void execute(String[] args, Repository<Group> repository) {
        long id = Long.parseLong(args[1]);
        Group group = repository.get(id);
        int i = 2;
        while (i < args.length) {
            switch (args[i]) {
                case "help" -> showHelp();
                case "-n" -> group.setGroupName(args[i + 1]);
                case "-c" -> group.setCuratorId(Integer.parseInt(args[i + 1]));
            }
            i += 2;
            if (!args[i - 2].equals("help")) {
                System.out.println("Данные исправлены");
            }
        }
    }

    public void showHelp() {
        System.out.println("""
                Group edit command parameters:
                -i - ID (required first parameter)
                -n - edit group name
                -c - edit curator ID
                """);
    }
}
