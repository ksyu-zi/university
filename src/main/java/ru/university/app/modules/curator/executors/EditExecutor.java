package ru.university.app.modules.curator.executors;

import ru.university.model.Curator;
import ru.university.repository.Repository;

public class EditExecutor implements Executor<Curator> {
    @Override
    public void execute(String[] args, Repository<Curator> repository) {
        long id = Long.parseLong(args[1]);
        Curator curator = repository.get(id);
        int i = 2;
        while (i < args.length) {
            switch (args[i]) {
                case "help" -> showHelp();
                case "-s" -> curator.setSurname(args[i + 1]);
                case "-n" -> curator.setName(args[i + 1]);
            }
            i += 2;
            if (!args[i - 2].equals("help")) {
                System.out.println("Данные исправлены");
            }
        }
    }

    public void showHelp() {
        System.out.println("""
                Curator edit command parameters:
                -i - ID (required first parameter)
                -s - edit surname
                -n - edit name
                """);
    }
}
