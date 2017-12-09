package com.example.nathan.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Nathan on 2017-10-13.
 */

public class ChatDatabaseHelper extends SQLiteOpenHelper {

    private static final String ACTIVITY_NAME = "ChatDatabaseHelper";
    public static final String DATABASE_NAME = "CHAT_DATABASE.db";
    public static final int VERSION_NUM = 2;
    public static final String CHAT_TABLE = "CHAT_TABLE";
    public static final String KEY_ID = "_id";
    public static final String KEY_MESSAGE = "message";

    public ChatDatabaseHelper(Context ctx){
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        Log.i(ACTIVITY_NAME, "Calling onCreate()");
        db.execSQL("CREATE TABLE " + CHAT_TABLE + " ( "
        + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
        + KEY_MESSAGE + " TEXT"
        + " ); ");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        Log.i(ACTIVITY_NAME, "Calling onUpgrade, oldVersion = " + oldVersion
            + " newVersion = " + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + CHAT_TABLE);
        onCreate(db);
    }
}
