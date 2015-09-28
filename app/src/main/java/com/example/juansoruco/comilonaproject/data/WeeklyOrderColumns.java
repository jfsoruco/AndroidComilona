package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrder;
import com.example.juansoruco.comilonaproject.weeklyOrder.WeeklyOrderLogic;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class WeeklyOrderColumns implements BaseColumns {
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.WEEKLY_ORDERS_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.WEEKLY_ORDERS_RESULT;

    public static final String TABLE_NAME = "weekly_order";

    public static final String COLUMN_NAME_ID = "_id";
    public static final String COLUMN_NAME_DATE = "date";
    public static final String COLUMN_NAME_GROUP_MEMBER_ID = "group_member_id";
    public static final String COLUMN_NAME_STATUS = "status";
    public static final String COLUMN_NAME_MENU_1 = "menu_1";
    public static final String COLUMN_NAME_MENU_2 = "menu_2";
    public static final String COLUMN_NAME_MENU_3 = "menu_3";
    public static final String COLUMN_NAME_MENU_4 = "menu_4";
    public static final String COLUMN_NAME_MENU_SELECTED = "menu_selected";
    public static final String COLUMN_NAME_MENU_1_COUNT = "menu_1_count";
    public static final String COLUMN_NAME_MENU_2_COUNT = "menu_2_count";
    public static final String COLUMN_NAME_MENU_3_COUNT = "menu_3_count";
    public static final String COLUMN_NAME_MENU_4_COUNT = "menu_4_count";
    public static final String[] TABLE_COLUMNS = new String[]{COLUMN_NAME_ID,
            COLUMN_NAME_DATE, COLUMN_NAME_GROUP_MEMBER_ID, COLUMN_NAME_STATUS,
            COLUMN_NAME_MENU_1, COLUMN_NAME_MENU_2, COLUMN_NAME_MENU_3,
            COLUMN_NAME_MENU_4, COLUMN_NAME_MENU_SELECTED, COLUMN_NAME_MENU_1_COUNT,
            COLUMN_NAME_MENU_2_COUNT, COLUMN_NAME_MENU_3_COUNT, COLUMN_NAME_MENU_4_COUNT};

    public static DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private Context context;
    private ComilonaDbHelper dbHelper;
    private SQLiteDatabase db;

    public WeeklyOrderColumns(Context context) {
        this.context = context;
    }

    public static String getCreateTableScript() {
        return "CREATE TABLE " + TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_DATE + " DATE, " +
                COLUMN_NAME_GROUP_MEMBER_ID + " INTEGER, " +
                COLUMN_NAME_STATUS + " TEXT, " +
                COLUMN_NAME_MENU_1 + " TEXT, " +
                COLUMN_NAME_MENU_2 + " TEXT, " +
                COLUMN_NAME_MENU_3 + " TEXT, " +
                COLUMN_NAME_MENU_4 + " TEXT, " +
                COLUMN_NAME_MENU_SELECTED + " TEXT, " +
                COLUMN_NAME_MENU_1_COUNT + " INTEGER, " +
                COLUMN_NAME_MENU_2_COUNT + " INTEGER, " +
                COLUMN_NAME_MENU_3_COUNT + " INTEGER, " +
                COLUMN_NAME_MENU_4_COUNT + " INTEGER )";
    }

    public static String getCreateIndexScript() {
        return "CREATE INDEX " + TABLE_NAME + "_IDX ON " + TABLE_NAME + "(" +
                COLUMN_NAME_STATUS + " ASC)";
    }

    public WeeklyOrderColumns abrir() throws SQLException {
        dbHelper = new ComilonaDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long insert(WeeklyOrder weeklyOrder) throws SQLException {
        if (db == null) { abrir(); }
        return db.insert(TABLE_NAME, null, toContentValues(weeklyOrder));
    }

    public long update(WeeklyOrder weeklyOrder) throws SQLException {
        if (db == null) { abrir(); }
        ContentValues values = toContentValues(weeklyOrder);
        return db.update(TABLE_NAME, values, "_id=" + weeklyOrder.get_id(), null);
    }

    public WeeklyOrder getRecord(int w_o_id) throws SQLException{
        WeeklyOrder element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, "_id=" + w_o_id, null, null, null, null);
        while(cursor.moveToNext()) {
            Date date;
            try {
                date = df.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                date = null;
            }
            GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
            String[] descriptions = groupMemberAdapter.getDescriptions(cursor.getInt(2));
            element = new WeeklyOrder(cursor.getInt(0),date,cursor.getInt(2), descriptions[0], descriptions[1],
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12));
        }

        return element;
    }

    public ArrayList<WeeklyOrder> getList(int w_o_id) throws SQLException{
        ArrayList<WeeklyOrder> resultList = new ArrayList<WeeklyOrder>();

        if (db == null) { abrir(); }
        String query = "SELECT * FROM " + TABLE_NAME + " w INNER JOIN " + GroupMemberColumns.TABLE_NAME +
                " gm ON w." + COLUMN_NAME_GROUP_MEMBER_ID + " = gm." + GroupMemberColumns._ID +
                " WHERE w." + _ID + " > ? AND gm." + GroupMemberColumns.COLUMN_NAME_GROUP_ID +
                " = (SELECT gm2." + GroupMemberColumns.COLUMN_NAME_GROUP_ID + " FROM " + GroupMemberColumns.TABLE_NAME +
                " gm2, " + TABLE_NAME + " w2 WHERE w2." + _ID + " = ? AND w2." + COLUMN_NAME_GROUP_MEMBER_ID +
                " = gm2." + GroupMemberColumns._ID + ")";
        System.out.println("WeeklyOrder.getList>>>" + query);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(w_o_id), String.valueOf(w_o_id)});
        while (cursor.moveToNext()) {
            Date date;
            try {
                date = df.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                date = null;
            }
            GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
            String[] descriptions = groupMemberAdapter.getDescriptions(cursor.getInt(2));
            resultList.add(new WeeklyOrder(cursor.getInt(0),date,cursor.getInt(2), descriptions[0], descriptions[1],
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12)));
        }

        return resultList;
    }

    public WeeklyOrder getRecord(int groupId, Date date) throws SQLException {
        WeeklyOrder weeklyOrder = null;
        if (db == null) { abrir(); }
        String query = "SELECT * FROM " + TABLE_NAME + " w INNER JOIN " + GroupMemberColumns.TABLE_NAME +
                " gm ON w." + COLUMN_NAME_GROUP_MEMBER_ID + " = gm." + GroupMemberColumns._ID +
                " WHERE gm." + GroupMemberColumns.COLUMN_NAME_GROUP_ID + " = " + groupId +
                " AND w." + COLUMN_NAME_DATE + " = ?";
        Log.d("WOColumns.getRecord",query + ">> " + df.format(date));
        Cursor cursor = db.rawQuery(query, new String[]{df.format(date)});
        while (cursor.moveToNext()) {
            Log.d("en loop", "registro " + cursor.getPosition());
            Date _date;
            try {
                _date = df.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                _date = null;
            }
            GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
            String[] descriptions = groupMemberAdapter.getDescriptions(cursor.getInt(2));
            weeklyOrder = new WeeklyOrder(cursor.getInt(0),_date,cursor.getInt(2), descriptions[0], descriptions[1],
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12));
            Log.d("end registro", weeklyOrder.toString());
        }
        return weeklyOrder;
    }

    public WeeklyOrder getActiveRecord(int groupId) throws SQLException{
        WeeklyOrder weeklyOrder = null;
        if (db == null) { abrir(); }
        String query = "SELECT * FROM " + TABLE_NAME + " w INNER JOIN " + GroupMemberColumns.TABLE_NAME +
                " gm ON w." + COLUMN_NAME_GROUP_MEMBER_ID + " = gm." + GroupMemberColumns._ID +
                " WHERE gm." + GroupMemberColumns.COLUMN_NAME_GROUP_ID + " = " + groupId +
                " AND w." + COLUMN_NAME_STATUS + " in (?,?,?)";
        Log.d("getActiveRecord", query);
        Cursor cursor = db.rawQuery(query, new String[]{WeeklyOrderLogic.cActive, WeeklyOrderLogic.cMenuCompleted, WeeklyOrderLogic.cPollCompleted});
        while (cursor.moveToNext()) {
            Log.d("loop", String.valueOf(cursor.getInt(0)));
            Date _date;
            try {
                _date = df.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                _date = null;
            }
            GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
            String[] descriptions = groupMemberAdapter.getDescriptions(cursor.getInt(2));
            weeklyOrder = new WeeklyOrder(cursor.getInt(0),_date,cursor.getInt(2), descriptions[0], descriptions[1],
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12));
        }
        return  weeklyOrder;
    }

    // TODO quitar este metodo de trace
    public void showContent() throws SQLException {
        System.out.println(">>>>>>>>>>>>>>>>>>>> WeeklyOrder.showContent ");
        WeeklyOrder element = null;
        Date _date;
        GroupMemberColumns groupMemberAdapter = new GroupMemberColumns(context);
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, null);
        while(cursor.moveToNext()) {
            try {
                _date = df.parse(cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE)));
            } catch (ParseException e) {
                e.printStackTrace();
                _date = null;
            }
            String[] descriptions = groupMemberAdapter.getDescriptions(cursor.getInt(2));
            element = new WeeklyOrder(cursor.getInt(0),_date,cursor.getInt(2), descriptions[0], descriptions[1],
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6),
                    cursor.getString(7), cursor.getString(8), cursor.getInt(9), cursor.getInt(10),
                    cursor.getInt(11), cursor.getInt(12));
            System.out.println(element.toString());
        }
    }

    public static ContentValues toContentValues(WeeklyOrder weeklyOrder) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_DATE, df.format(weeklyOrder.getDate()));
        values.put(COLUMN_NAME_GROUP_MEMBER_ID, weeklyOrder.getGroupMemberId());
        values.put(COLUMN_NAME_STATUS, weeklyOrder.getStatus());
        if (weeklyOrder.getMenu1() != null) {
            values.put(COLUMN_NAME_MENU_1, weeklyOrder.getMenu1());
        }
        if (weeklyOrder.getMenu2() != null) {
            values.put(COLUMN_NAME_MENU_2, weeklyOrder.getMenu2());
        }
        if (weeklyOrder.getMenu3() != null) {
            values.put(COLUMN_NAME_MENU_3, weeklyOrder.getMenu3());
        }
        if (weeklyOrder.getMenu4() != null) {
            values.put(COLUMN_NAME_MENU_4, weeklyOrder.getMenu4());
        }
        if (weeklyOrder.getMenuSelected() != null) {
            values.put(COLUMN_NAME_MENU_SELECTED, weeklyOrder.getMenuSelected());
        }
        values.put(COLUMN_NAME_MENU_1_COUNT, weeklyOrder.getMenuCount1());
        values.put(COLUMN_NAME_MENU_2_COUNT, weeklyOrder.getMenuCount2());
        values.put(COLUMN_NAME_MENU_3_COUNT, weeklyOrder.getMenuCount3());
        values.put(COLUMN_NAME_MENU_4_COUNT, weeklyOrder.getMenuCount4());

        return values;
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
