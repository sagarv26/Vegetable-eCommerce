package sweinc.com.buyvegitables.activity;


import android.database.Cursor;
import android.graphics.Rect;
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
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;

import sweinc.com.buyvegitables.R;
import sweinc.com.buyvegitables.adapter.CartListAdapter;
import sweinc.com.buyvegitables.adapter.FavListAdapter;
import sweinc.com.buyvegitables.adapter.FavRecyclerItemTouchHelper;
import sweinc.com.buyvegitables.database.Favorite_Database;
import sweinc.com.buyvegitables.model.FavItem;

public class FavoritePage extends AppCompatActivity implements FavRecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private FavListAdapter adapter;
    Favorite_Database database;

    private List<FavItem> listItem;
    Cursor res;

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private boolean includeEdge;
        private int spacing;
        private int spanCount;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % this.spanCount;
            if (this.includeEdge) {
                outRect.left = this.spacing - ((this.spacing * column) / this.spanCount);
                outRect.right = ((column + 1) * this.spacing) / this.spanCount;
                if (position < this.spanCount) {
                    outRect.top = this.spacing;
                }
                outRect.bottom = this.spacing;
                return;
            }
            outRect.left = (this.spacing * column) / this.spanCount;
            outRect.right = this.spacing - (((column + 1) * this.spacing) / this.spanCount);
            if (position >= this.spanCount) {
                outRect.top = this.spacing;
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fav_page);

        ActionBar toolbar = getSupportActionBar();
        assert toolbar != null;
        toolbar.setTitle("Wishlist");
        this.database = new Favorite_Database(getApplicationContext());
        this.listItem = new ArrayList();
        res = database.read();

        while (res.moveToNext()) {
            Favorite_Database breakfast_Database = this.database;
            this.listItem.add(new FavItem(res.getString(res.getColumnIndex("name")), res.getString(res.getColumnIndex("url")), res.getString(res.getColumnIndex("price"))));
        }

        this.adapter = new FavListAdapter(getApplication(), this.listItem);
        RecyclerView recyclerView = findViewById(R.id.favRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(1, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(this.adapter);
        new ItemTouchHelper(new FavRecyclerItemTouchHelper(0, 4, this)).attachToRecyclerView(recyclerView);

    }

    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if (viewHolder instanceof FavListAdapter.MyViewHolder) {
            final String name = this.listItem.get(viewHolder.getAdapterPosition()).getFavName();
            final String cost = this.listItem.get(viewHolder.getAdapterPosition()).getFavCost().substring(1);
            final String url = this.listItem.get(viewHolder.getAdapterPosition()).getFavCost();
            final FavItem deletedItem = this.listItem.get(viewHolder.getAdapterPosition());
            final int deletedIndex = viewHolder.getAdapterPosition();
            this.adapter.removeItem(viewHolder.getAdapterPosition());
            if (Long.valueOf(this.database.delete(name)).longValue() == -1) {
                Toast.makeText(this, "Product not removed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Product removed", Toast.LENGTH_SHORT).show();
            }
            Snackbar snackbar = Snackbar.make(findViewById(R.id.fav_rec_view), name + " removed from Wishlist!", 0);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                public void onClick(View view) {
                    FavoritePage.this.adapter.restoreItem(deletedItem, deletedIndex);
                    if (Long.valueOf(FavoritePage.this.database.create(name, url, cost)).longValue() == -1) {
                        Toast.makeText(FavoritePage.this.getApplicationContext(), name + " : Not Added to Wishlist", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(FavoritePage.this.getApplicationContext(), name + " : Added back to Wishlist", Toast.LENGTH_SHORT).show();
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