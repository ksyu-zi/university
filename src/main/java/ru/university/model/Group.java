package ru.university.model;

public class Group {

    private int id;
    private String groupName;
    private int curatorId;

    public Group() {
    }

    public Group(int id, String groupName, int curatorId) {
        this.id = id;
        this.groupName = groupName;
        this.curatorId = curatorId;
    }

    public int getId() {
        return id;
    }

    public String getGroupName() {
        return groupName;
    }

    public int getCuratorId() {
        return curatorId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public void setCuratorId(int curatorId) {
        this.curatorId = curatorId;
    }
}
