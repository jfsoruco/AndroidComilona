package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

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

    public static String getCreateTableScript() {
        return "CREATE TABLE " + TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_GROUP_ID + " INTEGER, " +
                COLUMN_NAME_EMPLOYEE_ID + " INTEGER, " +
                COLUMN_NAME_RESPONSIBLE_ORDER + " INTEGER, " +
                COLUMN_NAME_CURRENT_RESPONSIBLE + " TEXT )";
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
