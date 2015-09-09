package com.example.juansoruco.comilonaproject.weeklyOrder;

import java.util.Date;

/**
 * Created by juan.soruco on 31/08/2015.
 */
public class WeeklyOrder {
    private int _id;
    private Date date;
    private int groupMemberId;
    private String groupFullname;
    private String responsibleFullname;
    private String status;
    private String menu1;
    private String menu2;
    private String menu3;
    private String menu4;
    private String menuSelected;
    private int menuCount1;
    private int menuCount2;
    private int menuCount3;
    private int menuCount4;

    public WeeklyOrder() {
    }

    public WeeklyOrder(int _id, Date date, int groupMemberId, String groupFullname, String responsibleFullname, String status) {
        this.set_id(_id);
        this.setDate(date);
        this.setGroupMemberId(groupMemberId);
        this.setGroupFullname(groupFullname);
        this.setResponsibleFullname(responsibleFullname);
        this.setStatus(status);
    }

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getGroupMemberId() {
        return groupMemberId;
    }

    public void setGroupMemberId(int groupMemberId) {
        this.groupMemberId = groupMemberId;
    }

    public String getGroupFullname() {
        return groupFullname;
    }

    public void setGroupFullname(String groupFullname) {
        this.groupFullname = groupFullname;
    }

    public String getResponsibleFullname() {
        return responsibleFullname;
    }

    public void setResponsibleFullname(String responsibleFullname) {
        this.responsibleFullname = responsibleFullname;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMenu1() {
        return menu1;
    }

    public void setMenu1(String menu1) {
        this.menu1 = menu1;
    }

    public String getMenu2() {
        return menu2;
    }

    public void setMenu2(String menu2) {
        this.menu2 = menu2;
    }

    public String getMenu3() {
        return menu3;
    }

    public void setMenu3(String menu3) {
        this.menu3 = menu3;
    }

    public String getMenu4() {
        return menu4;
    }

    public void setMenu4(String menu4) {
        this.menu4 = menu4;
    }

    public String getMenuSelected() {
        return menuSelected;
    }

    public void setMenuSelected(String menuSelected) {
        this.menuSelected = menuSelected;
    }

    public int getMenuCount1() {
        return menuCount1;
    }

    public void setMenuCount1(int menuCount1) {
        this.menuCount1 = menuCount1;
    }

    public int getMenuCount2() {
        return menuCount2;
    }

    public void setMenuCount2(int menuCount2) {
        this.menuCount2 = menuCount2;
    }

    public int getMenuCount3() {
        return menuCount3;
    }

    public void setMenuCount3(int menuCount3) {
        this.menuCount3 = menuCount3;
    }

    public int getMenuCount4() {
        return menuCount4;
    }

    public void setMenuCount4(int menuCount4) {
        this.menuCount4 = menuCount4;
    }

    @Override
    public String toString() {
        return "WeeklyOrder{" +
                "_id=" + _id +
                ", date=" + date +
                ", groupMemberId=" + groupMemberId +
                ", groupFullname='" + groupFullname + '\'' +
                ", responsibleFullname='" + responsibleFullname + '\'' +
                ", status='" + status + '\'' +
                ", menu1='" + menu1 + '\'' +
                ", menu2='" + menu2 + '\'' +
                ", menu3='" + menu3 + '\'' +
                ", menu4='" + menu4 + '\'' +
                ", menuSelected='" + menuSelected + '\'' +
                ", menuCount1=" + menuCount1 +
                ", menuCount2=" + menuCount2 +
                ", menuCount3=" + menuCount3 +
                ", menuCount4=" + menuCount4 +
                '}';
    }
}
