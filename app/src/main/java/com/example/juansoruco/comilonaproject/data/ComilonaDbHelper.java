package com.example.juansoruco.comilonaproject.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class ComilonaDbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    static final String DATABASE_NAME = "comilona.db";

    public ComilonaDbHelper(Context context) {
        super (context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public ComilonaDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EmployeeColumns.getCreateTableScript());
        db.execSQL(GroupColumns.getCreateTableScript());
        db.execSQL(WeeklyOrderColumns.getCreateTableScript());
        db.execSQL(GroupMemberColumns.getCreateTableScript());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeColumns.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + EmployeeColumns.TABLE_NAME);

        db.execSQL(EmployeeColumns.getCreateTableScript());
        db.execSQL(GroupColumns.getCreateTableScript());
        db.execSQL(WeeklyOrderColumns.getCreateTableScript());
        db.execSQL(GroupMemberColumns.getCreateTableScript());
    }
}
