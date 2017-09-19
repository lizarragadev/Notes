package com.miramicodigo.notesbeta.helper;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.miramicodigo.notesbeta.model.Note;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by gusn8 on 24-08-17.
 */

public class ExternalFileManager {
    private static final String folderName = "DevCode";
    private static final String fileName = "log.txt";
    private Context context;

    private static final String ADD = "add";
    private static final String DEL = "del";
    private static final String MOD = "mod";
    private static final String REC = "rec";

    private SharedPreferences sharedPreferences;

    public ExternalFileManager(Context context) {
        this.context = context;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void guardarExterno(Note note, String type) {
        if(sharedPreferences.getBoolean("cbExterno", false)) {
            if (note != null) {
                boolean sdDisponible = false;
                boolean sdAccesoEscritura = false;
                String state = Environment.getExternalStorageState();
                if (Environment.MEDIA_MOUNTED.equals(state)){
                    sdDisponible = true;
                    sdAccesoEscritura = true;
                }else {
                    if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                        sdDisponible = true;
                        sdAccesoEscritura = false;
                    } else {
                        sdDisponible = false;
                        sdAccesoEscritura = false;
                    }
                }
                if (sdDisponible && sdAccesoEscritura) {
                    try {
                        File dir = new File(Environment.getExternalStorageDirectory()+"/"+folderName);
                        if (!dir.exists()) {
                            dir.mkdir();
                        }
                        File file = new File(dir, fileName);
                        String status = "";
                        switch (type){
                            case ADD:   status = "ADICIONAR";
                                break;
                            case DEL:   status = "ELIMINAR PERMANENTEMENTE";
                                break;
                            case MOD:   status = "MODIFICAR";
                                break;
                            case REC:   status = "RECUPERAR";
                                break;
                        }
                        Date dateChange = new Date();
                        String fin ="[ACCION] -> " + status + "\n[TITULO] -> " +note.getTitle()+ "\n[CONTENIDO] -> "+note.getNote()+"\n[FECHA] -> "+dateFormat(dateChange)+"\n\n";
                        try {
                            OutputStreamWriter osw = new OutputStreamWriter(new FileOutputStream(file, true));
                            osw.append(fin);
                            osw.close();
                        }catch (Exception e) {
                            System.out.println("Error: "+e.getMessage());
                        }
                    } catch (Exception e) {
                        System.out.println("Error: "+e.getMessage());
                    }
                } else {
                    System.out.println("No se puede escribir en su memoria");
                }
            } else {
                Toast.makeText(context.getApplicationContext(),
                        "Debe ingresar datos para guardar",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String leerExterno() {
        try {
            File file = Environment.getExternalStorageDirectory();
            File f = new File(file.getAbsolutePath(), "/" + folderName + "/" + fileName);

            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(f)));
            String cadena, resultado = "";
            while((cadena = br.readLine()) != null) {
                resultado = resultado + "\n" + cadena;
            }
            br.close();
            return resultado;
        } catch (Exception e) {
            return "";
        }
    }

    public String dateFormat(Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(d);
    }
}
