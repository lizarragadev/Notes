package com.miramicodigo.notesbeta.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.miramicodigo.notesbeta.model.Note;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by gusn8 on 18-08-17.
 */

public class SQLiteManager {
    private final SQLiteOpenHelper dbHelper;
    private SQLiteDatabase db;
    private final Context context;
    private ExternalFileManager externalFileManager;

    private static final String ADD = "add";
    private static final String DEL = "del";
    private static final String MOD = "mod";
    private static final String REC = "rec";

    private String columnas [] = new String[] {
            SQLiteHelper.COL_KEY,
            SQLiteHelper.COL_TITLE,
            SQLiteHelper.COL_NOTE,
            SQLiteHelper.COL_ELIMINATION_DATE,
            SQLiteHelper.COL_STATUS
    };

    public SQLiteManager(Context context) {
        this.context = context;
        dbHelper = new SQLiteHelper(context);
        externalFileManager = new ExternalFileManager(context);
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }
    /*
        C = Create: Crear
        R = Read:   Leer
        U = Update: Actualizar
        D = Delete: Eliminar
    */

    public boolean createNote(String title, String note, Date dt, int status, boolean edit) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_TITLE, title);
        contentValues.put(SQLiteHelper.COL_NOTE, note);
        contentValues.put(SQLiteHelper.COL_ELIMINATION_DATE, dt.getTime());
        contentValues.put(SQLiteHelper.COL_STATUS, status);
        contentValues.put(SQLiteHelper.COL_EDIT, edit);

        Note aux = new Note();
        aux.setTitle(title);
        aux.setNote(note);
        aux.setCreationDate(new Date());

        externalFileManager.guardarExterno(aux, ADD);
        return db.insert(SQLiteHelper.TABLE_NAME, null, contentValues) > 0;
    }

    public Cursor readNote(long id) {
        return db.query(
                SQLiteHelper.TABLE_NAME,
                columnas,
                SQLiteHelper.COL_KEY+"=?",
                new String[] { id +""},
                null,
                null,
                null);
    }

    public Cursor readAllNotes() {
        return db.query(
                SQLiteHelper.TABLE_NAME,
                columnas,
                null, null, null, null, null
        );
    }

    public boolean updateNote(Note note, int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(SQLiteHelper.COL_TITLE, note.getTitle());
        contentValues.put(SQLiteHelper.COL_NOTE, note.getNote());
        contentValues.put(SQLiteHelper.COL_ELIMINATION_DATE, note.getCreationDate().getTime());

        return db.update(
                SQLiteHelper.TABLE_NAME,
                contentValues,
                SQLiteHelper.COL_KEY +"="+id, null) > 0;
    }

    public boolean deleteNote(int id, int value) {
        Cursor cursor = readNote(id);
        Note aux = new Note();
        if (cursor.moveToFirst()) {
            do {
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String creationDate = cursor.getString(3);
                aux.setTitle(title);
                aux.setNote(note);
                aux.setCreationDate(new Date(Long.parseLong(creationDate)));
            } while (cursor.moveToNext());
        }
        if(value == 1){
            externalFileManager.guardarExterno(aux, REC);
        }else{
            externalFileManager.guardarExterno(aux, DEL);
        }
        return db.delete(SQLiteHelper.TABLE_NAME, SQLiteHelper.COL_KEY+"="+id, null) > 0;
    }
}
