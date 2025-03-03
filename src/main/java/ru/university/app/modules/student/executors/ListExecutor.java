package ru.university.app.modules.student.executors;

import ru.university.exception.UnexpectedParameterException;
import ru.university.model.Student;
import ru.university.repository.Repository;

import java.util.Comparator;
import java.util.stream.Stream;

public class ListExecutor implements Executor<Student> {
    @Override
    public void execute(String[] args, Repository<Student> repository) {
        int i = 0;
        Stream<Student> allStudents = repository.list().stream();
        String printOrder = "isngG";
        while (i < args.length) {
            int finalI = i;
            switch (args[i]) {
                case "help" -> showHelp();
                case "-i" -> allStudents = allStudents.filter(s -> s.getId() == Integer.parseInt(args[finalI + 1]));
                case "-s" -> allStudents = allStudents.filter(s -> s.getSurname().equals(args[finalI + 1]));
                case "-n" -> allStudents = allStudents.filter(s -> s.getName().equals(args[finalI + 1]));
                case "-g" -> allStudents = allStudents.filter(s -> s.getGender().name().equals(args[finalI + 1]));
                case "-G" -> allStudents = allStudents.filter(s -> s.getGroupId() == Integer.parseInt(args[finalI + 1]));
                case "-sort" -> allStudents = sortStudents(allStudents, args[i + 1]);
                case "-p" -> printOrder = args[i + 1];
            }
            i += 2;
        }
        String finalPrintOrder = printOrder;
        allStudents.forEach(g -> {
            for (int j = 0; j < finalPrintOrder.length(); ++j) {
                switch (finalPrintOrder.charAt(j)) {
                    case 'i' -> System.out.print(g.getId() + " ");
                    case 's' -> System.out.print(g.getSurname() + " ");
                    case 'n' -> System.out.print(g.getName() + " ");
                    case 'g' -> System.out.print(g.getGender() + " ");
                    case 'G' -> System.out.print(g.getGroupId() + " ");
                }
            }
            System.out.println();
        });
        allStudents.close();
    }

    public void showHelp() {
        System.out.println("""
                Student list command parameters:
                -i - filter by id
                -s - filter by surname
                -n - filter by name
                -g - filter by gender
                -G - filter by group ID
                -sort - sorting by (id, surname, name, groupId)
                -p - print order (e.g. isngG)
                """);
    }

    private Stream<Student> sortStudents(Stream<Student> students, String field) {
        return switch (field) {
            case "id" -> students.sorted(Comparator.comparingInt(Student::getId));
            case "surname" -> students.sorted(Comparator.comparing(Student::getSurname));
            case "name" -> students.sorted(Comparator.comparing(Student::getName));
            case "groupId" -> students.sorted(Comparator.comparingInt(Student::getGroupId));
            default -> throw new UnexpectedParameterException(field);
        };
    }
}
