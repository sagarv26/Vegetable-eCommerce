package sweinc.com.buyvegitables.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Favorite_Database extends SQLiteOpenHelper
{
    private static final String DB = "FavDB.db";

    private static final String Fav_Table = "fav_list";
    private static final String ID = "id";
    private static final String Name = "name";
    private static final String Price = "price";
    private static final String URL = "url";
    private static final int version = 1;
    private static SQLiteDatabase fav_myDB;

    public Favorite_Database(Context context) {
        super(context, DB, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE fav_list (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, url TEXT NOT NULL, price TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        fav_myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (fav_myDB != null && fav_myDB.isOpen()) {
            fav_myDB.close();
        }
    }

    public long create(String name, String url, String price) {
        fav_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(URL, url);
        values.put(Price, price);
        return fav_myDB.insert(Fav_Table, null, values);
    }

    public long update(String name, String url, String price) {
        fav_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(URL, url);
        values.put(Price, price);
        return (long) fav_myDB.update(Fav_Table, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        fav_myDB = getWritableDatabase();
        return (long) fav_myDB.delete(Fav_Table, "name = ?", new String[]{name});
    }

    public Cursor read() {
        fav_myDB = getWritableDatabase();
        return fav_myDB.rawQuery("SELECT * FROM fav_list", null);
    }

    public static int validate(String name) {
        try {
            Cursor c = fav_myDB.rawQuery("SELECT * FROM fav_list WHERE name=?", new String[]{name});
            c.moveToFirst();
            int i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
