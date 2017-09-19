package com.miramicodigo.notesbeta.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "database";
    private static final int VERSION = 1;

    public static final String TABLE_NAME = "Note";
    public static final String COL_KEY = "_id";
    public static final String COL_TITLE = "title";
    public static final String COL_NOTE = "note";
    public static final String COL_ELIMINATION_DATE = "elimination_date";
    public static final String COL_STATUS = "status";
    public static final String COL_EDIT = "edit";

    private static final String QUERY_CREATE = "CREATE TABLE "+TABLE_NAME + "(" +
                                                            COL_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                                                            COL_TITLE + " TEXT, " +
                                                            COL_NOTE + " TEXT, " +
                                                            COL_ELIMINATION_DATE + " DATE, " +
                                                            COL_STATUS+" INTEGER, "+
                                                            COL_EDIT + " BOOLEAN)";
    private static final String QUERY_UPGRADE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(QUERY_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(QUERY_UPGRADE);
        onCreate(sqLiteDatabase);
    }
}
