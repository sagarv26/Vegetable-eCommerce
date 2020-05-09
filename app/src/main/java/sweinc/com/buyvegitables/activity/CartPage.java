package sweinc.com.buyvegitables.activity;


import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.InputDeviceCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import sweinc.com.buyvegitables.Config;
import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.adapter.CartListAdapter;
import sweinc.com.buyvegitables.adapter.RecyclerItemTouchHelper;
import sweinc.com.buyvegitables.database.Cart_Database;
import sweinc.com.buyvegitables.model.CartItem;

public class CartPage extends AppCompatActivity implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private CartListAdapter adapter;
    Cart_Database bf_database;

    private List<CartItem> listItem;

    Button last_step;
    Cursor res;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cart_page);
        last_step = findViewById(R.id.last_step);
        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle("Vegetable Cart");

        bf_database = new Cart_Database(getApplicationContext());
        listItem = new ArrayList<>();

        res = bf_database.read();
        while (res.moveToNext()) {
            listItem.add(new CartItem(res.getString(res.getColumnIndex("name")), "kgs: " + res.getString(res.getColumnIndex("quantity")), "₹" + res.getString(res.getColumnIndex("cost"))));
        }

        adapter = new CartListAdapter(getApplication(), this.listItem);
        RecyclerView recyclerView = findViewById(R.id.cartRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.addItemDecoration(new Config.GridSpacingItemDecoration(1, dpToPx(0), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(this.adapter);
        new ItemTouchHelper(new RecyclerItemTouchHelper(0, 4, this)).attachToRecyclerView(recyclerView);

        last_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                res = bf_database.read();
                if (!res.moveToNext()) {
                    Config.BuildAlert.buildAlert(getApplicationContext(), "Cart is Empty....Add Products to it now", "Empty Cart");
                } else {
                    Config.BuildIntent.buildIntent(getApplicationContext(), CartBill.class, "Cart Receipt");
                }
            }
        });
    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof CartListAdapter.MyViewHolder) {
            final String name = this.listItem.get(viewHolder.getAdapterPosition()).getCartOrderName();
            final String cost = this.listItem.get(viewHolder.getAdapterPosition()).getCartOrderCost().substring(1);
            final String quantity = this.listItem.get(viewHolder.getAdapterPosition()).getCartQuantityName().substring(5);
            final CartItem deletedItem = this.listItem.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            this.adapter.removeItem(viewHolder.getAdapterPosition());
            if (Long.valueOf(bf_database.delete(name)).longValue() == -1) {
                Toast.makeText(this, "Data not Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Data Deleted", Toast.LENGTH_SHORT).show();
            }
            Snackbar snackbar = Snackbar.make(findViewById(R.id.fav_rec_view), name + " removed from cart!", 0);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                public void onClick(View view) {
                    CartPage.this.adapter.restoreItem(deletedItem, deletedIndex);
                    if (Long.valueOf(bf_database.create(name, quantity, cost)).longValue() == -1) {
                        Toast.makeText(getApplicationContext(), name + " : Not Added to cart", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), name + " : Added back to Cart", Toast.LENGTH_SHORT).show();
                    }

                }
            });
            snackbar.setActionTextColor(InputDeviceCompat.SOURCE_ANY);
            snackbar.show();
        }
    }

    private int dpToPx(int dp) {
        return Math.round(TypedValue.applyDimension(1, (float) dp, getResources().getDisplayMetrics()));
    }
}