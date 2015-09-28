package com.example.juansoruco.comilonaproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class ComilonaDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "comilona.db";
    private static CursorFactory DATABASE_FACTORY = null;

    public ComilonaDbHelper(Context context) {
        super (context, DATABASE_NAME, DATABASE_FACTORY, DATABASE_VERSION);
    }

    public ComilonaDbHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("start DbHelper.onCreate", "creando tablas ");

        dropTables(db);

        installTables(db);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        dropTables(db);

        db.execSQL(EmployeeColumns.getCreateTableScript());
        db.execSQL(GroupColumns.getCreateTableScript());
        db.execSQL(WeeklyOrderColumns.getCreateTableScript());
        db.execSQL(GroupMemberColumns.getCreateTableScript());
    }

    public void dropTables(SQLiteDatabase db) {
        try {
            db.execSQL("DROP TABLE IF EXISTS " + WeeklyOrderColumns.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GroupMemberColumns.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + GroupColumns.TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + EmployeeColumns.TABLE_NAME);
        } catch (SQLiteException e) {
            Log.d("dropTables", e.getMessage());
        }
    }

    public void installTables(SQLiteDatabase db) {
        try {
            db.execSQL(EmployeeColumns.getCreateTableScript());
            db.execSQL(GroupColumns.getCreateTableScript());
            db.execSQL(WeeklyOrderColumns.getCreateTableScript());
            db.execSQL(GroupMemberColumns.getCreateTableScript());
            Log.i(this.getClass().toString(), "Tablas creadas");

            db.execSQL(EmployeeColumns.getCreateIndexScript());
            db.execSQL(GroupColumns.getCreateIndexScript());
            db.execSQL(WeeklyOrderColumns.getCreateIndexScript());
            db.execSQL(GroupMemberColumns.getCreateIndexScript());
            Log.i(this.getClass().toString(), "Indices creados");

            ArrayList<String> insertList = EmployeeColumns.getInsertListScript();
            for (String element : insertList) {
                db.execSQL(element);
            }
            Log.i(this.getClass().toString(), "Empleados registrados " + insertList.size());

            insertList = GroupColumns.getInsertListScript();
            for (String element : insertList) {
                Log.d("loopGroups", ">" + element);
                db.execSQL(element);
            }
            Log.i(this.getClass().toString(), "Grupos registrados " + insertList.size());

            insertList = GroupMemberColumns.getInsertListScript();
            //System.out.println(">>>>>//>>>>>>>>>>> llenando datos:" + insertList.size());
            for (String element : insertList) {
                Log.d("loopGrMembers", ">" + element);
                db.execSQL(element);
            }
            Log.i(this.getClass().toString(), "Miembros de grupo registrados " + insertList.size());
        } catch (SQLiteException e) {
            Log.d("installTables", e.getMessage());
        }
    }
}
