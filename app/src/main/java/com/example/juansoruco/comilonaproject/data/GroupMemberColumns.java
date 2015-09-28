package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.juansoruco.comilonaproject.groupDetails.GroupDetails;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class GroupMemberColumns implements BaseColumns {
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.GROUP_MEMBERS_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.GROUP_MEMBERS_RESULT;

    public static final String TABLE_NAME = "group_member";

    public static final String COLUMN_NAME_GROUP_ID = "group_id";
    public static final String COLUMN_NAME_EMPLOYEE_ID = "employee_id";
    public static final String COLUMN_NAME_RESPONSIBLE_ORDER = "responsible_order";
    public static final String COLUMN_NAME_CURRENT_RESPONSIBLE = "current_responsible";

    public static final String[] TABLE_COLUMNS = new String[]{_ID,
            COLUMN_NAME_GROUP_ID, COLUMN_NAME_EMPLOYEE_ID, COLUMN_NAME_RESPONSIBLE_ORDER,
            COLUMN_NAME_CURRENT_RESPONSIBLE};

    private Context context;
    private ComilonaDbHelper dbHelper;
    private SQLiteDatabase db;

    public GroupMemberColumns(Context context) {
        this.context = context;
    }

    public static String getCreateTableScript() {
        return "CREATE TABLE " + TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_GROUP_ID + " INTEGER, " +
                COLUMN_NAME_EMPLOYEE_ID + " INTEGER, " +
                COLUMN_NAME_RESPONSIBLE_ORDER + " INTEGER, " +
                COLUMN_NAME_CURRENT_RESPONSIBLE + " TEXT )";
    }

    public static String getCreateIndexScript() {
        return "CREATE UNIQUE INDEX " + TABLE_NAME + "_IDX ON " + TABLE_NAME + "(" +
                COLUMN_NAME_GROUP_ID + ", " + COLUMN_NAME_EMPLOYEE_ID + ")";
    }

    public GroupMemberColumns abrir() throws SQLException {
        dbHelper = new ComilonaDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long insert(GroupDetails groupDetails) throws SQLException {
        if (db == null) { abrir(); }
        return db.insert(TABLE_NAME, null, toContentValues(groupDetails));
    }

    public long update(GroupDetails groupDetails) throws SQLException {
        if (db == null) { abrir(); }
        ContentValues values = toContentValues(groupDetails);
        return db.update(TABLE_NAME, values, "_id=" + groupDetails.get_id(), null);
    }

    public static ArrayList<String> getInsertListScript() {
        ArrayList<String> result = new ArrayList<>();
        String insertHead = "INSERT INTO " + TABLE_NAME + "(_ID," +
                COLUMN_NAME_GROUP_ID + ", " +
                COLUMN_NAME_EMPLOYEE_ID + ", " +
                COLUMN_NAME_RESPONSIBLE_ORDER + ", " +
                COLUMN_NAME_CURRENT_RESPONSIBLE;
        result.add(insertHead + ") VALUES(1, 1, 1, 1, 'YES')");
        result.add(insertHead + ") VALUES(2, 1, 2, 2, 'NO')");
        result.add(insertHead + ") VALUES(3, 1, 3, 3, 'NO')");
        result.add(insertHead + ") VALUES(4, 2, 3, 1, 'YES')");
        result.add(insertHead + ") VALUES(5, 2, 2, 2, 'NO')");
        result.add(insertHead + ") VALUES(6, 2, 1, 3, 'NO')");

        return result;
    }

    public GroupDetails getRecord(int groupMemberId) throws SQLException{
        System.out.println(">>> GroupMember.getRecord " + groupMemberId);
        GroupDetails element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, "_id=" + groupMemberId, null, null, null, null);
        while(cursor.moveToNext()) {
            GroupColumns groupAdapter = new GroupColumns(context);
            EmployeeColumns employeeAdapter = new EmployeeColumns(context);
            element = new GroupDetails(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                    groupAdapter.getDescription(cursor.getInt(1)),
                    employeeAdapter.getDescription(cursor.getInt(2)),
                    cursor.getInt(3), cursor.getString(4));
        }
        System.out.println(">>>>>>>> element >> " + element.toString());
        return element;
    }

    public String[] getDescriptions(int groupMemberId) throws SQLException {
        GroupDetails groupDetails = getRecord(groupMemberId);

        return (groupDetails != null ?
                new String[]{groupDetails.getGroupFullname(), groupDetails.getEmployeeFullname()} :
                new String[]{"", ""});
    }

    public ArrayList<GroupDetails> getList (int groupId, int groupMemberId) throws SQLException {
        ArrayList<GroupDetails> listResultFinal = new ArrayList<>();
        ArrayList<GroupDetails> listBefore = new ArrayList<>();
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, COLUMN_NAME_GROUP_ID +"=" + groupId, null, null, null, COLUMN_NAME_RESPONSIBLE_ORDER);
        boolean isBefore = true;
        while(cursor.moveToNext()) {
            GroupColumns groupAdapter = new GroupColumns(context);
            EmployeeColumns employeeAdapter = new EmployeeColumns(context);
            if (cursor.getInt(0) == groupMemberId) {isBefore = false;}
            if (isBefore) {
                listBefore.add(new GroupDetails(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                        groupAdapter.getDescription(cursor.getInt(1)),
                        employeeAdapter.getDescription(cursor.getInt(2)),
                        cursor.getInt(3), cursor.getString(4)));
            } else {
                listResultFinal.add(new GroupDetails(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                        groupAdapter.getDescription(cursor.getInt(1)),
                        employeeAdapter.getDescription(cursor.getInt(2)),
                        cursor.getInt(3), cursor.getString(4)));
            }

        }

        if (listBefore != null) {
            listResultFinal.addAll(listBefore);
        }

        return listResultFinal;
    }

    public ArrayList<GroupDetails> getGroupsByMember(int employeeId) throws SQLException {
        ArrayList<GroupDetails> resultList = new ArrayList<>();

        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, COLUMN_NAME_EMPLOYEE_ID +"=" + employeeId, null, null, null, COLUMN_NAME_GROUP_ID);
        while (cursor.moveToNext()) {
            GroupColumns groupAdapter = new GroupColumns(context);
            EmployeeColumns employeeAdapter = new EmployeeColumns(context);
            resultList.add(new GroupDetails(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                    groupAdapter.getDescription(cursor.getInt(1)),
                    employeeAdapter.getDescription(cursor.getInt(2)),
                    cursor.getInt(3), cursor.getString(4)));
        }

        return resultList;
    }

    public void showContents() throws SQLException {
        System.out.println(">>>>>>>>>>>>>>>>>>>> GroupMember.showContent ");
        GroupDetails element = null;
        if (db == null) { abrir(); }
        GroupColumns groupAdapter = new GroupColumns(context);
        EmployeeColumns employeeAdapter = new EmployeeColumns(context);
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, null);
        while(cursor.moveToNext()) {
            element = new GroupDetails(cursor.getInt(0),cursor.getInt(1),cursor.getInt(2),
                    groupAdapter.getDescription(cursor.getInt(1)),
                    employeeAdapter.getDescription(cursor.getInt(2)),
                    cursor.getInt(3), cursor.getString(4));
            System.out.println(element.toString());
        }
    }

    public static ContentValues toContentValues(GroupDetails groupDetails) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_GROUP_ID, groupDetails.getGroupId());
        values.put(COLUMN_NAME_EMPLOYEE_ID, groupDetails.getEmployeeId());
        values.put(COLUMN_NAME_RESPONSIBLE_ORDER, groupDetails.getResponsibleOrder());
        values.put(COLUMN_NAME_CURRENT_RESPONSIBLE, groupDetails.getCurrentResponsible());

        return values;
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
