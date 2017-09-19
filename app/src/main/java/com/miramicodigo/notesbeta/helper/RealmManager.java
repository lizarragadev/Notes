package com.miramicodigo.notesbeta.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.miramicodigo.notesbeta.model.Note;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by gusn8 on 20-08-17.
 */

public class RealmManager {
    private Realm realmManager;
    private static final String dbName = "mydb.db";
    private static InternalFileManager ifm;

    private static final String ADD = "add";
    private static final String DEL = "del";
    private static final String MOD = "mod";
    private static final String REC = "rec";

    private static SharedPreferences sharedPreferences;


    public static void initRealmConfiguration(Context context) {
        Realm.init(context);
        RealmConfiguration realmConfiguration =
                new RealmConfiguration.Builder()
                        .name(dbName)
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        ifm = new InternalFileManager(context);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public RealmManager() {
        realmManager = Realm.getDefaultInstance();
    }

    public void closeRealm() {
        realmManager.close();
    }

    public Note addNote(final String title, final String note, final Date date, final int status, final boolean edit){
        final int[] position = {0};
        realmManager.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(final Realm realm) {
                int primarykey = 0;
                Number number = realm.where(Note.class).max("id");
                if(number == null) {
                    primarykey = 1;
                } else {
                    primarykey = number.intValue() + 1;
                }
                position[0] = primarykey;
                Note obj = realmManager.createObject(Note.class, primarykey);
                obj.setTitle(title);
                obj.setNote(note);
                obj.setCreationDate(date);
                obj.setStatus(status);
                obj.setEdit(edit);
                if(status == 1) {
                    ifm.guardarInterno(obj, ADD);
                }else{
                    ifm.guardarInterno(obj, REC);
                }
            }
        });
        return getNote(position[0]);
    }

    public void updateNote(final int id, final String title, final String note, final Date date, final int status, final boolean edit) {
        realmManager.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Note editNote = getNote(id);
                editNote.setTitle(title);
                editNote.setNote(note);
                editNote.setCreationDate(date);
                editNote.setStatus(status);
                editNote.setEdit(edit);
                ifm.guardarInterno(editNote, MOD);
            }
        });
    }

    public void deleteNoteById(final int noteId){
        final RealmResults<Note> notes = realmManager.where(Note.class).equalTo("id", noteId).findAll();
        ifm.guardarInterno(notes.get(0), DEL);
        if (notes.isValid()) {
            System.out.println("IS VALID");
            realmManager.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    notes.deleteAllFromRealm();
                }
            });
        }
    }

    public List<Note> getAllNotes() {
        RealmResults<Note> results = realmManager.where(Note.class).findAll();
        return realmManager.copyFromRealm(results);
    }

    public Note getNote(int id) {
        Note result = realmManager.where(Note.class).equalTo("id", id).findFirst();
        return result;
    }

}
