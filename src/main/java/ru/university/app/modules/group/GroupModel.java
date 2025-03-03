package ru.university.app.modules.group;

import ru.university.app.modules.group.executors.AddExecutor;
import ru.university.app.modules.group.executors.DeleteExecutor;
import ru.university.app.modules.group.executors.EditExecutor;
import ru.university.app.modules.group.executors.ListExecutor;
import ru.university.exception.UnexpectedParameterException;
import ru.university.repository.GroupRepository;

import java.util.Arrays;

public class GroupModel {

    private final GroupRepository groupRepository;
    private final AddExecutor addExecutor;
    private final ListExecutor listExecutor;
    private final DeleteExecutor deleteExecutor;
    private final EditExecutor editExecutor;

    public GroupModel() {
        groupRepository = new GroupRepository();
        addExecutor = new AddExecutor();
        listExecutor = new ListExecutor();
        editExecutor = new EditExecutor();
        deleteExecutor = new DeleteExecutor();
    }

    public void executeCommand(String[] commandArgs) {
        switch (commandArgs[0]) {
            case "help" -> showHelp();
            case "add" -> addExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    groupRepository);
            case "list" -> listExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    groupRepository);
            case "delete" -> deleteExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    groupRepository);
            case "edit" -> editExecutor.execute(
                    Arrays.copyOfRange(commandArgs, 1, commandArgs.length),
                    groupRepository);
            default -> throw new UnexpectedParameterException(commandArgs[0]);
        }
    }

    private void showHelp() {
        System.out.println("""
                Group module commands:
                help
                list - show groups
                add - add new group
                edit - edit existing group
                delete - delete group or all groups
                """);
    }

    public void close() {
        groupRepository.saveAll();
    }
}
