package com.iwon.todolist.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {
    private static final String TAG = DbHelper.class.getSimpleName();

    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "todo.db";
    private static final String TB_TODO = "todo_list";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_DONE = "done";
    private static final String COLUMN_NOTE = "note";
    private static final String COLUMN_DATE = "date";

    private static final int DONE_FALSE = 0;
    private static final int DONE_TRUE = 1;


    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String createTbTodo = "CREATE TABLE " + TB_TODO + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_DONE + " CHART(1) NOT NULL, " +
                COLUMN_NOTE + " VARCHAR NOT NULL, " +
                COLUMN_DATE + " VARCHAR NOT NULL" +
                ")";
        Log.d(TAG, "onCreate: " + createTbTodo);
        sqLiteDatabase.execSQL(createTbTodo);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public ArrayList<HashMap<String, String>> allData(){
        ArrayList<HashMap<String, String>> wordList = new ArrayList<HashMap<String, String>>();

        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TB_TODO;
        Log.d(TAG, "allData: " + query);
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                map.put(COLUMN_ID, cursor.getString(0));
                map.put(COLUMN_DONE, cursor.getString(1));
                map.put(COLUMN_NOTE, cursor.getString(2));
                map.put(COLUMN_DATE, cursor.getString(3));
                wordList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();

        return wordList;
    }


    public void save(String note, String date){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "INSERT INTO " + TB_TODO + " (" + COLUMN_DONE + ", " + COLUMN_NOTE + "," + COLUMN_DATE + ") " +
                "VALUES ('" + DONE_FALSE +"', '"+ note +"', '"+ date +"')";
        Log.d(TAG, "save: " + query);
        db.execSQL(query);
        db.close();
    }

    public void update(int id, int done){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TB_TODO + " SET " + COLUMN_DONE + "='" + done + "' WHERE " + COLUMN_ID + "=" + id;
        Log.d(TAG, "update: " + query);
        db.execSQL(query);
        db.close();
    }

    public void delete(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TB_TODO + " WHERE " + COLUMN_ID + "=" + id;
        Log.d(TAG, "delete: " + query);
        db.execSQL(query);
        db.close();
    }

}
