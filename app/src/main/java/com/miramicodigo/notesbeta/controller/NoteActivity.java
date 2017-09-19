package com.miramicodigo.notesbeta.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.helper.RealmManager;
import com.miramicodigo.notesbeta.helper.SQLiteManager;
import com.miramicodigo.notesbeta.model.Note;

import java.util.Date;

import static com.miramicodigo.notesbeta.Constants.*;

public class NoteActivity extends AppCompatActivity  implements View.OnClickListener {

    private TextInputEditText tietTitle;
    private TextInputEditText tietNote;
    private Button btnSaveNote;
    private RealmManager realmManager;
    private SQLiteManager sqLiteManager;
    private int value;
    private Note note;
    private int id;
    private int position;
    private Activity activity;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        activity = this;

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        initUI();

        realmManager = new RealmManager();
        sqLiteManager = new SQLiteManager(this);

        Bundle bundle = getIntent().getExtras();

        value = bundle.getInt("value", 0);
        position = bundle.getInt("position", -1);

        switch (value) {
            case 1:
                //NUEVO
                btnSaveNote.setText("GUARDAR");
                actionBar.setTitle("Nueva Nota");
                break;
            case 2:
                //EDITAR
                id = bundle.getInt("id");
                note = realmManager.getNote(id);
                tietTitle.setText(note.getTitle());
                tietNote.setText(note.getNote());
                btnSaveNote.setText("EDITAR");
                actionBar.setTitle("Editar Nota");
                break;
            case 3:
                //ELIMINADOS
                id = bundle.getInt("id");
                sqLiteManager.open();
                Cursor cursor = sqLiteManager.readNote(id);
                if (cursor.moveToFirst()) {
                    do {
                        int cId = cursor.getInt(0);
                        String cTitle = cursor.getString(1);
                        String cNote = cursor.getString(2);
                        String cCreationDate = cursor.getString(3);
                        int cStatus = cursor.getInt(4);

                        note = new Note();

                        note.setId(cId);
                        note.setTitle(cTitle);
                        note.setNote(cNote);
                        note.setCreationDate(new Date(Long.parseLong(cCreationDate)));
                        note.setStatus(cStatus);
                    } while (cursor.moveToNext());
                }

                sqLiteManager.close();
                System.out.println("OBJETO NOTE: "+note);
                tietTitle.setText(note.getTitle());
                tietNote.setText(note.getNote());
                tietNote.setEnabled(false);
                tietTitle.setEnabled(false);
                btnSaveNote.setVisibility(View.INVISIBLE);
                actionBar.setTitle("Nota Eliminada");
                break;
        }
        btnSaveNote.setOnClickListener(this);
    }

    public void initUI() {
        tietTitle = (TextInputEditText) findViewById(R.id.tietTitle);
        tietNote = (TextInputEditText) findViewById(R.id.tietNote);
        btnSaveNote = (Button) findViewById(R.id.btnSaveNote);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.action_note_clean:
                cleanFields();
                break;
            case R.id.action_note_delete:
                deleteNote();
                break;
            case R.id.action_trash_recovery:
                trashRecovery();
                break;
            case R.id.action_trash_delete:
                trashDelete();
                break;
        }
        return (super.onOptionsItemSelected(menuItem));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        switch (value) {
            case 1:
                getMenuInflater().inflate(R.menu.menu_clean, menu);
                break;
            case 2:
                getMenuInflater().inflate(R.menu.menu_delete, menu);
                break;
            case 3:
                getMenuInflater().inflate(R.menu.menu_trash, menu);
                break;
        }
        return true;
    }

    public void cleanFields() {
        tietTitle.setText("");
        tietNote.setText("");
        tietTitle.requestFocus();
    }

    public void deleteNote() {
        Note noteDel = realmManager.getNote(note.getId());

        Intent intent = new Intent();
        intent.putExtra("res", REALM_ENV_PAPELERA);
        intent.putExtra("position", position);

        sqLiteManager.open();
        sqLiteManager.createNote(noteDel.getTitle(), noteDel.getNote(), noteDel.getCreationDate(), 1, false);
        sqLiteManager.close();

        realmManager.deleteNoteById(noteDel.getId());

        setResult(RESULT_OK, intent);
        finish();
    }

    public void trashRecovery() {
        Intent intent = new Intent();
        intent.putExtra("res", SQLITE_RECUPERAR);
        //intent.putExtra("id", note.getId());
        intent.putExtra("position", position);


        sqLiteManager.open();
        sqLiteManager.deleteNote(note.getId(), 1);
        sqLiteManager.close();

        realmManager.addNote(
                tietTitle.getText().toString().trim(),
                tietNote.getText().toString().trim(),
                note.getCreationDate(),
                2,
                true);

        setResult(RESULT_OK, intent);
        finish();
    }

    public void trashDelete() {
        Intent intent = new Intent();
        intent.putExtra("res", SQLITE_ELIMINAR_DEFINIT);
        intent.putExtra("id", note.getId());
        intent.putExtra("position", position);

        sqLiteManager.open();
        sqLiteManager.deleteNote(note.getId(), 2);
        sqLiteManager.close();
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View view) {
        if(!tietTitle.getText().toString().trim().equals("") && !tietNote.getText().toString().trim().equals("")) {
            Intent intent = new Intent();
            if(value == 1) {
                Note noteAdd = realmManager.addNote(
                        tietTitle.getText().toString().trim(),
                        tietNote.getText().toString().trim(),
                        new Date(),
                        1,
                        true);
                //intent.putExtra("res", 1);
                intent.putExtra("id", noteAdd.getId());
            } else {
                realmManager.updateNote(
                        note.getId(),
                        tietTitle.getText().toString().trim(),
                        tietNote.getText().toString().trim(),
                        note.getCreationDate(),
                        1,
                        true);
                intent.putExtra("res", REALM_EDITADO);
                intent.putExtra("id", note.getId());
            }
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Debe llenar los campos", Toast.LENGTH_SHORT).show();
        }
    }
}
