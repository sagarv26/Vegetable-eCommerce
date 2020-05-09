package sweinc.com.buyvegitables.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Profile_Database extends SQLiteOpenHelper {

    private static final String Address = "address";
    private static final String MAP = "map";
    private static final String DB = "myDB.db";
    private static final String Email = "email";
    public static final String ID = "id";
    private static final String Mob = "mobile";
    private static final String Name = "name";
    private static final String UPI = "upi";
    private static final String Table_Name = "profile";
    private static final int version = 1;
    private static SQLiteDatabase myDB;

    public Profile_Database(Context context) {
        super(context, DB, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE profile (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, mobile TEXT NOT NULL, address TEXT NOT NULL, map TEXT NOT NULL, upi TEXT NOT NULL, email TEXT NOT NULL )");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        this.myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (this.myDB != null && this.myDB.isOpen()) {
            this.myDB.close();
        }
    }

    public long create(String name, String email, String mob, String address, String upi, String map) {
        this.myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Mob, mob);
        values.put(Address, address);
        values.put(Email, email);
        values.put(UPI, upi);
        values.put(MAP, map);
        return this.myDB.insert(Table_Name, null, values);
    }

    public long update(String name, String email, String mob, String address, String upi, String map) {
        this.myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Mob, mob);
        values.put(Address, address);
        values.put(Email, email);
        values.put(UPI, upi);
        values.put(MAP, map);
        return (long) this.myDB.update(Table_Name, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        myDB = getWritableDatabase();
        return (long) this.myDB.delete(Table_Name, "name = ?", new String[]{name});
    }

    public Cursor read() {
        myDB = getWritableDatabase();
        return myDB.rawQuery("SELECT * FROM profile", null);
    }

    public static int validate(String name) {
        try {
            Cursor c = myDB.rawQuery("SELECT * FROM profile WHERE name=?", new String[]{name});
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
