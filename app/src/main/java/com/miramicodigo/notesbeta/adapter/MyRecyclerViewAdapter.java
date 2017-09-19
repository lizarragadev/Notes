package com.miramicodigo.notesbeta.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.miramicodigo.notesbeta.R;
import com.miramicodigo.notesbeta.controller.NoteActivity;
import com.miramicodigo.notesbeta.model.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 *
 * @author Gustavo Lizarraga
 * @since 1.0
 *
 */

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private List<Note> itemList;
    private Context context;
    private int type;

    public MyRecyclerViewAdapter(Context context, List<Note> itemList, int type) {
        this.itemList = itemList;
        this.context = context;
        this.type = type;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_card, null);
        MyViewHolder rcv = new MyViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTitle.setText(itemList.get(position).getTitle());
        holder.tvContent.setText(itemList.get(position).getNote());
        holder.tvDate.setText(dateFormat(itemList.get(position).getCreationDate()));
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvDate;

        public MyViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tvItemTitle);
            tvContent = (TextView) itemView.findViewById(R.id.tvItemContent);
            tvDate = (TextView) itemView.findViewById(R.id.tvItemDate);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Note noteSelected = itemList.get(getAdapterPosition());
            Intent intent = new Intent(context, NoteActivity.class);
            intent.putExtra("value", type);
            intent.putExtra("id", noteSelected.getId());
            intent.putExtra("position", getAdapterPosition());
            ((Activity)view.getContext()).startActivityForResult(intent, type);

        }
    }

    public int getPositionNote(Note note) {
        for(int i = 0; i < itemList.size(); i++) {
            if(itemList.get(i).getId() == note.getId()) {
                return i;
            }
        }
        return 0;
    }

    public void removeNote(int position) {
        itemList.remove(position);
        updateAdapter();
    }

    public void updateNote(Note note) {
        itemList.set(getPositionNote(note), note);
        updateAdapter();
    }

    public void insertNote(Note note) {
        itemList.add(note);
        updateAdapter();
    }

    public void updateAdapter(){
        notifyDataSetChanged();
    }

    public String dateFormat(Date d) {
        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        return df.format(d);
    }
}
