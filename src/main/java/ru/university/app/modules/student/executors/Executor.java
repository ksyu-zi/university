package ru.university.app.modules.student.executors;

import ru.university.repository.Repository;

public interface Executor<T> {
    void execute(String args[], Repository<T> repository);
}
