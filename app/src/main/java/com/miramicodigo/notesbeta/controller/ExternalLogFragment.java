package com.miramicodigo.notesbeta.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.helper.ExternalFileManager;

public class ExternalLogFragment extends Fragment {

    private View rootView = null;
    private TextView tvExternalFile;
    private ExternalFileManager externalFileManager;
    private RelativeLayout relativeLayout;

    public ExternalLogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_external_log, container, false);

        tvExternalFile = (TextView) rootView.findViewById(R.id.tvExternalFile);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rlPanelExternal);

        externalFileManager = new ExternalFileManager(rootView.getContext());

        String res = externalFileManager.leerExterno();

        if(res.equals("")) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
            tvExternalFile.setText(res);
        }
        return rootView;
    }
}
