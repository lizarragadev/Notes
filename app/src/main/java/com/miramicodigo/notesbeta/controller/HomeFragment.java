package com.miramicodigo.notesbeta.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.adapter.MyRecyclerViewAdapter;
import com.miramicodigo.notesbeta.helper.RealmManager;
import com.miramicodigo.notesbeta.model.Note;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.miramicodigo.notesbeta.Constants.*;

public class HomeFragment extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private FloatingActionButton floatingActionButton;
    private CoordinatorLayout coordinatorLayout;
    private View rootView = null;
    private RealmManager realmManager;
    private MyRecyclerViewAdapter adapter;
    private RelativeLayout relativeLayout;
    private SharedPreferences sharedPreferences;
    private List<Note> notes;

    public HomeFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        setup();
        initUI();

        notes = realmManager.getAllNotes();

        adapter = new MyRecyclerViewAdapter(rootView.getContext(), notes, NOTE_REALM_NUEVO);
        recyclerView.setAdapter(adapter);

        floatingActionButton.setOnClickListener(this);

        if(sharedPreferences.getBoolean("cbGreet", false)) {
            Snackbar snackbar = Snackbar
                    .make(coordinatorLayout, "Bienvenid@ " + sharedPreferences.getString("etName", "Usuario"), Snackbar.LENGTH_LONG)
                    .setAction("Ocultar", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorSecondaryText));

            View sbView = snackbar.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.colorAccent));
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.colorWhiteText));
            textView.setGravity(Gravity.CENTER);
            snackbar.show();
        }

        updateUI();

        return rootView;
    }

    public void initUI () {
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.clContent);
        floatingActionButton = (FloatingActionButton) rootView.findViewById(R.id.fabAddNote);

        relativeLayout = (RelativeLayout) rootView.findViewById(R.id.rlPanel);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.rvHome);
        recyclerView.setHasFixedSize(true);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);

        //if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            //staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        //} else{
          //  staggeredGridLayoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        //}
    }

    public void setup() {
        realmManager = new RealmManager();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(rootView.getContext());
    }

    public void updateUI() {
        if (notes.size() == 0) {
            relativeLayout.setVisibility(View.VISIBLE);
        } else {
            relativeLayout.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        rotateFabForward(floatingActionButton);
        Intent intent = new Intent(rootView.getContext(), NoteActivity.class);
        intent.putExtra("value", NOTE_REALM);
        getActivity().startActivityForResult(intent, NOTE_REALM);
    }

    public void rotateFabForward(FloatingActionButton fab) {
        ViewCompat.animate(fab)
                .rotation(135.0F)
                .withLayer()
                .scaleX(1.2F)
                .scaleY(1.2F)
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(10.0F))
                .start();
    }

    public void rotateFabBackward(FloatingActionButton fab) {
        ViewCompat.animate(fab)
                .rotation(0.0F)
                .withLayer()
                .scaleX(1.0F)
                .scaleY(1.0F)
                .setDuration(300L)
                .setInterpolator(new OvershootInterpolator(10.0F))
                .start();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == NOTE_REALM) {
            if(resultCode == RESULT_OK) {
                int idNote = data.getIntExtra("id", 0);
                Note newNote = realmManager.getNote(idNote);
                adapter.insertNote(newNote);
            }
        } else {
            if(resultCode == RESULT_OK) {
                int res = data.getIntExtra("res", 0);
                if(res == 1) {
                    int idNote = data.getIntExtra("id", 0);
                    Note newNote = realmManager.getNote(idNote);
                    adapter.updateNote(newNote);
                } else {
                    int position = data.getIntExtra("position", 0);
                    adapter.removeNote(position);
                }
            }
        }
        updateUI();
        rotateFabBackward(floatingActionButton);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        realmManager.closeRealm();
    }
}
