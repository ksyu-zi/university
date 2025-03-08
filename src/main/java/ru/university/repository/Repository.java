package ru.university.repository;

import java.util.List;

public interface Repository<T> {

    void loadAll();

    void saveAll();

    void add(T entity);

    void edit(T entity);

    void delete(long id);

    void deleteAll();

    T get(long id);

    List<T> list();

    long getCurId();
}
