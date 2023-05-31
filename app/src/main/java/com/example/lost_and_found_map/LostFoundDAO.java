package com.example.lost_and_found_map;

import java.util.List;
import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

//The same however everything was updated to include the latitude and longitude
public class LostFoundDAO {
    private SQLiteDatabase DB;
    private LostFoundDbHelper Helper;

    private static final String TABLE_NAME = "lost_found_items";
    private static final String COLUMN_ID = "id";

    public LostFoundDAO(Context context) {
        Helper = new LostFoundDbHelper(context);
    }

    public void open() {
        DB = Helper.getWritableDatabase();
    }

    public void close() {
        Helper.close();
    }

    //create advert activty is the issue
    public long insertItem(String postType, String name, String phone, String description, String date, String location, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(LostFoundContract.LostFoundEntry.COLUMN_POST_TYPE, postType);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_NAME, name);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_PHONE, phone);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_DESCRIPTION, description);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_DATE, date);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LOCATION, location);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LATITUDE, latitude); // Add latitude value
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LONGITUDE, longitude); // Add longitude value

        return DB.insert(LostFoundContract.LostFoundEntry.TABLE_NAME, null, values);
    }

    public void updateItem(long itemId, String postType, String name, String phone, String description, String date, String location, double latitude, double longitude) {
        ContentValues values = new ContentValues();
        values.put(LostFoundContract.LostFoundEntry.COLUMN_POST_TYPE, postType);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_NAME, name);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_PHONE, phone);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_DESCRIPTION, description);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_DATE, date);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LOCATION, location);
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LATITUDE, latitude); // Add latitude value
        values.put(LostFoundContract.LostFoundEntry.COLUMN_LONGITUDE, longitude); // Add longitude value

        String selection = LostFoundContract.LostFoundEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        DB.update(LostFoundContract.LostFoundEntry.TABLE_NAME, values, selection, selectionArgs);
    }

    public void deleteItem(LostFoundItem item) {
        String[] whereArgs = {String.valueOf(item.getItemId())};
        DB.delete(TABLE_NAME, COLUMN_ID + " = ?", whereArgs);
    }

    public List<LostFoundItem> getAllItems() {
        List<LostFoundItem> items = new ArrayList<>();

        Cursor cursor = DB.query(
                LostFoundContract.LostFoundEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            long itemId = cursor.getLong(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_ID));
            String postType = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_POST_TYPE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LOCATION));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LATITUDE)); // Retrieve latitude value
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LONGITUDE)); // Retrieve longitude value

            LostFoundItem item = new LostFoundItem(itemId, postType, name, phone, description, date, location, latitude, longitude); // Update LostFoundItem constructor
            items.add(item);
        }

        cursor.close();

        return items;
    }

    public LostFoundItem getItemById(long itemId) {
        String selection = LostFoundContract.LostFoundEntry.COLUMN_ID + " = ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        Cursor cursor = DB.query(
                LostFoundContract.LostFoundEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        LostFoundItem item = null;
        if (cursor.moveToFirst()) {
            String postType = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_POST_TYPE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_DESCRIPTION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_DATE));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LOCATION));
            double latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LATITUDE)); // Retrieve latitude value
            double longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(LostFoundContract.LostFoundEntry.COLUMN_LONGITUDE)); // Retrieve longitude value

            item = new LostFoundItem(itemId, postType, name, phone, description, date, location, latitude, longitude); // Update LostFoundItem constructor
        }

        cursor.close();

        return item;
    }
}

