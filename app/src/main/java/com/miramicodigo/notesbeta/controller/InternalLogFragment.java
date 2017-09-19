package com.miramicodigo.notesbeta.controller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.helper.InternalFileManager;

public class InternalLogFragment extends Fragment {

    private View rootView = null;
    private TextView tvInternalFile;
    private InternalFileManager internalFileManager;
    private RelativeLayout relativeLayout;

    public InternalLogFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_internal_log, container, false);
        setRetainInstance(true);

        tvInternalFile = (TextView) rootView.findViewById(R.id.tvInternalFile);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rlPanelInternal);

        internalFileManager = new InternalFileManager(rootView.getContext());

        String res = internalFileManager.leerInterno();

        if(res.equals("")) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
            tvInternalFile.setText(res);
        }
        return rootView;
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
