package com.example.juansoruco.comilonaproject.weeklyOrder;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.juansoruco.comilonaproject.R;
import com.example.juansoruco.comilonaproject.data.GroupMemberColumns;
import com.example.juansoruco.comilonaproject.data.WeeklyOrderColumns;
import com.example.juansoruco.comilonaproject.employee.Employee;
import com.example.juansoruco.comilonaproject.groupDetails.GroupDetails;
import com.example.juansoruco.comilonaproject.menu.MenuDia;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

/**
 * Created by juan.soruco on 07/09/2015.
 */
public class WeeklyOrderLogic {
    private final static int daysInterval = 7;
    private final static int futurePeriods = 5;

    // estados
    public final static String cInitial = "INITIAL";
    public final static String cActive = "ACTIVE";
    public final static String cMenuCompleted = "POLL_OPENED";
    public final static String cPollCompleted = "POLL_COMPLETED";
    public final static String cClosed = "CLOSED";

    private WeeklyOrderColumns dbAdapter = null;

    // Recuperar la orden activa para el grupo
    public static WeeklyOrder getActiveWeeklyOrder(int groupId, Context context) {
        WeeklyOrder weeklyOrderActive = null;
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        try {
            weeklyOrderActive = dbAdapter.getActiveRecord(groupId);

            if (weeklyOrderActive == null) {
                scheduleOrders(new Date(), groupId, context);
                weeklyOrderActive = dbAdapter.getActiveRecord(groupId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weeklyOrderActive;
    }

    // Programar las ordenes semanales
    public static void scheduleOrders(Date date, int groupId, Context context) {
        System.out.println(">>>>>>>>>>>> schedule orders " + date + ":" + groupId);
        ArrayList<Date> datesToCheck = new ArrayList<>();
        Date friday = toFriday(date);
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        datesToCheck.add(friday);

        Calendar cal = new GregorianCalendar();
        cal.setTime(friday);
        int groupMemberId = -1;

        try {
            WeeklyOrder w_o_start = dbAdapter.getRecord(groupId, friday);
            if (w_o_start != null) {
                groupMemberId = w_o_start.getGroupMemberId();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i = 1; i < futurePeriods; i++) {

            cal.add(Calendar.DAY_OF_MONTH, daysInterval);
            datesToCheck.add(cal.getTime());

        }


        GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
        ArrayList<GroupDetails> groupResponsibleList = null;
        try {
            groupResponsibleList = groupMemberAdapter.getList(groupId, groupMemberId);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Iterator<Date> it = datesToCheck.iterator();
        int i = 0;
        while (it.hasNext()) {
            Date checkDate = it.next();
            String status;
            try {

                WeeklyOrder weeklyOrder = dbAdapter.getRecord(groupId, checkDate);
                System.out.println(">>>>>> validado " + ((weeklyOrder != null) ? weeklyOrder.toString() : null));
                if (weeklyOrder == null) {
                    if (i == 0) {
                        status = WeeklyOrderLogic.cActive;
                    } else {
                        status = WeeklyOrderLogic.cInitial;
                    }
                    GroupDetails groupResponsible = groupResponsibleList.get(i++);
                    weeklyOrder = new WeeklyOrder(-1, checkDate, groupResponsible.get_id(),groupResponsible.getGroupFullname(),
                            groupResponsible.getEmployeeFullname(), status);
                    saveOrder(weeklyOrder, context);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static WeeklyOrder getWeeklyOrder(int w_o_id, Context context) {
        System.out.println(">>>> getWeeklyOrder" + w_o_id);
        WeeklyOrder result = null;
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        try {
            result = dbAdapter.getRecord(w_o_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static WeeklyOrder getNextWeeklyOrder(int w_o_id, Context context) {
        System.out.println(">>>>> getNextWeeklyOrder >> " + w_o_id);
        WeeklyOrder weeklyOrder = null;
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        ArrayList<WeeklyOrder> w_o_list = null;
        try {
            w_o_list = dbAdapter.getList(w_o_id);

            for (WeeklyOrder i: w_o_list) {
                System.out.println("getNext: " + i.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (w_o_list != null) {
            weeklyOrder = w_o_list.get(0);
        }

        return weeklyOrder;
    }

    // Guardar menu de orden semanal (Insert or Update)
    public static void saveOrder(WeeklyOrder weeklyOrder, Context context) {
        System.out.println(">>>> saveOrder >> " + weeklyOrder.toString());
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        try {
            long result = dbAdapter.update(weeklyOrder);
            if (result == 0) {
                dbAdapter.insert(weeklyOrder);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    // Completar menu de orden semanal
    public static void completeMenu(WeeklyOrder weeklyOrder, Context context) {
        System.out.println("<>>>> completeMenu >>> > >" + weeklyOrder.toString());
        if (weeklyOrder.getMenu1() == null || "".equals(weeklyOrder.getMenu1()) ||
                weeklyOrder.getMenu2() == null || "".equals(weeklyOrder.getMenu2()) ||
                weeklyOrder.getMenu3() == null || "".equals(weeklyOrder.getMenu3()) ||
                weeklyOrder.getMenu4() == null || "".equals(weeklyOrder.getMenu4())) {
            Log.e("Incomplete Data", "No ha llenado todos los menus");
            Toast.makeText(context,"No ha llenado todos los menus",Toast.LENGTH_SHORT).show();
        } else {
            weeklyOrder.setMenuCount1(0);
            weeklyOrder.setMenuCount2(0);
            weeklyOrder.setMenuCount3(0);
            weeklyOrder.setMenuCount4(0);
            weeklyOrder.setStatus(cMenuCompleted);
            System.out.println(">>>antes de enviar  " + weeklyOrder.toString());
            saveOrder(weeklyOrder, context);
        }
    }

    // Completar votacion de orden semanal
    public static void completePoll(WeeklyOrder weeklyOrder, Context context) {
        System.out.println("<>>>> completePoll >>> > >" + weeklyOrder.toString());
        String menuSelected = weeklyOrder.getMenu1();
        int maxVotes = weeklyOrder.getMenuCount1();

        if (maxVotes < weeklyOrder.getMenuCount2()) {
            menuSelected = weeklyOrder.getMenu2();
            maxVotes = weeklyOrder.getMenuCount2();
        }
        if (maxVotes < weeklyOrder.getMenuCount3()) {
            menuSelected = weeklyOrder.getMenu3();
            maxVotes = weeklyOrder.getMenuCount3();
        }
        if (maxVotes < weeklyOrder.getMenuCount4()) {
            menuSelected = weeklyOrder.getMenu4();
            maxVotes = weeklyOrder.getMenuCount4();
        }

        weeklyOrder.setMenuSelected(menuSelected);
        weeklyOrder.setStatus(cPollCompleted);

        saveOrder(weeklyOrder, context);

    }

    // Cerrar orden semanal y habilitar siguiente
    public static void closeOrder(WeeklyOrder weeklyOrder, Context context) {
        System.out.println("<>>>> closeOrder >>> > >" + weeklyOrder.toString());
        weeklyOrder.setStatus(cClosed);
        saveOrder(weeklyOrder, context);

        WeeklyOrder nextWeeklyOrder = getNextWeeklyOrder(weeklyOrder.get_id(), context);
        saveOrder(nextWeeklyOrder, context);

    }

    // Cambiar responsable
    public static void resignResponsability(WeeklyOrder weeklyOrder, Context context) {
        System.out.println("<>>>> resignResponsaability >>> > >" + weeklyOrder.toString());
        WeeklyOrderColumns dbAdapter = new WeeklyOrderColumns(context);
        try {
            ArrayList<Integer> responsibleList = new ArrayList<>();
            ArrayList<WeeklyOrder> nextWeeklyOrders = dbAdapter.getList(weeklyOrder.get_id());
            WeeklyOrder lastWeeklyOrder = null;
            int i = 0;
            for(WeeklyOrder element : nextWeeklyOrders) {
                if (i == 0) {
                    lastWeeklyOrder = element;
                } else {
                    responsibleList.add(element.getGroupMemberId());
                }
            }
            if (lastWeeklyOrder != null) {
                responsibleList.add(lastWeeklyOrder.getGroupMemberId());
            }

            Iterator<WeeklyOrder> itw = nextWeeklyOrders.iterator();
            Iterator<Integer> itr = responsibleList.iterator();
            while (itw.hasNext()) {
                WeeklyOrder _wo = itw.next();
                _wo.setGroupMemberId(itr.next());

                saveOrder(_wo, context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Date toFriday(Date _date) {
        Calendar cal = new GregorianCalendar();
        cal.setTime(_date);

        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        cal.getFirstDayOfWeek();
        cal.add(Calendar.DAY_OF_MONTH, 4);

        return cal.getTime();
    }

    public static ArrayList<MenuDia> getMenusList(WeeklyOrder weeklyOrder) {
        ArrayList<MenuDia> list = new ArrayList<>();
        list.add(new MenuDia(1, weeklyOrder.getMenu1(), weeklyOrder.getMenuCount1()));
        list.add(new MenuDia(2, weeklyOrder.getMenu2(), weeklyOrder.getMenuCount2()));
        list.add(new MenuDia(3, weeklyOrder.getMenu3(), weeklyOrder.getMenuCount3()));
        list.add(new MenuDia(4, weeklyOrder.getMenu4(), weeklyOrder.getMenuCount4()));

        return list;
    }
}
