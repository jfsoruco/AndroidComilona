package com.example.juansoruco.comilonaproject.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class GroupColumns implements BaseColumns{
    public static final Uri CONTENT_URI = ComilonaDbContract.BASE_CONTENT_URI.buildUpon().appendPath(ComilonaDbContract.GROUPS_RESULT).build();

    public static final String CONTENT_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + ComilonaDbContract.CONTENT_AUTHORITY + "/" + ComilonaDbContract.GROUPS_RESULT;

    public static final String TABLE_NAME = "group";

    public static final String COLUMN_NAME_FULL_NAME = "full_name";
    public static final String COLUMN_NAME_STATUS = "status";

    public static String getCreateTableScript() {
        return "CREATE TABLE " + TABLE_NAME +
                "(_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_FULL_NAME + " TEXT, " +
                COLUMN_NAME_STATUS + " TEXT )";
    }

    public static Uri buildMatchUri(long id) {
        return ContentUris.withAppendedId(CONTENT_URI, id);
    }
}
