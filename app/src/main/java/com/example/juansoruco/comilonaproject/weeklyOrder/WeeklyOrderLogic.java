package com.example.juansoruco.comilonaproject.weeklyOrder;

import android.util.Log;

import com.example.juansoruco.comilonaproject.R;
import com.example.juansoruco.comilonaproject.data.WeeklyOrderColumns;

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
    public final static String cMenuCompleted = "MENU_COMPLETED";
    public final static String cPollCompleted = "POLL_COMPLETED";
    public final static String cClosed = "CLOSED";

    // Recuperar la orden activa para el grupo
    public static WeeklyOrder getActiveWeeklyOrder(int groupId) {
        // TODO recuperar de BD la orden activa del grupo
        WeeklyOrder weeklyOrderActive = new WeeklyOrder(1, toFriday(new Date()), 1, "Mi Grupo de Comilona", "Yeso", cActive);

        if (weeklyOrderActive == null) {
            scheduleOrders(new Date(), groupId);
            // TODO recuperar de BD la orden activa del grupo
            weeklyOrderActive = new WeeklyOrder(1, toFriday(new Date()), 1, "Mi Grupo de Comilona", "Yeso", cActive);
        }

        return weeklyOrderActive;
    }

    // Programar las ordenes semanales
    public static void scheduleOrders(Date date, int groupId) {
        ArrayList<Date> datesToCheck = new ArrayList<>();
        Date friday = toFriday(date);
        datesToCheck.add(friday);

        Calendar cal = new GregorianCalendar();
        cal.setTime(friday);

        for(int i = 1; i < futurePeriods; i++) {

            cal.add(Calendar.DAY_OF_MONTH, daysInterval);
            datesToCheck.add(cal.getTime());

        }

        ArrayList<Integer> groupResponsibleIdList = new ArrayList<>();
        // TODO retrieve responsibles from db
        for (int i = 0; i < futurePeriods; i++) {
            groupResponsibleIdList.add(i);
        }

        Iterator<Date> it = datesToCheck.iterator();
        int i = 0;
        while (it.hasNext()) {
            Date checkDate = it.next();

            // TODO verify in database

            // TODO insert into database
            WeeklyOrder weeklyOrder = new WeeklyOrder(1, checkDate, groupResponsibleIdList.get(i++),null, null, cInitial);

        }

    }

    // Guardar menu de orden semanal (Insert or Update)
    public static void saveOrder(WeeklyOrder weeklyOrder) {
        // TODO actualizar o insertar en BD
        System.out.println(">>>> saveOrder >> " + weeklyOrder.toString());

    }

    // Completar menu de orden semanal
    public static void completeMenu(WeeklyOrder weeklyOrder) {
        System.out.println("<>>>> completeMenu >>> > >" + weeklyOrder.toString());
        if (weeklyOrder.getMenu1() == null || "".equals(weeklyOrder.getMenu1()) ||
                weeklyOrder.getMenu2() == null || "".equals(weeklyOrder.getMenu2()) ||
                weeklyOrder.getMenu3() == null || "".equals(weeklyOrder.getMenu3()) ||
                weeklyOrder.getMenu4() == null || "".equals(weeklyOrder.getMenu4())) {
            Log.e("Incomplete Data", "No ha llenado todos los menus");
            // TODO llevar el mensaje a pantalla
        } else {
            weeklyOrder.setMenuCount1(0);
            weeklyOrder.setMenuCount2(0);
            weeklyOrder.setMenuCount3(0);
            weeklyOrder.setMenuCount4(0);
            weeklyOrder.setStatus(cMenuCompleted);
            System.out.println(">>>antes de enviar  " + weeklyOrder.toString());
            saveOrder(weeklyOrder);
        }
    }

    // Completar votacion de orden semanal
    public static void completePoll(WeeklyOrder weeklyOrder) {
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

        saveOrder(weeklyOrder);

    }

    // Cerrar orden semanal y habilitar siguiente
    public static void closeOrder(WeeklyOrder weeklyOrder) {
        System.out.println("<>>>> closeOrder >>> > >" + weeklyOrder.toString());
        weeklyOrder.setStatus(cClosed);
        saveOrder(weeklyOrder);

        // TODO traer de BD la siguiente orden semanal
        WeeklyOrder nextWeeklyOrder = new WeeklyOrder(2, toFriday(new Date()), weeklyOrder.getGroupMemberId(),null, null, cActive);
        saveOrder(nextWeeklyOrder);

    }

    // Cambiar responsable
    public static void resignResponsability(WeeklyOrder weeklyOrder) {
        System.out.println("<>>>> resignResponsaability >>> > >" + weeklyOrder.toString());
        // TODO traer de BD siguiente responsable y siguientes ordenes
        ArrayList<Integer> nextResponsibleList = new ArrayList<>();
        ArrayList<WeeklyOrder> nextWeeklyOrders = new ArrayList<>();

        nextResponsibleList.add(2);
        nextWeeklyOrders.add(weeklyOrder);

        Iterator<WeeklyOrder> itw = nextWeeklyOrders.iterator();
        Iterator<Integer> itr = nextResponsibleList.iterator();
        while (itw.hasNext()) {
            WeeklyOrder _wo = itw.next();
            _wo.setGroupMemberId(itr.next());

            saveOrder(_wo);
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
}
