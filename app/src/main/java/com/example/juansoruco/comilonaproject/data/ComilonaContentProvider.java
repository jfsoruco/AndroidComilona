package com.example.juansoruco.comilonaproject.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by juan.soruco on 30/08/2015.
 */
public class ComilonaContentProvider extends ContentProvider {

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private ComilonaDbHelper dbHelper;

    // Default values
    private static final int RESULT_EMP = 10;
    private static final int RESULT_GRP = 11;
    private static final int RESULT_WKL_WITH_GROUP = 20;
    private static final int RESULT_MEM_WITH_GROUP = 21;


    /**
     * Construccion del UriMatcher, Este UriMatcher hara posible
     * que por cada URI que se pase, se devuelva un valor constante
     * que nos permita identificar luego en el sistema
     * */
    static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = ComilonaDbContract.CONTENT_AUTHORITY;

        // content://com.salamancasolutions.footballnews/matchs > Acceso generico a tabla de partidos
        // content://com.salamancasolutions.footballnews/matchs/81? > Acceso directo a partidos por id equipo
        matcher.addURI(authority, ComilonaDbContract.EMPLOYEES_RESULT, RESULT_EMP);
        matcher.addURI(authority, ComilonaDbContract.GROUPS_RESULT, RESULT_GRP);
        matcher.addURI(authority, ComilonaDbContract.WEEKLY_ORDERS_RESULT + "/#", RESULT_WKL_WITH_GROUP)    ;
        matcher.addURI(authority, ComilonaDbContract.GROUP_MEMBERS_RESULT + "/#", RESULT_MEM_WITH_GROUP);

        return matcher;
    }

    public ComilonaContentProvider() {
    }

    @Override
    public String getType(Uri uri) {
        int match = uriMatcher.match(uri);

        switch (match) {
            case RESULT_EMP:
                return EmployeeColumns.CONTENT_TYPE;
            case RESULT_GRP:
                return GroupColumns.CONTENT_TYPE;
            case RESULT_WKL_WITH_GROUP:
                return WeeklyOrderColumns.CONTENT_TYPE;
            case RESULT_MEM_WITH_GROUP:
                return GroupMemberColumns.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        Uri returnUri = null;
        boolean unknown = false;

        switch (match) {
            case RESULT_EMP: {
                long id = db.insert(EmployeeColumns.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = EmployeeColumns.buildMatchUri(id);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }
            case RESULT_GRP: {
                long id = db.insert(GroupColumns.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = GroupColumns.buildMatchUri(id);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }
            case RESULT_WKL_WITH_GROUP: {
                long id = db.insert(WeeklyOrderColumns.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = WeeklyOrderColumns.buildMatchUri(id);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }
            case RESULT_MEM_WITH_GROUP: {
                long id = db.insert(GroupMemberColumns.TABLE_NAME, null, values);

                if (id > 0) {
                    returnUri = GroupMemberColumns.buildMatchUri(id);
                } else {
                    throw new UnsupportedOperationException("Unknown uri: " + uri);
                }
            }
            default:
                unknown = true;
        }
        if (unknown) {
            throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int deleted;
        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case RESULT_EMP: {
                deleted = db.delete(EmployeeColumns.TABLE_NAME, selection, selectionArgs);
            }
            case RESULT_GRP: {
                deleted = db.delete(GroupColumns.TABLE_NAME, selection, selectionArgs);
            }
            case RESULT_WKL_WITH_GROUP: {
                deleted = db.delete(WeeklyOrderColumns.TABLE_NAME, selection, selectionArgs);
            }
            case RESULT_MEM_WITH_GROUP: {
                deleted = db.delete(GroupMemberColumns.TABLE_NAME, selection, selectionArgs);
            }
            default:
                deleted = 0;
        }
        if (deleted != 0)
            getContext().getContentResolver().notifyChange(uri, null);


        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int match = uriMatcher.match(uri);
        int updated;

        if (selection == null) {
            selection = "1";
        }

        switch (match) {
            case RESULT_EMP: {
                updated = db.update(EmployeeColumns.TABLE_NAME, values, selection, selectionArgs);
            }
            case RESULT_GRP: {
                updated = db.update(GroupColumns.TABLE_NAME, values, selection, selectionArgs);
            }
            case RESULT_WKL_WITH_GROUP: {
                updated = db.update(WeeklyOrderColumns.TABLE_NAME, values, selection, selectionArgs);
            }
            case RESULT_MEM_WITH_GROUP: {
                updated = db.update(GroupMemberColumns.TABLE_NAME, values, selection, selectionArgs);
            }
            default:
                updated = 0;
        }

        if (updated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return updated;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new ComilonaDbHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;

        String where = selection;

        SQLiteDatabase db = dbHelper.getWritableDatabase();

        if (uriMatcher.match(uri) == RESULT_WKL_WITH_GROUP) {
            where = WeeklyOrderColumns._ID + "=" + uri.getLastPathSegment();
            Cursor c = db.query(WeeklyOrderColumns.TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
            return c;
        } else if (uriMatcher.match(uri) == RESULT_MEM_WITH_GROUP) {
            where = GroupMemberColumns._ID + "=" + uri.getLastPathSegment();
            Cursor c = db.query(GroupMemberColumns.TABLE_NAME, projection, where, selectionArgs, null, null, sortOrder);
            return c;
        }
        return null;
    }
}
