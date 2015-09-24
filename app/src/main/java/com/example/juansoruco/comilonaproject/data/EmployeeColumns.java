package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.BaseColumns;

import com.example.juansoruco.comilonaproject.employee.Employee;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class EmployeeColumns implements BaseColumns{
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.EMPLOYEES_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.EMPLOYEES_RESULT;

    public static final String TABLE_NAME = "employee";

    public static final String COLUMN_NAME_FULL_NAME = "full_name";
    public static final String COLUMN_NAME_STATUS = "status";

    public static final String[] TABLE_COLUMNS = new String[]{_ID,
            COLUMN_NAME_FULL_NAME, COLUMN_NAME_STATUS};

    private Context context;
    private ComilonaDbHelper dbHelper;
    private SQLiteDatabase db;

    public EmployeeColumns(Context context) {
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
        result.add(insertHead + ") VALUES(1, 'Milton', 'ACTIVO')");
        result.add(insertHead + ") VALUES(2, 'Yeso', 'ACTIVO')");
        result.add(insertHead + ") VALUES(3, 'Juane', 'ACTIVO')");
        result.add(insertHead + ") VALUES(4, 'Pepe', 'ACTIVO')");
        result.add(insertHead + ") VALUES(5, 'Valex', 'ACTIVO')");

        return result;
    }

    public EmployeeColumns abrir() throws SQLException {
        dbHelper = new ComilonaDbHelper(context);
        db = dbHelper.getWritableDatabase();
        return this;
    }

    public long insert(Employee employee) throws SQLException {
        if (db == null) { abrir(); }
        return db.insert(TABLE_NAME, null, toContentValues(employee));
    }

    public long update(Employee employee) throws SQLException {
        if (db == null) { abrir(); }
        ContentValues values = toContentValues(employee);
        return db.update(TABLE_NAME, values, "_id=" + employee.get_id(), null);
    }

    public Employee getRecord(int employeeId) throws SQLException {
        Employee element = null;
        if (db == null) { abrir(); }
        Cursor cursor = db.query(TABLE_NAME, TABLE_COLUMNS, "_id=" + employeeId, null, null, null, null);
        while(cursor.moveToNext()) {
            element = new Employee(cursor.getInt(0),cursor.getString(1),cursor.getString(2));
        }

        return element;
    }

    public String getDescription(int employeeId) throws SQLException {
        Employee employee = getRecord(employeeId);
        return (employee != null ? employee.getFullName() : null);
    }

    public static ContentValues toContentValues(Employee employee) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_FULL_NAME, employee.getFullName());
        values.put(COLUMN_NAME_STATUS, employee.getStatus());

        return values;
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
