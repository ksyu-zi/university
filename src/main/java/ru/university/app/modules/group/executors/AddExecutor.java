package ru.university.app.modules.group.executors;

import ru.university.model.Group;
import ru.university.repository.Repository;

public class AddExecutor implements Executor<Group> {

    @Override
    public void execute(String[] args, Repository<Group> repository) {
        Group group = new Group();
        int i = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> group.setId(Integer.parseInt(args[i + 1]));
                case "-n" -> group.setGroupName(args[i + 1]);
                case "-c" -> group.setCuratorId(Integer.parseInt(args[i + 1]));
            }
            i += 2;
        }
        repository.add(group);
        if (!args[i - 2].equals("help")) {
            System.out.println("Группа добавлена");
        }
    }

    public void showHelp() {
        System.out.println("""
                Group add command parameters:
                -i - ID
                -n - name group
                -c - curator ID
                """);
    }
}
