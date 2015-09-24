package com.example.juansoruco.comilonaproject.group;

/**
 * Created by juan.soruco on 31/08/2015.
 */
public class Group {
    private int _id;
    private String fullName;
    private String status;

    public Group() {
    }

    public Group(int _id, String fullName, String status) {
        this._id = _id;
        this.fullName = fullName;
        this.status = status;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Group{" +
                "_id=" + _id +
                ", fullName='" + fullName + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
