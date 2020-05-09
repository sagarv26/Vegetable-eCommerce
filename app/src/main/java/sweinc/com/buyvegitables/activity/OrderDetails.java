package sweinc.com.buyvegitables.activity;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.database.Order_Database;

public class OrderDetails extends AppCompatActivity {

    Order_Database database;
    Cursor res;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details);
        ArrayList listItem = new ArrayList();
        database = new Order_Database(getApplicationContext());

        res = database.readOrder();
        while (res.moveToNext()) {
            String items = res.getString(res.getColumnIndex("orderDetail"));
            listItem.add(items);
        }

        listView = findViewById(R.id.listView);

        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, listItem);
        listView.setAdapter(adapter);
    }
}
