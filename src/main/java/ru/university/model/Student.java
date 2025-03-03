package ru.university.model;

import ru.university.exception.UnexpectedParameterException;

public class Student {
    private int id;
    private String surname;
    private String name;
    private Gender gender;
    private int groupId;

    public Student(int id, String surname, String name, Gender gender, int groupId) {
        this.id = id;
        this.surname = surname;
        this.name = name;
        this.gender = gender;
        this.groupId = groupId;
    }

    public Student() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(String gender) {
        switch (gender) {
            case "MALE" -> this.gender = Gender.MALE;
            case "FEMALE" -> this.gender = Gender.FEMALE;
            default -> throw new UnexpectedParameterException(gender);
        }
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}
