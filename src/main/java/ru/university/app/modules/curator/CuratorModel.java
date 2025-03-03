package ru.university.app.modules.curator;

import ru.university.app.modules.curator.executors.AddExecutor;
import ru.university.app.modules.curator.executors.EditExecutor;
import ru.university.app.modules.curator.executors.ListExecutor;
import ru.university.app.modules.curator.executors.DeleteExecutor;
import ru.university.exception.UnexpectedParameterException;
import ru.university.repository.CuratorRepository;

import java.util.Arrays;

public class CuratorModel {
    private final CuratorRepository curatorRepository;
    private final AddExecutor addExecutor;
    private final ListExecutor listExecutor;
    private final DeleteExecutor deleteExecutor;
    private final EditExecutor editExecutor;

    public CuratorModel() {
        curatorRepository = new CuratorRepository();
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
                    curatorRepository);
            case "list" -> listExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    curatorRepository);
            case "delete" -> deleteExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    curatorRepository);
            case "edit" -> editExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    curatorRepository);
            default -> throw new UnexpectedParameterException(commandArgs[0]);
        }
    }

    void showHelp() {
        System.out.println("""
                Curator module commands:
                help
                list - show curators
                add - add new curator
                edit - edit existing curator
                delete - delete curator or all curators
                """);
    }

    public void close() {
        curatorRepository.saveAll();
    }

}
