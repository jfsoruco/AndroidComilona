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
        this._id = _id;
        this.date = date;
        this.groupMemberId = groupMemberId;
        this.groupFullname = groupFullname;
        this.responsibleFullname = responsibleFullname;
        this.status = status;
    }
}
