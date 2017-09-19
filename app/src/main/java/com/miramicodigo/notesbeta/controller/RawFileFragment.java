package com.miramicodigo.notesbeta.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miramicodigo.notesbeta.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class RawFileFragment extends Fragment {
    private TextView tvRawFile;

    private View rootView = null;
    private RelativeLayout relativeLayout;

    public RawFileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_raw_file, container, false);
        setRetainInstance(true);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rlPanelRawFile);
        tvRawFile = (TextView) rootView.findViewById(R.id.tvRawFile);

        String res = readRawFile();

        if(res.equals("")) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
            tvRawFile.setText(res);
        }
        return rootView;
    }

    public String readRawFile() {
        String cadena, resultado = "";
        try {
            InputStream is = getResources().openRawResource(R.raw.archivo_raw);
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            while((cadena = br.readLine()) != null) {
                resultado = resultado + "\n" + cadena;
            }
        }catch (Exception e) {
            Log.e("RawFileFragment", e.getMessage());
        }

        return resultado;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
