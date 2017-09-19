package com.miramicodigo.notesbeta.model;

import java.io.Serializable;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class Note extends RealmObject {

    @PrimaryKey
    private int id;
    private String title;
    private String note;
    private Date creationDate;
    private int status;
    private boolean edit;

    public Note() {

    }

    public Note(String title, String note, Date creationDate, int status, boolean edit) {
        this.title = title;
        this.note = note;
        this.creationDate = creationDate;
        this.status = status;
        this.edit = edit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }
}
