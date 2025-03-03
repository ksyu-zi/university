package ru.university.app.modules.group.executors;

import ru.university.model.Group;
import ru.university.repository.Repository;

public class DeleteExecutor implements Executor<Group> {
    @Override
    public void execute(String[] args, Repository<Group> repository) {
        int i = 0;
        while (i < args.length) {
            int finalI = i;
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> repository.delete(Long.parseLong(args[i + 1]));
                case "-a" -> repository.deleteAll();
                case "-c" -> repository.list().stream()
                        .filter(g -> g.getCuratorId() == Long.parseLong(args[finalI + 1]))
                        .forEach(g -> repository.delete(g.getId()));
            }
            i += 2;
        }
    }

    public void showHelp() {
        System.out.println("""
                Group delete command parameters:
                -i - delete by ID
                -a - delete all
                -c - delete by curator ID
                """);
    }
}
