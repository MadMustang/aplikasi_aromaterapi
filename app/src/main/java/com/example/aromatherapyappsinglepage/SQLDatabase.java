package com.example.aromatherapyappsinglepage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLDatabase extends SQLiteOpenHelper {

    private static final String COLUMN_1 = "MINOR_850";
    private static final String COLUMN_2 = "MINOR_865";
    private static final String COLUMN_3 = "MINOR_1470";
    private static final String COLUMN_4 = "MINOR_1471";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_X = "x";
    private static final String COLUMN_Y = "y";
    private static final String CREATE_TABLE = "CREATE TABLE RSSID(id INTEGER PRIMARY KEY AUTOINCREMENT,x TEXT,y TEXT,MINOR_850 INTEGER,MINOR_865 INTEGER,MINOR_1470 INTEGER,MINOR_1471 INTEGER)";
    private static final String DATABASE_NAME = "rssi_db";
    private static final int DATABASE_VERSION = 2;
    private static final String TABLE_NAME = "RSSID";
    private static final String TAG = SQLDatabase.class.getSimpleName();

    public SQLDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS RSSID");
        onCreate(db);
    }

    public long insertData(String x, String y, Integer data1, Integer data2, Integer data3, Integer data4) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_X, x);
        values.put(COLUMN_Y, y);
        values.put(COLUMN_1, data1);
        values.put(COLUMN_2, data2);
        values.put(COLUMN_3, data3);
        values.put(COLUMN_4, data4);
        long id = db.insert(TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public List<Map<String, String>> getData() {
        List<Map<String, String>> data = new ArrayList();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM RSSID ORDER BY id DESC", null);
        data.clear();
        if (cursor.moveToFirst()) {
            do {
                Map<String, String> map = new HashMap();
                map.put("ID", cursor.getString(cursor.getColumnIndex(COLUMN_ID)));
                map.put("X", cursor.getString(cursor.getColumnIndex(COLUMN_X)));
                map.put("Y", cursor.getString(cursor.getColumnIndex(COLUMN_Y)));
                map.put("RSSI1", cursor.getString(cursor.getColumnIndex(COLUMN_1)));
                map.put("RSSI2", cursor.getString(cursor.getColumnIndex(COLUMN_2)));
                map.put("RSSI3", cursor.getString(cursor.getColumnIndex(COLUMN_3)));
                map.put("RSSI4", cursor.getString(cursor.getColumnIndex(COLUMN_4)));
                data.add(map);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return data;
    }

    public boolean exportDB() {
        String selectQuery = "SELECT * FROM RSSID ORDER BY id DESC";
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
            File sdCardDir = new File(Environment.getExternalStorageDirectory(), "");
            if (!sdCardDir.exists()) {
                sdCardDir.mkdirs();
            }
            String str = TAG;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("File location :");
            stringBuilder.append(sdCardDir.toString());
            Log.v(str, stringBuilder.toString());
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(sdCardDir, "ResponseTime.csv")));
            int rowcount = cursor.getCount();
            int colcount = cursor.getColumnCount();
            if (rowcount > 0) {
                int i;
                cursor.moveToFirst();
                for (i = 0; i < colcount; i++) {
                    if (i != colcount - 1) {
                        StringBuilder stringBuilder2 = new StringBuilder();
                        stringBuilder2.append(cursor.getColumnName(i));
                        stringBuilder2.append(",");
                        bw.write(stringBuilder2.toString());
                    } else {
                        bw.write(cursor.getColumnName(i));
                    }
                }
                bw.newLine();
                for (i = 0; i < rowcount; i++) {
                    cursor.moveToPosition(i);
                    for (int j = 0; j < colcount; j++) {
                        if (j != colcount - 1) {
                            StringBuilder stringBuilder3 = new StringBuilder();
                            stringBuilder3.append(cursor.getString(j));
                            stringBuilder3.append(",");
                            bw.write(stringBuilder3.toString());
                        } else {
                            bw.write(cursor.getString(j));
                        }
                    }
                    bw.newLine();
                }
                bw.flush();
                Log.d(TAG, "Exported Successfully");
            }
            return true;
        } catch (Exception ex) {
            if (db.isOpen()) {
                db.close();
                String str2 = TAG;
                StringBuilder stringBuilder4 = new StringBuilder();
                stringBuilder4.append("Error");
                stringBuilder4.append(ex.getMessage());
                Log.e(str2, stringBuilder4.toString());
            }
            return false;
        }
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM RSSID");
        db.delete("SQLITE_SEQUENCE", "NAME = ?", new String[]{TABLE_NAME});
        db.close();
    }

}
