package com.miramicodigo.notesbeta.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.miramicodigo.notesbeta.model.Note;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by gusn8 on 22-08-17.
 */

public class InternalFileManager {

    private String name = "archivo.txt";
    private Context context;

    private static final String ADD = "add";
    private static final String DEL = "del";
    private static final String MOD = "mod";
    private static final String REC = "rec";

    private SharedPreferences sharedPreferences;

    public InternalFileManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public String leerInterno() {
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(context.openFileInput(name)));
            String cadena, resultado = "";
            while((cadena = br.readLine()) != null) {
                resultado = resultado + "\n" + cadena;
            }
            return resultado;
        } catch (Exception e) {
            return "";
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void guardarInterno(Note note, String type) {
        if(sharedPreferences.getBoolean("cbInterno", false)) {
            BufferedWriter bufferedWriter = null;
            if (note != null) {
                try {
                    String status = "";
                    switch (type){
                        case ADD:   status = "ADICIONAR";
                            break;
                        case DEL:   status = "ELIMINAR";
                            break;
                        case MOD:   status = "MODIFICAR";
                            break;
                        case REC:   status = "RECUPERAR";
                            break;
                    }
                    Date dateChange = new Date();
                    String fin ="[ACCION] -> " + status + "\n[TITULO] -> " +note.getTitle()+ "\n[CONTENIDO] -> "+note.getNote()+"\n[FECHA] -> "+dateFormat(dateChange)+"\n";

                    FileOutputStream fileOutputStream = context.openFileOutput(name, Context.MODE_APPEND);
                    bufferedWriter = new BufferedWriter(new OutputStreamWriter(fileOutputStream));

                    bufferedWriter.write(fin);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                } catch (Exception e) {
                    Log.e("InternalFileManager", e.getMessage());
                } finally {
                    if (bufferedWriter != null) {
                        try {
                            bufferedWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            } else {
                Toast.makeText(context.getApplicationContext(),
                        "Debe ingresar datos para guardar.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    public String dateFormat(Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(d);
    }
}
