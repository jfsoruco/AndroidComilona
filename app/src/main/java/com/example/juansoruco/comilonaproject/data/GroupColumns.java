package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.juansoruco.comilonaproject.group.Group;
import com.example.juansoruco.comilonaproject.groupDetails.GroupDetails;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class GroupColumns implements BaseColumns{
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.GROUPS_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.GROUPS_RESULT;

    public static final String TABLE_NAME = "group_comilona";

    public static final String COLUMN_NAME_FULL_NAME = "full_name";
    public static final String COLUMN_NAME_STATUS = "status";

    public static final String[] TABLE_COLUMNS = new String[]{_ID,
            COLUMN_NAME_FULL_NAME, COLUMN_NAME_STATUS};

    private Context context;
    private ComilonaDbHelper dbHelper;
    private SQLiteDatabase db;

    public GroupColumns(Context context) {
        this.context = context;
    }

    public static String getCreateTableScript() {
        return "CREATE TABLE " + TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_FULL_NAME + " TEXT, " +
                COLUMN_NAME_STATUS + " TEXT )";
    }

    public static String getCreateIndexScript() {
        return "CREATE UNIQUE INDEX " + TABLE_NAME + "_IDX ON " + TABLE_NAME + "(" +
                COLUMN_NAME_FULL_NAME + " ASC)";
    }

    public static ArrayList<String> getInsertListScript() {
        ArrayList<String> result = new ArrayList<>();
        String insertHead = "INSERT INTO " + TABLE_NAME + "(_ID," +
                COLUMN_NAME_FULL_NAME + ", " +
                COLUMN_NAME_STATUS;
        result.add(insertHead + ") VALUES(1, 'Billing', 'ACTIVO')");
        result.add(insertHead + ") VALUES(2, 'CRM', 'ACTIVO')");
        result.add(insertHead + ") VALUES(3, 'Servicios', 'ACTIVO')");
        result.add(insertHead + ") VALUES(4, 'La Rosca', 'ACTIVO')");
        result.add(insertHead + ") VALUES(5, 'El barco fantasma', 'ACTIVO')");

        return result;
    }

    public GroupColumns abrir() throws SQLException {
        dbHelper = new ComilonaDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long insert(Group group) throws SQLException {
        if (db == null) { abrir(); }
        return db.insert(TABLE_NAME, null, toContentValues(group));
    }

    public long update(Group group) throws SQLException {
        if (db == null) { abrir(); }
        ContentValues values = toContentValues(group);
        return db.update(TABLE_NAME, values, "_id=" + group.get_id(), null);
    }

    public Group getRecord(int groupId) throws SQLException {
        Group element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, "_id=" + groupId, null, null, null, null);
        while(cursor.moveToNext()) {
            element = new Group(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }

        return element;
    }

    public Group getRecord(String groupName) throws SQLException {
        Group element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, COLUMN_NAME_FULL_NAME + "='" + groupName + "'", null, null, null, null);
        while(cursor.moveToNext()) {
            element = new Group(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }

        return element;
    }

    // TODO quitar este metodo de trace
    public void showContent() throws SQLException {
        System.out.println(">>>>>>>>>>>>>>>>>>>> Group.showContent ");
        Group element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, null, null, null, null, null);
        while(cursor.moveToNext()) {
            element = new Group(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
            System.out.println(element.toString());
        }
    }

    public String getDescription(int groupId) throws SQLException {
        Group group = getRecord(groupId);
        return (group != null ? group.getFullName() : null);
    }

    public static ContentValues toContentValues(Group group) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FULL_NAME, group.getFullName());
        values.put(COLUMN_NAME_STATUS, group.getStatus());

        return values;
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
