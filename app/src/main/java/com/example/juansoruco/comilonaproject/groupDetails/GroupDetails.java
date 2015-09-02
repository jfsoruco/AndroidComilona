package com.example.juansoruco.comilonaproject.groupDetails;

import java.util.Date;

/**
 * Created by juan.soruco on 31/08/2015.
 */
public class GroupDetails {
    private int _id;
    private int groupId;
    private int employeeId;
    private String groupFullname;
    private String employeeFullname;
    private int responsibleOrder;
    private int currentResponsible;

    public GroupDetails() {
    }

    public GroupDetails(int _id, int groupId, int employeeId, String groupFullname, String employeeFullname, int responsibleOrder, int currentResponsible) {
        this._id = _id;
        this.groupId = groupId;
        this.employeeId = employeeId;
        this.groupFullname = groupFullname;
        this.employeeFullname = employeeFullname;
        this.responsibleOrder = responsibleOrder;
        this.currentResponsible = currentResponsible;
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getGroupFullname() {
        return groupFullname;
    }

    public void setGroupFullname(String groupFullname) {
        this.groupFullname = groupFullname;
    }

    public String getEmployeeFullname() {
        return employeeFullname;
    }

    public void setEmployeeFullname(String employeeFullname) {
        this.employeeFullname = employeeFullname;
    }

    public int getResponsibleOrder() {
        return responsibleOrder;
    }

    public void setResponsibleOrder(int responsibleOrder) {
        this.responsibleOrder = responsibleOrder;
    }

    public int getCurrentResponsible() {
        return currentResponsible;
    }

    public void setCurrentResponsible(int currentResponsible) {
        this.currentResponsible = currentResponsible;
    }
}
