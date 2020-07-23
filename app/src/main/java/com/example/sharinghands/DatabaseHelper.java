package com.example.sharinghands;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    public DatabaseHelper(Context context) {

        super(context, "history", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table histories(id INTEGER primary key autoincrement, donor text, ngo text, ptitle text, amount int)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("drop table if exists histories");

    }

    public boolean DataInsertion(String donor, String ngo, String ptitle, int amount)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("donor",donor);
        values.put("ngo",ngo);
        values.put("ptitle",ptitle);
        values.put("amount",amount);

        long InsertionStatus = db.insert("histories",null, values);

        if (InsertionStatus==-1)
            return false;
        else
            return true;
    }

    public Cursor getUserHistory(String userId)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor myCursor = db.rawQuery("select * from histories where donor =?", new String[] {userId});

        return myCursor;
    }
}
