package ru.university.app.modules.group.executors;

import ru.university.exception.UnexpectedParameterException;
import ru.university.model.Group;
import ru.university.repository.Repository;

import java.util.Comparator;
import java.util.stream.Stream;

public class ListExecutor implements Executor<Group> {
    @Override
    public void execute(String[] args, Repository<Group> repository) {
        int i = 0;
        Stream<Group> allGroups = repository.list().stream();
        String printOrder = "inc";
        while (i < args.length) {
            int finalI = i;
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> allGroups = allGroups.filter(g -> g.getId() == Integer.parseInt(args[finalI + 1]));
                case "-n" -> allGroups = allGroups.filter(g -> g.getGroupName().equals(args[finalI + 1]));
                case "-c" -> allGroups = allGroups.filter(g -> g.getCuratorId() == Integer.parseInt(args[finalI + 1]));
                case "-sort" -> allGroups = sortGroups(allGroups, args[i + 1]);
                case "-p" -> printOrder = args[i + 1];
            }
            i += 2;
        }
        String finalPrintOrder = printOrder;
        allGroups.forEach(g -> {
            for (int j = 0; j < finalPrintOrder.length(); ++j) {
                switch (finalPrintOrder.charAt(j)) {
                    case 'i' -> System.out.print(g.getId() + " ");
                    case 'n' -> System.out.print(g.getGroupName() + " ");
                    case 'c' -> System.out.print(g.getCuratorId() + " ");
                }
            }
            System.out.println();
        });
        allGroups.close();
    }

    public void showHelp() {
        System.out.println("""
                Group list command parameters:
                -i - filter by id
                -n - filter by group name
                -c - filter by curator id
                -sort - sorting by (id, name, curatorId)
                -p - print order (e.g. inc)
                """);
    }

    private Stream<Group> sortGroups(Stream<Group> groups, String field) {
        return switch (field) {
            case "id" -> groups.sorted(Comparator.comparingInt(Group::getId));
            case "name" -> groups.sorted(Comparator.comparing(Group::getGroupName));
            case "curatorId" -> groups.sorted(Comparator.comparingInt(Group::getCuratorId));
            default -> throw new UnexpectedParameterException(field);
        };
    }
}
