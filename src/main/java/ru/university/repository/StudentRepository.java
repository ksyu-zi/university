package ru.university.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.university.model.Curator;
import ru.university.model.Group;
import ru.university.model.Student;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StudentRepository implements Repository<Student> {
    private final List<Student> students;
    private final ObjectMapper mapper;

    public StudentRepository() {
        this.students = new ArrayList<>();
        this.mapper = new ObjectMapper();
        loadAll();
    }

    @Override
    public void loadAll() {
        Path path = Path.of(System.getProperty("user.home")).resolve("student.json");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            students.addAll(mapper.readValue(
                    Files.newBufferedReader(path),
                    new TypeReference<List<Student>>(){}));
        } catch (JsonMappingException ignored) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll() {
        Path path = Path.of(System.getProperty("user.home")).resolve("student.json");
        try (OutputStream os =  Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            mapper.writeValue(os, students);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Student entity) {
        students.add(entity);
        saveAll();
    }

    @Override
    public void edit(Student entity) {
        Student currentStudent = students.stream().filter(c -> c.getId() == entity.getId()).findFirst().orElseThrow();
        currentStudent.setSurname(entity.getSurname());
        currentStudent.setName(entity.getName());
        currentStudent.setGender(String.valueOf(entity.getGender()));
        currentStudent.setGroupId(entity.getGroupId());
        saveAll();
    }

    @Override
    public void delete(long id) {
        students.stream().filter(s -> s.getId() == id)
                .findFirst().map(students::remove);
        System.out.println("Студент удален");
        saveAll();
    }

    @Override
    public void deleteAll() {
        students.removeIf(g -> true);
        System.out.println("Все студенты удалены");
        saveAll();
    }

    @Override
    public Student get(long id) {
        return students.stream().filter(s -> s.getId() == id).findFirst().orElseThrow();
    }

    @Override
    public List<Student> list() {
        return students;
    }

    @Override
    public long getCurId() {
        return list().get(list().size() - 1).getId();
    }
}
