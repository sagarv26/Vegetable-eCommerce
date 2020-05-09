package sweinc.com.buyvegitables.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Order_Database extends SQLiteOpenHelper {

    private static final String DB = "OrderDB.db";

    private static final String OrderDetail = "orderDetail";
    private static final String OrderTable = "orderList";
    private static final int version = 1;
    private static SQLiteDatabase cart_myDB;

    public Order_Database(Context context) {
        super(context, DB, null, version);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE orderList (id INTEGER PRIMARY KEY AUTOINCREMENT, orderDetail TEXT NOT NULL)");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void openDB() {
        cart_myDB = getWritableDatabase();
    }

    public void closeDB() {
        if (cart_myDB != null && cart_myDB.isOpen()) {
            cart_myDB.close();
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
        return cart_myDB.rawQuery("SELECT * FROM orderList", null);
    }
    
}
