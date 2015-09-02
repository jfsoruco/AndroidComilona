package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class WeeklyOrderColumns implements BaseColumns {
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.WEEKLY_ORDERS_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.WEEKLY_ORDERS_RESULT;

    public static final String TABLE_NAME = "weekly_order";

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

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
