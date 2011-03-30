package com.rosehulman.android.homework;

import java.util.GregorianCalendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

public class HomeworkDbAdapter {
    
    private static final String DATABASE_NAME = "tasks.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tasks";
    
    private static final String ID_KEY = "id";
    private static final int ID_COLUMN = 0;
    private static final String NAME_KEY = "name";
    private static final int NAME_COLUMN = 1;
    private static final String DATE_KEY = "date";
    private static final int DATE_COLUMN = 2;
    
    private HomeworkDbHelper mOpenHelper;
    private SQLiteDatabase mDb;
    
    public HomeworkDbAdapter(Context context) {
        mOpenHelper = new HomeworkDbHelper(context);
    }
    
    public void open() throws SQLiteException {
        mDb = mOpenHelper.getWritableDatabase();
    }
    
    public void close() {
        mDb.close();
    }
    
    private ContentValues getContentValuesFromTask(Task task) {
        ContentValues rowValues = new ContentValues();
        rowValues.put(NAME_KEY, task.getName());
        /* An easy way to store date/time information is to just get
           its value in milliseconds and treat it as a number when
           we store it. */
        rowValues.put(DATE_KEY, task.getDateDue().getTimeInMillis());
        return rowValues;
    }
    
    private Task getTaskFromCursor(Cursor cursor) {
        Task task = new Task();
        task.setId(cursor.getInt(ID_COLUMN));
        task.setName(cursor.getString(NAME_COLUMN));
        
        GregorianCalendar date = new GregorianCalendar();
        date.setTimeInMillis(cursor.getLong(DATE_COLUMN));
        task.setDateDue(date);
        
        return task;
    }
    
    public Task addTask(Task task) {
        /* Perform the insertion */
        ContentValues rowValues = getContentValuesFromTask(task);
        mDb.insert(TABLE_NAME, null, rowValues);
        
        /* Find the row with the highest ID */
        Cursor cursor = mDb.query(TABLE_NAME, new String[] {ID_KEY, NAME_KEY, DATE_KEY}, null, null, null, null, ID_KEY + " DESC", "1");
        cursor.moveToFirst();
        return getTaskFromCursor(cursor);
    }
    
    public void deleteTask(Task task) {
        mDb.delete(TABLE_NAME, ID_KEY + " = ?", new String[] {Integer.toString(task.getId())});
    }
    
    private static class HomeworkDbHelper extends SQLiteOpenHelper {
        
        private static String CREATE_STATEMENT;
        private static String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE_NAME;
        
        static {
            StringBuilder s = new StringBuilder();
            s.append("CREATE TABLE ");
            s.append(TABLE_NAME);
            s.append(" (");
            s.append(ID_KEY);
            s.append(" INTEGER PRIMARY KEY AUTOINCREMENT, ");
            s.append(NAME_KEY);
            s.append(" TEXT, ");
            s.append(DATE_KEY);
            s.append(" REAL)");
            CREATE_STATEMENT = s.toString();
        }

        public HomeworkDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_STATEMENT);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_STATEMENT);
            onCreate(db);
        } 
    }
}