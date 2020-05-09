package sweinc.com.buyvegitables.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Cart_Database extends SQLiteOpenHelper {

    private static final String DB = "VegetableDB.db";
    private static final String Name = "name";
    private static final String Cost = "cost";
    private static final String Quantity = "quantity";
    private static final String Cart_Table = "cartList";

    private static final String OrderDetail = "orderDetail";
    private static final String OrderTable = "orderList";
    private static final int version = 1;
    private static SQLiteDatabase cart_myDB;

    public Cart_Database(Context context) {
        super(context, DB, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE orderList (id INTEGER PRIMARY KEY AUTOINCREMENT, orderDetail TEXT NOT NULL)");
        db.execSQL("CREATE TABLE cartList (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT NOT NULL, quantity TEXT NOT NULL, cost TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        this.cart_myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (this.cart_myDB != null && this.cart_myDB.isOpen()) {
            this.cart_myDB.close();
        }
    }

    public long create(String name, String quantity, String cost) {
        cart_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Cost, cost);
        values.put(Quantity, quantity);
        return cart_myDB.insert(Cart_Table, null, values);
    }

    public long update(String name,  String quantity, String cost) {
        cart_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(Name, name);
        values.put(Cost, cost);
        values.put(Quantity, quantity);
        return (long) cart_myDB.update(Cart_Table, values, "name = ?", new String[]{name});
    }

    public long delete(String name) {
        cart_myDB = getWritableDatabase();
        return (long) cart_myDB.delete(Cart_Table, "name = ?", new String[]{name});
    }

    public long deleteAll() {
        cart_myDB = getWritableDatabase();
        return (long) cart_myDB.delete(Cart_Table, null, null);
    }


    public Cursor read() {
        cart_myDB = getWritableDatabase();
        return cart_myDB.rawQuery("SELECT * FROM cartList", null);
    }

    public static int validate(String name) {
        try {
            Cursor c = cart_myDB.rawQuery("SELECT * FROM cartList WHERE name=?", new String[]{name});
            c.moveToFirst();
            int i = c.getCount();
            c.close();
            return i;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    public long create(String details) {
        cart_myDB = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(OrderDetail, details);
        return cart_myDB.insert(OrderTable, null, values);
    }

    public Cursor readOrder() {
        cart_myDB = getWritableDatabase();
        return cart_myDB.rawQuery("SELECT * FROM orderDetail", null);
    }
    
}
