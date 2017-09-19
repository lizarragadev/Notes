package com.miramicodigo.notesbeta.controller;

import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.adapter.MyRecyclerViewAdapter;
import com.miramicodigo.notesbeta.helper.RealmManager;
import com.miramicodigo.notesbeta.helper.SQLiteManager;
import com.miramicodigo.notesbeta.model.Note;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static com.miramicodigo.notesbeta.Constants.NOTE_SQLITE;

public class TrashFragment extends Fragment {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private View rootView = null;
    private MyRecyclerViewAdapter adapter;
    private SQLiteManager sqLiteManager;
    private RelativeLayout relativeLayout;
    private List<Note> notes;

    public TrashFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_trash, container, false);
        rootView.setTag(TAG);

        sqLiteManager = new SQLiteManager(rootView.getContext());
        sqLiteManager.open();

        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvTrash);
        recyclerView.setHasFixedSize(true);
        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rlPanelTrash);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        } else{
            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        }
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        Cursor cursor = sqLiteManager.readAllNotes();
        notes = new ArrayList<Note>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);
                String creationDate = cursor.getString(3);
                int status = cursor.getInt(4);

                Note aux = new Note();
                aux.setId(id);
                aux.setTitle(title);
                aux.setNote(note);
                aux.setCreationDate(new Date(Long.parseLong(creationDate)));
                aux.setStatus(status);
                notes.add(aux);
            } while (cursor.moveToNext());
        }

        if(notes.size() <= 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }

        updateUI();
        adapter = new MyRecyclerViewAdapter(rootView.getContext(), notes, NOTE_SQLITE);
        recyclerView.setAdapter(adapter);

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOTE_SQLITE) {
            if(resultCode == RESULT_OK) {
                int res = data.getIntExtra("res", 0);
                int position = data.getIntExtra("position", 0);
                adapter.removeNote(position);
            }
        }
        updateUI();
    }

    public void updateUI() {
        if (notes.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }

}
