package ru.university.app.modules.curator.executors;

import ru.university.model.Curator;
import ru.university.repository.Repository;

public class DeleteExecutor implements Executor<Curator>{
    @Override
    public void execute(String[] args, Repository<Curator> repository) {
        int i = 0;
        while (i < args.length) {
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> repository.delete(Long.parseLong(args[i + 1]));
                case "-a" -> repository.deleteAll();
            }
            i += 2;
        }
    }

    public void showHelp() {
        System.out.println("""
                Curator delete command parameters:
                -i - delete by ID
                -a - delete all
                """);
    }
}
