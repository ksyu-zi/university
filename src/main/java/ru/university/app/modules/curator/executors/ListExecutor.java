package ru.university.app.modules.curator.executors;

import ru.university.exception.UnexpectedParameterException;
import ru.university.model.Curator;
import ru.university.repository.Repository;

import java.util.Comparator;
import java.util.stream.Stream;

public class ListExecutor implements Executor<Curator> {
    @Override
    public void execute(String[] args, Repository<Curator> repository) {
        int i = 0;
        Stream<Curator> allCurators = repository.list().stream();
        String printOrder = "isn";
        while (i < args.length) {
            int finalI = i;
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> allCurators = allCurators.filter(s -> s.getId() == Integer.parseInt(args[finalI + 1]));
                case "-s" -> allCurators = allCurators.filter(s -> s.getSurname().equals(args[finalI + 1]));
                case "-n" -> allCurators = allCurators.filter(s -> s.getName().equals(args[finalI + 1]));
                case "-sort" -> allCurators = sortStudents(allCurators, args[i + 1]);
                case "-p" -> printOrder = args[i + 1];
            }
            i += 2;
        }
        String finalPrintOrder = printOrder;
        allCurators.forEach(g -> {
            for (int j = 0; j < finalPrintOrder.length(); ++j) {
                switch (finalPrintOrder.charAt(j)) {
                    case 'i' -> System.out.print(g.getId() + " ");
                    case 's' -> System.out.print(g.getSurname() + " ");
                    case 'n' -> System.out.print(g.getName() + " ");
                }
            }
            System.out.println();
        });
        allCurators.close();
    }

    public void showHelp() {
        System.out.println("""
                Curator list command parameters:
                -i - filter by ID
                -s - filter by surname
                -n - filter by name
                -sort - sorting by (id, surname, name)
                -p - print order (e.g. isn)
                """);
    }

    private Stream<Curator> sortStudents(Stream<Curator> curators, String field) {
        return switch (field) {
            case "id" -> curators.sorted(Comparator.comparingInt(Curator::getId));
            case "surname" -> curators.sorted(Comparator.comparing(Curator::getSurname));
            case "name" -> curators.sorted(Comparator.comparing(Curator::getName));
            default -> throw new UnexpectedParameterException(field);
        };
    }
}
