package ru.university.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.university.model.Curator;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CuratorRepository implements Repository<Curator> {
    private final List<Curator> curators;
    private final ObjectMapper mapper;

    public CuratorRepository() {
        this.curators = new ArrayList<>();
        this.mapper = new ObjectMapper();
        loadAll();
    }

    @Override
    public void loadAll() {
        Path path = Path.of(System.getProperty("user.home")).resolve("curator.json");
        try {
            if (Files.notExists(path)) {
                Files.createFile(path);
            }
            curators.addAll(mapper.readValue(
                    Files.newBufferedReader(path),
                    new TypeReference<List<Curator>>() {
                    }));
        } catch (JsonMappingException ignored) {

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveAll() {
        Path path = Path.of(System.getProperty("user.home")).resolve("curator.json");
        try (OutputStream os = Files.newOutputStream(path, StandardOpenOption.CREATE)) {
            mapper.writeValue(os, curators);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(Curator entity) {
        curators.add(entity);
        saveAll();
    }

    @Override
    public void edit(Curator entity) {
        Curator currentCurator = curators.stream().filter(c -> c.getId() == entity.getId()).findFirst().orElseThrow();
        currentCurator.setSurname(entity.getSurname());
        currentCurator.setName(entity.getName());
        saveAll();
    }

    @Override
    public void delete(long id) {
        curators.stream().filter(c -> c.getId() == id)
                .findFirst().map(curators::remove);
        System.out.println("Куратор удален");
        saveAll();
    }

    @Override
    public void deleteAll() {
        curators.removeIf(c -> true);
        System.out.println("Все кураторы удалены");
        saveAll();
    }

    @Override
    public Curator get(long id) {
        return curators.stream().filter(c -> c.getId() == id).findFirst().orElseThrow();
    }

    @Override
    public List<Curator> list() {
        return curators;
    }

    @Override
    public long getCurId() {
        return list().get(list().size() - 1).getId();
    }
}
